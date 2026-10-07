import static java.lang.System.out;
import static java.lang.Thread.*;
import static java.util.stream.IntStream.range;

private static final Random random = new Random();

void main() throws InterruptedException {
    record Coche(int id, boolean isVip, int fee) {
        private Coche(int id) {
            var isVip = random.nextBoolean();
            this(id, isVip, isVip ? 2 : 1);
        }
    }
    final class Plaza {
        private final boolean isVip;
        private Coche coche;

        private Plaza(boolean isVip) {
            this.isVip = isVip;
            coche = null;
        }
    }
    var cochesQueue = new ArrayBlockingQueue<Coche>(10);
    var cochesFlow = ofPlatform().start(() -> range(0, 100).mapToObj(Coche::new).forEach(coche -> {
        var isQueued = cochesQueue.offer(coche);
        out.printf("Coche %s: %s\n", coche.id, isQueued ? "Esperando en cola" : "Abandonando cola");
        try {
            sleep(random.nextInt(500, 2001));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }));
    var plazasList = range(0, 25).mapToObj(i -> new Plaza(i < 20)).toList();
    var plazasQueue = new ArrayBlockingQueue<Plaza>(plazasList.size());
    while (cochesFlow.isAlive()) {
        var coche = cochesQueue.take();
        ofVirtual().start(() -> plazasList.stream()
                .filter(plaza -> plaza.coche == null && (coche.isVip || !plaza.isVip))
                .findFirst()
                .ifPresent(plaza -> {
                    plaza.coche = coche;
                    try {
                        plazasQueue.put(plaza);
                        sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }
}