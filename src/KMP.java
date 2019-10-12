import java.util.ArrayList;

public class KMP {
	static int [] retenue(String r){
		int [] retenue = new int [r.length()];

		char[]liste =r.toCharArray();
		char debut=liste[0];

		retenue[0] = 0;
		retenue[1] = 0;
		int i = 2, j = 0;
		while(i < liste.length) {
			System.out.println("j vaut " + j + " i vaut " + i);
			//increasing size of prefix-suffix
			if(liste[j] == liste[i-1]) {
				retenue[i] = j + 1;
				i++;
				j++;
			}
			else {
				if (j > 0) {
					j = retenue[j];
				}
				else {
					retenue[i] = 0;
					i++;
				}
			}
		}

		for (i = 0; i < retenue.length; i++) {
			if(liste[i] == debut && retenue[i] == 0)
				retenue[i] = -1;
		}
		return retenue;

	}
	 
	 
	 static ArrayList<Coordinates> match (char[] facteur, int[] retenue, char[] texte, int nline) {
		 int i = 0;
		 int j = 0;
		 ArrayList<Coordinates> res = new ArrayList<>();
		 while(i < texte.length) {
			 if (j==facteur.length) {
				 res.add(new Coordinates(nline+1, i-facteur.length+1));
				 j = 0;
			 }
			 
			 if(texte[i] == facteur[j]) {
				 i++;
				 j++;
			 }
			 
			 else {
				 if(retenue[j] == -1) {
					 i++;
					 j=0;
				 }
				 else {
					 j = retenue[j];
				 }
			 }

		 }
		 
		 if(j==facteur.length) {
			 res.add(new Coordinates(nline+1, i-facteur.length+1));
		 }
		 return res;
	 }
	 
	 
	 static ArrayList<Coordinates> kmp(ArrayList<String> lines, String w){
		 int[] t = retenue(w);
		 
		 for (int e : t)
			 System.out.print(e + " ");
		 System.out.println();
		 
	     char[] facteur = new String(w).toCharArray();
	     ArrayList<Coordinates> res = new ArrayList<>();
	     int i = 0;
	     for(String l:lines) {
	    	 char[] txt = l.toCharArray();
	    	 
	    	 res.addAll(match(facteur,t,txt,i));
	    	 i++;
	     }
	     
	     return res;
	      
	 }
	 
	 
	 
	 public static void main(String [] args) {
		 ArrayList<String> l = new ArrayList<String>();
		 l.add("maimamaimai");
		 ArrayList<Coordinates> c = kmp(l,"maimamaimaik");
		 System.out.println(c);
	 }
}
