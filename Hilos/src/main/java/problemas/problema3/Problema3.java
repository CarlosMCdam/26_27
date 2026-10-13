import static java.lang.IO.println;
import static java.lang.System.*;
import static java.lang.Thread.*;
import static java.time.LocalTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

private final static int MULTIPLICADOR_DE_ESPERA = 100;

void main() throws InterruptedException {
    println("=== RESTAURANTE CONCURRENTE ===");

    var cocineros = 3;
    var camareros = 5;

    out.printf("Iniciando servicio con %s cocineros y %s camareros...\n", cocineros, camareros);

    enum TipoPlato {
        ENSALADA(2), PASTA(3), PIZZA(4), CARNE(5);
        private final int tiempoMs;

        TipoPlato(int tiempoMs) {
            this.tiempoMs = tiempoMs * MULTIPLICADOR_DE_ESPERA * 10;
        }
    }
    record Pedido(String nombreCliente, TipoPlato plato, long horaCreacionMs) {
    }

    var now = (Supplier<String>) () -> now().format(ofPattern("HH:mm:ss"));

    var clientes = 100;
    var clientesAtendidos = new CountDownLatch(clientes);

    var mesa = new ArrayBlockingQueue<Pedido>(10);
    var platosServidos = new AtomicInteger();
    var esperaTotalMillis = new AtomicLong();
    var cocinerosEsperandoMillis = new AtomicLong();

    for (int i = 1; i <= cocineros; i++) {
        var nombre = "Cocinero-%d".formatted(i);
        ofPlatform().name(nombre).start(() -> {
            while (true) {
                var timeStartedWaiting = currentTimeMillis();
                try {
                    var pedido = mesa.take();
                    cocinerosEsperandoMillis.addAndGet(currentTimeMillis() - timeStartedWaiting);
                    sleep(pedido.plato().tiempoMs);
                    esperaTotalMillis.addAndGet(currentTimeMillis() - pedido.horaCreacionMs());
                    platosServidos.incrementAndGet();
                    out.printf("[%s] %s termina %s para %s\n", now.get(), nombre, pedido.plato, pedido.nombreCliente());
                    clientesAtendidos.countDown();
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                }
            }
        });
    }
    var colaClientes = new ArrayBlockingQueue<Pedido>(clientes);
    var mesaLlenaCount = new AtomicInteger();
    for (int i = 1; i <= camareros; i++) {
        var nombre = "Camarero-%d".formatted(i);
        ofPlatform().name(nombre).start(() -> {
            while (true) {
                try {
                    var pedido = colaClientes.take();
                    sleep(10 * MULTIPLICADOR_DE_ESPERA);
                    out.printf("[%s] %s toma pedido %s\n", now.get(), nombre, pedido.plato);
                    if (mesa.remainingCapacity() == 0) mesaLlenaCount.incrementAndGet();
                    mesa.put(pedido);
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                }
            }
        });
    }
    var random = new Random();
    for (int i = 1; i <= clientes; i++) {
        var nombreCliente = "Cliente-%d".formatted(i);
        var platos = TipoPlato.values();
        var plato = platos[random.nextInt(platos.length)];
        out.printf("[%s] %s pide %s\n", now.get(), nombreCliente, plato);
        try {
            colaClientes.put(new Pedido(nombreCliente, plato, currentTimeMillis()));
            sleep(5 * MULTIPLICADOR_DE_ESPERA);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }
    clientesAtendidos.await();

    println("--- ESTADÍSTICAS FINALES ---");
    out.printf("Clientes atendidos: %s\n", clientes);
    out.printf("Platos servidos: %s\n", platosServidos.get());
    out.printf("Tiempo promedio de espera: %d segundos\n", esperaTotalMillis.get() / clientes / 1000);
    out.printf("Mesa llena: %d\n", mesaLlenaCount.get());
    out.printf("Tiempo esperado por cocineros: %d segundos\n", cocinerosEsperandoMillis.get() / 1000);

    exit(0);
}