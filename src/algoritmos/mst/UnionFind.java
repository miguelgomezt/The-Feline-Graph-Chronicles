package algoritmos.mst;

/**
 * Estructura Union-Find (conjuntos disjuntos) para Kruskal.
 * Con compresion de caminos y union por tamaño, tal como lo exige
 * el enunciado.
 *
 * Complejidad: practicamente O(1) amortizado por operacion (el
 * inverso de la funcion de Ackermann, en la practica una constante).
 */
public class UnionFind {
    private final int[] parent;
    private final int[] size;

    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int node) {
        while (parent[node] != node) {
            parent[node] = parent[parent[node]]; // path halving
            node = parent[node];
        }
        return node;
    }

    public boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if (rootA == rootB) {
            return false;
        }

        if (size[rootA] < size[rootB]) {
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }
        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        return true;
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }
}
