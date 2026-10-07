import static java.util.concurrent.CompletableFuture.supplyAsync;

void main() {
    supplyAsync(() -> 10).thenApply(n -> n * 2).thenApply(n -> n + 5).thenApply(String::valueOf).thenAccept(IO::println).join();
}