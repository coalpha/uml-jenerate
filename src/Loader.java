import java.net.*;
import java.util.*;

import static java.lang.System.*;

interface Loader {
   static Class<?> load(final ClassFile classfile) {
      final var maybeClass = (
         classfile
            .stream()
            .filter(Objects::nonNull)
            .<Class<?>>map(Loader::load)
            .filter(Objects::nonNull)
            .findFirst()
      );

      if (maybeClass.isEmpty()) {
         out.println("load bad " + classfile);
      } else {
         out.println("load ok  " + classfile);
      }

      return maybeClass.orElse(null);
   }

   static Class<?> load(final AB_Entry e) {
      final URL path;
      try {
         path = e.context.toUri().toURL();
      } catch (Throwable t) {
         return null;
      }

      final var classpath = new URL[]{path};
      try (final var loader = new URLClassLoader(classpath)) {
         return loader.loadClass(e.className);
      } catch (Throwable t) {
         return null;
      }
   }
}
