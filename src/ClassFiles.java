import java.nio.file.*;
import java.util.stream.Stream;

import static opre.Result.trycatch;

interface ClassFiles {
   static boolean isClassFile(final Path p) {
      return true
         && Files.isRegularFile(p)
         && p.getFileName().toString().endsWith(".class")
         ;
   }

   /**
    * This just walks a path turns any .class files into ClassFile objects.
    */
   static Stream<ClassFile> classes(final Path where) {
      return (
         trycatch(() -> Files.walk(where))
            .unwrap_or_else(Stream::empty)
            .filter(ClassFiles::isClassFile)
            .map(ClassFile::new)
      );
   }
}
