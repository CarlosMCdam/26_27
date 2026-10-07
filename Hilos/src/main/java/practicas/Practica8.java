import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static java.util.concurrent.CompletableFuture.supplyAsync;

void main() {
    var execution = (BiFunction<Integer, Integer, CompletableFuture<Integer>>) (data, sleepSeconds) -> supplyAsync(() -> {
        try {
            sleep(sleepSeconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return data;
    });
    var consultarTemperatura = execution.apply(25, 2);
    var consultarHumedad = execution.apply(60, 2);
    consultarTemperatura.thenCompose(temperatura ->
                    consultarHumedad.thenApply(humedad -> format("Temperatura: %dºC\nHumedad: %s%%", temperatura, humedad)))
            .thenAccept(IO::println).join();
}
