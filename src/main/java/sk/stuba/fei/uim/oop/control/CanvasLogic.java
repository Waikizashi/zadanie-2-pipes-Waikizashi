import lombok.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;


@Getter
@Setter
public class CanvasLogic extends UniversalAdapter {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private static final int MIN_SIZE = 100;
    private static final int MAX_SIZE = 500;
    private static final int COLOR_COUNT = 3;
    //private JSlider slider;
    private List<Color> colors;
    private Color currentColor;
    private int currentColorIndex;
    private JPanel menu;
    private Canvas canvas;
    private Mode currentMode;
    private Tree selectedTree; // Выбранное дерево
    private Point previousMousePosition;

    public CanvasLogic(JFrame mainFrame){
        this.currentColorIndex = 0;
        this.colors = Arrays.asList(Color.GREEN, Color.BLUE, Color.RED);
        this.currentMode = Mode.ADD;
        this.canvas = new Canvas();
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        mainFrame.add(canvas, BorderLayout.CENTER);
        mainFrame.add(this.createMenu(), BorderLayout.SOUTH);
    }

    public JPanel createMenu(){
        var menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        var buttonPanel = new JPanel(); // новая панель для кнопок

        var addButton = new JButton("add");
        var moveButton = new JButton("move");
        var resizeButton = new JButton("resize");
        var colorButton = new JButton("color");
        colorButton.setBackground(colors.get(currentColorIndex));
        var cleanButton = new JButton("clean");

        // Создаем слайдер
        var slider = new JSlider(0, COLOR_COUNT - 1, 0);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(1);
        // Создаем новые JLabel
        var sliderModeLabel = new JLabel("Color mode");
        var sliderValueLabel = new JLabel("Current color: " + getColorFromSlider(slider.getValue()).toString());

        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                switch (currentMode) {
                    case RESIZE:
                        sliderModeLabel.setText("Resize mode");
                        sliderValueLabel.setText("Current size: " + slider.getValue());
                        break;
                    case ADD:
                        sliderModeLabel.setText("Color mode");
                        sliderValueLabel.setText("Current color: " + getColorFromSlider(slider.getValue()).toString());
                        break;
                    default:
                        sliderValueLabel.setText("");
                }
            }
        });



        addButton.addActionListener(e -> currentMode = Mode.ADD);
        moveButton.addActionListener(e -> currentMode = Mode.MOVE);
        resizeButton.addActionListener(e -> {
            currentMode = Mode.RESIZE;
            //slider.setMinimum(MIN_SIZE);
            //slider.setMaximum(MAX_SIZE);

        });
        colorButton.addActionListener(e -> {
//            slider.setMinimum(0);
//            slider.setMaximum(COLOR_COUNT - 1);
            currentColorIndex = (currentColorIndex + 1) % colors.size(); // переключение на следующий цвет
            colorButton.setBackground(colors.get(currentColorIndex));

        });
        cleanButton.addActionListener(e -> {
            canvas.removeAll();
            canvas.repaint();
        });

        buttonPanel.add(addButton);
        buttonPanel.add(moveButton);
        buttonPanel.add(resizeButton);
        buttonPanel.add(colorButton);
        buttonPanel.add(cleanButton);

        menu.add(slider);
        menu.add(sliderModeLabel);
        menu.add(sliderValueLabel);
        menu.add(buttonPanel); // добавляем панель с кнопками в меню

        return menu;
    }
    public Color getCurrentColor() {
        return colors.get(currentColorIndex);
    }
    private Color getColorFromSlider(int sliderValue) {
        switch (sliderValue) {
            case 1:
                return Color.RED;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.BLUE;
            default:
                return Color.BLACK;
        }
    }

    private void exit() {
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                exit();
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (currentMode) {
            case ADD:
                Tree tree = new Tree();
                tree.setColor(getCurrentColor()); // Установка цвета дерева
                tree.setBounds(e.getX() - DEFAULT_WIDTH / 2, e.getY() - DEFAULT_HEIGHT / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                canvas.add(tree);
                canvas.repaint();
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        previousMousePosition = e.getPoint();
        selectedTree = null;
        for (Component component : canvas.getComponents()) {
            if (component instanceof Tree && component.getBounds().contains(previousMousePosition)) {
                selectedTree = (Tree) component;
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        selectedTree = null;
        previousMousePosition = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedTree != null && previousMousePosition != null) {
            switch (currentMode) {
                case MOVE:
                    int dx = e.getX() - previousMousePosition.x;
                    int dy = e.getY() - previousMousePosition.y;
                    Rectangle bounds = selectedTree.getBounds();
                    selectedTree.setBounds(bounds.x + dx, bounds.y + dy, bounds.width, bounds.height);
                    break;
                case RESIZE:
                    selectedTree.setSize(e.getX(), e.getY());
                    break;
            }
            canvas.repaint();
            previousMousePosition = e.getPoint();
        }
    }
}
