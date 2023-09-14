package massrenamer.gui;

import lombok.extern.log4j.Log4j2;
import massrenamer.common.ExceptionUtils;
import massrenamer.engine.BackgroundWorker;
import massrenamer.engine.model.OrderBy;
import massrenamer.engine.model.RenamePattern;
import massrenamer.engine.model.WorkerOutcome;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static massrenamer.common.CommonUtils.isEmpty;
import static massrenamer.engine.model.OrderBy.DATE;
import static massrenamer.engine.model.OrderBy.FILENAME;
import static massrenamer.engine.model.RenamePattern.TAG_DATE_DAILY_ORDINAL;
import static massrenamer.engine.model.RenamePattern.TAG_ORDINAL;

@Log4j2
public class MassRenamerImpl extends MassRenamer {

    public MassRenamerImpl() {
        super();

        dirButton.addActionListener(evt -> dirButtonClicked());
        ordinalPatternRadioButton.addActionListener(evt -> ordinalPatternRadioButtonClicked());
        dateAndOrdinalPatternRadioButton.addActionListener(evt -> dateAndOrdinalPatternRadioButtonClicked());
        startButton.addActionListener(evt -> startButtonClicked());
    }

    private void dirButtonClicked() {
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        JFileChooser fc = new JFileChooser(Files.isDirectory(Paths.get(dirTextField.getText())) ? Paths.get(dirTextField.getText()).toFile() : null);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(false);
        int ret = fc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File selectedDir = fc.getSelectedFile();
            dirTextField.setText(selectedDir.toString());
        }
    }

    private void ordinalPatternRadioButtonClicked() {
        sortByFilenameRadioButton.setEnabled(true);
        sortByDateRadioButton.setEnabled(true);
    }

    private void dateAndOrdinalPatternRadioButtonClicked() {
        sortByFilenameRadioButton.setEnabled(false);
        sortByDateRadioButton.setEnabled(false);
    }

    public void lockWidgets() {
        dirButton.setEnabled(false);
        ordinalPatternRadioButton.setEnabled(false);
        dateAndOrdinalPatternRadioButton.setEnabled(false);
        sortByFilenameRadioButton.setEnabled(false);
        sortByDateRadioButton.setEnabled(false);
        tagTextField.setEditable(false);
        startButton.setEnabled(false);
        progressBar.setValue(0);
    }

    public void unlockWidgets() {
        dirButton.setEnabled(true);
        ordinalPatternRadioButton.setEnabled(true);
        dateAndOrdinalPatternRadioButton.setEnabled(true);
        if (ordinalPatternRadioButton.isSelected()) {
            sortByFilenameRadioButton.setEnabled(true);
            sortByDateRadioButton.setEnabled(true);
        } else {
            sortByFilenameRadioButton.setEnabled(false);
            sortByDateRadioButton.setEnabled(false);
        }
        tagTextField.setEditable(true);
        startButton.setEnabled(true);
        progressBar.setValue(0);
    }

    public void setProgress(int percent) {
        if (percent <= 0) {
            progressBar.setValue(0);
        } else progressBar.setValue(Math.min(percent, 100));
    }

    public void startButtonClicked() {
        lockWidgets();
        if (isEmpty(dirTextField.getText())) {
            unlockWidgets();
            JOptionPane.showMessageDialog(this, "Please select a directory first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Path workDir = Paths.get(dirTextField.getText());
        RenamePattern pattern = ordinalPatternRadioButton.isSelected() ? TAG_ORDINAL : TAG_DATE_DAILY_ORDINAL;
        OrderBy orderBy = ordinalPatternRadioButton.isSelected() ? (sortByFilenameRadioButton.isSelected() ? FILENAME : DATE) : null;
        String tag = !isEmpty(tagTextField.getText()) ? tagTextField.getText().trim() : null;
        BackgroundWorker worker = new BackgroundWorker(this, workDir, pattern, orderBy, tag);
        worker.execute();
    }

    public void backgroundTaskCallback(WorkerOutcome outcome) {
        unlockWidgets();
        if (outcome.success()) {
            JOptionPane.showMessageDialog(this, outcome.message(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, outcome.message(), "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void backgroundTaskException(Throwable ex) {
        unlockWidgets();
        log.error(ExceptionUtils.getRelevantStackTrace(ex));
        JOptionPane.showMessageDialog(this, ExceptionUtils.getCanonicalForm(ex), "Error", JOptionPane.ERROR_MESSAGE);
    }

}
