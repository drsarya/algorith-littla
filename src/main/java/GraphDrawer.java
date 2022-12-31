import static ru.vsu.Utils.INF;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import ru.vsu.Edge;
import ru.vsu.Main;
import ru.vsu.utils.JTableUtils;

public class GraphDrawer extends JFrame {

    private JPanel jPanel;
    private JButton getPath;
    private JPanel main;
    private JTable table1;

    public static void main(String[] args) {
        GraphDrawer gd = new GraphDrawer();
        gd.setVisible(true);
    }

    public GraphDrawer() {
        this.setTitle("List");
        this.setContentPane(main);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setBackground(Color.WHITE);
        this.setSize(1500, 750);

        getPath.addActionListener(actionEvent -> {
            String[][] values = JTableUtils.readStringMatrixFromJTable(table1);
            int[][] matrix = convertToIntMatrix(values);
            minPath = Main.getMinPath(matrix);
            repaint();

        });
        jPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("mouseClicked " + e.getX() + " " + e.getY());
                x = e.getX();
                y = e.getY();
                 revalidate();
                repaint();
            }
        });

    }

    private int[][] convertToIntMatrix(String[][] values) {
        int[][] matrix = new int[values.length][values.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (values[i][j].equals("INF")) {
                    matrix[i][j] = INF;
                } else {
                    matrix[i][j] = Integer.parseInt(values[i][j]);
                }
            }
        }
        return matrix;
    }

    private Integer x;
    private Integer y;
    private final int RADIUS = 15;
    private Map<Integer, Point> vertexes = new HashMap<>();
    private List<Edge> minPath = new ArrayList<>();

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < minPath.size(); i++) {
            Point p1 = vertexes.get(minPath.get(i).row);
            Point p2 = vertexes.get(minPath.get((i + 1) % minPath.size()).row);
            g.setColor(Color.GREEN);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        minPath = new ArrayList<>();

        if (x != null && y != null) {
            vertexes.put(vertexes.size(), new Point(x, y + 2 * RADIUS));
            x = null;
            y = null;
        }
        for (int i = 0; i < vertexes.size(); i++) {
            g.setColor(Color.RED);
            g.drawOval(vertexes.get(i).x, vertexes.get(i).y, RADIUS, RADIUS);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(i), vertexes.get(i).x, vertexes.get(i).y);
        }
        String[][] valueVertexes = new String[vertexes.size()][vertexes.size()];
        for (int i = 0; i < vertexes.size(); i++) {
            for (int j = 0; j < vertexes.size(); j++) {
                if (i == j) {
                    valueVertexes[i][j] = "INF";
                } else {
                    valueVertexes[i][j] = String.valueOf(getDistance(vertexes.get(i), vertexes.get(j)));
                }
            }
        }
        JTableUtils.writeArrayToJTable(table1, valueVertexes);
    }

    private int getDistance(Point p1, Point p2) {
        return (int) Math.sqrt(Math.pow(Math.abs(p1.x - p2.x), 2) + Math.pow(Math.abs(p1.y - p2.y), 2));
    }

    public record Point(int x, int y) {

    }


}
