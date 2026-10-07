import static java.lang.IO.println;
import static java.lang.Thread.sleep;
import static java.time.LocalDateTime.now;
import static java.util.Collections.swap;
import static java.util.List.of;
import static java.util.concurrent.Executors.newFixedThreadPool;

void main() {
    record TaskParameters(int returnValue, int sleepMillis) {
        TaskParameters {
            sleepMillis *= 1000;
        }
    }
    var taskParametersList = new ArrayList<>(of(
            new TaskParameters(10, 2),
            new TaskParameters(20, 1),
            new TaskParameters(30, 3)));
    println(now());
    try (var executor = newFixedThreadPool(3)) {
        var execution = (Runnable) () -> {
            var taskList = taskParametersList.stream().map(taskParameters -> (Callable<Integer>) () -> {
                sleep(taskParameters.sleepMillis);
                return taskParameters.returnValue;
            }).map(executor::submit).toList();
            var summedResults = taskList.stream().map(task -> {
                try {
                    return task.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }).mapToInt(Integer::intValue).sum();
            println(summedResults);
            println(now());
        };
        execution.run();
        swap(taskParametersList, 0, 1);
        execution.run();
    }
}
