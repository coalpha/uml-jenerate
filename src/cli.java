import opre.Result;

import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Stream;
import static java.lang.System.*;

final class cli {
   cli(final String[] args) {
      if (args.length != 3) {
         err.println("usage: java -jar uml-jenerate.jar <root> <dot> <png>");
         return;
      }

      final var root = util.realpath(Paths.get(args[0]));
      final var dot_out = util.realpath(Paths.get(args[1]));
      final var png_out = util.realpath(Paths.get(args[2]));

      out.println(""
         + "<root>   = " + root + '\n'
         + ".dot out = " + dot_out + '\n'
         + ".png out = " + png_out + '\n'
      );

      final var walked = new FileWalker(root, "class");
      final var parents = (
         Stream
            .concat(Stream.of(root), walked.parents.stream())
            .map(util::realpath)
      );

      final var classpath = Loader.convertPaths(parents);
      final var loader = new Loader(classpath);

      final Stream<Class<?>> classes = (
         walked.filesFound.stream()
            .map(Path::getFileName)
            .map(Path::toString)
            .map(util::basename)
            .map(loader::load)
            .filter(Result::is_ok)
            .map(Result::unwrap)
      );

      final var dotfile = new DOTFile(dot_out, "UML", classes);
      if (util.cancel(dot_out)) {
         return;
      }

      try {
         dotfile.write();
      } catch (IOException e) {
         err.println("cli: error writing to " + dot_out);
         err.println(e);
      }
      out.println("wrote " + dot_out);

      if (util.cancel(png_out)) {
         return;
      }

      final var graphviz = new ProcessBuilder(
         "wsl", "dot", "-Tpng", "-Gdpi=300",
         util.wslpath(dot_out), "-o", util.wslpath(png_out)
      );
      graphviz.inheritIO();
      try {
         final var process = graphviz.start();
         Result.ignore(process::waitFor);
      } catch (Throwable e) {
         err.println("cli: error in starting the graphviz process");
         err.println(e);
      }

      out.println("wrote " + png_out);
   }
}
