package problemas.problema2;

import static problemas.problema2.Cliente.Mode.SYNCHRONIZED;

public final class ClienteSynchronized extends Cliente {
    private final Object lock = new Object();

    private int saldo = SALDO_INICIAL;

    public ClienteSynchronized() {
        super(SYNCHRONIZED);
    }

    @Override
    protected boolean retiro(int amount) {
        if (amount > saldo) return false;
        synchronized (lock) {
            saldo -= amount;
            addTransaction();
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