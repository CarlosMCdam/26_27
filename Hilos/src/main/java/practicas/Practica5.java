import static java.util.concurrent.CompletableFuture.supplyAsync;

void main() {
    supplyAsync(() -> 10 + 20).thenApply(n -> n * 2).thenAccept(IO::println).join();
}