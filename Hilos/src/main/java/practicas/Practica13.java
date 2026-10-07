import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.stream.IntStream.range;

void main() {
    try (var executor = newFixedThreadPool(3)) {
        range(0, 5).forEach(i -> supplyAsync(() -> out.printf("Tarea %s - hilo: %s\n", i, currentThread().getName()), executor));
    }
}
