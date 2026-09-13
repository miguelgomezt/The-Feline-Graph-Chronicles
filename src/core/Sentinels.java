package core;

public class Sentinels {
    public static final long NO_ROUTE = Long.MAX_VALUE;

    public static final long NO_ROUTE_INT = Integer.MIN_VALUE;

    public static final long UNBOUNDED = Long.MAX_VALUE;

    private Sentinels() {

    }

    public static boolean isNoRoute(long value){
        return value == NO_ROUTE;
    }

    public static boolean isNoRoute(int value){
        return value == NO_ROUTE_INT;
    }

    public static boolean isUnbounded(long value){
        return value == UNBOUNDED;
    }

    public static long safeAdd(long a, long b){
        if(isNoRoute(a) || isNoRoute(b)){
            return NO_ROUTE;
        }
        if (isUnbounded(a) || isUnbounded(b)){
            return UNBOUNDED;
        }
        return a+b;
    }
}
