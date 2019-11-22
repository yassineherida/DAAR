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

public class Indexing {


	public static Map<String, Integer> indexing (String fileName) throws IOException {
		InputStream flux=new FileInputStream(fileName); 
		InputStreamReader lecture=new InputStreamReader(flux);
		BufferedReader buff=new BufferedReader(lecture);
		try {
			Map<String, Integer> index = new HashMap<>();
			String ligne;
			int c=0;
			int i = 1, j = 1;
			while ((ligne=buff.readLine())!=null){
				if(ligne.length() == 0) continue;
				j=1;
				String[] words = ligne.split("\\s*[^a-zA-Z'-]+\\s*");
				int cpt = 0;
				for (cpt=0;cpt<words.length;cpt++) {
					String word = words[cpt];
					word = word.toLowerCase();
					//word = word.toLowerCase();
					if(index.containsKey(word)) {
						index.put(word, index.get(word)+1);
					}
					else {
						index.put(word, 1);
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

			return index;
		}finally {
			buff.close();
		}

	}

}
