import static java.lang.IO.println;
import static java.lang.Thread.sleep;
import static java.util.List.of;
import static java.util.concurrent.CompletableFuture.*;

void main() {
    var tasksParameters = of(2, 1, 3, 2, 1);
    var taskList = tasksParameters.stream().map(sleepSeconds -> runAsync(() -> {
        try {
            sleep(sleepSeconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    })).toList();
    allOf(taskList.toArray(CompletableFuture[]::new)).join();
    println("Tareas terminadas");
}