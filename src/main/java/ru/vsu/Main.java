package ru.vsu;


import static ru.vsu.Utils.INF;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import ru.vsu.utils.ArrayUtils;


public class Main {

    private static int record = Integer.MAX_VALUE;
    private static List<Edge> result = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        result = new ArrayList<>();
        record = Integer.MAX_VALUE;
        String[] ints = ArrayUtils.readLinesFromFile(
            "test1.txt");
        int[][] valueGraph = new int[ints.length][ints.length];
        for (int i = 0; i < ints.length; i++) {
            String[] s = ints[i].split("\t");
            for (int j = 0; j < s.length; j++) {
                if (s[j].equals("INF")) {
                    valueGraph[i][j] = INF;
                } else {
                    valueGraph[i][j] = Integer.parseInt(s[j]);
                }
            }
        }
//        int[][] valueGraph = {{INF, 20, 18, 12, 8},
//            {5, INF, 14, 7, 11},
//            {12, 18, INF, 6, 11},
//            {11, 17, 11, INF, 12},
//            {5, 5, 5, 5, INF}};
        getMinPath(valueGraph);
        System.out.println("Длина полученного пути: " + record);
    }

    public static List<Edge> getMinPath(int[][] matrix) {
        result = new ArrayList<>();
        record = Integer.MAX_VALUE;
        Node[][] graph = new Node[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                graph[i][j] = new Node(matrix[i][j], new Edge(i, j));
            }
        }
        int graphSize = graph.length;
        List<Edge> edges = new ArrayList<>(graphSize);
        findSolution(graphSize, edges, graph, 0);
        result = Edge.refreshEdges(result);
        System.out.println();
        for (Edge edge : result) {
            System.out.println(edge);
        }
        System.out.println("Длина полученного пути: " + record);
        return result;
    }

    private static void findSolution(int graphSize, List<Edge> edges, Node[][] graph, int H) {
        Utils.checkCycle(graph);
        if (graph.length == 2 && edges.size() == graphSize - 2) {
            // на случай, если имеется путь в каждую вершину, первый элемент сделаем недоступным

            System.out.println("----------------Вариант решения-----------------");
            H += Utils.reduceMatrix(graph);
            record = H;
            System.out.println("Стоимость:  " + record);
            Utils.setEstimates(graph);
            System.out.println("\nРедуцированная матрица:");
            Utils.printMatrix(graph, true);
            Edge lastEdge = Utils.searchMaxEstimate(graph);
            System.out.println("Путь с максимальным штрафом " + lastEdge);
            edges.add(lastEdge);
            graph = Utils.deleteRow(graph, lastEdge.row);
            graph = Utils.deleteColumn(graph, lastEdge.column);

            Edge e = new Edge(graph[0][0].edge.row, graph[0][0].edge.column);
            System.out.println("Путь с максимальным штрафом " + e);
            edges.add(e);
            edges = Edge.refreshEdges(edges);
            for (Edge edge : edges) {
                System.out.println(edge);
            }
            result.clear();
            result.addAll(edges);
            return;
        } else if (graph.length == 2 && edges.size() != graphSize - 2) {
            return;
        }
        if (!Utils.isGraphWithPaths(graph)) {
            return;
        }
        System.out.println();

        Utils.printMatrix(graph, false);

        H += Utils.reduceMatrix(graph);
        if (H > record) {
            System.out.println("Полученная оценка: " + H + ", существующая: " + record);
            return;
        }
        Utils.setEstimates(graph);
        System.out.println("\nРедуцирование и вычисление штрафов:");
        Utils.printMatrix(graph, true);
        Edge edge = Utils.searchMaxEstimate(graph);
        System.out.println("Путь с максимальным штрафом " + edge);

        // переход к множеству,содержащему ребро edge
        Node[][] gr = Utils.copyGraph(graph);
        edges.add(edge);
        Utils.reverseReturn(gr, edge.row, edge.column);
        gr = Utils.deleteRow(gr, edge.row);
        gr = Utils.deleteColumn(gr, edge.column);

        System.out.println("Нижняя граница оценки: " + H);

        findSolution(graphSize, edges, gr, H);

        // переход к множеству, не содержащему ребро edge
        Node[][] gr2 = Utils.copyGraph(graph);
        System.out.println("Игнорируем путь " + edge.row + " " + edge.column);
        Utils.reverseReturn(gr2, edge.column, edge.row);
        if (!edges.isEmpty()) {
            edges.remove(edges.size() - 1);
        }
        findSolution(graphSize, edges, gr2, H);
    }


}
