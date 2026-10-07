import static java.util.List.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;

void main() {
    var dividir = (BiFunction<Integer, Integer, CompletableFuture<Integer>>) (a, b) ->
            supplyAsync(() -> a / b).handle((result, error) -> error == null ? result : 0);
    var operationParametersList = of(
            new int[]{10, 2},
            new int[]{10, 0}
    );
    operationParametersList.forEach(parameters -> dividir.apply(parameters[0], parameters[1]).thenAccept(IO::println).join());
}