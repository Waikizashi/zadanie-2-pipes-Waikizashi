package sk.stuba.fei.uim.oop.control;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.board.GameBoard;
import sk.stuba.fei.uim.oop.board.Orientation;
import sk.stuba.fei.uim.oop.board.Pipe;
import sk.stuba.fei.uim.oop.board.Type;

import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameLogic extends UniversalAdapter {
    public static final int INITIAL_BOARD_SIZE = 8;
    @Getter
    @Setter
    private int lvl;
    private int boardSize;
    private Pipe[][] board;
    private boolean[][] visited;
    private int startX, startY;
    private int endX, endY;
    private final int[][] directions;


    private final JFrame mainFrame;
    private GameBoard gameBoard;
    private JLabel levelInfo;
    private final Random random;

    public GameLogic(JFrame mainGame) {
        this.boardSize = INITIAL_BOARD_SIZE;
        lvl = 1;
        directions = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        random = new Random();
        mainFrame = mainGame;
        generateNewGame();
        mainFrame.add(initializeGameBoard(), BorderLayout.CENTER);

    }

    private GameBoard initializeGameBoard(){
        gameBoard = new GameBoard(this);
        gameBoard.addMouseListener(this);
        gameBoard.addMouseMotionListener(this);
        return gameBoard;
    }
    public void generateNewGame() {
        board = new Pipe[boardSize][boardSize];

        startX = 0;
        startY = (int) (Math.random() * boardSize);
        endX = boardSize - 1;
        endY = (int) (Math.random() * boardSize);

        board[startX][startY] = new Pipe(Type.START, Orientation.RIGHT, startX, startY);
        board[startX][startY].setHighlighted(true);

        board[endX][endY] = new Pipe(Type.FINISH,  Orientation.LEFT, endX, endY);
        board[endX][endY].setHighlighted(true);

        generateRandomPath(board[startX][startY], board[endX][endY], 10000, board);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if(board[i][j] == null){
                    Type randomType = Math.random() < 0.5 ? Type.I : Type.L;
                    Orientation randomOrientation = Orientation.values()[(int) (Math.random() * Orientation.values().length)];
                    board[i][j] = new Pipe(randomType, randomOrientation, i, j);
                }
            }
        }
    }

    public void generateRandomPath(Pipe startPipe, Pipe endPipe, int maxSteps, Pipe[][] board) {
        Pipe currentPipe = startPipe;
        int steps = 0;

        while (steps < maxSteps) {
            int randomDirection = random.nextInt(4);
            Type randomType = random.nextBoolean() ? Type.I : Type.L;
            Orientation randomOrientation = Orientation.values()[random.nextInt(4)];

            int newX = currentPipe.getX() + directions[randomDirection][0];
            int newY = currentPipe.getY() + directions[randomDirection][1];
            if (newX < 0 || newX >= board.length || newY < 0 || newY >= board[0].length || board[newX][newY] != null) {
                steps++;
                continue;
            }

            Pipe newPipe = new Pipe(randomType, randomOrientation, newX, newY);
            board[newX][newY] = newPipe;

            boolean isConnected = false;
            for (int i = 0; i < 4; i++) {
                if (canConnect(currentPipe, newX, newY, board)) {
                    isConnected = true;
                    break;
                }
                newPipe.rotateClockwise();
            }

            if (isConnected) {
                currentPipe = newPipe;
                board[newX][newY].setHighlighted(true);
                for (int i = 0; i < random.nextInt(3); i++) {
                    board[newX][newY].rotateClockwise();
                }
                if (currentPipe.getX() == endPipe.getX() && currentPipe.getY() == endPipe.getY()) {
                    break;
                }
            } else {
                board[newX][newY] = null;
            }

            steps++;
        }

    }




    public boolean canConnect(Pipe currentPipe, int newX, int newY, Pipe[][] board) {
        if (newX < 0 || newX >= board.length || newY < 0 || newY >= board[0].length) {
            return false;
        }

        Pipe nextPipe = board[newX][newY];
        if (nextPipe == null) {
            return false;
        }

        int[] currentConnections = currentPipe.getConnections();
        int[] nextConnections = nextPipe.getConnections();

        if (newX > currentPipe.getX()) {
            return currentConnections[1] == 1 && nextConnections[3] == 1;
        } else if (newX < currentPipe.getX()) {
            return currentConnections[3] == 1 && nextConnections[1] == 1;
        } else if (newY > currentPipe.getY()) {
            return currentConnections[0] == 1 && nextConnections[2] == 1;
        } else {
            return currentConnections[2] == 1 && nextConnections[0] == 1;
        }
    }



    public void rotatePipeAt(int x, int y) {
        if (isValidCoordinate(x, y) && board[x][y] != null) {
            board[x][y].rotateClockwise();
        }
    }

    public Pipe getPipeAt(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return board[x][y];
        }
        return null;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        generateNewGame();
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    public boolean checkPath(){
        return findPath(board[startX][startY], board[endX][endY]);
    }
    public boolean findPath(Pipe start, Pipe end) {
        visited = new boolean[boardSize][boardSize];
        return dfs(start.getX(), start.getY(), end.getX(), end.getY());
    }

    private boolean isValidConnection(int x1, int y1, int x2, int y2) {
        if (x2 < 0 || x2 >= boardSize || y2 < 0 || y2 >= boardSize) {
            return false;
        }

        Pipe pipe1 = board[x1][y1];
        Pipe pipe2 = board[x2][y2];
        if (pipe2 == null) {
            return false;
        }

        int[] connections1 = pipe1.getConnections();
        int[] connections2 = pipe2.getConnections();

        int dx = x2 - x1;
        int dy = y2 - y1;

        if (dx == 1 && dy == 0) {
            return connections1[1] == 1 && connections2[3] == 1;
        } else if (dx == -1 && dy == 0) {
            return connections1[3] == 1 && connections2[1] == 1;
        } else if (dx == 0 && dy == 1) {
            return connections1[2] == 1 && connections2[0] == 1;
        } else if (dx == 0 && dy == -1) {
            return connections1[0] == 1 && connections2[2] == 1;
        }

        return false;
    }
    private boolean dfs(int x, int y, int endX, int endY) {
        if (x < 0 || x >= boardSize || y < 0 || y >= boardSize || visited[x][y]) {
            return false;
        }

        visited[x][y] = true;

        if (x == endX && y == endY) {
            return true;
        }

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (isValidConnection(x, y, newX, newY) && dfs(newX, newY, endX, endY)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                restartGame();
                break;
            case KeyEvent.VK_ESCAPE:
                exitGame();
                break;
            case KeyEvent.VK_ENTER:
                finalCheckPath();
                break;
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / gameBoard.getTileSize();
        int y = e.getY() / gameBoard.getTileSize();
        rotatePipeAt(x, y);
        gameBoard.repaint();
    }
    @Override
    public void mouseMoved (MouseEvent e) {
        gameBoard.setHoverX(e.getX() / gameBoard.getTileSize());
        gameBoard.setHoverY(e.getY() / gameBoard.getTileSize());
        gameBoard.repaint();
    }

    private void restartGame() {
        setLvl(1);
        levelInfo.setText("Level: " + getLvl());
        generateNewGame();
        gameBoard.repaint();
        mainFrame.requestFocusInWindow();
    }

    private void exitGame() {
        System.exit(0);
    }

    private void finalCheckPath() {
        if (checkPath()) {
            JOptionPane.showMessageDialog(mainFrame, "The path is correct! Move on to the next level!");
            generateNewGame();
            gameBoard.repaint();

            setLvl(getLvl() + 1);
            levelInfo.setText("Level: " + getLvl());
        } else {
            JOptionPane.showMessageDialog(mainFrame, "The path is incorrect. Please try again.");
        }
        gameBoard.repaint();
        mainFrame.requestFocusInWindow();
    }

    public JPanel createSideMenu() {
        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.X_AXIS));

        levelInfo = new JLabel("Level: " + getLvl());
        sideMenu.add(levelInfo);

        JLabel boardSizeInfo = new JLabel(" ::: Board Size: 8x8 ::: ");
        sideMenu.add(boardSizeInfo);

        JComboBox<Integer> boardSizeSelector = new JComboBox<>(new Integer[]{8, 10, 12});
        boardSizeSelector.addActionListener(e -> {
            Integer selectedSize = (Integer) boardSizeSelector.getSelectedItem();
            if (selectedSize != null) {
                int newSize = selectedSize;
                setBoardSize(newSize);
                gameBoard.repaint();
                boardSizeInfo.setText(" ::: Board Size: " + newSize + "x" + newSize + " ::: ");
                updateWindowSize(sideMenu);
            }
        });
        sideMenu.add(boardSizeSelector);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> restartGame());
        sideMenu.add(resetButton);

        JButton checkButton = new JButton("Check");
        checkButton.addActionListener(e -> {
            finalCheckPath();
            gameBoard.repaint();
        });
        sideMenu.add(checkButton);

        return sideMenu;
    }

    public void updateWindowSize(JPanel sideMenu) {
        int boardSize = getBoardSize();
        int totalSize = boardSize * gameBoard.getTileSize();
        Insets insets = mainFrame.getInsets();
        int windowWidth = totalSize + insets.left + insets.right;
        int windowHeight = totalSize + insets.top + insets.bottom + sideMenu.getPreferredSize().height;
        mainFrame.setSize(windowWidth, windowHeight);
        mainFrame.setVisible(true);
        mainFrame.requestFocusInWindow();
    }
}
