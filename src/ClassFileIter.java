import java.util.Iterator;

final class ClassFileIter implements Iterator<AB_Entry> {
   private int cursor;
   private final ClassFile underlaying;

   ClassFileIter(ClassFile underlaying) {
      this.underlaying = underlaying;
      this.cursor = underlaying.nameCount - 1;
   }

   @Override
   public boolean hasNext() {
      return this.cursor > 1;
   }

   @Override
   public AB_Entry next() {
      return this.underlaying.chopAt(cursor--);
   }
}
