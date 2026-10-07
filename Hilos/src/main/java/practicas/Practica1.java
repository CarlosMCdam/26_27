import static java.lang.IO.println;
import static java.lang.System.out;
import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newFixedThreadPool;

void main() throws ExecutionException, InterruptedException {
    try (var executor = newFixedThreadPool(2)) {
        var execution = (Runnable) () -> {
            println("Tarea enviada");
            var task = executor.submit(() -> {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return 42;
            });
            try {
                println("Esperando resultado...");
                var result = task.get();
                out.printf("%s", result);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        };
        executor.submit(execution).get();
    }
}
