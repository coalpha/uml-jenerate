final class OuterNode extends Node {
   OuterNode(final Class<?> clazz) {
      super.clazz = clazz;
   }

   @Override
   String name() {
      try {
         return fmt.str(this.clazz);
      } catch (NoClassDefFoundError e) {
         return "?";
      }
   }

   @Override
   public String toString() {
      final var sb = new StringBuilder(0xFF);

      sb
         .append(super.dot_symbol())
         .append(" [\n   label=\"{")
         .append(this.name())
         .append("}\"\n]");

      return sb.toString();
   }
}
