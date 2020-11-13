import java.util.*;
import java.nio.file.Path;
import java.util.function.BiPredicate;
import static opre.Result.*;

class util {
   static String basename(final String s) {
      return s.substring(0, s.lastIndexOf('.'));
   }

   static Path realpath(final Path path) {
      return trycatch(() -> path.toRealPath()).unwrap_or(path);
   }

   static <T> List<T> dedupe_right(final T[] ary) {
      return util.dedupe_right(ary, Objects::equals);
   }

   /**
    * Dedupes keeping the later elements
    */
   static <T> List<T> dedupe_right(final T[] ary, final BiPredicate<T, T> eq) {
      final var len = ary.length;
      final var out = new ArrayList<T>(len);

      first:
      for (var i = 0; i < len; i++) {
         final var first = ary[i];
         for (var j = i + 1; j < len; j++) {
            final var second = ary[j];
            if (eq.test(first, second)) {
               continue first;
            }
         }
         out.add(first);
      }

      return out;
   }

   private static final Scanner sc = new Scanner(System.in);
   static boolean cancel(final Path path) {
      var f = path.toFile();
      if (f.exists()) {
         System.out.print(""
            + util.realpath(path)
            + " already exists\nOverwrite? [y/N]\n> "
         );
         return !sc.nextLine().equals("y");
      }
      return false;
   }
}
