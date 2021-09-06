package view;

import model.Game;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class Window extends Canvas {
        @Serial
        private static final long serialVersionUID = -784868000698187371L;

    private JMenuItem quit;

        public Window(int width, int height, String title, Game game) {
            JFrame frame = new JFrame(title);

            frame.setPreferredSize(new Dimension(width, height));
            frame.setMaximumSize(new Dimension(width, height));
            frame.setMinimumSize(new Dimension(width, height));

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(game);
            frame.setJMenuBar(getJMenuBar());
            frame.setVisible(true);
            game.start();
        }

        private JMenuBar getJMenuBar() {
            JMenuBar jMenuBar = new JMenuBar();
            JMenu jMenu = new JMenu("Menu");
            quit = new JMenuItem("Quit");
            jMenu.add(quit);
            jMenuBar.add(jMenu);
            return  jMenuBar;
        }

    public JMenuItem getQuit() {
        return quit;
    }
}
