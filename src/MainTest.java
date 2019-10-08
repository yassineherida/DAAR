import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MainTest {

	
	public static void printMatch(String w, ArrayList<String> txt, ArrayList<Coordinates> match) {
		String reset = new String("\u001B[0m");
		String yellow = new String("\u001B[31m");
		for(Coordinates c: match) {
			int i = c.getX()-1;
			int j = c.getY()-1;
			String ligne = txt.get(i);
			System.out.println(ligne.substring(0,j)+ yellow + ligne.substring(j, j+w.length()) + reset + ligne.substring(j+w.length()));
		}
		
	}
	
	 public static ArrayList<String> readFile (String fileName) throws IOException {
	        InputStream flux=new FileInputStream(fileName); 
	        InputStreamReader lecture=new InputStreamReader(flux);
	        BufferedReader buff=new BufferedReader(lecture);
	        ArrayList<String> res = new ArrayList<>();
	        String ligne;
	        int c=0;
	        int i = 1, j = 1;
	        while ((ligne=buff.readLine())!=null){
	            if(ligne.length() > 0) {
	                res.add(ligne);
	              }        
	        }

	       
	        return res;
	        
	 }
	 
	 public static void main(String arg[]) {
		 //RegExTree a = new RegExTree((int)'a', new ArrayList<RegExTree>());
		 //RegExTree b = new RegExTree((int)'b', new ArrayList<RegExTree>());
		 //ArrayList<RegExTree> te= new ArrayList<>();
		 //te.add(a);
		 //te.add(b);
		 //RegExTree t = exampleAhoUllman();
		 //RegExTree t= new RegExTree(CONCAT, te);
		 //Automate test =new Automate(t);
		 //test.test(t);
		 //System.out.println(test.automate[0][97]);
		 //test.determ(test.start);
		 //System.out.println(test.deter);
		 //System.out.println(test.etat);
		 //System.out.println(test.etatF);
		 //System.out.println(test.parcours("bcaa aa b aaa ab"));
		 
		 /*

	 		System.out.println(Automate.match(facteur, t, txt));*/
		 try {
			 String filename = "test1.txt";
			Set<Entry<String, ArrayList<Coordinates>>> words = Indexing.indexing(filename);
			RadixTree rd = new RadixTree();
			String filename2 = "rd.ser";
			for (Entry<String, ArrayList<Coordinates>> w: words) {
				rd.insertWord(w.getKey(), w.getValue());
			}
			System.out.println(rd);
			ArrayList<Coordinates> l_s = rd.searchWord("and");
			//System.out.println(rd);
			rd.saveRadixTree(filename2);
			RadixTree rd2 = RadixTree.readRadixTree(filename2);
	        //System.out.println(rd2);
			
			ArrayList<String> lignes = readFile(filename);

			for(Coordinates c: l_s) {
				System.out.println(lignes.get(c.getX()-1));
			}
			ArrayList<Coordinates> locs = KMP.kmp(lignes, "and");
			System.out.println(locs);
			//printMatch("and", lignes, locs );
			printMatch("and", lignes, l_s);
	                 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 }

}
