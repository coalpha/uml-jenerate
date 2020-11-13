import java.util.Arrays;
import java.util.stream.Stream;

abstract class Node {
   Class<?> clazz;

   static boolean good_class(final Node node) {
      if (node.clazz == null) {
         return false;
      }

      if (node.clazz.equals(Object.class)) {
         return false;
      }

      return true;
   }

   String name() {
      return fmt.str(this.clazz);
   }

   String dot_symbol() {
      return '"' + this.name() + '"';
   }

   Stream<Class<?>> referenced() {
      final var superClazz = Stream.of(this.clazz.getSuperclass());
      final var interfaces = Arrays.stream(this.clazz.getInterfaces());
      return Stream.concat(superClazz, interfaces);
   }

   @Override
   public abstract String toString();

   @Override
   public boolean equals(final Object obj) {
      if (obj == this) {
         return true;
      }

      if (obj instanceof Node) {
         Node node = (Node) obj;
         return this.clazz.equals(node.clazz);
      }

      return false;
   };
}
