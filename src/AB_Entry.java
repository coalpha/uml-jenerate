import java.nio.file.*;

final class AB_Entry {
   final Path context;

   final String className;

   AB_Entry(final Path context, final String name) {
      this.context = context;
      this.className = name;
   }

   @Override
   public String toString() {
      return (
         new StringBuilder()
            .append('[')
            .append(this.context)
            .append("]: ")
            .append(this.className)
            .toString()
      );
   }
}
