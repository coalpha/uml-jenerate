import java.util.stream.Stream;

import java.net.*;
import java.nio.file.Path;

import opre.Result;
import static opre.Result.*;

class Loader {
   private final URLClassLoader loader;

   static URL[] convertPaths(final Stream<Path> paths) {
      return (
         paths
            .map(p -> trycatch(() -> p.toUri().toURL()))
            .map(r -> r.unwrap())
            .toArray(URL[]::new)
      );
   }

   Loader(final URL[] classpath) {
      final var sb = new StringBuilder(0xFFF);
      for (final var url : classpath) {
         sb
            .append(url)
            .append(';');
      }
      System.out.println(sb.toString());
      this.loader = URLClassLoader.newInstance(classpath);
   }

   Result<Class<?>, Throwable> load(final String className) {
      System.out.println("load " + className);
      return trycatch(() -> loader.loadClass(className));
   }
}
