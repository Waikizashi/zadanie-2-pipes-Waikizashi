package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.control.GameLogic;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel {
    private final GameLogic gameLogic;
    @Getter
    private final int tileSize;
    @Getter
    @Setter
    private int hoverX = -1;
    @Getter
    @Setter
    private int hoverY = -1;

    public GameBoard(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        this.tileSize = 60;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        for (int i = 0; i <= gameLogic.getBoardSize(); i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, gameLogic.getBoardSize() * tileSize);
            g.drawLine(0, i * tileSize, gameLogic.getBoardSize() * tileSize, i * tileSize);
        }
        if (hoverX >= 0 && hoverY >= 0 && hoverX < gameLogic.getBoardSize() && hoverY < gameLogic.getBoardSize()) {
            g.setColor(new Color(170, 216, 255, 128));
            g.fillRect(hoverX * tileSize, hoverY * tileSize, tileSize, tileSize);
        }

        for (int i = 0; i < gameLogic.getBoardSize(); i++) {
            for (int j = 0; j < gameLogic.getBoardSize(); j++) {
                Pipe pipe = gameLogic.getPipeAt(i, j);

                if (pipe != null) {
                    drawPipe(g, pipe, i * tileSize, j * tileSize, tileSize, pipe.isHighlighted());
                }
            }
        }

    }


    private void drawPipe(Graphics g, Pipe pipe, int x, int y, int tileSize, boolean isHighlighted) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isHighlighted) {
            g2d.setColor(new Color(255, 255, 0, 128));
            g2d.fillRect(x, y, tileSize, tileSize);
        }

        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(8));

        int padding = 4;

        if (pipe.getType() == Type.START || pipe.getType() == Type.FINISH) {
            g2d.setColor(pipe.getType() == Type.START ? Color.RED : Color.GREEN);

            int boardSize = gameLogic.getBoardSize();
            boolean isCorner = (x == 0 || x == tileSize * (boardSize - 1)) && (y == 0 || y == tileSize * (boardSize - 1));

            if (!isCorner || y != 0) {
                g2d.drawLine(x + tileSize / 2, y + tileSize / 2, x + tileSize / 2, y + padding);
            }
            if (!isCorner || y != tileSize * (boardSize - 1)) {
                g2d.drawLine(x + tileSize / 2, y + tileSize / 2, x + tileSize / 2, y + tileSize - padding);
            }

            if (pipe.getType() == Type.START) {
                if (!isCorner || x != tileSize * (boardSize - 1)) {
                    g2d.drawLine(x + tileSize / 2, y + tileSize / 2, x + tileSize - padding, y + tileSize / 2);
                }
            } else {
                if (!isCorner || x != 0) {
                    g2d.drawLine(x + tileSize / 2, y + tileSize / 2, x + padding, y + tileSize / 2);
                }
            }

            g2d.setColor(Color.DARK_GRAY);
        } else if (pipe.getType() == Type.I) {
            if (pipe.getOrientation() == Orientation.UP || pipe.getOrientation() == Orientation.DOWN) {
                g2d.drawLine(x + tileSize / 2, y + padding, x + tileSize / 2, y + tileSize - padding);
            } else {
                g2d.drawLine(x + padding, y + tileSize / 2, x + tileSize - padding, y + tileSize / 2);
            }
        } else if(pipe.getType() == Type.L) {
            switch (pipe.getOrientation()) {
                case RIGHT:
                    g2d.drawLine(x + tileSize / 2, y + tileSize - padding, x + tileSize / 2, y + tileSize / 2);
                    g2d.drawLine(x + tileSize / 2, y + tileSize / 2, x + tileSize - padding, y + tileSize / 2);
                    break;
                case UP:
                    g2d.drawLine(x + tileSize / 2, y + tileSize / 2, x + tileSize / 2, y + padding);
                    g2d.drawLine(x + tileSize / 2, y + tileSize / 2, x + tileSize - padding, y + tileSize / 2);
                    break;
                case LEFT:
                    g2d.drawLine(x + tileSize / 2, y + tileSize / 2, x + tileSize / 2, y + padding);
                    g2d.drawLine(x + tileSize / 2, y + tileSize / 2, x + padding, y + tileSize / 2);
                    break;
                case DOWN:
                    g2d.drawLine(x + tileSize / 2, y + tileSize / 2, x + tileSize / 2, y + tileSize - padding);
                    g2d.drawLine(x + tileSize / 2, y + tileSize / 2, x + padding, y + tileSize / 2);
                    break;
            }
        }
    }


}

