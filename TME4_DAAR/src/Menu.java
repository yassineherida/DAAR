import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Menu {
   private Map<String, RadixTree> database;
    private Map<String, File> allFiles;
    
    public Menu() {
    	database = new HashMap<>();
    	allFiles = new HashMap<>();
    }
    
	public void help() {
	    System.out.println("\nResearch format : pattern filename [-w]\n");
	    System.out.println("-w is an option for whole word research (use of radix tree in this case).");
	    System.out.println("Otherwise, KMP is first-pick method\n");
	    System.out.println("To quit, enter q\n");
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
			if(w.charAt(j)=='.'||w.charAt(j)=='|'||w.charAt(j)=='*') {
				cond=true;
				break;
				
			}
		}
		return cond;
	}
	

	public void printMatch(String w, ArrayList<String> txt, ArrayList<Coordinates> match) {
		String reset = new String("\u001B[0m");
		String yellow = new String("\u001B[31m");
		if(match.isEmpty())
			System.out.println("Found no occurrences of word " + w);
		for(Coordinates c: match) {
			int i = c.getX()-1;
			int j = c.getY()-1;
			String ligne = txt.get(i);
			System.out.println(ligne.substring(0,j)+ yellow + ligne.substring(j, j+w.length()) + reset + ligne.substring(j+w.length()));
		}

	}
	public static void printMatchAuto(String txt, ArrayList<Coordinates> match) {
		String reset = new String("\u001B[0m");
		String yellow = new String("\u001B[31m");
		
		for(Coordinates c: match) {
			int i = c.getX();
			int j = c.getY();
			System.out.println(txt.substring(0,i)+ yellow + txt.substring(i, i+j) + reset + txt.substring(j+i));
		}

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
	
	public void menu() throws IOException {
		
		System.out.println("Enter the repository containing the data");
	    BufferedReader reader =  
                   new BufferedReader(new InputStreamReader(System.in)); 
	    Map.Entry<String,Integer> entry =
	    	    new AbstractMap.SimpleEntry<String, Integer>("exmpleString", 42);
        // Reading data using readLine 
        String name = reader.readLine(); 
        // Printing the read line 
        System.out.println(name);
          
        File dir = new File(name);
        if (!buildDataBase(dir)) throw new IOException("Not a repository");
        
        ArrayList<Coordinates> locs;
	    help();
	    while(true) {
	        String command = reader.readLine(); 
	        if(command.equals("q")) break;
	        String[] parameters = command.split("\\s+");
	        if(parameters.length>3 || parameters.length<2) {
	        	System.out.println("Wrong format, expected 2 or 3 parameters, got " + parameters.length);
	        	help();
	        	continue;
	        }
	        String pattern = parameters[0];
	        String filename = parameters[1];
	        if(!allFiles.containsKey(filename)) {
	        	System.out.println("filename parameter is not present in the database");
	        	System.out.println("Database contains following files");
	            System.out.println(database.keySet());
	        	help();
	        	continue;
	        }
	        if(parameters.length == 3 && (!parameters[2].equals("-w") || !pattern.matches("[a-zA-z-']+"))) {
	        	System.out.println("Third parameter must be option -w and pattern must be a word matching [a-zA-z-']+");
	        	help();
	        	continue;
	        }
	        
	        ArrayList<String> lignes = readFile(allFiles.get(filename));
	        if(parameters.length == 3) {
	        	locs = database.get(filename).searchWord(pattern.toLowerCase());
	        }
	        else {
	        	//check if is a regex or not
	        	if(isRegex(pattern)) {
	        		System.out.println("le nom est "+filename);
	        		RegEx.RegEx(pattern,name+"/"+filename);
	        		
	        	}
	        	else {
	        		locs = KMP.kmp(lignes, pattern);
	        		printMatch(pattern, lignes, locs);
	        	}
	        }
	        
	    }
    }
}
