package core;

import java.util.ArrayList;
import java.util.List;

/**
 * Grafos creados desde cero, para las misiones 2, 3 y 4.
 *
 * Se guardan dos listas, una de adyacencias y
 * otra lista plana de aristas.
 *
 * NODOS: Se numera de 0 a N-1. La mision 4 los recibe
 * de 1 a N, asi que su parser debe restarles 1 antes de llamar
 * a addEdge.
 */
public class Graph {
    private final int nodeCount;
    private final boolean directed;
    private final List<List<Edge>> adjacency;
    private final List<Edge> allEdges;

    //Creamos un grafo vacio.

    public Graph(int nodeCount, boolean directed) {
        this.nodeCount = nodeCount;
        this.directed = directed;
        this.allEdges = new ArrayList<Edge>();
        this.adjacency = new ArrayList<List<Edge>>();

        //Creamos una lista vacia para cada nodo.
        for (int i = 0; i < nodeCount; i++) {
            adjacency.add(new ArrayList<>());
        }
    }

    /**
     * Hacemos la conexion al grafo. Si el grafo es no diriguido
     * se registran las dos direcciones dentro de la lista de
     * adyacencia.
     */

    public void addEdge(int from, int to, long weight) {
        if(from < 0 || from >= nodeCount || to < 0 || to >= nodeCount) {
            throw new IllegalArgumentException("Nodo fuera del rango" + from + " " + to
            + ". Los nodos validos van de 0 a " + (nodeCount - 1));
        }

        Edge edge = new Edge(from, to, weight);
        adjacency.get(from).add(edge);
        allEdges.add(edge);

        if(!directed) {
            adjacency.get(to).add(new Edge(to, from, weight));
        }
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public boolean isDirected() {
        return directed;
    }

    /**
     * Los artistas que salen del nodo indicado.
     * Es lo que recorre Dijkstra cuando saca un nodo
     * de la cola de prioridad.
     */
    public List<Edge> getNeighbors(int node) {
        if(node < 0 || node >= nodeCount) {
            throw new IllegalArgumentException("Nodo fuera del rango" + node + "." +
                    "Los nodos van de 0 a  " + (nodeCount - 1));
        }
        return adjacency.get(node);
    }

    /**Todos los artistas del grafo, cada uno una sola vez.
     * Es lo que recorren Bellman-Ford y Kruskal.
     */
    public List<Edge> getAllEdges() {
        return allEdges;
    }

    public int getEdgeCount() {
        return allEdges.size();
    }
}
