import java.util.StringJoiner;

import java.lang.reflect.*;
import static java.lang.reflect.Modifier.*;

interface fmt {
   /** "borrowed" from OpenJDK */
   static char viz(int mod) {
      if ((mod & PUBLIC) != 0) {
         return '+';
      } else
      if ((mod & PROTECTED) != 0) {
         return '#';
      } else
      if ((mod & PRIVATE) != 0) {
         return '-';
      } else {
         return '~';
      }
   }

   static String str(final int mod) {
      final var sb = new StringBuilder(16);
      final var sj = new StringJoiner(" ");

      sb.append(viz(mod));

      if ((mod & ABSTRACT) != 0)      sj.add("abstract");
      if ((mod & STATIC) != 0)        sj.add("static");
      if ((mod & FINAL) != 0)         sj.add("final");
      if ((mod & TRANSIENT) != 0)     sj.add("transient");
      if ((mod & VOLATILE) != 0)      sj.add("volatile");
      if ((mod & SYNCHRONIZED) != 0)  sj.add("synchronized");
      if ((mod & NATIVE) != 0)        sj.add("native");
      if ((mod & STRICT) != 0)        sj.add("strictfp");
      if ((mod & INTERFACE) != 0)     sj.add("interface");

      if (sj.length() > 0) {
         sb
            .append(sj)
            .append(' ');
      }

      return sb.toString();
   }

   static String str(final Class<?> type) {
      // because interfaces have a superclass that is null
      if (type == null) {
         return "";
      }

      final var sn = type.getSimpleName();
      if (sn == null) {
         return type.getCanonicalName();
      }
      return sn;
   }

   static String str(final Parameter p) {
      final var sb = new StringBuilder(16);
      sb
         .append(p.getName())
         .append(": ")
         // most uml does not include this
         // .append(str(p.getModifiers()))
         .append(str(p.getType()));
      return sb.toString();
   }

   static String str(final Method m) {
      final var sb = new StringBuilder(128);
      sb
         .append(str(m.getModifiers()))
         .append(m.getName())
         .append('(');
      final var sj = new StringJoiner(", ");
      for (var param : m.getParameters()) {
         sj.add(str(param));
      }
      sb
         .append(sj)
         .append("): ")
         .append(str(m.getReturnType()));
      return sb.toString();
   }

   static String str(final Field f) {
      final var sb = new StringBuilder(64);
      sb
         .append(str(f.getModifiers()))
         .append(f.getName())
         .append(": ")
         .append(str(f.getType()));
      return sb.toString();
   }
}
