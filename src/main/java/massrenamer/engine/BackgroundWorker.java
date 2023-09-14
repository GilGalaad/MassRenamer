package massrenamer.engine;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import massrenamer.engine.model.FileToRename;
import massrenamer.engine.model.OrderBy;
import massrenamer.engine.model.RenamePattern;
import massrenamer.engine.model.WorkerOutcome;
import massrenamer.gui.MassRenamerImpl;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static massrenamer.common.CommonUtils.*;
import static massrenamer.engine.model.OrderBy.DATE;
import static massrenamer.engine.model.OrderBy.FILENAME;
import static massrenamer.engine.model.RenamePattern.TAG_DATE_DAILY_ORDINAL;
import static massrenamer.engine.model.RenamePattern.TAG_ORDINAL;

@Log4j2
@RequiredArgsConstructor
public class BackgroundWorker extends SwingWorker<WorkerOutcome, Void> {

    private final MassRenamerImpl frame;
    private final Path workDir;
    private final RenamePattern pattern;
    private final OrderBy orderBy;
    private final String tag;

    @Override
    protected WorkerOutcome doInBackground() throws Exception {
        log.info("*** Execution start ***");
        long startTime = System.nanoTime();

        List<Path> srcFiles;
        try (var stream = Files.walk(workDir, 1)) {
            srcFiles = stream
                    .filter(i -> !i.equals(workDir))
                    .filter(Files::isRegularFile)
                    .toList();
        }
        if (srcFiles.isEmpty()) {
            return new WorkerOutcome(false, "No file found in directory");
        }
        long totalFiles = srcFiles.size();
        long unsortableFiles = 0;
        List<FileToRename> files = new ArrayList<>(srcFiles.stream().map(i -> new FileToRename(i.getFileName())).toList());

        // read EXIF metadata if necessary
        if ((pattern == TAG_ORDINAL && orderBy == DATE) || pattern == TAG_DATE_DAILY_ORDINAL) {
            setDates(files);
        }

        if (pattern == TAG_ORDINAL && orderBy == FILENAME) {
            sortByFilename(files);
        } else if ((pattern == TAG_ORDINAL && orderBy == DATE) || pattern == TAG_DATE_DAILY_ORDINAL) {
            unsortableFiles = files.stream().filter(i -> i.getDate() == null).count();
            sortByDate(files);
        } else {
            throw new UnsupportedOperationException("Unreachable code");
        }

        switch (pattern) {
            case TAG_ORDINAL -> setDestFilenamesByGlobalOrdinal(files);
            case TAG_DATE_DAILY_ORDINAL -> setDestFilenamesByDailyOrdinal(files);
            default -> throw new UnsupportedOperationException("Unreachable code");
        }

        files.forEach(i -> log.info(i.dump()));
        doRename(files);

        long endTime = System.nanoTime();
        log.info("*** Execution time: {} ***", smartElapsed(endTime - startTime));

        if (unsortableFiles == 0) {
            return new WorkerOutcome(true, String.format("Operation complete, renamed %s files", totalFiles));
        } else {
            return new WorkerOutcome(false, String.format("Operation complete, renamed %s files, not renamed %s files (no EXIF metadata found)", totalFiles - unsortableFiles, unsortableFiles));
        }
    }

    private void sortByFilename(List<FileToRename> files) {
        files.sort(Comparator.comparing(i -> i.getSrcFileName().getFileName().toString().toLowerCase()));
    }

    private void sortByDate(List<FileToRename> files) {
        List<FileToRename> sortable = files.stream().filter(i -> i.getDate() != null).sorted(Comparator.comparing(FileToRename::getDate)).toList();
        files.clear();
        files.addAll(sortable);
    }

    private void setDates(List<FileToRename> files) {
        long startTime = System.nanoTime();
        frame.setProgress(0);
        for (int i = 0; i < files.size(); i++) {
            FileToRename file = files.get(i);
            try {
                file.setDate(getPictureExifDate(file.getSrcFileName()));
            } catch (ImageProcessingException | IOException ex) {
                file.setDate(null);
            }
            frame.setProgress(calcPercent(i + 1, files.size()));
        }
        long endTime = System.nanoTime();
        log.info("Processed EXIF metadata in {}", smartElapsed(endTime - startTime));
    }

