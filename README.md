# Matrix Multiplication Performance Analysis

This project benchmarks the performance of matrix multiplication using threads and a thread pool in Java. The task involves dividing the computation of matrix elements into parallel tasks. The results are analyzed based on varying matrix sizes and the number of tasks.

## Performance Comparison

| Matrix Size | Number of Tasks | Time with Threads (ms) | Time with Thread Pool (ms) |
|-------------|-----------------|------------------------|----------------------------|
| 9 x 9       | 4               | 3.214                  | 3.0142                     |
| 9 x 9       | 9               | 3.6918                 | 5.1631                     |
| 9 x 9       | Auto (Optimal)  | 2.733                  | 3.8316                     |
| 90 x 90     | 15              | 9.8862                 | 4.5019                     |
| 90 x 90     | 45              | 10.3343                | 7.3792                     |

## Conclusions

1. **Small Matrix (9x9) with Fewer Tasks**:
    - Using a thread pool slightly outperforms threads when tasks are fewer (4 tasks).
    - Individual threads introduce more overhead with 9 tasks, resulting in slower performance compared to a thread pool.

2. **Small Matrix (9x9) with Optimal Task Distribution**:
    - Automatically determining the optimal number of tasks provides the best performance for threads.

3. **Larger Matrix (90x90)**:
    - For larger matrices, the thread pool significantly outperforms threads with moderate task counts (15 tasks). This highlights the thread pool's efficiency in handling larger workloads.
    - With more tasks (45), the performance of individual threads declines due to increased overhead, but the thread pool maintains better efficiency.

4. **General Observations**:
    - **Thread Pool**: Consistently provides better performance for larger matrices and higher task counts, due to reduced thread creation overhead and efficient task scheduling.
    - **Threads**: Perform well with a small number of tasks but suffer from higher overhead with more tasks, making thread pools preferable for handling larger matrices or higher workloads.

