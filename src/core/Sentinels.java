package core;

/*
La clase sentinela es para agregar un valor especial, que signigica que aqui
no hay respuesta. Esto es para no agregar arimetica, es decir todo se consulta
con los metodos de esta clase.
 */

public class Sentinels {
    /** Significa no hay ruta, para las misiones 2, 3, 4 */
    public static final long NO_ROUTE = Long.MAX_VALUE;

    /** Es el mismo sentinela pero para la mision 1, pero con int
     * porque cuenta movimientos.*/
    public static final long NO_ROUTE_INT = Integer.MIN_VALUE;

    /** Este significa no acotado principalmente para la mision 3*/
    public static final long UNBOUNDED = Long.MAX_VALUE;

    private Sentinels() {
    }

    /** True si el valor recibido es el sentinela que no tiene ruta*/
    public static boolean isNoRoute(long value){
        return value == NO_ROUTE;
    }

    /** Ahora true si el valor recibido es el sin ruta pero
     * la versión int*/
    public static boolean isNoRoute(int value){
        return value == NO_ROUTE_INT;
    }

    /** True si el valor recibido es el no acotado*/
    public static boolean isUnbounded(long value){
        return value == UNBOUNDED;
    }

    public static long safeAdd(long a, long b){
        if(isNoRoute(a) || isNoRoute(b)){  //Si el ninguno de ellos tiene ruta, el resultado tampoco
            return NO_ROUTE;
        }
        if (isUnbounded(a) || isUnbounded(b)){ // Si alguno de ellos es infinito, el resultado tambien
            return UNBOUNDED;
        }
        return a+b; //Caso normal, suma entre ambos.
    }
}
