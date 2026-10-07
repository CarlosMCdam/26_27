import static java.lang.IO.println;
import static java.lang.Thread.sleep;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;

void main() {
    runAsync(() -> {
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }).orTimeout(3, SECONDS).exceptionally(error -> {
        println(error);
        return null;
    }).join();
    supplyAsync(() -> {
        try {
            sleep(10000);
            return "Tarea terminada";
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }).completeOnTimeout("Resultado no disponible", 3, SECONDS).thenAccept(IO::println).join();
}
