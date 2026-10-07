import static java.lang.IO.println;

void main() throws ExecutionException, InterruptedException {
    var task = new FutureTask<>(() -> 42);
    task.run();
    do {
        println("Esperando...");
    } while (!task.isDone());
    println(task.get());
}