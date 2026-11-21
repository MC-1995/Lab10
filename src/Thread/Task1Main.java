package Thread;

public class Task1Main {
    public static void main(String[] args) {
        Thread evenThread = new EvenThread();
        Thread oddThread = new Thread(new OddRunnable());

        evenThread.start();
        oddThread.start();

        try {
            evenThread.join();
            oddThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Task 1 completed.");
    }
}
