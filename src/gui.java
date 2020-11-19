import javax.swing.*;
import com.bulenkov.darcula.DarculaLaf;

import opre.Result;

final class gui extends JFrame {
   static {
      var darcula = new DarculaLaf();
      Result.ignore(() -> UIManager.setLookAndFeel(darcula));
   }

   gui() {
      setVisible(true);
      setSize(400, 300);
      var b = new JButton("pain");
      super.add(b);
   }
}
