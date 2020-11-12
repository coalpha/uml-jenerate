import java.util.Arrays;

import java.net.*;
import java.nio.file.Path;

import opre.Result;
import static opre.Result.*;

class Loader {
   final URLClassLoader loader;

   static URL[] convertPaths(final Path[] paths) {
      return (
         Arrays.stream(paths)
            .map(p -> trycatch(() -> p.toUri().toURL()))
            .map(r -> r.expect("Impossible"))
            .toArray(URL[]::new)
      );
   }

   Loader(final URL[] classpath) {
      this.loader = URLClassLoader.newInstance(classpath);
   }

   Result<Class<?>, Throwable> load(final String className) {
      return trycatch(() -> loader.loadClass(className));
   }
}