    private Date getPictureExifDate(Path srcFile) throws ImageProcessingException, IOException {
        //log.info("Reading EXIF metadata from file: {}", srcFile.getFileName());
        Metadata metadata = ImageMetadataReader.readMetadata(workDir.resolve(srcFile).toFile());
        // try TAG_DATETIME_ORIGINAL first
        ExifSubIFDDirectory subdir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (subdir != null && subdir.getDateOriginal() != null) {
            //log.info("{} -> {} -> {}", subdir.getName(), subdir.getTagName(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL), subdir.getDateOriginal());
            return subdir.getDateOriginal();
        }
        // fallback to TAG_DATETIME
        ExifIFD0Directory dir = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (dir != null) {
            //log.info("{} -> {} -> {}", dir.getName(), dir.getTagName(ExifIFD0Directory.TAG_DATETIME), dir.getDate(ExifIFD0Directory.TAG_DATETIME, TimeZone.getDefault()));
            return dir.getDate(ExifIFD0Directory.TAG_DATETIME, TimeZone.getDefault());
        }
        return null;
    }

    private List<String> getAllTags(Path srcFile) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(workDir.resolve(srcFile).toFile());
        return StreamSupport.stream(metadata.getDirectories().spliterator(), false)
                .flatMap(d -> d.getTags().stream())
                .map(t -> String.format("[%s] %s = %s", t.getDirectoryName(), t.getTagName(), t.getDescription()))
                .toList();
    }

    private void setDestFilenamesByGlobalOrdinal(List<FileToRename> files) {
        long startTime = System.nanoTime();
        int digits = getDigitCount(files.size());
        String ptr = "%s" + "%0" + digits + "d" + ".%s";
        for (int i = 0; i < files.size(); i++) {
            FileToRename file = files.get(i);
            String destFileName = String.format(ptr, !isEmpty(tag) ? tag + " - " : "", i + 1, getFileExtension(file.getSrcFileName().toString()).toLowerCase());
            file.setDestFileName(Paths.get(destFileName));
        }
        long endTime = System.nanoTime();
        log.info("Formatted destination filename in {}", smartElapsed(endTime - startTime));
    }

    private void setDestFilenamesByDailyOrdinal(List<FileToRename> files) {
        long startTime = System.nanoTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<Date, Long> datesTotalCount = files.stream().map(i -> truncateDate(i.getDate())).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<Date, Integer> datesCurrentCount = new HashMap<>();
        for (FileToRename file : files) {
            Date day = truncateDate(file.getDate());
            int digits = getDigitCount(datesTotalCount.get(day).intValue());
            int ordinal = datesCurrentCount.getOrDefault(day, 0) + 1;
            datesCurrentCount.put(day, ordinal);
            String ptr = "%s" + "%s" + "_%0" + digits + "d" + ".%s";
            String destFileName = String.format(ptr, !isEmpty(tag) ? tag + " - " : "", sdf.format(day), ordinal, getFileExtension(file.getSrcFileName().toString()).toLowerCase());
            file.setDestFileName(Paths.get(destFileName));
        }
        long endTime = System.nanoTime();
        log.info("Formatted destination filename in {}", smartElapsed(endTime - startTime));
    }

    private void doRename(List<FileToRename> files) throws IOException {
        long startTime = System.nanoTime();
        for (int i = 0; i < files.size(); i++) {
            FileToRename ftr = files.get(i);
            Path src = workDir.resolve(ftr.getSrcFileName());
            Path dest = workDir.resolve(ftr.getDestFileName());
            Files.move(src, dest);
            frame.setProgress(calcPercent(i + 1, files.size()));
        }
        long endTime = System.nanoTime();
        log.info("Renamed files in {}", smartElapsed(endTime - startTime));
    }

    private Date truncateDate(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private int getDigitCount(int count) {
        int digits = (int) (Math.log10(count) + 1);
        // flooring to 2 digits
        return Math.max(digits, 2);
    }

    @Override
    protected void done() {
        try {
            WorkerOutcome outcome = this.get();
            frame.backgroundTaskCallback(outcome);
        } catch (ExecutionException | InterruptedException ex) {
            frame.backgroundTaskException(ex);
        }
    }

}
