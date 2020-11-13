import java.util.*;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static opre.Result.*;

class FileWalker {
   final List<Path> filesFound;
   final List<Path> parents;

   FileWalker(final Path where, final String type) {
      final var path = util.realpath(where);
      System.out.println("Searching for ." + type + " files in " + path);

      filesFound = (
         trycatch(() -> Files.walk(path))
            .unwrap_or(Stream.empty())
            .filter(Files::isRegularFile)
            .filter(f -> f.toString().endsWith(".class"))
            .collect(Collectors.toUnmodifiableList())
      );

      parents = (
         filesFound
            .stream()
            .map(f -> f.getParent())
            .collect(Collectors.toUnmodifiableList())
      );
   }
}
