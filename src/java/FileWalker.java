import java.nio.file.*;
import java.util.stream.Stream;

import static opre.Result.*;

interface FileWalker {
   static Stream<String> walk(final Path where, final String type) {
      final var abspath = where.toAbsolutePath();
      System.out.println("Searching for ." + type + " files in " + abspath);

      return (
         trycatch(() -> Files.walk(abspath)
            .filter(Files::isRegularFile)
            .map(f -> f.getFileName().toString())
            .filter(f -> f.endsWith(".class"))
         ).unwrap_or(Stream.empty())
      );
   }
}
