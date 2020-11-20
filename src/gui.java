import java.awt.*;
import javax.swing.*;

import com.bulenkov.darcula.DarculaLaf;

import opre.Result;

@SuppressWarnings("serial")
final class gui extends JFrame {
   static {
      var darcula = new DarculaLaf();
      Result.ignore(() -> UIManager.setLookAndFeel(darcula));
   }

   static private final Font mono = new Font(Font.MONOSPACED, Font.BOLD, 14);
   static private final String TOP = SpringLayout.NORTH;
   static private final String RIGHT = SpringLayout.EAST;
   static private final String BOTTOM = SpringLayout.SOUTH;
   static private final String LEFT = SpringLayout.WEST;
   static private final String H_CENTER = SpringLayout.HORIZONTAL_CENTER;

   private final SpringLayout layout = new SpringLayout();
   private Container contentPain;
   private int oy = 8;

   JTextField lbltxt(final String name) {
      final var lbl = new JLabel(name);
      lbl.setFont(mono);
      super.add(lbl);

      final var txt = new JTextField(0);
      txt.setFont(mono);
      super.add(txt);

      layout.putConstraint(LEFT, lbl, 10, LEFT, contentPain);
      layout.putConstraint(TOP, lbl, oy + 2, TOP, contentPain);

      layout.putConstraint(LEFT, txt, 0, RIGHT, lbl);
      // this doesn't work and I have no idea why
      layout.putConstraint(RIGHT, txt, -10, RIGHT, contentPain);
      layout.putConstraint(TOP, txt, oy, TOP, contentPain);

      oy += 30;
      return txt;
   }

   gui() {
      contentPain = super.getContentPane();
      contentPain.setLayout(layout);

      lbltxt("graphviz: ");
      lbltxt("root: ");
      lbltxt(".dot: ");
      lbltxt(".png: ");

      var b = new JButton("ok do it");
      super.add(b);
      layout.putConstraint(H_CENTER, b, 0, H_CENTER, contentPain);
      layout.putConstraint(BOTTOM, b, -8, BOTTOM, contentPain);
      // b.addActionListener(() -> );

      super.pack();
      super.setMinimumSize(new Dimension(400, 260));
      super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setVisible(true);

   }

}
