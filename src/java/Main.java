import opre.Result;
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

      final var root = util.realpath(Paths.get(args[0]));
      final var dotpath = util.realpath(Paths.get(args[1]));
      final var pngpath = util.realpath(Paths.get(args[2]));

      out.println(""
         + "<root>   = " + root + '\n'
         + ".dot out = " + dotpath + '\n'
         + ".png out = " + pngpath + '\n'
      );

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
      out.println("wrote " + dotpath);

      if (util.cancel(pngpath)) {
         return;
      }
      final var graphviz = new ProcessBuilder(
         "wsl", "dot", "-Tpng", "-Gdpi=300",
         util.wslpath(dotpath), "-o", util.wslpath(pngpath)
      );
      graphviz.inheritIO();
      final var process = graphviz.start();
      Result.ignore(() -> process.waitFor());

      out.println("wrote " + pngpath);
   }
}
