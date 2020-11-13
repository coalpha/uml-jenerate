import java.util.Arrays;
import java.util.stream.Stream;

abstract class Node {
   Class<?> clazz;

   Stream<Class<?>> referenced() {
      final var superClazz = Stream.of(clazz.getSuperclass());
      final var interfaces = Arrays.stream(clazz.getInterfaces());
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
