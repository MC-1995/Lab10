package ExecutorService;

import ProducerConsumer.Order;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ShoeWarehouseExecutor {
    public static final List<String> SHOE_TYPES = List.of(
            "Nike", "Adidas", "Puma", "Reebok", "New Balance"
    );

    private final Queue<Order> orders = new LinkedList<>();
    private final int capacity;
    private final ExecutorService consumerExecutor;

    public ShoeWarehouseExecutor(int capacity, int consumerThreads) {
        this.capacity = capacity;
        this.consumerExecutor = Executors.newFixedThreadPool(consumerThreads);
    }

    public synchronized void receiveOrder(Order order) throws InterruptedException {
        while (orders.size() >= capacity) {
            wait();
        }
        orders.add(order);
        System.out.println("Producer добавил заказ: " + order);
        notifyAll();
    }

    public synchronized Order fulfillOrder() throws InterruptedException {
        while (orders.isEmpty()) {
            wait();
        }
        Order order = orders.poll();
        System.out.println("Consumer забрал заказ: " + order);
        notifyAll();
        return order;
    }

    public void startConsumers(int consumerCount, int ordersPerConsumer) {
        for (int i = 0; i < consumerCount; i++) {
            final int consumerId = i + 1;
            consumerExecutor.submit(() -> {
                try {
                    for (int j = 0; j < ordersPerConsumer; j++) {
                        Order order = fulfillOrder();
                        System.out.println("Consumer " + consumerId + " обработал заказ: " + order);
                        Thread.sleep(150);
                    }
                    System.out.println("Consumer " + consumerId + " завершил работу.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    public void shutdownConsumers() throws InterruptedException {
        consumerExecutor.shutdown();
        if (!consumerExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
            consumerExecutor.shutdownNow();
        }
    }
}