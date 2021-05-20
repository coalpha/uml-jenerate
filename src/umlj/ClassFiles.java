package umlj;

import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Stream;

interface ClassFiles {
   static boolean isClassFile(final Path p) {
      return true
         && Files.isRegularFile(p)
         && p.getFileName().toString().endsWith(".class");
   }

   static Stream<Path> getClassFilesFrom(final Path where) {
      try (final var files = Files.walk(where)) {
         return files.filter(ClassFiles::isClassFile);
      } catch (IOException e) {
         return Stream.empty();
      }
   }
}
