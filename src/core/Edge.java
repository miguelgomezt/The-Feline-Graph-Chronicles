package core;

/**
 * Una artista del grafo. Es la conexion entre dos nodos.
 *
 * Esta clase sirve para las tres misiones porque usan grafos,
 * que se distinguen por el peso del nodo.
 * Mision 2: Dijkstra el peso es tiempo entonces siempre >= 0
 * Mision 3: Floyd/Bellman el peso es churum, puede ser negativo.
 * Mision 4: Kruskal el peso es el costo de tender un cable.
 */

public class Edge {
    private final int from;
    private final int to;
    private final long weight;

    public Edge(int from, int to, long weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public long getWeight() {
        return weight;
    }

    public int other (int node){
        if(node == from){
            return to;
        }
        return from;
    }
    @Override
    public String toString() {
        return from + " -> " + to + " (" + weight + ")";
    }
}
