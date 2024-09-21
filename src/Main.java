import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static void work(int taskCount, Runnable task, ExecutorService executorService) throws Exception {
        long start = System.nanoTime();

        try (executorService) {
            for(int i = 0; i < taskCount; i++){
                executorService.execute(task);
            }
        }

        long end = System.nanoTime();

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

        long workTime = end - start;
        System.out.println(start);
        System.out.println(end);
        System.out.println("총 작업 처리 시간: " + workTime + " ns");
    }

    public static void main(String[] args) throws Exception {
        Runnable task = () -> {
            try {
                long result = 0;
                for(int i = 1; i <= 1000; i++){
                    result += i;
                }

                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        int taskCount = 10000;

        ExecutorService threadExecutor = Executors.newFixedThreadPool(100);
        work(taskCount, task, threadExecutor);

        ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
        work(taskCount, task, virtualExecutor);

        System.out.println("---------------------------------" + "\n");

        Thread.startVirtualThread(() -> {
            System.out.println("가상 스레드1 실행");
        });

        Thread.ofVirtual()
                .start(() -> {
                    System.out.println("가상 스레드2 실행");
                });

        Thread virtualThread3 = Thread.ofVirtual()
                .name("스스레드")
                .start(() -> {
                    System.out.println("가상 스레드3 실행");
                });

        System.out.println("가상스레드3 이름: " + virtualThread3.getName());
    }
}
