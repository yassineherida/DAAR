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

public class Indexing {

	
	 public static void indexing (String fileName) throws IOException {
	        InputStream flux=new FileInputStream(fileName); 
	        InputStreamReader lecture=new InputStreamReader(flux);
	        BufferedReader buff=new BufferedReader(lecture);
	        Map<String, ArrayList<Coordinates>> index = new HashMap<String, ArrayList<Coordinates>>();
	        String ligne;
	        int c=0;
	        int i = 1, j = 1;
	        while ((ligne=buff.readLine())!=null){
	        	j=1;
	        	String[] words = ligne.split("\\s*[^a-zA-Z'-]+\\s*");
	        	int cpt = 0;
	        	for (cpt=0;cpt<words.length;cpt++) {
		        	String word = words[cpt];
		        	word = word.toLowerCase();
		        	//word = word.toLowerCase();
	        		if(index.containsKey(word)) {
	        			index.get(word).add(new Coordinates(i, j));
	        		}
	        		else {
	        			index.put(word, new ArrayList<>());
	        			index.get(word).add(new Coordinates(i, j));
	        		}
	        		int space_count = 0;
	        		j += word.length();
	        		if(cpt < words.length-1) 
		        		while(ligne.charAt(j-1) != words[cpt+1].charAt(0)) {
		        			j++;
		        		}
	        	}
	        	i++;
	        }
	        System.out.println(index.get("sargon's").size());
	        HashMap<String, ArrayList<Coordinates>> sorted = index.entrySet().stream()
	        	    .sorted(comparingInt(e -> e.getValue().size()))
	        	    .collect(toMap(
	        	        Map.Entry::getKey,
	        	        Map.Entry::getValue,
	        	        (a, b) -> { throw new AssertionError(); },
	        	        LinkedHashMap::new
	        	    ));
	        sorted.remove("");
	       
	        for(Entry<String, ArrayList<Coordinates>> e:sorted.entrySet()) {
	    
	        	//System.out.println(e.getKey() + " " + e.getValue());
	        }
	        
	 }
	 
}
