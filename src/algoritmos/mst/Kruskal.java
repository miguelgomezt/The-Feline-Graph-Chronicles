package algoritmos.mst;

import core.Edge;
import core.Graph;
import core.MstResult;

import java.util.ArrayList;
import java.util.List;

public class Kruskal {

    public static MstResult solve(Graph graph) {
        int n = graph.getNodeCount();

        List<Edge> edges = new ArrayList<Edge>(graph.getAllEdges());
        edges.sort((a, b) -> Long.compare(a.getWeight(), b.getWeight()));

        UnionFind unionFind = new UnionFind(n);
        List<Edge> edgesUsed = new ArrayList<Edge>();
        long totalCost = 0L;
        int edgesInTree = 0;

        for (Edge edge : edges) {
            if (edgesInTree == n - 1) {
                break;
            }
            if (unionFind.union(edge.getFrom(), edge.getTo())) {
                edgesUsed.add(edge);
                totalCost += edge.getWeight();
                edgesInTree++;
            }
        }

        if (edgesInTree < n - 1) {
            return MstResult.disconnected();
        }

        return new MstResult(totalCost, edgesUsed, true);
    }
}
