import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class PageRank2 {
	
	static MathContext mc = new MathContext(40, RoundingMode.HALF_UP);

	void prodmatvect(Graph G, BigDecimal []A, int n, BigDecimal []B){
		int i;
		for(i=0; i < n; i++){
			B[i] = new BigDecimal(0);
		}
		for(i=0; i < n; i++){
			int degree = G.degree(i);
			for(int v : G.voisins(i)){
				B[v] = B[v].add(A[i].divide(new BigDecimal(degree), mc));
			}
		}
	}

	public BigDecimal [] page_rank(Graph G, double alpha, int t, int n) {
		BigDecimal[] I = new BigDecimal[n];
		BigDecimal[] P = new BigDecimal[n];
		BigDecimal[] P_tmp = new BigDecimal[n];
		int i, j;
		
		
		for(i = 0; i < n; i++) {
			I[i] = new BigDecimal(1.0).divide(new BigDecimal( n), mc);
			P_tmp[i] = new BigDecimal(1.0).divide(new BigDecimal( n), mc);
			P[i] = new BigDecimal(1.0).divide(new BigDecimal( n), mc);
		}

		BigDecimal norm = new BigDecimal(0);
		boolean converging = false;

		while (!converging  && i < t){
			converging = true;
			prodmatvect(G, P_tmp, n, P);

			norm = new BigDecimal(0);
			for(j=0; j<n; j++){
				P[j] = P[j].multiply(new BigDecimal(1-alpha)).add(I[j].multiply(new BigDecimal(alpha)));
				norm = norm.add(P[j]);
				//printf("P[%d] before vaut %g\n", j,P[j]);
			}
			for(j=0; j<n; j++){
				P[j] = P[j].add(new BigDecimal(1).subtract(norm).divide(new BigDecimal(n), mc));
				if(P_tmp[j] != P[j])
					converging = false;
				P_tmp[j] = P[j];
			}
			//printf("P[j] after vaut %lf\n", P[j]);
			System.out.println("Finished iteration " + i);
			i = i+1;
		}

		return P;
	}
	
	public  void test() {
		Graph g = new Graph(8);
		g.addEdgeDirected(0, 1);
		g.addEdgeDirected(0, 3);
		g.addEdgeDirected(0, 2);
		g.addEdgeDirected(1, 2);
		g.addEdgeDirected(3, 2);
		g.addEdgeDirected(1, 4);
		g.addEdgeDirected(4, 0);
		g.addEdgeDirected(3, 5);
		g.addEdgeDirected(5, 0);
		g.addEdgeDirected(2, 6);
		g.addEdgeDirected(6, 0);
		g.addEdgeDirected(2, 7);
		g.addEdgeDirected(7, 0);
		
		BigDecimal[] scores = page_rank(g, 0.15, 10, g.size());
		
		for(int i = 0; i < scores.length; i++) {
			System.out.println("SCORES");
			System.out.println(scores[i]);
		}
	}
	
	public static void main(String[] args) {
		PageRank2 p = new PageRank2();
		p.test();
		
	}
}
