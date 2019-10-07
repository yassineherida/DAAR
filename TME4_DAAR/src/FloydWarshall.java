import java.util.ArrayList;
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
	
	public ArrayList<Object> floydWarshallv2(Set<Integer>[] edge, int edgeThreshold) {
		
		int n = edge.length;
		double[][] M = new double[n][n];
		
		ArrayList<ArrayList<ArrayList<Integer>>> R = new ArrayList<>();
		
		int[][] nb_ppc = new int[n][n];
		for (int i = 0; i < n; i++) {
			R.add(new ArrayList<>());
			for (int j = 0; j < n; j++) {
				if (i==j)
					M[i][j]=0;
				else 
					M[i][j]= Double.POSITIVE_INFINITY;
				
				R.get(i).add(new ArrayList<>());
				nb_ppc[i][j] = 0;
				// System.out.println("R[i][j] vaut " + R[i][j]);
			}
		}
		for (int i=0;i<edge.length;i++) {
			for (Integer e:edge[i]) {
				M[i][e]=1;
			}
		}
		

		for (int p = 0; p < n; p++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					
					ArrayList<Integer> rclone = (ArrayList<Integer>) R.get(i).get(j).clone();
						if (M[i][p] + M[p][j] < M[i][j]) {
							nb_ppc[i][j] = 1;
							M[i][j] = M[i][p] + M[p][j];
							R.get(i).get(j).clear();
							R.get(i).get(j).add(p);
						}
						if (M[i][p] + M[p][j] == M[i][j]) {
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
	
	public static void main (String[] args) {
		FloydWarshall f = new FloydWarshall();
		
		int[][] dist = {{0, 1, 10, 1}, {1, 0, 10, 10}, {1, 10, 0, 1}, {1, 10, 1, 0}};
		ArrayList<Object> l  = f.floydWarshall(dist, 2);
		
		ArrayList<Object> l2  = f.floydWarshallv2(dist, 2);
		ArrayList<ArrayList<ArrayList<Integer>>> R2 = (ArrayList<ArrayList<ArrayList<Integer>>>) l2.get(1);
		for(ArrayList<ArrayList<Integer>> t: R2) {
			for(ArrayList<Integer> e: t) {
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
