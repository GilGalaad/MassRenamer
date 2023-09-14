package massrenamer.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileToRename {

    private Path srcFileName;
    private Date date;
    private Path destFileName;

    public FileToRename(Path srcFileName) {
        this.srcFileName = srcFileName;
    }

    public String dump() {
        return String.format("src: %s, date=%s => %s", srcFileName, date, destFileName);
    }

}
