class OuterNode extends Node {
   OuterNode(final Class<?> clazz) {
      super.clazz = clazz;
   }

   @Override
   public String toString() {
      final var sb = new StringBuilder(0xFF);

      final var name = fmt.str(super.clazz);
      sb
         .append(name)
         .append(" [\n   label=\"{")
         .append(name)
         .append("}\"\n]");

      return sb.toString();
   }
}
