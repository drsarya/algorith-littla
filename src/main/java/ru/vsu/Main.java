package ru.vsu;


import java.util.ArrayList;
import java.util.List;

import static ru.vsu.Utils.INF;


public class Main {
    public static int record = Integer.MAX_VALUE;
    private static List<Edge> result = new ArrayList<>();


    public static void main(String[] args) {
        int[][] valueGraph = {{INF, 20, 18, 12, 8},
            {5, INF, 14, 7, 11},
            {12, 18, INF, 6, 11},
            {11, 17, 11, INF, 12},
            {5, 5, 5, 5, INF}};
        Node[][] graph = new Node[valueGraph.length][valueGraph.length];
        for (int i = 0; i < valueGraph.length; i++) {
            for (int j = 0; j < valueGraph[i].length; j++) {
                graph[i][j] = new Node(valueGraph[i][j], new Edge(i, j));
            }
        }
        int graphSize = graph.length;
        List<Edge> edges = new ArrayList<>(graphSize);
        findSolution(edges, graph, 0);
        result = Edge.refreshEdges(result);
        System.out.println();
        for (Edge edge : result) {
            System.out.println(edge);
        }

        System.out.println("Длина полученного пути: " + record);
    }


    public static void findSolution(List<Edge> edges, Node[][] graph, int H) {
        if (graph.length == 2) {
            Utils.checkCycle(graph);
            System.out.println("----------------Вариант решения-----------------");
            H += Utils.reduceMatrix(graph);
            record = H;
            System.out.println("Стоимость:  " + record);

            Utils.setEstimates(graph);
            Edge lastEdge = Utils.searchMaxEstimate(graph);
            edges.add(lastEdge);
            graph = Utils.deleteRow(graph, lastEdge.row);
            graph = Utils.deleteColumn(graph, lastEdge.column);

            edges.add(new Edge(graph[0][0].edge.row, graph[0][0].edge.column));
            for (Edge edge : edges) {
                System.out.println(edge);
            }
            result.clear();
            result.addAll(edges);
            return;
        }
        System.out.println();
        printMatrix(graph);
        H += Utils.reduceMatrix(graph);
        if (H > record) {
            System.out.println("Полученная оценка: " + H + ", существующая: " + record);
            if (!edges.isEmpty()) {
                edges.remove(edges.size() - 1);
            }
            return;
        }
        Utils.setEstimates(graph);
        System.out.println("\nРедуцированная матрица:");
        printMatrix(graph);
        Edge edge = Utils.searchMaxEstimate(graph);
        System.out.println("Путь с максимальным штрафом " + edge);

        // переход к множеству,содержащему ребро edge
        Node[][] gr = Utils.copyGraph(graph);
        edges.add(edge);
        Utils.reverseReturn(gr, edge.row, edge.column);
        gr = Utils.deleteRow(gr, edge.row);
        gr = Utils.deleteColumn(gr, edge.column);

        System.out.println("Нижняя граница оценки: " + H);

        findSolution(edges, gr, H);

        // переход к множеству, не содержащему ребро edge
        Node[][] gr2 = Utils.copyGraph(graph);
        System.out.println("Игнорируем путь " + edge.row + " " + edge.column);
        Utils.reverseReturn(gr2, edge.column, edge.row);
        edges.remove(edges.size() - 1);
        findSolution(edges, gr2, H);
    }


    public static void printMatrix(Node[][] matrix) {
        for (int j = 0; j < matrix.length; j++) {
            System.out.print("    " + matrix[0][j].edge.column);
        }
        System.out.println();
        for (Node[] nodes : matrix) {
            System.out.print(nodes[0].edge.row + "  ");
            for (int j = 0; j < nodes.length; j++) {
                if (nodes[j].weight == INF) {
                    System.out.printf("%5s", "∞ |");
                } else {
                    System.out.printf("%2d/%d|", nodes[j].weight, nodes[j].estimate);
                }
            }
            System.out.println();
        }
    }
}
