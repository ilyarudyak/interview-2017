package core_java.exercises;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by ilyarudyak on 4/2/17.
 */
public class WordInFiles {

    public static boolean isContainWord(String word, Path path) {
        try {
            String contents = new String(Files.readAllBytes(path),
                    StandardCharsets.UTF_8);
            return Pattern.compile("\\PL+")
                    .splitAsStream(contents)
                    .filter(Predicate.isEqual(word))
                    .count() > 0;
        } catch (IOException ex) {
            return false;
        }
    }

    public static void main(String[] args) {

        final Path pathToFiles = Paths.get("src/main/resources/words");
        final String door = "door";

        try (Stream<Path> paths = Files.walk(pathToFiles)) {
            paths.parallel()
                    .filter(path -> isContainWord(door, path))
//                    .limit(1)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
