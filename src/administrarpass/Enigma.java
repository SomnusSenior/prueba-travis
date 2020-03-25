package administrarpass;

import static administrarpass.EjecutarEnigma.cifrado;
import static administrarpass.EjecutarEnigma.modo;

public class Enigma {

    private Rotor entrada = new Rotor("ABCDEFGHIJKLMNOPQRSTUVWXYZ", '-'); // rotor entrada
    private Rotor entradaAmpliado = new Rotor("!\"#$%&'()*+0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", '-'); // rotor entrada AMPLIADO
    private static Rotor rotorDerecha; // rotor derecho
    private static Rotor rotorCentral; // rotor medio
    private static Rotor rotorIzquierda; // rotor izquierdo
    private static Rotor reflector = new Rotor("YRUHQSLDPXNGOKMIEBFZCWVJAT", '-'); // reflector B
    private static Rotor reflectorAmpliado = new Rotor("bnD(gcF3SzpT6Q%#lRv70\"+V8J5eOoLxGN2)r$PiwAy1hfm'jX*t!4sCkYMHBq&KudWUE9ZaI", '-'); // reflector B AMPLIADO
    private static Clavijero plugboard; // tablero de clavijas
    public static int indiceP = 0;
    public static char[] pintar = new char[16];

    /**
     * Creación la maquina enigma
     *
     * @param rDe rotor derecho
     * @param rCen rotor medio
     * @param rIz rotor izquierdo
     */
    public Enigma(Rotor rIz, Rotor rCen, Rotor rDe) {
        this.rotorDerecha = rDe;
        this.rotorCentral = rCen;
        this.rotorIzquierda = rIz;
        this.plugboard = new Clavijero();
    }

    /**
     * Gira los rotores a la posición de sus claves
     *
     * @param claveDer Clave / posición inicial del rotor derecho
     * @param claveCen Clave / posición inicial del rotor medio
     * @param claveIzq Clave / posición inicial del rotor izquierdo
     */
    public void setRotorsIni(char claveIzq, char claveCen, char claveDer) {
        int posDer, posCen, posIzq;
        if (cifrado == 0) {
            posDer = claveDer - 'A';
            posCen = claveCen - 'A';
            posIzq = claveIzq - 'A';
        } else {
            posDer = posCheck(claveDer);
            posCen = posCheck(claveCen);
            posIzq = posCheck(claveIzq);
        }
        this.rotorDerecha = this.rotorDerecha.giro(posDer);
        this.rotorCentral = this.rotorCentral.giro(posCen);
        this.rotorIzquierda = this.rotorIzquierda.giro(posIzq);
    }

    /**
     * Comprueba las posiciones de la clave pasada; funcionalidad de los rotores
     * ampliados.
     *
     * @param clave
     * @return posición de la clave
     */
    private int posCheck(int clave) {
        int pos, check = clave - '!';
        if (check < 11) {
            pos = clave - '!';
        } else if (check < 25) {
            pos = clave - '!' - 4;
        } else if (check < 58) {
            pos = clave - '!' - 11;
        } else {
            pos = clave - '!' - 17;
        }
        return pos;
    }

    /**
     * Actualiza la posicion de los rotores haciendo que giren 1 vez
     */
    private void actualizarRotores() {
        char claveActualDer, claveActualCen;
        if (cifrado == 0) {
            claveActualDer = this.rotorDerecha.obtenerContEscritura().charAt(0); // obtiene la clave actual del rotor derecho
            claveActualCen = this.rotorCentral.obtenerContEscritura().charAt(0); // obtiene la clave actual del rotor central
        } else {
            claveActualDer = this.rotorDerecha.obtenerContEscrituraAmpliado().charAt(0); // obtiene la clave actual del rotor derecho
            claveActualCen = this.rotorCentral.obtenerContEscrituraAmpliado().charAt(0); // obtiene la clave actual del rotor central
        }
        if (claveActualDer == this.rotorDerecha.obtenerPuntoGiro()) { // si la clave actual del rotor derecho es el punto de giro del rotor derecho
            if (claveActualCen == this.rotorCentral.obtenerPuntoGiro()) { // si la clave actual del rotor central es el punto de giro del rotor central
                this.rotorIzquierda = this.rotorIzquierda.giro(1); // gira rotor izquierdo
            }
            this.rotorCentral = this.rotorCentral.giro(1); // gira rotor central
        } else {
            if (claveActualCen == this.rotorCentral.obtenerPuntoGiro()) { // si la clave actual del rotor central es el punto de giro del rotor central
                this.rotorIzquierda = this.rotorIzquierda.giro(1); // gira rotor izquierdo
                this.rotorCentral = this.rotorCentral.giro(1); // gira rotor central
            }
        }
        this.rotorDerecha = this.rotorDerecha.giro(1); // gira rotor derecho
    }

