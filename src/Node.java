import java.util.Arrays;
import java.util.stream.Stream;

import java.lang.reflect.*;

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

   abstract String name();

   String dot_symbol() {
      return '"' + this.name() + '"';
   }

   Field[] fields() {
      try {
         return this.clazz.getDeclaredFields();
      } catch (NoClassDefFoundError e) {
         return new Field[0];
      }
   }

   Method[] methods() {
      try {
         return this.clazz.getDeclaredMethods();
      } catch (NoClassDefFoundError e) {
         return new Method[0];
      }
   }

   Stream<Class<?>> referenced() {
      final var superClazz = Stream.of(this.clazz.getSuperclass());
      final var interfaces = Arrays.stream(this.clazz.getInterfaces());
      return Stream.concat(superClazz, interfaces);
   }

   @Override
   public abstract String toString();
}
