import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class Graph {
	Map<Integer, Set<Integer>> adjList;
	double edgeThreshold;
	
	Map<Integer,Integer> prvIndex = new HashMap<>();
	Map<Integer,Integer> futureIndex = new HashMap<>();

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
	
	public double edgeThreshold() {
		return edgeThreshold;
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
	
	//return true if graph is strongly connex, false otherwise
	public boolean isConnex() {
		boolean[] marked = new boolean[size()];
		for(int i = 0; i < size(); i++) {
			marked[i] = false;
		}
		Stack<Integer> p = new Stack<>();
		//strong hypothesis here ... vertice indexes go from 0 to n-1
		p.add(0);
		while(!p.isEmpty()) {
			int n = p.pop();
			marked[n] = true;
			for(Integer v : voisins(n)) {
				if(!marked[v]) {
					marked[v] = true;
					p.add(v);
				}
			}
		}
		System.out.println("\n\n");
		for(int i = 0; i < size(); i++) {
			if(!marked[i]) {
				System.out.println("it is " + i );
				return false;
			}
		}
		System.out.println("\n\n");
		return true;
	}
	
	//returns list of strongly connex components of graph
	public List<Graph> connexComp() {
		List<Graph> res = new ArrayList<Graph>();
		
		boolean[] marked = new boolean[size()];
		for(int i = 0; i < size(); i++) {
			marked[i] = false;
		}
		Stack<Integer> p = new Stack<>();
		Set<Integer> compo = new HashSet<>();
		//strong hypothesis here ... vertice indexes go from 0 to n-1
		p.add(0);
		
		do {
			while(!p.isEmpty()) {
				int n = p.pop();
				marked[n] = true;
				compo.add(n);
				for(Integer v : voisins(n)) {
					if(!marked[v]) {
						marked[v] = true;
						p.add(v);
					}
				}
			}
			
			//create component
			Graph gCompo = new Graph(compo.size());
			gCompo.edgeThreshold = edgeThreshold;
			int i = 0;
			for(Integer s:compo) {
				gCompo.prvIndex.put(i, s);
				gCompo.futureIndex.put(s, i);
				for(Integer v: voisins(s)) {
					if(v < s) {
						gCompo.addEdge(i, gCompo.futureIndex.get(v));
					}
				}
				i++;
			}
			res.add(gCompo);
			for(i = 0; i < size(); i++) {
				if(!marked[i]) {
					p.add(i);
					break;
				}
			}
			compo.clear();
		}while(!p.isEmpty());
		return res;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Entry<Integer,Set<Integer>> v:adjList.entrySet()) {
			sb.append(v.getKey() + " [");
			for(Integer e:v.getValue()) {
				sb.append(e + " ");
			}
			sb.append("]\n");
		}
		return sb.toString();
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
		g.edgeThreshold = edgeThreshold;
		return g;
	}
	
	public static void main(String[] args) {
		Graph g = new Graph(4);
		g.addEdge(0, 1);
		g.addEdge(2, 3);
		System.out.println(g.connexComp());
	}
}
