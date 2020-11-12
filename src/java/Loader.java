import java.io.IOException;

import java.net.*;
import java.nio.file.*;
import static java.lang.System.*;

class Loader {
   private static String basename(String s) {
      return s.substring(0, s.lastIndexOf('.'));
   }
   public static void main(String a[]) throws IOException, ClassNotFoundException {
      var cwd = Paths.get(".");
      var classFiles = FileWalker.walk(cwd, "class");

      var loader = URLClassLoader.newInstance(
         new URL[] { cwd.toUri().toURL() }
      );


      while (true) {
         var choice = Chooser.choose(classFiles);

         if (choice == null) {
            // end the program
            return;
         }

         var classNameMaybe = basename(choice);

         try {
            Class<?> clazz;
            try {
               clazz = loader.loadClass(classNameMaybe);
            } catch (ClassNotFoundException e) {
               err.println("ClassNotFound: " + e.getMessage());
               continue;
            } catch (NoClassDefFoundError e) {
               err.println(e.getMessage());
               continue;
            }

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
