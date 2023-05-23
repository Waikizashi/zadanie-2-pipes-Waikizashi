import javax.swing.*;
import java.awt.*;

public class Tree extends JPanel {
    private Color color;
    public Tree() {
        this.color = Color.BLUE;
        this.setOpaque(false); // Делаем фон панели прозрачным
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(this.color);
        int ovalWidth = getWidth(); // Ширина овала равна ширине панели
        int ovalHeight = getHeight() / 2; // Высота овала равна половине высоты панели
        int squareSize = getWidth() / 3; // Размер стороны квадрата равен половине ширины панели
        // Овал рисуется от верхнего левого угла панели с размерами, вычисленными выше
        g.fillOval(0, 0, ovalWidth, ovalHeight);
        // Квадрат рисуется ниже овала (поэтому его верхний край равен высоте овала)
        // и по центру панели (поэтому его левый край равен половине ширины панели минус половина размера квадрата)
        g.fillRect(getWidth() / 2 - squareSize / 2, ovalHeight-ovalWidth/6, squareSize, squareSize+ovalWidth/6);
    }
    public void setColor(Color color) {
        this.color = color;
    }

}
