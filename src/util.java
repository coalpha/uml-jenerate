import java.util.*;
import java.nio.file.Path;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import opre.Option;
import static opre.Result.*;

interface util {
   static String basename(final String s) {
      return s.substring(0, s.lastIndexOf('.'));
   }

   static Path realpath(final Path path) {
      return (
         trycatch(() -> path.toRealPath())
            .unwrap_or_else(() -> path.toAbsolutePath())
      );
   }

   static <T> List<T> dedupe_right(final T[] ary) {
      return util.dedupe_right(ary, Object::equals);
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

   static final Scanner sc = new Scanner(System.in);
   static String prompt(final String prompt) {
      System.out.print(prompt + "\n> ");
      return sc.nextLine();
   }

   static boolean cancel(final Path path) {
      var f = path.toFile();
      return f.exists() && !(
         prompt("\n" + path + " already exists\nOverwrite? [y/N]")
            .equals("y")
      );
   }

   static String wslpath(final Path path) {
      final var winpath = util.realpath(path).toString().toCharArray();
      final var len = winpath.length;
      final var cary = new char[len + 4];
      cary[0] = '/';
      cary[1] = 'm';
      cary[2] = 'n';
      cary[3] = 't';
      cary[4] = '/';
      cary[5] = Character.toLowerCase(winpath[0]);

      for (var i = 2; i < len; ++i) {
         final var c = winpath[i];
         cary[i + 4] = c == '\\' ? '/' : c;
      }

      return new String(cary);
   }

   static Stream<Path> allParents(final Path path) {
      final var parent = path.getParent();
      if (parent == null) {
         return Stream.empty();
      } else {
         return Stream.concat(Stream.of(parent), allParents(parent));
      }
   }
}
