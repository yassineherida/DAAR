import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class FloydWarshall {

	public ArrayList<Object> floydWarshall(int[][] dists, int edgeThreshold) {

		int n = dists.length;
		double[][] M = new double[n][n];

		int[][] R = new int[n][n];
		
		int[][] nb_ppc = new int[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				double dist = dists[i][j];
				M[i][j] = dist < edgeThreshold ? dist : Double.POSITIVE_INFINITY;
				R[i][j] = j;
				nb_ppc[i][j] = 1;
				// System.out.println("R[i][j] vaut " + R[i][j]);
			}
		}

		for (int p = 0; p < n; p++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (M[i][j] > M[i][p] + M[p][j]) {
						M[i][j] = M[i][p] + M[p][j];
						R[i][j] = R[i][p];
					}
					if (M[i][j] == M[i][p] + M[p][j]) {
						nb_ppc[i][j]++;
					}
				}
			}
		}
		ArrayList<Object> result = new ArrayList<>();
		result.add(M);
		result.add(R);
		return result;
	}
	
	public ArrayList<Object> floydWarshallv2(ArrayList<Set<Integer>> edge, int edgeThreshold) {
		
		int n = edge.size();
		double[][] M = new double[n][n];
		
		ArrayList<ArrayList<Set<Integer>>> R = new ArrayList<>();
		
		int[][] nb_ppc = new int[n][n];
		for (int i = 0; i < n; i++) {
			R.add(new ArrayList<>());
			for (int j = 0; j < n; j++) {
				if (i==j)
					M[i][j]=0;
				else 
					M[i][j]= Double.POSITIVE_INFINITY;
				
				R.get(i).add(new HashSet<>());
				nb_ppc[i][j] = 0;
				// System.out.println("R[i][j] vaut " + R[i][j]);
			}
		}
		for (int i=0;i<edge.size();i++) {
			R.get(i).get(i).add(i);
			for (Integer e:edge.get(i)) {
				M[i][e]=1;
				R.get(i).get(e).add(e);
			}
		}
		

		for (int p = 0; p < n; p++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					
					if((i==p || j==p) || i==j) continue;

						if (M[i][p] + M[p][j] < M[i][j]) {
							nb_ppc[i][j] = 1;
							M[i][j] = M[i][p] + M[p][j];
							R.get(i).get(j).clear();
							R.get(i).get(j).add(p);
						}
						if (M[i][p] + M[p][j] == M[i][j]) {
							System.out.println("i: " + i + " j: " + j + " p: " + p);
							nb_ppc[i][j]++;
							R.get(i).get(j).add(p);
						}
					
				}
			}
		}
		ArrayList<Object> result = new ArrayList<>();
		result.add(M);
		result.add(R);
		result.add(nb_ppc);
		return result;
	}
	
	public float betweeness(int v, int[][] nb_ppc, ArrayList<ArrayList<ArrayList<Integer>>> R) {
		int s, t;
		for(s=0; s < nb_ppc.length; s++) {
			if(s==v) continue;
			for(t=s+1; t < nb_ppc.length; t++) {
				if (t==v) continue;
				
			}
	
		}
		return 0;
		
	}
	
	public void addEdge (ArrayList<Set<Integer>> g, int i, int j) {
		g.get(i).add(j);
		g.get(j).add(i);
	}
	
	public static void main (String[] args) {
		FloydWarshall f = new FloydWarshall();
		
		int[][] dist = {{0, 1, 10, 1}, {1, 0, 10, 10}, {1, 10, 0, 1}, {1, 10, 1, 0}};
		ArrayList<Object> l  = f.floydWarshall(dist, 2);
		
		ArrayList<Set<Integer>> g = new ArrayList<>();
		g.add(new HashSet<>());
		g.add(new HashSet<>());
		g.add(new HashSet<>());
		g.add(new HashSet<>());
		g.add(new HashSet<>());
		g.add(new HashSet<>());
		
		f.addEdge(g, 0, 1);
		f.addEdge(g, 0, 2);
		f.addEdge(g, 1, 2);
		f.addEdge(g, 1, 3);
		f.addEdge(g, 3, 4);
		f.addEdge(g, 2, 4);
		f.addEdge(g, 3, 5);
		f.addEdge(g, 4, 5);
		f.addEdge(g, 1, 4);
		f.addEdge(g, 2, 3);
		
		ArrayList<Object> l2  = f.floydWarshallv2(g, 2);
		ArrayList<ArrayList<HashSet<Integer>>> R2 = (ArrayList<ArrayList<HashSet<Integer>>>) l2.get(1);
		for(ArrayList<HashSet<Integer>> t: R2) {
			for(HashSet<Integer> e: t) {
				System.out.print(e + " ");
			}
			System.out.println();
		}
		System.out.println(l2.get(1));
		int [][] R = (int[][]) l.get(1);
		double [][] M = (double[][]) l.get(0);
		for(int[] t: R) {
			for(int e: t) {
				System.out.print(e + " ");
			}
			System.out.println();
		}
		
		for(double[] t: M) {
			for(double e: t) {
				System.out.print(e + " ");
			}
			System.out.println();
		}
		System.out.println(l.get(1));
	}
}
