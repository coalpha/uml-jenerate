final class OuterNode extends Node {
   OuterNode(final Class<?> clazz) {
      super.clazz = clazz;
   }

   @Override
   public String toString() {
      final var sb = new StringBuilder(0xFF);

      sb
         .append(super.dot_symbol())
         .append(" [\n   label=\"{")
         .append(super.name())
         .append("}\"\n]");

      return sb.toString();
   }
}
