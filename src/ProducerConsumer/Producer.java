package ProducerConsumer;

import java.util.Random;

public class Producer implements Runnable {
    private final ShoeWarehouse warehouse;
    private final int orderCount;

    public Producer(ShoeWarehouse warehouse, int orderCount) {
        this.warehouse = warehouse;
        this.orderCount = orderCount;
    }

    @Override
    public void run() {
        Random random = new Random();
        try {
            for (int i = 1; i <= orderCount; i++) {
                String type = ShoeWarehouse.SHOE_TYPES.get(random.nextInt(ShoeWarehouse.SHOE_TYPES.size()));
                int qty = 1 + random.nextInt(10);
                Order order = new Order(i, type, qty);
                warehouse.receiveOrder(order);
                Thread.sleep(100); // имитация работы
            }
            System.out.println("Producer завершил генерацию " + orderCount + " заказов.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
