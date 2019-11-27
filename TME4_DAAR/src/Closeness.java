import java.util.ArrayList;

public class Closeness {
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
}
