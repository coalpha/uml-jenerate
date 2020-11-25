import java.util.*;
import java.nio.file.*;
import java.util.stream.Stream;

import static opre.Result.trycatch;

interface ClassFiles {
   static Stream<ClassFile> classes(final Path where) {
      return (
         trycatch(() -> Files.walk(where))
            .unwrap_or_else(Stream::empty)
            .filter(Files::isRegularFile)
            .map(ClassFile::make)
            .filter(Objects::nonNull)
      );
   }
}
