package ejercicios.ejercicio1;

import static java.lang.Math.min;
import static java.lang.Math.random;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public record RunnableImplementation(int millisPrintDelay) implements Runnable {
    @Override
    public void run() {
        var startTime = currentTimeMillis();
        var timeSpent = 0L;
        var fileSize = (long) (random() * 10000);
        while (timeSpent < fileSize) {
            try {
                sleep(millisPrintDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            timeSpent = min(currentTimeMillis() - startTime, fileSize);
            var progress = min(timeSpent * 100 / fileSize, 100);
            out.printf("%s: %d / %d Bytes (%d%%)\n", currentThread().getName(), timeSpent, fileSize, progress);
        }
    }
}
