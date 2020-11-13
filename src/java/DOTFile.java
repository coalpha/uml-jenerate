import java.nio.file.*;
import java.util.stream.Stream;

class DOTFile {
   private final StringBuilder sb = new StringBuilder(0x1000);
   private final Path outfile;

   DOTFile(
      final String filename,
      final String name,
      final Stream<Class<?>> classes
   ) {
      outfile = Paths.get(filename);
      this.sb
         .append("digraph ")
         .append(name)
         .append(" {\nnode [\nfontname=\"Bitstream Vera Sans\"\nfontsize=8\nshape=record\n]\n");
   }

   void write() {
      sb.append('}');
      System.out.println(sb.toString());
   }
}
