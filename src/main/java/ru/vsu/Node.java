package ru.vsu;

/**
 * Описывает часть матрицы пути
 */
public class Node implements Comparable<Node> {
    /**
     * Вес, определяется расстоянием между городами
     */
    public Integer weight;
    /**
     * Оценка штрафа
     */
    public Integer estimate = 0;
    public Edge edge;

    public Node(Integer weight, Edge edge) {
        this.weight = weight;
        this.edge = edge;
    }

    @Override
    public int compareTo(Node o) {
        return this.weight.compareTo(o.weight);
    }
}
