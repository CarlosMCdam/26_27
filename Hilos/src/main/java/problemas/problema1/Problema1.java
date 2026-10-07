import static java.lang.System.out;
import static java.lang.Thread.ofPlatform;
import static java.lang.Thread.sleep;
import static java.util.stream.IntStream.range;

void main() {
    enum Mode {
        NORMAL, SYNCHRONIZED, ATOMIC
    }
    var random = new Random();
    var lock = new Object();
    var counter = new int[1];
    var synchronizedCounter = new int[1];
    var atomicCounter = new AtomicInteger(0);
    var execution = (Consumer<Mode>) mode -> range(0, 1000).mapToObj(_ -> (Runnable) () -> {
        try {
            sleep(random.nextInt(50, 151));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        switch (mode) {
            case NORMAL -> counter[0]++;
            case SYNCHRONIZED -> {
                synchronized (lock) {
                    synchronizedCounter[0]++;
                }
            }
            case ATOMIC -> atomicCounter.incrementAndGet();
        }
    }).map(ofPlatform()::start).toList().forEach(thread -> {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    });
    for (var mode : Mode.values()) {
        execution.accept(mode);
        out.printf("%s: %d\n", mode, switch (mode) {
            case NORMAL -> counter[0];
            case SYNCHRONIZED -> synchronizedCounter[0];
            case ATOMIC -> atomicCounter.get();
        });
    }
}
