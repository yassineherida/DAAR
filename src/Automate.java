import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;

public class Automate {

		int[][] automate = new int [257][257];
		int [][]startEndEps = new int [257][3];
		ArrayList<Integer> value =new ArrayList<>();
		ArrayList<ArrayList<Integer>> epsilon =new ArrayList<>();
		HashMap<Integer, HashMap<Integer,Set<Integer> >> deter=new HashMap();
		HashMap<Integer, HashMap<Integer,Integer >> deterfacile=new HashMap();
		HashMap<Integer,Set<Integer>>etat=new HashMap();
		HashMap<Integer,int[]>etatF=new HashMap();
		int start;
		int deterstart;
		int end;
		int nbt=0;
		int nbetat=0;
		
		static final int CONCAT = 0xC04CA7;
		  static final int ETOILE = 0xE7011E;
		  static final int ALTERN = 0xA17E54;
		  static final int PROTECTION = 0xBADDAD;

		  static final int PARENTHESEOUVRANT = 0x16641664;
		  static final int PARENTHESEFERMANT = 0x51515151;
		  static final int DOT = 0xD07;
		  private static String regEx;
	public Automate(RegExTree a) {
		for (int i=0;i<257;i++) {
			value.add(i);
		}
		for (int i = 0; i < 257; i++) {
		    for (int j = 0; j < 257; j++) {
		    	
		        automate[i][j] = -1;
		    }
		}
		for (int i = 0; i < 257; i++) {
		    for (int j = 0; j < 3; j++) {
		        startEndEps[i][j] = -1;
		    }
		}
		
		createValue(a);
		exprToAuto(a);
		
	}
	public void createValue(RegExTree abr) {
		if (abr.root<258  ) {
			value.remove(abr.root);
		}
		else
			for (RegExTree i : abr.subTrees) {
				createValue(i);
				
			}
	}
	public int[] exprToAuto(RegExTree abr) {
		System.out.println(rootToString(abr.root));
		if (abr.subTrees.isEmpty()) {
			int a =value.get(0);
			value.remove(0);
			int b =value.get(0);
			value.remove(0);
			automate[a][abr.root]=b;
			startEndEps[a][0]=1;
			startEndEps[a][1]=0;
			epsilon.add(a,new ArrayList<>());
			epsilon.add(b,new ArrayList<>());
			startEndEps[b][0]=0;
			startEndEps[b][1]=1;
			start=a;
			end=b;
			nbt+=1;	
			int[] r= {a,b};
			return r;
		}
		else {
		switch(rootToString(abr.root)) {
		case "|":{
			int[] a =exprToAuto(abr.subTrees.get(0));
			int[] b =exprToAuto(abr.subTrees.get(1));
			//System.out.println("a"+a[0]+" "+a[1]);
			//System.out.println("b"+b[0]+" "+b[1]);
			int c =value.get(0);
			value.remove(0);
			int d =value.get(0);
			value.remove(0);
			startEndEps[a[0]][0]=0;
			startEndEps[a[1]][1]=0;
			startEndEps[b[0]][0]=0;
			startEndEps[b[1]][1]=0;
			startEndEps[c][0]=1;
			startEndEps[c][1]=0;
			startEndEps[d][0]=0;
			startEndEps[d][1]=1;
			System.out.println(c);
			System.out.println(epsilon);
			epsilon.add(c,new ArrayList<>());
			epsilon.add(d,new ArrayList<>());
			epsilon.get(c).add(a[0]);
			epsilon.get(c).add(b[0]);
			startEndEps[a[0]][1]=0;
			startEndEps[b[0]][1]=0;
			epsilon.get(a[1]).add(d);
			epsilon.get(b[1]).add(d);
			start=c;
			end=d;
			int[] r= {c,d};
			return r;
		}
		case "." :{
			int[] a =exprToAuto(abr.subTrees.get(0));
			int[] b =exprToAuto(abr.subTrees.get(1));
			startEndEps[a[1]][1]=0;
			startEndEps[b[0]][0]=0;
			epsilon.get(a[1]).add(b[0]);
			start=a[0];
			end=b[1];
			int[] r= {a[0],b[1]};
			return r;
		}
		case "*":{
			int[] a =exprToAuto(abr.subTrees.get(0));
			int c =value.get(0);
			value.remove(0);
			int d =value.get(0);
			value.remove(0);
			startEndEps[a[0]][0]=0;
			startEndEps[a[1]][1]=0;
			startEndEps[c][0]=1;
			startEndEps[c][1]=0;
			startEndEps[d][0]=0;
			startEndEps[d][1]=1;
			epsilon.add(c,new ArrayList<>());
			epsilon.add(d,new ArrayList<>());
			epsilon.get(c).add(a[0]);
			epsilon.get(c).add(d);
			epsilon.get(a[1]).add(d);
			epsilon.get(a[1]).add(a[0]);
			start=c;
			end=d;
			int[] r= {c,d};
			return r;
			
			
		}
		}
	}
		return null;
	}
	
