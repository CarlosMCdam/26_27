import ejercicios.ejercicio1.RunnableImplementation;

import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;
import static java.util.stream.IntStream.generate;

void main() {
    var runnableList = generate(() -> 200).limit(4).mapToObj(RunnableImplementation::new);
    try (var executor = newVirtualThreadPerTaskExecutor()) {
        runnableList.forEach(executor::submit);
    }
}
