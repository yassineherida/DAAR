import java.awt.image.PackedColorModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Main{
	private Map<String, Map<String,Integer>> database = new HashMap<String, Map<String,Integer>>();
	private Map<String, File> allFiles = new HashMap<>();
	private Map<Integer, String> fileNumber = new HashMap<>();
	static int cpt = 0;
	final DecimalFormat df = new DecimalFormat("#0.000");
	
	//should be a parameter of exec
	final String commonestWordFilename = "200_commonest_word.txt";
	static Set<String> commonestWords = new HashSet<>();
	
	public Map<String, Integer> buildIndexing(Path p) throws IOException {
		return Indexing.indexing(p.toString());
	}

	public void parseCommonestWords(String filename) throws IOException {
		InputStream flux=new FileInputStream(filename); 
		InputStreamReader lecture=new InputStreamReader(flux);
		BufferedReader buff=new BufferedReader(lecture);
		try {
			String ligne;
			while ((ligne=buff.readLine())!=null){
				commonestWords.add(ligne);
			}
		}finally {
			buff.close();
		}
	}
	
	public boolean buildDataBase(File dir) throws IOException {
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				System.out.println("Computing for " + child);
				database.put(child.getName(), buildIndexing(child.toPath()));
				allFiles.put(child.getName(), child);
				fileNumber.put(cpt++, child.getName());
			}
		} else {
			System.out.println(dir.getName() + " is not a directory");
			return false;
		}
		System.out.println("Database contains following files");
		System.out.println(database.keySet());
		return true;
	}

	public void menu() throws IOException {
		parseCommonestWords(commonestWordFilename);
		System.out.println("Enter the repository containing the data");
		BufferedReader reader =  
				new BufferedReader(new InputStreamReader(System.in)); 
		Map.Entry<String,Integer> entry =
				new AbstractMap.SimpleEntry<String, Integer>("exmpleString", 42);
		// Reading data using readLine 
		String name = reader.readLine(); 
		File dir = new File(name);
		if (!buildDataBase(dir)) throw new IOException("Not a repository");

	}

	public double[][] matDistJaccard(){
		double [][] mat = new double[fileNumber.size()][fileNumber.size()];
		int i = 0, j = 0;
		for(Entry<Integer,String> file1 : fileNumber.entrySet()) {
			for(Entry<Integer,String> file2: fileNumber.entrySet()) {
				if (file1 == file2) {
					mat[file1.getKey()][file2.getKey()] = 0;
				}
				else {
					
					mat[file1.getKey()][file2.getKey()] = DistanceJaccard.distanceJaccard(database.get(file1.getValue()), database.get(file2.getValue()));
				}
			}
		}
		return mat;
	}

	public void printMatJac(double[][] mat) {
		int n = fileNumber.size();
		String[] fileName = new String[n];
		for(Entry<Integer,String> file : fileNumber.entrySet()) {
			fileName[file.getKey()] = file.getValue();
		}
		
		System.out.print("\t");
		int fixed_number = 6;
		for(String s:fileName) {
			if(s.length() > fixed_number) {
				System.out.print(s.substring(0, fixed_number) + "\t");
			}
			else {
				System.out.print(s + "\t");
			}
		}
		System.out.println();
		
		
		for(int i = 0; i < n; i++) {
			if(fileName[i].length() > fixed_number) {
				System.out.print(fileName[i].substring(0, fixed_number) + "\t");
			}
			else {
				System.out.print(fileName[i] + "\t");
			}
			for(int j = 0; j < n; j++) {
				System.out.print(df.format(mat[i][j]) + "\t");
			}
			System.out.println();
		}
		

	}
	
	public void print_table() {
		for(Entry<Integer,String> s:fileNumber.entrySet()) {
			System.out.println(s.getKey() + " " + s.getValue());
		}
	}

	
	public static void main(String[] args) throws IOException  {
		Main m = new Main();
		m.menu();
		m.print_table();
		double [][] distJac = m.matDistJaccard();
		m.printMatJac(distJac);
		Graph g =Util.chooseThreshold(distJac);
		g.writeGraph("../graph.txt");
		
		
		
		
		Betweeness b = new Betweeness();
		PageRank pg = new PageRank();
		
		PageRank2 pg2 = new PageRank2();
		Closeness c = new Closeness();
		//WITH CONNEX GRAPH
		double[] betweeness = b.betweenessGraph(g, g.edgeThreshold());
		double[] page_rank = pg.page_rank(g, 0.1, 10, g.size()); 
		
		BigDecimal[] page_rank3 = pg2.page_rank(g, 0.1, 20, g.size());
		
		//WITH FIXED EDGETHRESHOLD AND MULTIPLE COMPONENTS GRAPH
		List<Graph> compoG = Util.threshold75(distJac);
		double[] betweeness2 = new double[g.size()];
		//double[] page_rank2 = new double[g.size()];
		
		BigDecimal[] page_rank2 = new BigDecimal[g.size()];
		double[][] matdis = c.floydWarshallv2(g, g.edgeThreshold());
		double [] closene = c.closeness(matdis,g.size());
		double [] closene2=new double [g.size()];
		for(Graph g1: compoG) {
			double[] betweenessG = b.betweenessGraph(g1, g1.edgeThreshold());
			//double[] page_rankG = pg.page_rank(g1, 0.1, 10, g1.size()); 
			
			
			BigDecimal[] page_rankG = pg2.page_rank(g1, 0.1, 10, g1.size());
			double[][] matdisG = c.floydWarshallv2(g1, g1.edgeThreshold());
			double [] closeneG = c.closeness(matdisG,g1.size());
			for(int i = 0; i < betweenessG.length; i++) {
				betweeness2[g1.prvIndex.get(i)] = betweenessG[i];
				page_rank2[g1.prvIndex.get(i)] = page_rankG[i];
				closene2[g1.prvIndex.get(i)]=closeneG[i];
			}
			
		}
		
		OutputStream fluxPR=new FileOutputStream("../resPageRank"); 
		OutputStreamWriter ecriturePR=new OutputStreamWriter(fluxPR);
		BufferedWriter buffPR=new BufferedWriter(ecriturePR);
		
		OutputStream fluxBT=new FileOutputStream("../resBetweeness"); 
		OutputStreamWriter ecritureBT=new OutputStreamWriter(fluxBT);
		BufferedWriter buffBT=new BufferedWriter(ecritureBT);
		
		OutputStream fluxCL1=new FileOutputStream("../resCloseness1"); 
		OutputStreamWriter ecritureCL1=new OutputStreamWriter(fluxCL1);
		BufferedWriter buffCL1=new BufferedWriter(ecritureCL1);
		
		OutputStream fluxCL2=new FileOutputStream("../resCloseness2"); 
		OutputStreamWriter ecritureCL2=new OutputStreamWriter(fluxCL2);
		BufferedWriter buffCL2=new BufferedWriter(ecritureCL2);
		
		
		System.out.println("---------------PAGE RANK----------------");
		for(int i = 0; i < g.size(); i++) {
			System.out.println(m.fileNumber.get(i) + " " +page_rank2[i]);
			System.out.println(i + " " + m.fileNumber.get(i) + " " +page_rank3[i]);
			
			buffPR.write(page_rank2[i].toPlainString());
			
			
		}
		System.out.println("----------------------------------------\n");
		
		System.out.println("---------------BETWEENESS----------------");
		for(int i = 0; i < g.size(); i++) {
			//System.out.println(m.fileNumber.get(i) + " " +betweeness[i]);
			System.out.println(i + " " + m.fileNumber.get(i) + " " +betweeness2[i]);
			
			buffBT.write( betweeness2[i]+"\n");
		}
		System.out.println("---------------CLOSENESS----------------");
		for(int i = 0; i < g.size(); i++) {
			//System.out.println(m.fileNumber.get(i) + " " +betweeness[i]);
			System.out.println(i + " " + m.fileNumber.get(i) + " " +closene[i]);
			
			buffCL1.write( closene[i]+"\n");
		}
		System.out.println("---------------CLOSENESS2----------------");
		for(int i = 0; i < g.size(); i++) {
			//System.out.println(m.fileNumber.get(i) + " " +betweeness[i]);
			System.out.println(i + " " + m.fileNumber.get(i) + " " +closene2[i]);
			
			buffCL2.write( closene2[i]+"\n");
		}
		System.out.println("----------------------------------------\n");
		buffPR.close();
		buffBT.close();
	}
}


