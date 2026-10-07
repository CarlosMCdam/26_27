import static java.util.concurrent.CompletableFuture.supplyAsync;

void main() {
    supplyAsync(() -> {
        throw new RuntimeException("Error al consultar el servidor");
    }).exceptionally(_ -> "DATOS POR DEFECTO").thenAccept(IO::println).join();
}