	private String rootToString(int root) {
	    if (root==RegEx.CONCAT) return ".";
	    if (root==RegEx.ETOILE) return "*";
	    if (root==RegEx.ALTERN) return "|";
	    if (root==RegEx.DOT) return ".";
	    return Character.toString((char)root);
	  }
	public int  test(RegExTree a) {
		Automate test = new Automate(a);
		for (int i =0;i<257;i++) {
			for (int j =0;j<257;j++) {
				if(automate[i][j]!= -1){
					System.out.println("automate["+i+"]["+j+"]="+automate[i][j] );
					
				}
			}
		}
		for (int i =0;i<257;i++) {
			for (int j =0;j<3;j++) {
				if(startEndEps[i][j]!= -1){
					System.out.println("startEnd["+i+"]["+j+"]="+startEndEps[i][j] );
					
				}
			}
		}
		System.out.println(epsilon);
		return test.start;
		
	}
	private Set<Integer> addEpsilon(Set<Integer> e,int len) {
		Set<Integer> d=(Set<Integer>) ((HashSet) e).clone();
		//System.out.println(d);
		for(int i:e) {
			
			d.addAll( epsilon.get(i)) ;
		}
		ArrayList<Integer> l = new ArrayList<Integer>(d);
		//System.out.println(l);
		if(l.size()==len) {
			return d;
		}else {
			return addEpsilon(d,d.size());
		}
		
	}
	void determ(int starte) {
		int courant;
		Set<Integer> d;
		if(etat.get(starte)==null) {
			
			d=new HashSet<Integer> ((Collection<? extends Integer>) epsilon.get(starte).clone());
			d.add(starte);
			System.out.println(d);
			etat.put(nbetat, d);
			courant=nbetat;
			deterstart=courant;
			int[] ef= {1,0};
			if (etat.get(courant).contains(end))
				ef[1]=1;
			etatF.put(courant,ef);
			nbetat+=1;
		}else {
			d=(Set<Integer>) ((HashSet) etat.get(starte)).clone();
			courant=starte;
			int[] ef= {0,0};
			 if (etat.get(courant).contains(end))
				ef[1]=1;
	    	 if (etat.get(courant).contains(start)) {
				ef[0]=1;
				deterstart=courant;
	    	 }
	    	 
			etatF.put(courant,ef);
		}
		
		
		deter.put(courant, new HashMap());
		deterfacile.put(courant,new HashMap());
		ArrayList<Integer>nb=new ArrayList<Integer>();
		for (int i:d) {
			
			for (int j =0;j<257;j++) {
				if(i==0) {
					if (j==97)
						System.out.println("laaa");
				}
				if (automate[i][j]!=-1) {
					if(deter.get(courant).get(j)==null) {
						deter.get(courant).put(j, (Set<Integer>) new HashSet<Integer>());
					} 
					//System.out.println("ici");
					 deter.get(courant).get(j).add(automate[i][j]);
					 //System.out.println(  deter.get(courant).get(j)  );
					 deter.get(courant).get(j).addAll( addEpsilon(deter.get(courant).get(j),0) );
					 if ( !(etat.containsValue(deter.get(courant).get(j)))) {
						 etat.put(nbetat, deter.get(courant).get(j));
						 int[] ef= {0,0};
						 if (etat.get(courant).contains(end))
							ef[1]=1;
				    	 if (etat.get(courant).contains(start)) {
							ef[0]=1;
							deterstart=courant;
				    	 }
						etatF.put(courant,ef);
						nb.add(nbetat);
						deterfacile.get(courant).put(j,nbetat);
						nbetat+=1;
						
						 
					 }
					 
				}
			}
		}
		
		for(int i:nb)
			determ(i);
		
	}
	private int numEtat(Set<Integer> courant) {
		for (int e:etat.keySet()) {
			if(courant.equals(etat.get(e))) {
				return e;
			}
		}
		return -1;
		
	}
	 private static RegExTree exampleAhoUllman() {
		    RegExTree a = new RegExTree((int)'a', new ArrayList<RegExTree>());
		    RegExTree b = new RegExTree((int)'b', new ArrayList<RegExTree>());
		    RegExTree c = new RegExTree((int)'c', new ArrayList<RegExTree>());
		    ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
		    subTrees.add(c);
		    RegExTree cEtoile = new RegExTree(ETOILE, subTrees);
		    subTrees = new ArrayList<RegExTree>();
		    subTrees.add(b);
		    subTrees.add(cEtoile);
		    RegExTree dotBCEtoile = new RegExTree(CONCAT, subTrees);
		    subTrees = new ArrayList<RegExTree>();
		    subTrees.add(a);
		    subTrees.add(dotBCEtoile);
		    return new RegExTree(ALTERN, subTrees);
		  }
		
