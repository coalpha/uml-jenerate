package umlj;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

interface ClassFiles {
   static boolean isClassFile(final Path p) {
      return true
         && Files.isRegularFile(p)
         && p.getFileName().toString().endsWith(".class");
   }

   /**
    * This just walks a path turns any .class files into ClassFile objects.
    */
   static Stream<ClassFile> classes(final Path where) {
      try (final var files = Files.walk(where)) {
         return
            (files
               .filter(ClassFiles::isClassFile)
               .map(ClassFile::new));
      } catch (IOException e) {
         return Stream.empty();
      }
   }
}
