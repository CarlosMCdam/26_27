import problemas.problema2.Cliente;
import problemas.problema2.ClienteReentrantLock;
import problemas.problema2.ClienteSynchronized;
import problemas.problema2.ClienteVolatile;

import static java.util.List.of;

void main() {
    var implementations = of(
            new ClienteReentrantLock(),
            new ClienteVolatile(),
            new ClienteSynchronized());
    implementations.forEach(Cliente::run);
}