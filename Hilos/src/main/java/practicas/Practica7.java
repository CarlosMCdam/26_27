import static java.lang.Thread.sleep;
import static java.util.concurrent.CompletableFuture.supplyAsync;

void main() {
    var obtenerUsuario = (Supplier<CompletableFuture<String>>) () -> supplyAsync(() -> {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Oscar";
    });
    var obtenerEmail = (Function<String, CompletableFuture<String>>) usuario -> supplyAsync(() -> {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return usuario + "@example.com";
    });
    obtenerUsuario.get().thenCompose(obtenerEmail).thenAccept(IO::println).join();
}