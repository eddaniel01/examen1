package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
public class ProducerConsumer {
    private static final int QUEUE_CAPACITY = 10;
    private static final int PRODUCER_COUNT = 2;
    private static final int CONSUMER_COUNT = 2;
    private static final int PRODUCE_COUNT = 100;

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        Thread[] hilosProductor = new Thread[PRODUCER_COUNT];
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            hilosProductor[i] = new Thread(new Producer(queue, PRODUCE_COUNT));
            hilosProductor[i].start();
        }

        Thread[] hilosConsumidor = new Thread[CONSUMER_COUNT];
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            hilosConsumidor[i] = new Thread(new Consumer(queue));
            hilosConsumidor[i].start();
        }

        for (Thread hiloProductor : hilosProductor) {
            try {
                hiloProductor.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        for (Thread hiloConsumidor: hilosConsumidor) {
            hiloConsumidor.interrupt();
            try {
                hiloConsumidor.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Producer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private final Random random = new Random();
        public Producer(BlockingQueue<Integer> queue, int produceCount) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < PRODUCE_COUNT; i++) {
                    int num = random.nextInt(100);
                    queue.put(num);
                    System.out.println("Producido: " + num);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue<Integer> queue;
        int total = 0;
        int num=0;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    num = queue.take();
                   System.out.println("Consumido: " + num);
                    total= total + num;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
          System.out.println("Sumatoria= "+total);
        }
    }
}
