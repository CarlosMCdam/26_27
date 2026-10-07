import static java.lang.IO.println;
import static java.lang.System.out;
import static java.lang.Thread.ofPlatform;
import static java.lang.Thread.sleep;

void main() throws InterruptedException {
    var cookThread = ofPlatform().unstarted(() -> {
        for (int i = 0; i < 10; i++) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                out.println("Cocción cancelada");
                break;
            }
        }
    });
    println(cookThread.getState());
    cookThread.start();
    println(cookThread.getState());
    sleep(3000);
    cookThread.interrupt();
    println(cookThread.getState());
    try {
        cookThread.join();
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    println(cookThread.getState());
}