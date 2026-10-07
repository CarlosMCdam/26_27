package problemas.problema2;

import java.util.concurrent.locks.ReentrantLock;

import static problemas.problema2.Cliente.Mode.REENTRANT_LOCK;

public final class ClienteReentrantLock extends Cliente {
    private final ReentrantLock lock = new ReentrantLock();

    private int saldo = SALDO_INICIAL;

    public ClienteReentrantLock() {
        super(REENTRANT_LOCK);
    }

    @Override
    protected boolean retiro(int amount) {
        if (amount > saldo) return false;
        lock.lock();
        try {
            saldo -= amount;
            addTransaction();
        } finally {
            lock.unlock();
        }
        return true;
    }

    @Override
    protected void ingreso(int amount) {

    }

    @Override
    protected int getSaldo() {
        return saldo;
    }
}