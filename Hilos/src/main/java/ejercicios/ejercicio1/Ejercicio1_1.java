import ejercicios.ejercicio1.RunnableImplementation;

import static java.lang.Thread.*;

void main() {
    for (int i = 0; i < 4; i++) ofPlatform().start(new RunnableImplementation(200));
}
