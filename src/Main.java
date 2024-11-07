import java.util.concurrent.*;

class MatrixMultiplication {

    // Function to compute a single element of the resulting matrix
    public static int computeElement(int[][] A, int[][] B, int row, int col) {
        int sum = 0;
        for (int i = 0; i < A[0].length; i++) {
            sum += A[row][i] * B[i][col];
        }
        return sum;
    }

    // Task function to compute part of the resulting matrix
    static class MatrixTask implements Runnable {
        private final int[][] A, B, C;
        private final int start, end, totalColumns;

        public MatrixTask(int[][] A, int[][] B, int[][] C, int start, int end, int totalColumns) {
            this.A = A;
            this.B = B;
            this.C = C;
            this.start = start;
            this.end = end;
            this.totalColumns = totalColumns;
        }

        @Override
        public void run() {
            for (int index = start; index < end; index++) {
                int row = index / totalColumns;
                int col = index % totalColumns;
                C[row][col] = computeElement(A, B, row, col);
            }
        }
    }

    // Create and run tasks using individual threads
    public static void multiplyWithThreads(int[][] A, int[][] B, int[][] C, int numTasks) {
        int totalElements = C.length * C[0].length;
        int chunkSize = totalElements / numTasks;
        Thread[] threads = new Thread[numTasks];

        for (int i = 0; i < numTasks; i++) {
            int start = i * chunkSize;
            int end = (i == numTasks - 1) ? totalElements : start + chunkSize;
            threads[i] = new Thread(new MatrixTask(A, B, C, start, end, C[0].length));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Create and run tasks using a thread pool
    public static void multiplyWithThreadPool(int[][] A, int[][] B, int[][] C, int numTasks) {
        int totalElements = C.length * C[0].length;
        int chunkSize = totalElements / numTasks;
        ExecutorService executor = Executors.newFixedThreadPool(numTasks);

        for (int i = 0; i < numTasks; i++) {
            int start = i * chunkSize;
            int end = (i == numTasks - 1) ? totalElements : start + chunkSize;
            executor.execute(new MatrixTask(A, B, C, start, end, C[0].length));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Helper method to initialize matrices with random values
    public static int[][] initializeMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (int) (Math.random() * 10);
            }
        }
        return matrix;
    }

    // Main method to test the implementations
    public static void main(String[] args) {
        int rows = 90;
        int cols = 90;
        int numTasks = 35;

        int[][] A = initializeMatrix(rows, cols);
        int[][] B = initializeMatrix(cols, rows);
        int[][] C = new int[rows][rows];

        // Using individual threads
        long startTime = System.nanoTime();
        multiplyWithThreads(A, B, C, numTasks);
        long endTime = System.nanoTime();
        System.out.println("Time with threads: " + (endTime - startTime) / 1e6 + " ms");

        // Reset result matrix
        C = new int[rows][rows];

        // Using thread pool
        startTime = System.nanoTime();
        multiplyWithThreadPool(A, B, C, numTasks);
        endTime = System.nanoTime();
        System.out.println("Time with thread pool: " + (endTime - startTime) / 1e6 + " ms");
    }
}
