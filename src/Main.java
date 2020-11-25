import static opre.Result.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.file.*;
import static java.lang.System.*;

interface Main {
   static void main(String[] args) {
      if (args.length != 3) {
         err.println("usage: java -jar uml-jenerate.jar <root> <dot> <png>");
         out.println("Switching to interactive mode:");
         args = new String[3];
         args[0] = util.prompt("Root Directory:");
         args[1] = util.prompt(".dot path (leave empty for temp file):");

         if (args[1].isBlank()) {
            final var temp = (
               trycatch(() -> File.createTempFile("temp", ".dot"))
                  .expect("Could not create temporary file")
            );
            temp.deleteOnExit();
            temp.delete();
            args[1] = temp.toString();
         }

         args[2] = util.prompt("png file output:");
      }

      final var root = util.realpath(Paths.get(args[0]));
      final var dot = util.realpath(Paths.get(args[1]));
      final var png = util.realpath(Paths.get(args[2]));

      out.println(""
         + " dir root = " + root + '\n'
         + ".dot path = " + dot + '\n'
         + ".png path = " + png + '\n'
      );

      util.prompt("Press ctrl c to cancel");

      final var classes = (
         ClassFiles.classes(root)
            .<Class<?>>map(Loader::load)
            .filter(Objects::nonNull)
            .collect(Collectors.toUnmodifiableList())
      );

      out.println("Finished loading classes");

      final var dotfile = new DOTFile(dot, "UML", classes.stream());
      if (util.cancel(dot)) {
         out.println("canceled");
         return;
      }

      try {
         dotfile.write();
      } catch (IOException e) {
         err.println("cli: error writing to " + dot);
         err.println(e);
      }
      out.println("wrote " + dot);

      if (util.cancel(png)) {
         out.println("canceled");
         return;
      }

      final var graphviz = new ProcessBuilder(
         "wsl", "dot", "-Tpng", "-Gdpi=300",
         util.wslpath(dot), "-o", util.wslpath(png)
      );
      graphviz.inheritIO();
      try {
         final var process = graphviz.start();
         process.waitFor();
      } catch (Throwable e) {
         err.println("cli: error in starting the graphviz process");
         err.println(e);
      }

      out.println("wrote " + png);
   }
}
