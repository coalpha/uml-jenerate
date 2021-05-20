package umlj;

import java.util.Arrays;

import org.objectweb.asm.*;
import org.objectweb.asm.signature.SignatureReader;

interface TypeDescriptor {}

class Boolean_t implements TypeDescriptor {}
class Char_t    implements TypeDescriptor {}
class Byte_t    implements TypeDescriptor {}
class Short_t   implements TypeDescriptor {}
class Int_t     implements TypeDescriptor {}
class Float_t   implements TypeDescriptor {}
class Long_t    implements TypeDescriptor {}
class Double_t  implements TypeDescriptor {}

class Object_t implements TypeDescriptor {
   
}
record Array_t(TypeDescriptor elem) implements TypeDescriptor {}

public class ClassCollector extends ClassVisitor {
   public ClassCollector() {
      super(Opcodes.ASM8);
   }

   String name;
   Object_t superClass = null;
   Object_t[] interfaces;

   public void visit(
      int version,
      int access,
      String name,
      String sig,
      String superName,
      String[] interfaces
   ) {
      this.name = name;
      this.superClass = new Object_t(superName);

      // todo: parse signatures
      if (interfaces == null) {
         this.interfaces = new Object_t[0];
      } else {
         this.interfaces = (
            (Object_t []) Arrays.stream(interfaces).map(Object_t::new).toArray()
         );
      }
   }

   // public FieldVisitor visitField(
   //    int access,
   //    String name,
   //    String desc,
   //    String sig,
   //    Object default_value
   // )
   // {
   //    System.out.printf(
   //       """
   //       field {
   //          access  = %d
   //          name    = %s
   //          desc    = %s
   //          sig     = %s
   //          default = %s
   //       }
   //       """
   //       , access
   //       , name
   //       , desc
   //       , sig
   //       , default_value
   //    );
   //    return null;
   // }

   public MethodVisitor visitMethod(
      int access,
      String name,
      String desc,
      String sig,
      String[] except
   )
   {
      System.out.printf(
         """
         method {
            access = %d
            name   = %s
            desc   = %s
            sig    = %s
         }
         """
         , access
         , name
         , desc
         , sig
      );

      return null;
   }
}
