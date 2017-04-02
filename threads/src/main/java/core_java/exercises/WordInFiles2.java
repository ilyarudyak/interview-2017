package core_java.exercises;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by ilyarudyak on 4/3/17.
 */
public class WordInFiles2 {

    private static ConcurrentHashMap<String, Set<File>> map = new ConcurrentHashMap<>();

    public static void processFile(Path path) {
        try {
            String contents = new String(Files.readAllBytes(path),
                    StandardCharsets.UTF_8);
            Pattern.compile("\\PL+")
                    .splitAsStream(contents)
                    .forEach(word -> map.merge(word, new HashSet<>(Arrays.asList(path.toFile())),
                            (oldSet, newSet) -> {
                                    oldSet.add(path.toFile());
                                    return oldSet;
                                })
                    );
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final Path pathToFiles = Paths.get("src/main/resources/words");

        try (Stream<Path> paths = Files.walk(pathToFiles)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> processFile(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.forEach((k,v) -> {
            if (v.size() > 1) {
                System.out.println(k + ":" + v);
            }
        });
    }


}
