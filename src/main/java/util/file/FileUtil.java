package util.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class FileUtil {

    private static FileUtil INSTANCE = null;

    private FileUtil(){}

    public static String readFile(String path)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public static List<String> getAllFilePaths(String srcPath) throws IOException {
        Stream<Path> paths = Files.walk(Paths.get(srcPath));
        List<String> listOfPaths = new ArrayList<>();
        paths.filter(Files::isRegularFile)
                .forEach(path -> {
                    listOfPaths.add(path.normalize().toString());
                });
        return listOfPaths;
    }

    public static String getLastSegmentOfPath(String path){
        String[] directories = path.split("\\\\");
        return directories[directories.length - 1];
    }

    public synchronized static FileUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileUtil();
        }
        return INSTANCE;
    }
}
