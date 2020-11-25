import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import static java.util.Spliterator.*;

final class ClassFile implements Iterable<AB_Entry> {
   final Path path;
   final int nameCount;

   static ClassFile make(final Path p) {
      if (p.getFileName().toString().endsWith(".class")) {
         return new ClassFile(p);
      } else {
         return null;
      }
   }

   private ClassFile(final Path p) {
      this.path = p.toAbsolutePath();
      this.nameCount = p.getNameCount();
   }

   AB_Entry chopAt(final int idx) {
      final var left = this.path.subpath(0, idx);

      final var right = this.path.subpath(idx, this.nameCount);
      final var len = right.getNameCount();
      final var sj = new StringJoiner(".");

      for (var i = 0; i < len - 1; ++i) {
         sj.add(right.getName(i).toString());
      }

      final var filename = right.getFileName().toString();
      sj.add(filename.substring(0, filename.lastIndexOf(".class")));

      return new AB_Entry(left, sj.toString());
   }

   Stream<AB_Entry> stream() {
      return StreamSupport.stream(this.spliterator(), false);
   }

   @Override
   public Iterator<AB_Entry> iterator() {
      return new ClassFileIter(this);
   }

   @Override
   public Spliterator<AB_Entry> spliterator() {
      return Spliterators.spliteratorUnknownSize(
         this.iterator(), 0b0
         | DISTINCT
         | NONNULL
         | ORDERED
         | IMMUTABLE
      );
   }

   @Override
   public String toString() {
      return "ClassFile@" + this.path;
   }
}
