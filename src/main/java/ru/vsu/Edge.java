package ru.vsu;

import java.util.ArrayList;
import java.util.List;

/**
 * Описывает ребро выбранного пути
 */
public class Edge implements Comparable<Edge> {

    /**
     * Из какой вершины
     */
    public int row;
    /**
     * В какую вершину
     */
    public int column;

    @Override
    public String toString() {
        return "Edge{" + "row=" + row + ", col=" + column + '}';
    }

    public Edge(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Edge() {
    }

    @Override
    public int compareTo(Edge o) {
        return 0;
    }

    public static List<Edge> refreshEdges(List<Edge> edges) {
        List<Edge> res = new ArrayList<>();
        if (edges.isEmpty()) {
            return res;
        }
        res.add(edges.get(0));
        for (int i = 0; i < edges.size() - 1; i++) {
            for (int j = 0; j < edges.size(); j++) {
                if (res.get(res.size() - 1).column == edges.get(j).row) {
                    res.add(edges.get(j));
                    break;
                }
            }
        }
        return res;
    }
}
