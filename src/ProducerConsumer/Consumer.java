package ProducerConsumer;

public class Consumer implements Runnable {
    private final ShoeWarehouse warehouse;
    private final int consumerId;
    private final int ordersToProcess;

    public Consumer(ShoeWarehouse warehouse, int consumerId, int ordersToProcess) {
        this.warehouse = warehouse;
        this.consumerId = consumerId;
        this.ordersToProcess = ordersToProcess;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < ordersToProcess; i++) {
                Order order = warehouse.fulfillOrder();
                System.out.println("Consumer " + consumerId + " обработал заказ: " + order);
                Thread.sleep(150); // имитация обработки
            }
            System.out.println("Consumer " + consumerId + " завершил работу.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}