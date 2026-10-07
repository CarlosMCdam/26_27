import static java.lang.System.out;
import static java.util.List.of;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;

void main() {
    var numeros = of(1, 2, 3, 4, 5);
    var taskList = numeros.stream().map(numero -> supplyAsync(() -> numero * numero)).toList();
    allOf(taskList.toArray(CompletableFuture[]::new)).thenAccept(_ -> {
        for (int i = 0; i < taskList.size(); i++) {
            try {
                out.printf("%d -> %d\n", numeros.get(i), taskList.get(i).get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    });
}