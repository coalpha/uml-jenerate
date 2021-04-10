package umlj;

import java.util.Arrays;
import java.lang.reflect.Method;

final class InnerNode extends Node {
   final String name;

   InnerNode(final LoadedClass lc) {
      super.clazz = lc.clazz;
      this.name = lc.how.className;
   }

   @Override
   String name() {
      return this.name;
   }

   @Override
   public String toString() {
      final var sb = new StringBuilder(0xFF);

      sb
         .append(super.dot_symbol())
         .append(" [\n   label=\"{\n      ")
         .append(this.name())
         .append('\n');

      final var fields = super.fields();
      if (fields.length != 0) {
         sb.append("      |\n");
      }
      for (final var field : fields) {
         sb
            .append("      ")
            .append(fmt.str(field))
            .append("\\l\n");
      }

      final var methods = (
         Arrays.stream(super.methods())
            .filter(m -> !m.getName().startsWith("lambda$"))
            .toArray(Method[]::new)
      );

      if (methods.length != 0) {
         sb.append("      |\n");
      }
      for (final var method : methods) {
         if (method.getName().startsWith("lambda$")) {
            continue;
         }
         sb
            .append("      ")
            .append(fmt.str(method))
            .append("\\l\n");
      }

      sb.append("   }\"\n]");
      return sb.toString();
   }
}
