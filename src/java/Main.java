import java.nio.file.*;

class Main {
   private static String basename(String s) {
      return s.substring(0, s.lastIndexOf('.'));
   }
   public static void main(String a[]) {
      var cwd = Paths.get(".");
      var paths = FileWalker.walk(cwd, "class");

      var classpath = Loader.convertPaths(new Path[]{cwd});
      var loader = new Loader(classpath);

      while (true) {
         var choice = Chooser.choose(paths);

         if (choice == null) {
            // end the program
            return;
         }

         var classNameMaybe = basename(choice);

         var clazz_r = loader.load(classNameMaybe);
         if (clazz_r.is_err()) {
            continue;
         }

         var clazz = clazz_r.unwrap();
         try {
            var main = clazz.getMethod("main", String[].class);
            Object args = new String[]{ classNameMaybe };
            main.invoke(null, args);
            return;
         } catch (Exception e) {
            e.printStackTrace();
            continue;
         }
      }
   }
}
