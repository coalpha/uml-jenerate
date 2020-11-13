import java.nio.file.*;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.stream.Collectors;

import opre.Result;
import static opre.Result.*;

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
         .append(" {\n")
         .append("   node [\n")
         .append("      fontname=\"Bitstream Vera Sans\"\n")
         .append("      fontsize=8\n")
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

      final var allNodesLen = allNodes.length;
      final var combinedNodes = new ArrayList<Node>(allNodesLen);

      // dedupe leaving things at the back
      first:
      for (var i = 0; i < allNodesLen; i++) {
         var first = allNodes[i];
         for (var j = i + 1; j < allNodesLen; j++) {
            var second = allNodes[j];
            if (first.equals(second)) {
               continue first;
            }
         }
         combinedNodes.add(first);
      }

      for (final var node : combinedNodes) {
         sb
            .append('\n')
            .append(node.toString().indent(3));
      }

      for (final var first : combinedNodes) {
         var refd = first.referenced().toArray(Class<?>[]::new);

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

   void write() {
      sb.append('}');
      ignore(() -> Files.write(this.outfile, sb.toString().getBytes()));
   }
}
