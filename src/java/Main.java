import java.nio.file.*;
import java.util.stream.Stream;

class Main {
   private static String basename(final String s) {
      return s.substring(0, s.lastIndexOf('.'));
   }
   public static void main(final String args[]) {
      final var cwd = Paths.get(".");
      final var paths = FileWalker.walk(cwd, "class");

      final var classpath = Loader.convertPaths(cwd);
      final var loader = new Loader(classpath);

      Stream<Class<?>> classes = (
         paths
            .map(Main::basename)
            .map(loader::load)
            .filter(r -> r.is_ok())
            .map(r -> r.unwrap())
      );

      final var dotfile = new DOTFile("UML.dot", "uhhhh", classes);
   }
}
