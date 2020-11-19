import java.util.*;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static opre.Result.*;

final class FileWalker {
   final List<Path> filesFound;
   final List<Path> parents;

   FileWalker(final Path where, final String type) {
      filesFound = (
         trycatch(() -> Files.walk(where))
            .unwrap_or(Stream.empty())
            .filter(Files::isRegularFile)
            .filter(f -> f.toString().endsWith(type))
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