    /**
     * Cifrado para la interfaz interactiva, revisar utilidad para usar esta
     * función o la de cifrado.
     *
     * @param c
     * @return Completar una vez terminada la depuración completa?
     */
    public char cifradoBase(char c) {
        char aux;
        int i;
        indiceP = 0;
        pintar[indiceP] = c;
        indiceP++;
        actualizarRotores();
        i = this.entrada.obtenerContenido().indexOf(c); // obtiene el indice del caracter pasado
        i = this.rotorDerecha.cifrarIda(i);
        i = this.rotorCentral.cifrarIda(i);
        i = this.rotorIzquierda.cifrarIda(i);
        i = this.reflector.cifrarIda(i);            //Reflector
        i = this.rotorIzquierda.cifrarVuelta(i);
        i = this.rotorCentral.cifrarVuelta(i);
        i = this.rotorDerecha.cifrarVuelta(i);
        aux = this.entrada.obtenerContenido().charAt(i); // obtiene el caracter del indice obtenido
        pintar[indiceP] = aux;
        indiceP++;
        menu.setcI((char) this.rotorIzquierda.getContEscritura().charAt(0));
        menu.setcC((char) this.rotorCentral.getContEscritura().charAt(0));
        menu.setcD((char) this.rotorDerecha.getContEscritura().charAt(0));
        return aux;
    }

    /**
     * Cifra el caracter pasado
     *
     * @param c Caracter del mensaje para cifrar
     * @return Caracter cifrado del mensaje
     */
    public char cifrado(char c) {
        indiceP = 0;
        pintar[indiceP] = c;
        System.out.println(" carácter que llega al primer rotor (llegada): " + c);
        indiceP++;

        char aux;
        actualizarRotores();
        int i;
        if (cifrado == 0) {
            i = this.entrada.obtenerContenido().indexOf(c); // obtiene el indice del caracter pasado
        } else {
            i = this.entradaAmpliado.obtenerContenido().indexOf(c); // obtiene el indice del caracter pasado
        }

        System.out.println("---ROTOR DERECHO---");
        i = this.rotorDerecha.cifrarIda(i);
        System.out.println("---ROTOR CENTRAL---");
        i = this.rotorCentral.cifrarIda(i);
        System.out.println("---ROTOR IZQUIERDO---");
        i = this.rotorIzquierda.cifrarIda(i);

        System.out.println("---REFLECTOR---");
        if (modo == 0) {
            if (cifrado == 0) {
                i = this.reflector.cifrarIda(i);
            } else {
                i = this.reflectorAmpliado.cifrarIda(i);
            }
        } else {
            i = this.reflectorAmpliado.cifrarVuelta(i);
        }

        System.out.println("---ROTOR IZQUIERDO---");
        i = this.rotorIzquierda.cifrarVuelta(i);
        System.out.println("---ROTOR CENTRAL---");
        i = this.rotorCentral.cifrarVuelta(i);
        System.out.println("---ROTOR DERECHO---");
        i = this.rotorDerecha.cifrarVuelta(i);

        if (cifrado == 0) {
            aux = this.entrada.obtenerContenido().charAt(i); // obtiene el caracter del indice obtenido
        } else {
            aux = this.entradaAmpliado.obtenerContenido().charAt(i); // obtiene el caracter del indice obtenido
        }

        System.out.println(" *** carácter cifrado (salida): " + aux);
        pintar[indiceP] = aux;
        indiceP++;
        return aux;
    }

    /**
     * Pone la conexión de las clavijas en el alfabeto
     *
     * @param a clavija a
     * @param b clavija b
     */
    public void ponerClavija(char a, char b) {
        if (this.plugboard.establecerConexion(a, b)) {
            moverClavijas(a, b);
        }
    }

    /**
     * Elimina la conexión de las clavijas del alfabeto
     *
     * @param a clavija a
     * @param b clavija b
     */
    public void eliminarClavija(char a, char b) {
        if (this.plugboard.eliminarConexion(a, b)) {
            moverClavijas(a, b);
        }
    }

    /**
     * Mueve las clavijas en el alfabeto
     *
     * @param a clavija a
     * @param b clavija b
     */
    public void moverClavijas(char a, char b) {
        String aux = "", contenido;
        char susIzq, susDer, c;
        if (cifrado == 0) {
            contenido = this.entrada.obtenerContenido();
        } else {
            contenido = this.entradaAmpliado.obtenerContenido();
        }
        // ordena las clavijas
        if (a < b) {
            susIzq = a;
            susDer = b;
        } else {
            susIzq = b;
            susDer = a;
        }
        for (int i = 0; i < contenido.length(); i++) {
            c = contenido.charAt(i);
            // reordena el alfabeto de escritura
            if (c == susIzq) {
                aux += susDer;
            } else if (c == susDer) {
                aux += susIzq;
            } else {
                aux += c;
            }
        }
        if (cifrado == 0) {
            this.entrada = new Rotor(aux, this.entrada.obtenerPuntoGiro());
        } else {
            this.entradaAmpliado = new Rotor(aux, this.entradaAmpliado.obtenerPuntoGiro());
        }
    }

    /**
     * Obtiene las conexiones de las clavijas.
     *
     * @return conexiones del clavijero
     */
    public Clavijero getPlugboard() {
        return plugboard;
    }
}
