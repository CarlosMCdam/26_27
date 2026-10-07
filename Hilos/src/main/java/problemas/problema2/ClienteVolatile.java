package problemas.problema2;

import static problemas.problema2.Cliente.Mode.VOLATILE;

public final class ClienteVolatile extends Cliente {
    private volatile int saldo = SALDO_INICIAL;

    public ClienteVolatile() {
        super(VOLATILE);
    }

    @Override
    protected boolean retiro(int amount) {
        if (amount > saldo) return false;
        saldo -= amount;
        addTransaction();
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