import static java.lang.IO.println;
import static java.lang.System.out;
import static java.lang.Thread.ofPlatform;
import static java.lang.Thread.sleep;

void main() {
    var rocketThread = ofPlatform().start(() -> {
        var launchSeconds = 10;
        for (int i = 0; i < launchSeconds; i++) {
            try {
                out.printf("%d...\n", launchSeconds - i);
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    });
    try {
        rocketThread.join();
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    println("¡Despegue!");
}
