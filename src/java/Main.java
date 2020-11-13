import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Stream;
import static java.lang.System.*;

class Main {
   public static void main(final String[] args) throws IOException {
      if (args.length != 3) {
         err.println("usage: java -jar uml-generate <root> <dot> <png>");
         return;
      }

      final var root = Paths.get(args[0]);
      final var dotpath = Paths.get(args[1]);
      final var pngpath = Paths.get(args[2]);

      final var walked = new FileWalker(root, "class");
      final var parents = (
         Stream.concat(Stream.of(root), walked.parents.stream())
            .map(util::realpath)
      );

      final var classpath = Loader.convertPaths(parents);
      final var loader = new Loader(classpath);

      Stream<Class<?>> classes = (
         walked.filesFound.stream()
            .map(p -> p.getFileName().toString())
            .map(util::basename)
            .map(loader::load)
            .filter(r -> r.is_ok())
            .map(r -> r.unwrap())
      );

      final var dotfile = new DOTFile(dotpath, "UML", classes);
      if (util.cancel(dotpath)) {
         return;
      }
      dotfile.write();

      if (util.cancel(pngpath)) {
         return;
      }
      Runtime.getRuntime().exec(new String[] {
         "wsl", "dot", "-Tpng", "-Gdpi=300",
         "" + dotpath, "-o", "" + pngpath
      });

      out.println("Done.");
   }
}
