package ru.vsu;

public class Utils {
    public static int INF = Integer.MAX_VALUE / 2;

    public static void checkCycle(Node[][] graph) {
        boolean[] infRow = new boolean[graph.length];
        boolean[] infColumn = new boolean[graph.length];


        for (int i = 0; i < graph.length; i++)
            for (int j = 0; j < graph[i].length; j++)
                if (graph[i][j].weight == INF) {
                    infRow[i] = true;
                    infColumn[j] = true;
                }
        // поиск строки, не содержащей бесконечности
        int notInf = 0;
        for (int i = 0; i < infRow.length; i++)
            if (!infRow[i]) {
                notInf = i;
                break;
            }

        // поиск столбца, не содержащего бесконечности и добавление бесконечности
        for (int j = 0; j < infColumn.length; j++)
            if (!infColumn[j]) {
                graph[notInf][j].weight = INF;
                break;
            }
    }

    public static Node[][] copyGraph(Node[][] graph) {
        Node[][] copyGraph = new Node[graph.length][graph.length];
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                Node n = new Node(graph[i][j].weight, new Edge(graph[i][j].edge.row, graph[i][j].edge.column));
                copyGraph[i][j] = n;
            }
        }
        return copyGraph;
    }

    public static void reverseReturn(Node[][] graph, int row, int col) {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j].edge.column == row && graph[i][j].edge.row == col) {
                    graph[i][j].weight = INF;
                }
            }
        }
    }

    public static Edge searchMaxEstimate(Node[][] graph) {
        Edge e = new Edge();
        int maxBad = Integer.MIN_VALUE;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (maxBad < graph[i][j].estimate && graph[i][j].weight != INF) {
                    maxBad = graph[i][j].estimate;
                    e.row = graph[i][j].edge.row;
                    e.column = graph[i][j].edge.column;
                }
            }
        }
        return e;
    }

    public static void setEstimates(Node[][] graph) {
        //Сумма минимальной стоимости в i-й строке (за исключением cij) и минимальной
        //стоимости в j-ом столбце (за исключением cij)
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                Integer minValueFromRow = getMinValueFromRow(graph, i, j, true);
                Integer minValueFromColumn = getMinValueFromColumn(graph, j, i, true);
                if (graph[i][j].weight == 0) {
                    graph[i][j].estimate = minValueFromRow + minValueFromColumn;
                } else {
                    graph[i][j].estimate = 0;
                }
            }
        }
    }

    public static int reduceMatrix(Node[][] graph) {
        if (graph.length == 0) {
            return 0;
        }
        //граница Н множества решений
        int minEstimate = 0;
        // приводим по строкам
        for (int i = 0; i < graph.length; i++) {
            Integer minValueFromRow = getMinValueFromRow(graph, i, null, false);
            minEstimate += minValueFromRow;
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j].weight != INF) {
                    graph[i][j].weight = graph[i][j].weight - minValueFromRow;
                }
            }
        }
        // приводим по столбцам
        for (int j = 0; j < graph[0].length; j++) {
            Integer minValueFromColumn = getMinValueFromColumn(graph, j, null, false);
            minEstimate += minValueFromColumn;
            for (int i = 0; i < graph.length; i++) {
                if (graph[i][j].weight != INF) {
                    graph[i][j].weight = graph[i][j].weight - minValueFromColumn;
                }
            }
        }
        return minEstimate;
    }

    public static Integer getMinValueFromRow(Node[][] graph, int row, Integer column, boolean checkCurrenPosition) {
        int min = Integer.MAX_VALUE;
        for (int j = 0; j < graph.length; j++) {
            if (min > graph[row][j].weight && graph[row][j].weight != INF && (!checkCurrenPosition || j != column)) {
                min = graph[row][j].weight;
            }
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    public static Integer getMinValueFromColumn(Node[][] graph, int column, Integer row, boolean checkCurrenPosition) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < graph[0].length; i++) {
            if (min > graph[i][column].weight && graph[i][column].weight != INF && (!checkCurrenPosition || i != row)) {
                min = graph[i][column].weight;
            }
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    /**
     * Удалить строку из матрицы
     *
     * @param matrix   матрица
     * @param indexRow индекс строки для удаления
     * @return модифицированная матрица
     */
    public static Node[][] deleteRow(Node[][] matrix, int indexRow) {
        if (indexRow < 0) {
            return new Node[0][];
        }
        Node[][] modMatrix = new Node[matrix.length - 1][matrix[0].length];
        int indForDelete = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][0].edge.row == indexRow) {
                indForDelete = i;
                break;
            }
        }
        for (int row = 0, realRow = 0; row < matrix.length; row++, realRow++) {
            if (row == indForDelete) {
                realRow--;
                continue;
            }
            for (int col = 0; col < matrix[0].length; col++) {
                modMatrix[realRow][col] = matrix[row][col];
            }
        }
        return modMatrix;
    }

    /**
     * Удалить столбец из матрицы
     *
     * @param matrix      матрица
     * @param columnIndex индекс столбца
     * @return модифицированная матрица
     */
    public static Node[][] deleteColumn(Node[][] matrix, int columnIndex) {
        if (matrix.length == 0 || columnIndex < 0) {
            return new Node[0][];
        }
        int indForDelete = 0;
        for (int j = 0; j < matrix[0].length; j++) {
            if (matrix[0][j].edge.column == columnIndex) {
                indForDelete = j;
                break;
            }
        }
        Node[][] modMatrix = new Node[matrix.length][matrix[0].length - 1];
        for (int col = 0, realCol = 0; col < matrix[0].length; col++, realCol++) {
            if (col == indForDelete) {
                realCol--;
                continue;
            }
            for (int row = 0; row < matrix.length && realCol < modMatrix[row].length; row++) {
                modMatrix[row][realCol] = matrix[row][col];
            }
        }
        return modMatrix;
    }
}
