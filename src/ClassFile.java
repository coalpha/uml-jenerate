import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import static java.util.Spliterator.*;

/**
 * This ClassFile represents a literal file that was found on disc.
 * It is responsible for producing various AB_Entries for the Loader.
 */

final class ClassFile implements Iterable<AB_Entry> {
   final Path path;
   final int nameCount;

   ClassFile(final Path p) {
      this.path = p.toAbsolutePath();
      this.nameCount = p.getNameCount();
   }

   /**
    * This method is for splitting a path into an AB_Entry, which is a classname
    * plus the context/classpath.
    * This method is only really used by ClassFileIter when it enumerates all
    * possible AB_Entries.
    * For example: Let's say that you have a ClassFile `foo/bar/baz.class`.
    * <pre>
    *new ClassFile("foo/bar/baz.class").chopAt(1) //> ["foo", "bar.baz"]
    * </pre>
    */
   AB_Entry chopAt(final int idx) {
      // we trust that ClassFileIter doesn't give too high of an index.

      // BUG:
      // On windows, Path#subpath(0) does not include the drive
      // @see https://stackoverflow.com/questions/49118306/
      final var left = this.path.subpath(0, idx);

      final var context = this.path.getRoot().resolve(left);

      final var right = this.path.subpath(idx, this.nameCount);
      // the directory count is one less than the name count
      // where in "foo/bar/baz.class", [foo, bar, baz.class] are names.
      final var rightDirectoryCount = right.getNameCount() - 1;
      final var className = new StringJoiner(".");

      // the point is basically to replace the path separators with dots.
      for (var i = 0; i < rightDirectoryCount - 1; ++i) {
         className.add(right.getName(i).toString());
      }

      // this would be "bar.class" in "foo/bar/baz.class"
      final var filename = right.getFileName().toString();
      className.add(util.basename(filename));

      return new AB_Entry(context, className.toString());
   }

   // the rest of these aren't really important to understand.
   // They just make it easier to stream a ClassFile into many AB_Entries.

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
