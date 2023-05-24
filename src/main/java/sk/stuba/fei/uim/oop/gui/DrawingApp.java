import javax.swing.*;

public class DrawingApp extends JFrame {

    public DrawingApp() {
        // Создаем новый фрейм
        JFrame appFrame = new JFrame("Drawing a Square");
        var rectLogic = new CanvasLogic(appFrame);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(600, 600);

        // Добавляем панель, на которой будем рисовать


        // Показываем фрейм
        appFrame.setResizable(false);
        appFrame.setLocationRelativeTo(null);
        appFrame.setLocation(300, 100);
        appFrame.setVisible(true);
        appFrame.setFocusable(true);
        appFrame.requestFocusInWindow();
        appFrame.setVisible(true);
    }
}