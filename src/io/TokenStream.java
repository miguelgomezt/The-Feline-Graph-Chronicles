package io;

/**
 * Lector de cada entrada como flujo de tokens.
 * Parseamos la entrada como un flujo de tokens.
 * Partimos el texto por espacios y pedir los numeros de a uno.
 * Sirve mas que todo para la mision 1 que al ser bombas tenemos
 * filas con diferentes cantidades de numeros.
 */
public class TokenStream {
    private final String[] tokens;
    private int position;

    public TokenStream(String text) {
        if(text == null){
            throw new NullPointerException("La entrada esta vacia.");
        }
        String trimmed = text.trim();                // quita espacios y saltos al inicio y al final
        if (trimmed.isEmpty()) {                     // si no quedo nada, no hay tokens
            this.tokens = new String[0];
        } else {
            // "\\s+" es una expresion regular que significa "uno o mas espacios en blanco".
            // Cubre espacios, tabulaciones y saltos de linea, asi que las lineas en
            // blanco y los espacios sobrantes desaparecen solos. Eso es exactamente
            // lo que pide la seccion 2.2.
            this.tokens = trimmed.split("\\s+");
        }
        this.position = 0;                           // arrancamos en el primer token
    }

    /** true si todavia quedan tokens por leer. */
    public boolean hasNext() {
        return position < tokens.length;
    }

    /** Cuantos tokens quedan sin leer. Sirve para validar antes de leer un bloque. */
    public int remaining() {
        return tokens.length - position;
    }

    /**
     * Entrega el siguiente token convertido a entero y avanza el cursor.
     * Si no quedan tokens, o si el token no es un numero, lanza
     * InputFormatException con un mensaje que la GUI puede mostrar tal cual.
     */
    public int nextInt() {
        if (!hasNext()) {                            // se acabo la entrada antes de tiempo
            throw new InputFormatException(
                    "La entrada termino antes de lo esperado: faltan datos.");
        }
        String token = tokens[position];             // tomamos el token actual
        position++;                                  // y avanzamos el cursor
        try {
            return Integer.parseInt(token);          // intentamos convertirlo a numero
        } catch (NumberFormatException e) {
            // El token no era un numero. Decimos cual fue y en que posicion,
            // para que el usuario pueda encontrarlo en su texto.
            throw new InputFormatException(
                    "Se esperaba un numero entero pero se encontro \"" + token
                            + "\" en la posicion " + position + " de la entrada.");
        }
    }

    /**
     * Igual que nextInt() pero ademas verifica que el valor este en un rango.
     * Sirve para atrapar entradas absurdas (una fila negativa, por ejemplo)
     * antes de que revienten un arreglo mas adelante con un error incomprensible.
     */
    public int nextIntInRange(int min, int max, String nombreDelDato) {
        int value = nextInt();                       // leemos normalmente
        if (value < min || value > max) {            // y validamos el rango
            throw new InputFormatException(
                    nombreDelDato + " debe estar entre " + min + " y " + max
                            + ", pero se recibio " + value + ".");
        }
        return value;
    }

    /**
     * Entrega el siguiente token como long.
     * La Mision 1 no lo usa, pero las misiones 2, 3 y 4 si: el enunciado exige
     * long para los pesos, y un peso de 1.000.000 leido como int se desborda al
     * acumularlo 100.000 veces.
     */
    public long nextLong() {
        if (!hasNext()) {
            throw new InputFormatException(
                    "La entrada termino antes de lo esperado: faltan datos.");
        }
        String token = tokens[position];
        position++;
        try {
            return Long.parseLong(token);
        } catch (NumberFormatException e) {
            throw new InputFormatException(
                    "Se esperaba un numero pero se encontro \"" + token
                            + "\" en la posicion " + position + " de la entrada.");
        }
    }
}

