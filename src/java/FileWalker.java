import java.nio.file.*;

import static opre.Result.*;

interface FileWalker {
   static String[] walk(final Path where, final String type) {
      var abspath = where.toAbsolutePath();
      System.out.println("Searching for ." + type + " files in " + abspath);

      return (
         trycatch(() -> Files.walk(abspath)
            .filter(Files::isRegularFile)
            .map(f -> f.toString())
            .filter(f -> f.endsWith(".class"))
            .toArray(String[]::new)
         ).unwrap_or(new String[]{})
      );
   }
}
