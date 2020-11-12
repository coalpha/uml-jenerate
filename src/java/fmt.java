import java.util.StringJoiner;

import java.lang.reflect.*;
import static java.lang.reflect.Modifier.*;

interface fmt {
   /** "borrowed" from OpenJDK */
   static String str(int mod) {
      var sj = new StringJoiner(" ");

      if ((mod & PUBLIC) != 0) {
         sj.add("+");
      } else
      if ((mod & PROTECTED) != 0) {
         sj.add("#");
      } else
      if ((mod & PRIVATE) != 0) {
         sj.add("-");
      } else {
         sj.add("~");
      }

      if ((mod & ABSTRACT) != 0)      sj.add("abstract");
      if ((mod & STATIC) != 0)        sj.add("static");
      if ((mod & FINAL) != 0)         sj.add("final");
      if ((mod & TRANSIENT) != 0)     sj.add("transient");
      if ((mod & VOLATILE) != 0)      sj.add("volatile");
      if ((mod & SYNCHRONIZED) != 0)  sj.add("synchronized");
      if ((mod & NATIVE) != 0)        sj.add("native");
      if ((mod & STRICT) != 0)        sj.add("strictfp");
      if ((mod & INTERFACE) != 0)     sj.add("interface");
      return sj.toString();
   }

   static String str(final Class<?> type) {
      var sn = type.getSimpleName();
      if (sn == null) {
         return type.getCanonicalName();
      }
      return sn;
   }

   static String str(final Parameter p) {
      var sb = new StringBuilder(16);
      sb
         .append(p.getName())
         .append(": ")
         .append(str(p.getType()));
      return sb.toString();
   }

   static String str(final Method m) {
      var sb = new StringBuilder(80);
      sb
         .append(str(m.getModifiers()))
         .append(' ')
         .append(m.getName())
         .append('(');
      var sj = new StringJoiner(", ");
      for (var param : m.getParameters()) {
         sj.add(str(param));
      }
      sb
         .append(sj)
         .append("): ")
         .append(str(m.getReturnType()));
      return sb.toString();
   }
}
