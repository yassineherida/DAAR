import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
	Map<Integer, Set<Integer>> adjList;
	
	public Graph(List<Integer> nodes) {
		adjList = new HashMap<>();
		for(Integer n: nodes) {
			adjList.put(n, new HashSet<>());
		}
	}
	
	public Graph(int n) {
		adjList = new HashMap<>();
		for(int i = 0; i < n; i++) {
			adjList.put(i, new HashSet<>());
		}
	}
	
	public void addEdge(int i, int j) {
		adjList.get(i).add(j);
		adjList.get(j).add(i);
	}
	
	public void addEdgeDirected(int i, int j) {
		adjList.get(i).add(j);
	}
	
	public int degree(int n) {
		return adjList.get(n).size();
	}
	
	public Set<Integer> voisins(int n){
		return adjList.get(n);
	}
	
	static public Graph gFromMat(double edgeThreshold, double[][] mat) {
		int n = mat.length;
		Graph g = new Graph(n);
		for(int i = 0; i < n; i++) {
			for(int j = i+1; j < n; j++) {
				if(i==j) continue;
				if(mat[i][j] <= edgeThreshold) g.addEdge(i, j);
			}
		}
		return g;
	}
}
