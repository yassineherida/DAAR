import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.security.AllPermission;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class TestPerf {
	
	   private Map<String, RadixTree> database;
	    private Map<String, File> allFiles;
	    
	    public TestPerf() {
	    	database = new HashMap<>();
	    	allFiles = new HashMap<>();
	    }
	    
		
		public RadixTree buildRadixTree(Path p) throws IOException {
			Set<Entry<String, ArrayList<Coordinates>>> words = Indexing.indexing(p.toString());
			RadixTree rd = new RadixTree();
			for (Entry<String, ArrayList<Coordinates>> w: words) {
				rd.insertWord(w.getKey(), w.getValue());
			}
			return rd;
		}
		
		public boolean buildDataBase(File dir) throws IOException {
	        File[] directoryListing = dir.listFiles();
	        if (directoryListing != null) {
	          for (File child : directoryListing) {
	            database.put(child.getName(), buildRadixTree(child.toPath()));
	            allFiles.put(child.getName(), child);
	          }
	        } else {
	          System.out.println(dir.getName() + " is not a directory");
	          return false;
	        }
	        System.out.println("Database contains following files");
	        System.out.println(database.keySet());
	        return true;
		}
		
		public boolean isRegex(String w) {
			boolean cond =false;
			for (int j =0;j<w.length();j++) {
				if(w.charAt(j)=='.'||w.charAt(j)=='|'||w.charAt(j)=='*'||w.charAt(j)=='\\') {
					cond=true;
					break;
					
				}
			}
			return cond;
		}
		
		public static ArrayList<String> readFile (File f) throws IOException {
			InputStream flux=new FileInputStream(f.getAbsolutePath()); 
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
		

	public static void main(String[] args) throws IOException {
	    StringBuilder sortie = new StringBuilder();
		if(args.length < 4 || args.length > 4) {
			System.out.println("  " + args[0]);
			System.out.println("args len : " + args.length);
			System.out.println("Expected format: pattern dirfile method  output");
			return;
		}
		TestPerf tp = new TestPerf();
		String pattern = args[0];
		String output = args[3];
	    File dir = new File(args[1]);
	    tp.buildDataBase(dir);
	    int opt = Integer.parseInt(args[2]);
	    //search for pattern in each file and report computing time for each

	    System.out.println("hello");
	    for(String filename:tp.allFiles.keySet()) {
		    System.out.println("hello");
	    	ArrayList<String> lignes = readFile(tp.allFiles.get(filename));
	    	long start;
	    	long finish;
	    	long timeElapsed;
	    	start = System.nanoTime();
		    switch(opt) {
		    case 0:
		 // ...
		    	RegEx.RegEx(pattern, tp.allFiles.get(filename).getAbsolutePath());
			    break;
		    case 1:
		    	KMP.kmp(lignes, pattern);
		    	break;
		    
		    case 2:
		    	tp.database.get(filename).searchWord(pattern.toLowerCase());
		    	break;
		    }
		    finish = System.nanoTime();
		    timeElapsed = finish - start;
		    
		    
		    String[] arguments = new String[] {"/usr/bin/wc", "-w", tp.allFiles.get(filename).getAbsolutePath()};
		    Process proc = new ProcessBuilder(arguments).start();
		    BufferedReader stdInput = new BufferedReader(new 
		    	     InputStreamReader(proc.getInputStream()));

	    	// Read the output from the command
		    
	    	String s = null;
	    	sortie.append(filename + " ");
	    	while ((s = stdInput.readLine()) != null) {
	    	    sortie.append(s.split("\\s")[0] + " ");
	    	}
	    	sortie.append(timeElapsed + "\n");

	    }
    	BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        writer.write(sortie.toString());
         System.out.println(sortie.toString());
        writer.close();
	}
}

