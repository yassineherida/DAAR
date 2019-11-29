import java.util.ArrayList;
public class Closeness {
	public double[] closeness(int[][] dists) {
		double[] res = new double[dists.length];
		double dist =0.0;
		for (int i = 0;i<dists.length;i++) {
			for (int j =0;j<dists.length;i++) {
				dist = dist + dists[i][j];
			}
			res[i]=dists.length/dist;
			dist=0.0;
		}
		return res;
	}
}
