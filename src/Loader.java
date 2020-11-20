import java.util.stream.Stream;

import java.net.*;
import java.nio.file.Path;

import opre.Result;
import static opre.Result.*;

final class Loader {
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
      final var uniqclasspath = util.dedupe_right(classpath).toArray(new URL[0]);
      final var sb = new StringBuilder(0xFFF);
      sb.append("classpath: \n");
      for (final var url : uniqclasspath) {
         sb
            .append(url)
            .append('\n');
      }
      System.out.println(sb.toString());
      this.loader = URLClassLoader.newInstance(uniqclasspath);
   }

   Result<Class<?>, Throwable> load(final String className) {
      System.out.println("load " + className);
      return trycatch(() -> loader.loadClass(className));
   }
}
