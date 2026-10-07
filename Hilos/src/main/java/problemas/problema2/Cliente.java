package problemas.problema2;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;
import static java.lang.Thread.ofPlatform;
import static java.lang.Thread.sleep;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.IntStream.range;

public abstract sealed class Cliente implements Runnable permits ClienteReentrantLock, ClienteVolatile, ClienteSynchronized {
    protected final int SALDO_INICIAL = 10000;

    protected final Random random = new Random();

    protected final DateTimeFormatter formatter = ofPattern("yyyy-MM-dd HH:mm:ss.SS");

    protected final ArrayList<String> transactionLog = new ArrayList<>();
    protected final Mode mode;

    protected Cliente(Mode mode) {
        this.mode = mode;
    }

    protected abstract boolean retiro(int amount);

    protected abstract void ingreso(int amount);

    protected abstract int getSaldo();

    protected void addTransaction() {
        transactionLog.add(now().format(formatter));
    }

    public void run() {
        var totalThreads = 50;
        var transactionsPerThread = 10;
        var totalTransactions = transactionsPerThread * totalThreads;
        var successfulTransactions = new AtomicInteger(totalTransactions);
        range(0, totalThreads).mapToObj(_ -> (Runnable) () -> {
            for (int i = 0; i < transactionsPerThread; i++) {
                try {
                    sleep(random.nextInt(100, 301));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (random.nextInt(0, 10) < 6 && !retiro(random.nextInt(1, 101)))
                    successfulTransactions.decrementAndGet();
                else ingreso(random.nextInt(1, 501));
            }
        }).map(ofPlatform()::start).toList().forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        out.printf("%s: %d\n", mode, getSaldo());
        out.printf("Transacciones: %d/%d\n", successfulTransactions.get(), totalTransactions);
    }

    protected enum Mode {
        REENTRANT_LOCK, SYNCHRONIZED, VOLATILE
    }
}
