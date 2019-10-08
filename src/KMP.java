import java.util.ArrayList;

public class KMP {
	 static int [] retenue(String r){
		 int [] retenue = new int [r.length()];
		
		 char[]liste =r.toCharArray();
		 char debut=liste[0];
		 int courant =0;
		 int value=0;
		 for (char c:liste) {
			 value=0;
			 if (c==debut) {
				 retenue[courant]=-1;
				 courant +=1;
			 	continue;
			 }
			 
			 if(courant==1) {
				 courant ++;
				 retenue[courant]=0;
				 continue;
			 }
			 int i = 0;
			 while (i <= courant-i+1) {
				 if(r.substring(0,i).equals(r.substring(courant-i,courant))) {
					 value=i;
				 }
				 i++;
				 
			 }//rajoute normalisation si mm valeur entre courant et retenue courant
			 retenue[courant]=value;
			 courant +=1;
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
			 System.out.println(e);

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
}
