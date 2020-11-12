import java.util.*;
import static java.lang.System.*;

interface Chooser {
   static <T> T choose(final T[] choices) {
      var sc = new Scanner(System.in);

      var i = 0;
      var len = choices.length;
      var fmt = "%" + len + "d: %s\n";
      for (var choice : choices) {
         out.printf(fmt, i++, choice);
      }

      String line;
      int selectedIndex;
      while (true) {
         out.print("> ");

         try {
            line = sc.nextLine();
         } catch (Throwable t) {
            err.println(t.getMessage());
            continue;
         }

         try {
            selectedIndex = Integer.parseInt(line);
         }  catch (NumberFormatException e) {
            err.println("Could not parse '" + line + "' as integer");
            continue;
         }

         if (selectedIndex == -1) {
            return null;
         }

         if (selectedIndex < 0) {
            err.println("The selected index must be a positive integer");
            continue;
         }

         if (selectedIndex >= len) {
            err.println("The selected index must be less than " + len);
            continue;
         }

         break;
      }

      return choices[selectedIndex];
   }
}
