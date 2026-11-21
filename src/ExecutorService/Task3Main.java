package ExecutorService;

import ProducerConsumer.Order;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Task3Main {
    public static void main(String[] args) throws InterruptedException {
        int capacity = 5;
        int consumerCount = 3;
        int ordersPerConsumer = 5;
        int totalOrders = consumerCount * ordersPerConsumer; // 15

        ShoeWarehouseExecutor warehouse = new ShoeWarehouseExecutor(capacity, consumerCount);

        // Пул для производителя (отдельный, чтобы избежать взаимной блокировки)
        ExecutorService producerExecutor = Executors.newSingleThreadExecutor();

        // Запускаем потребителей (они начнут ждать заказы)
        warehouse.startConsumers(consumerCount, ordersPerConsumer);

        // Отправляем задачу производителя
        producerExecutor.submit(() -> {
            Random random = new Random();
            try {
                for (int i = 1; i <= totalOrders; i++) {
                    String type = ShoeWarehouseExecutor.SHOE_TYPES.get(
                            random.nextInt(ShoeWarehouseExecutor.SHOE_TYPES.size()));
                    int qty = 1 + random.nextInt(10);
                    Order order = new Order(i, type, qty);
                    warehouse.receiveOrder(order);
                    Thread.sleep(100);
                }
                System.out.println("Producer завершил генерацию " + totalOrders + " заказов.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Завершаем пул производителя и ждём его
        producerExecutor.shutdown();
        if (!producerExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
            producerExecutor.shutdownNow();
        }

        // Ждём завершения потребителей
        warehouse.shutdownConsumers();

        System.out.println("Task 3 completed.");
    }
}
