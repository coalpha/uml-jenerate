class InnerNode extends Node {
   InnerNode(final Class<?> clazz) {
      super.clazz = clazz;
   }

   @Override
   public String toString() {
      final var sb = new StringBuilder(0xFF);

      final var name = fmt.str(super.clazz);
      sb
         .append(name)
         .append(" [\n   label=\"{\n      ")
         .append(name)
         .append("\n      |\n");

      final var fields = super.clazz.getDeclaredFields();
      for (var field : fields) {
         sb
            .append("      ")
            .append(fmt.str(field))
            .append("\\l\n");
      }

      sb.append("      |\n");
      final var methods = clazz.getDeclaredMethods();
      for (var method : methods) {
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
