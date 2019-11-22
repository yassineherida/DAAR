import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;


public class Betweeness {



	

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

	//kind of hackish to return the two lists ;; returns List of Object and then we upcast them 
	// R is matrix of precedency
	// M is matrix of distance
	public ArrayList<Object> floydWarshallv2(ArrayList<Set<Integer>> edge, double edgeThreshold) {

		int n = edge.size();
		double[][] M = new double[n][n];

		ArrayList<ArrayList<Set<Integer>>> R = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			R.add(new ArrayList<>());
			for (int j = 0; j < n; j++) {
				if (i==j)
					M[i][j]=0;
				else 
					M[i][j]= Double.POSITIVE_INFINITY;

				R.get(i).add(new HashSet<>());
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
					
					if(Double.isInfinite(M[i][p]) || Double.isInfinite(M[p][j])) continue;
					
					if (M[i][p] + M[p][j] < M[i][j]) {

						M[i][j] = M[i][p] + M[p][j];
						R.get(i).get(j).clear();
						R.get(i).get(j).add(p);
					}
					if (M[i][p] + M[p][j] == M[i][j]) {
						//System.out.println("i: " + i + " j: " + j + " p: " + p);
						R.get(i).get(j).add(p);
					}

				}
			}
		}
		ArrayList<Object> result = new ArrayList<>();
		result.add(M);
		result.add(R);
		return result;
	}

	public double betweeness(int v, int[][] nb_ppc, ArrayList<ArrayList<HashSet<Integer>>> R) {
		int s, t;
		double b = 0;
		for(s=0; s < nb_ppc.length; s++) {
			if(s==v) continue;
			for(t=s+1; t < nb_ppc.length; t++) {
				if (t==v) continue;
				int tmp1 = nb_ppc_using_point(R, nb_ppc, nb_ppc.length, s, t, v);
				int tmp2 = nb_ppc[s][t];
				b += 1.0 * tmp1 /tmp2;
				
			}

		}
		return b;

	}

	public void addEdge (ArrayList<Set<Integer>> g, int i, int j) {
		g.get(i).add(j);
		g.get(j).add(i);
	}
	
	public int nb_ppc_from_point (ArrayList<ArrayList<HashSet<Integer>>> R, int n, int i, int j) {
		int nb_ppc = 0;
		for(int k: R.get(i).get(j)) {
			if(k==j) return 1;
			nb_ppc += nb_ppc_from_point(R, n, i, k) * nb_ppc_from_point(R, n, k, j);
		}
		return nb_ppc;
	}
	
	/*
	// returns the number of shortest paths from point i to point j with v in it
	public int nb_ppc_using_point (ArrayList<ArrayList<HashSet<Integer>>> R, int[][] t_nb_ppc, int n, int i, int j, int v) {
		if(v==i || v==j) return 1;
		int nb_ppc = 0;
		boolean flag = false;
		int acc = 0;
		for(int k: R.get(i).get(j)) {
			if(k==j) return 0;
			//bug here...
			nb_ppc = nb_ppc_using_point(R, t_nb_ppc, n, i, k, v) * nb_ppc_using_point(R, t_nb_ppc, n, k, j, v);
			if(k==v) {
				flag = true;
				break;
			}
			acc += nb_ppc;
		}
		if(flag) return nb_ppc;
		return acc;
	}
	*/
	
	// returns the number of shortest paths from point i to point j with v in it
	public int nb_ppc_using_point (ArrayList<ArrayList<HashSet<Integer>>> R, int[][] t_nb_ppc, int n, int i, int j, int v) {
		if(v==i || v==j) return 1;
		int nb_ppc = 0;
		for(int k: R.get(i).get(j)) {
			if(k==j) return 0;
			if(k==v) {
				nb_ppc += t_nb_ppc[i][v];
				break;
			}
			nb_ppc += nb_ppc_using_point(R, t_nb_ppc, n, i, k, v);
		}
		return nb_ppc;
	}
	
	public int[][] nb_ppc(ArrayList<ArrayList<HashSet<Integer>>> R, int n){

		int[][] nb_ppc = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				nb_ppc[i][j] = nb_ppc_from_point(R, n, i, j);
			}
		}
		return nb_ppc;
	}
	
	public double[] betweenessGraph(Graph g, double edgeThreshold) {
		int n = g.adjList.size();
		
		ArrayList<Set<Integer>> l = new ArrayList<>();
		for(Entry<Integer, Set<Integer>> e: g.adjList.entrySet()) {
			l.add(e.getValue());
		}
		
		ArrayList<Object> resFloydWarshall = floydWarshallv2(l, edgeThreshold);
		double [][] M = (double[][]) resFloydWarshall.get(0);
		ArrayList<ArrayList<HashSet<Integer>>> R = (ArrayList<ArrayList<HashSet<Integer>>>) resFloydWarshall.get(1);
		int[][] nb_ppc = nb_ppc(R, n);
		

		double[] res = new double[n];
		for(int i = 0; i < n; i++) {
			res[i] = betweeness(i, nb_ppc, R);
		}
		return res;
	}


	public static void main (String[] args) {
		Betweeness f = new Betweeness();

		int[][] dist = {{0, 1, 10, 1}, {1, 0, 10, 10}, {10, 10, 0, 1}, {1, 10, 1, 0}};
		ArrayList<Object> l  = f.floydWarshall(dist, 2);

		
		for(int[] t: (int[][]) l.get(1)) {
			for(int e: t) {
				System.out.print(e + " ");
			}
			System.out.println();
		}
		
		ArrayList<Set<Integer>> g = new ArrayList<>();
		/*
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
		*/
		g.add(new HashSet<>());
		g.add(new HashSet<>());
		g.add(new HashSet<>());
		g.add(new HashSet<>());
		g.add(new HashSet<>());
		g.add(new HashSet<>());
		g.add(new HashSet<>());
		g.add(new HashSet<>());

		f.addEdge(g, 0, 1);
		f.addEdge(g, 1, 2);
		f.addEdge(g, 1, 4);
		f.addEdge(g, 1, 5);
		f.addEdge(g, 3, 4);
		f.addEdge(g, 4, 6);
		f.addEdge(g, 4, 5);
		f.addEdge(g, 5, 6);
		f.addEdge(g, 5, 7);


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
		double [][] M = (double[][]) l2.get(0);
		
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
		
		int[][] nb_ppc = f.nb_ppc(R2, g.size());
		for(int[] t: nb_ppc) {
			for(int e: t) {
				System.out.print(e + " ");
			}
			System.out.println();
		}
		
		System.out.println(nb_ppc[3][5]);
		//System.out.println(f.nb_ppc_using_point(R2, g.size(), 3, 7, 4));
		
		System.out.println(f.betweeness(1, nb_ppc, R2));
		System.out.println(f.betweeness(4, nb_ppc, R2));
		System.out.println(f.betweeness(5, nb_ppc, R2));
	}
}
