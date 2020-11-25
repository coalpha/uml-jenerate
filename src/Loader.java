import java.net.*;
import java.util.*;

import static java.lang.System.*;

interface Loader {
   static Class<?> load(final ClassFile c) {
      return (
         c
            .stream()
            .filter(Objects::nonNull)
            .map(Loader::load)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null)
      );
   }

   static Class<?> load(final AB_Entry e) {
      final URL path;
      try {
         path = e.context.toUri().toURL();
      } catch (Throwable t) {
         err.println("ERR " + t.getMessage());
         return null;
      }

      final var classpath = new URL[]{path};
      try (final var loader = new URLClassLoader(classpath)) {
         out.println("TRY " + e);
         final var clazz = loader.loadClass(e.className);
         out.println("OKAY " + e);
         return clazz;
      } catch (Throwable t) {
         err.println("ERR " + t.getMessage());
         return null;
      }
   }
}
