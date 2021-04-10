package umlj;

import java.nio.file.*;

/**
 * To load a certain class file, you need to make sure that you're loading it
 * with the right context. For instance, a class `b.c` in directory `a` needs to
 * be loaded in the context/classpath of `a`.
 *
 * This holds a single "load instruction", so to speak.
 * AB_Entries are passed into `Loader` which will then attempt to load the
 * className with the provided context.
 */
final class AB_Entry {
   final Path context;

   final String className;

   AB_Entry(final Path context, final String name) {
      this.context = context;
      this.className = name;
   }

   /** Looks like <code>[foo/bar]: baz</code> */
   @Override
   public String toString() {
      return (
         new StringBuilder()
            .append('[')
            .append(this.context)
            .append("]: ")
            .append(this.className)
            .toString()
      );
   }
}
