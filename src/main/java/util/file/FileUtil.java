package util.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtil {

    private static FileUtil INSTANCE = null;

    private FileUtil(){}

    public static String readFile(String path)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public synchronized static FileUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileUtil();
        }
        return INSTANCE;
    }
}
