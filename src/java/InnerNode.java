class InnerNode extends Node {
   InnerNode(final Class<?> clazz) {
      super.clazz = clazz;
   }

   @Override
   public String toString() {
      final var sb = new StringBuilder(0xFF);

      sb
         .append(super.dot_symbol())
         .append(" [\n   label=\"{\n      ")
         .append(super.name())
         .append('\n');

      final var fields = super.clazz.getDeclaredFields();
      if (fields.length != 0) {
         sb.append("      |\n");
      }
      for (final var field : fields) {
         sb
            .append("      ")
            .append(fmt.str(field))
            .append("\\l\n");
      }

      final var methods = super.clazz.getDeclaredMethods();
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
