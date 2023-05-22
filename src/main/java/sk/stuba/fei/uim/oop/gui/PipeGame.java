package sk.stuba.fei.uim.oop.gui;
import sk.stuba.fei.uim.oop.control.GameLogic;

import javax.swing.*;
import java.awt.*;

public class PipeGame {

    public PipeGame() {
        JFrame frame = new JFrame("Pipe Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameLogic gameLogic = new GameLogic(frame);
        frame.add(gameLogic.createSideMenu(), BorderLayout.NORTH);
        frame.addKeyListener(gameLogic);
        frame.setSize(496,545);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLocation(300, 100);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }

}
