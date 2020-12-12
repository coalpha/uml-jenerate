import java.util.Iterator;

/**
 * This is the
 * iterator that is turned into a
 * spliterator that is used inside a
 * stream that is used inside the
 * Loader that loaded the
 * house that Jack built.
 */
final class ClassFileIter implements Iterator<AB_Entry> {
   private int cursor;
   private final ClassFile underlaying;

   ClassFileIter(final ClassFile underlaying) {
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
