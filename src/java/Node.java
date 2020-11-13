import java.util.Arrays;
import java.util.stream.Stream;

abstract class Node {
   static boolean eq(final Node a, final Node b) {
      return a.clazz.equals(b.clazz);
   };

   static boolean good_class(final Node node) {
      if (node.clazz == null) {
         return false;
      }

      if (node.clazz.equals(Object.class)) {
         return false;
      }

      return true;
   }

   Class<?> clazz;

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
}
