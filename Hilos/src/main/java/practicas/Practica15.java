import static java.lang.System.out;
import static java.util.concurrent.CompletableFuture.runAsync;

private int contador = 0;

private int contadorIncrementAndGet() {
    contador++;
    return contador;
}

void main() throws InterruptedException {
    var atomicContador = new AtomicInteger();
    var execution = (Consumer<Supplier<Integer>>) incrementMethod -> {
        for (int i = 0; i < 10; i++) runAsync(() -> {
            for (int j = 0; j < 10000; j++) {
                incrementMethod.get();
            }
        });
    };
    execution.accept(this::contadorIncrementAndGet);
    execution.accept(atomicContador::incrementAndGet);
    out.printf("Normal: %d\n", contador);
    out.printf("Atomic: %d\n", atomicContador.get());
}