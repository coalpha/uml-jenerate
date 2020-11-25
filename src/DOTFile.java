import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.Collectors;

final class DOTFile {
   private final StringBuilder sb = new StringBuilder(0x1000);
   private final Path outfile;

   DOTFile(
      final Path outfile,
      final String name,
      final Stream<LoadedClass> classes
   ) {
      this.outfile = outfile;
      this.sb
         .append("digraph ")
         .append(name)
         .append(" {\n")
         .append("   node [\n")
         .append("      fontname=\"Bitstream Vera Sans\"\n")
         .append("      fontsize=14\n")
         .append("      shape=record\n")
         .append("   ]\n");

      final var innerNodes = (
         classes
            .map(InnerNode::new)
            .collect(Collectors.toUnmodifiableList())
      );

      final var outerNodes = (
         innerNodes
            .stream()
            .flatMap(node -> node.referenced())
            .map(OuterNode::new)
      );

      final var allNodes = (
         // outer classes at the front
         Stream.concat(outerNodes, innerNodes.stream())
            .filter(Node::good_class)
            .toArray(Node[]::new)
      );

      final var combinedNodes = util.dedupe_right(allNodes, Node::eq);

      for (final var node : combinedNodes) {
         sb
            .append('\n')
            .append(node.toString().indent(3));
      }

      for (final var first : combinedNodes) {
         final var refd = first.referenced().toArray(Class<?>[]::new);

         for (final var second : combinedNodes) {
            for (final var ref : refd) {
               if (second.clazz.equals(ref)) {
                  sb
                     .append("   ")
                     .append(second.dot_symbol())
                     .append(" -> ")
                     .append(first.dot_symbol())
                     .append('\n');
               }
            }
         }
      }
   }

   void write() throws IOException {
      sb.append("}\n");
      Files.write(this.outfile, sb.toString().getBytes());
   }
}
