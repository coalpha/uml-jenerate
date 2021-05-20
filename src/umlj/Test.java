package umlj;

import java.nio.file.*;
import java.util.stream.Collectors;

import org.objectweb.asm.*;

public class Test {
   public static void main(String[] args) {
      final var classes = ClassFiles.classFiles(Paths.get("")).collect(Collectors.toUnmodifiableList());
      for (var path : classes) {
         try {
            var is = Files.newInputStream(path);
            var cr = new ClassReader(is);
            cr.accept(new ClassCollector(), 0);
            System.out.println(cr.getClassName());
         } catch (Throwable e) {}
      }
   }
}
