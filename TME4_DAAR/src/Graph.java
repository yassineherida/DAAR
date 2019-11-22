import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	
	public int size() {
		return adjList.size();
	}


	public void writeGraph(String fileName) throws IOException {
		OutputStream flux=new FileOutputStream(fileName); 
		OutputStreamWriter ecriture = new OutputStreamWriter(flux);
		BufferedWriter buff=new BufferedWriter(ecriture);
		try {
			for(Entry<Integer,Set<Integer>> e:adjList.entrySet()) {
				for(Integer v:e.getValue()) {
					if(e.getKey() < v) {
						buff.write(e.getKey() + " " + v + "\n");
					}
				}

			}
		}finally {
			buff.close();
		}
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
