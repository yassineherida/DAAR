import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

public class MainTest {

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
		 int[] t = Automate.retenue("mamamamia");
		 for (int e : t)
			 System.out.println(e);

	      char[] facteur = new String("mamamamia").toCharArray();
	      char[] txt = new String("memamamamia").toCharArray();
	 		System.out.println(Automate.match(facteur, t, txt));*/
		 try {
			Set<Entry<String, ArrayList<Coordinates>>> words = Indexing.indexing("src/test2.txt");
			RadixTree rd = new RadixTree();
			String filename = "rd.ser";
			for (Entry<String, ArrayList<Coordinates>> w: words)
				rd.insertWord(w.getKey(), w.getValue());
			
			//System.out.println(rd);
			rd.saveRadixTree(filename);
			RadixTree rd2 = RadixTree.readRadixTree(filename);
	        //System.out.println(rd2);
	                 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 }

}