	 ArrayList<String> parcours(String r) {
		 //System.out.println("Start");
		 ArrayList<String> res=new ArrayList<>();
		 char[]liste =r.toCharArray();
		 String courant="";
		 int etat_courant=deterstart;
		 int e=0;
		 for (int i:liste) {
			 if(deter.get(etat_courant).containsKey(i)|| deter.get(etat_courant).containsKey(43)) {
				 //System.out.println(i);
				 //System.out.println(liste[e]);
				 //System.out.println("yes");
				 if(deter.get(etat_courant).containsKey(43))
					 etat_courant= numEtat(deter.get(etat_courant).get(43));
				 else
					 etat_courant= numEtat(deter.get(etat_courant).get(i));
					//System.out.println(etat_courant);
					courant=courant+(char)i;
					if(etatF.get(etat_courant)[1]==1)
						res.add(courant);
				 for(int j=e+1;j<liste.length;j++) {
					 /*System.out.println("in");
					 System.out.println(liste[j]);
					 System.out.println( deter.get(etat_courant).get((int)liste[j]));
					 System.out.println("out");*/
					 if( deter.get(etat_courant).get((int)liste[j])==null && !deter.get(etat_courant).containsKey(43)) {
					// System.out.println(liste[j]);
						 courant="";
						 etat_courant=deterstart;
						 break;
					
					 }else {
						 //System.out.println("no");
						 if(deter.get(etat_courant).containsKey(43))
							 etat_courant= numEtat(deter.get(etat_courant).get(43));
						 else
							 etat_courant= numEtat(deter.get(etat_courant).get((int)liste[j]));
						//System.out.println(etat_courant);
						courant=courant+liste[j];
						if(etatF.get(etat_courant)[1]==1)
							res.add(courant);
							
					 }
						
				 }
				 courant="";
				 etat_courant=deterstart;
			 }
			

			 e++;
		 }
		 return res;
	 }
	 ArrayList<String> parcours2(String r) {
		 ArrayList<String> res=new ArrayList<>();
		 char[]liste =r.toCharArray();
		 String courant="";
		 int etat_courant=deterstart;
		 for (int i:liste) {
			 System.out.println(deter.get(etat_courant).get(i));
			 if( deter.get(etat_courant).get(i)==null) {
				 courant="";
				 etat_courant=deterstart;
			 }
			 else {
				etat_courant= numEtat(deter.get(etat_courant).get(i));
				//System.out.println(etat_courant);
				courant=courant+(char)i;
				if(etatF.get(etat_courant)[1]==1)
					res.add(courant);
					
			 }
			 
		 }
		 return res;
	 }
	 /*
	 public static void main(String arg[]) {
		 RegExTree a = new RegExTree((int)'a', new ArrayList<RegExTree>());
		 RegExTree b = new RegExTree((int)'b', new ArrayList<RegExTree>());
		 ArrayList<RegExTree> te= new ArrayList<>();
		 te.add(a);
		 te.add(b);
		 //RegExTree t = exampleAhoUllman();
		 RegExTree t= new RegExTree(CONCAT, te);
		 Automate test =new Automate(t);
		 test.test(t);
		 System.out.println(test.automate[0][97]);
		 test.determ(test.start);
		 System.out.println(test.deter);
		 System.out.println(test.etat);
		 System.out.println(test.etatF);
		 System.out.println(test.parcours("bcaa aa b aaa ab"));

		  }*/
	
}
