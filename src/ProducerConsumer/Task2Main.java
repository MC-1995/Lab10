package ProducerConsumer;

public class Task2Main {
    public static void main(String[] args) {
        int capacity = 5;
        int consumerCount = 3;
        int ordersPerConsumer = 5;
        int totalOrders = consumerCount * ordersPerConsumer; // 15

        ShoeWarehouse warehouse = new ShoeWarehouse(capacity);

        Thread producerThread = new Thread(new Producer(warehouse, totalOrders));
        producerThread.start();

        Thread[] consumers = new Thread[consumerCount];
        for (int i = 0; i < consumerCount; i++) {
            consumers[i] = new Thread(new Consumer(warehouse, i + 1, ordersPerConsumer));
            consumers[i].start();
        }

        try {
            producerThread.join();
            for (Thread c : consumers) {
                c.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Task 2 completed.");
    }
}