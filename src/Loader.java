import java.net.*;
import java.util.*;

import static java.lang.System.*;

interface Loader {
   static LoadedClass load(final ClassFile classfile) {
      final var maybeClass = (
         classfile
            .stream()
            .filter(Objects::nonNull)
            .<LoadedClass>map(Loader::load)
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

   static LoadedClass load(final AB_Entry e) {
      final URL path;
      try {
         path = e.context.toUri().toURL();
      } catch (Throwable t) {
         return null;
      }

      final var classpath = new URL[]{path};
      try (final var loader = new URLClassLoader(classpath)) {
         final var clazz = loader.loadClass(e.className);
         return new LoadedClass(e, clazz);
      } catch (Throwable t) {
         return null;
      }
   }
}
