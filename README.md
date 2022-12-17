# Алгоритмы на графах: Алгоритм Литтла

## Метод ветвей и границ

Разбиваем текущую задачу на подзадачи.
Для каждой подзадачи выполняем оценку стоимости.

1. Если оценка снизу лучше текущего рекорда, то она становится новым рекордом.
2. Если оценка сверху хуже текущего рекорда, то отбрасываем эту подзадачу.

Выбираем из нерешённых подзадач самую перспективную. Если нерешённых подзадач не осталось, то рекорд — это решение.

То есть мы **заранее** предсказываем наиболее оптимистичный вариант решения для каждой рекурсивной подветви, не проводя
полного анализа этой подветви.

## Алгоритм Литтла

На примере городов. Задача коммивояжёра.

1. Из каждого элемента каждой строки вычитается минимальный элемент данной строки. При этом минимальный элемент строки
   прибавляется к нижней границе
   Из каждого столбца аналогично вычитается минимальный элемент и прибавляется к нижней границе.

2. Для каждого нулевого элемента M(i, j) вычисляется коэффициент, равный сумме минимальных элементов строки i и столбца
   j, исключая сам элемент (i, j) – оценка штрафов.
   Этот коэффициент показывает, насколько гарантированно увеличится нижняя граница решения, если исключить из него
   ребро (i, j).
3. Ищется элемент с максимальным коэффициентом. Если их несколько, можно выбрать любой (все равно оставшиеся будут
   рассмотрены на следующих шагах рекурсии)

4. Рассматриваются 2 матрицы — M1 и M2.
   M1 равна M с удаленными строкой i и столбцом j. В ней находится столбец k и строка l, в которых элемент M(k, l)
   приравнивается к INF. Это необходимо, чтобы не получить цикл на конкретном маршруте.
   Матрица M1 соответствует множеству, содержащему ребро (i, j). Вместе с удалением столбца и строки ребро (i, j)
   включается в путь.
   M2 равна матрице M, у которой элемент (i, j) равен INF. Матрица соответствует множеству путей, не содержащих ребро (
   i, j)
5. Переход к п.1 для матриц M1 и M2.