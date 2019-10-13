import java.awt.List;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

import javax.swing.plaf.synth.SynthSplitPaneUI;



public class Automate {

		int[][] automate = new int [259][259]; //matrice qui represente les transitions
		int [][]startEndEps = new int [259][3];//matrice qui represente si les etats de débuts et de fin
		ArrayList<Integer> value =new ArrayList<>();
		ArrayList<ArrayList<Integer>> epsilon =new ArrayList<>();//ArrayList qui pour chaque etat contient la liste des etats qu'il peut atteindre via epsilon transition
		HashMap<Integer, HashMap<Integer,Set<Integer> >> deter=new HashMap();//Represente l'automate deterministe une map qui associe un numero d'etat à une map qui elle meme associe une transition à une liste d'etat accesible
		HashMap<Integer, HashMap<Integer,Integer >> deterfacile=new HashMap();//Represente l'automate deterministe une map qui associe un numero d'etat à une map qui elle meme associe une transition à un etat accesible dans l'automate deterministe
		HashMap<Integer,Set<Integer>>etat=new HashMap();//hashmap qui pour un numero d'etat dans l'automate deterministe associe le regroupement d'etat qu'il represente 
		HashMap<Integer,int[]>etatF=new HashMap();//Pour chaque etat de l'automate deterministe il contient un tableau qui contient les informations sur si il est un etat de depart ou un etat finale
		int start;//numero de l'etat de depart de l'automate non deterministe
		int deterstart;//numero de l'etat de depart de l'automate deterministe
		int end;//numero de fin de l'etat de depart de l'automate non deterministe
		int nbt=0;
		int nbetat=0;//variable qui sert à donner des noms au regroupement d'état lors de la minimisation
		
		static final int CONCAT = 0xC04CA7;
		  static final int ETOILE = 0xE7011E;
		  static final int ALTERN = 0xA17E54;
		  static final int PROTECTION = 0xBADDAD;

		  static final int PARENTHESEOUVRANT = 0x16641664;
		  static final int PARENTHESEFERMANT = 0x51515151;
		  static final int DOT = 0xD07;
		  private static String regEx;
	//initialisation des matrices
	public Automate(RegExTree a) {
		for (int i=0;i<259;i++) {
			value.add(i);
		}
		for (int i = 0; i < 259; i++) {
		    for (int j = 0; j < 259; j++) {
		    	
		        automate[i][j] = -1;
		    }
		}
		for (int i = 0; i < 258; i++) {
		    for (int j = 0; j < 3; j++) {
		        startEndEps[i][j] = -1;
		    }
		}
		
		createValue(a);
		exprToAuto(a);
		
	}
	//Permet de recuperer des numeros pour les etats de l'automate , on enleve les numeros des transitions pour eviter des conflits (est peut etre devenue obsolète)
	public void createValue(RegExTree abr) {
		if (abr.root<255 ) {
			value.remove(abr.root);
		}
		else
			for (RegExTree i : abr.subTrees) {
				createValue(i);
				
			}
	}
	//Transforme l'arbre en automate
	public int[] exprToAuto(RegExTree abr) {

	        //le cas du caractère universelle representer par la transition 257
		if (abr.root ==DOT) {
			int a =value.get(0);
			value.remove(0);
			int b =value.get(0);
			value.remove(0);
			automate[a][257]=b;
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
		
		
		if (abr.subTrees.size()==0) {
			//cas de base pas de fils
			int a =value.get(0);
			value.remove(0);
			int b =value.get(0);
			value.remove(0);
			automate[a][(int)(char)abr.root]=b;
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
		//les différents cas d'opérateurs
		switch(abr.root) {
		case ALTERN:{
			int[] a =exprToAuto(abr.subTrees.get(0));
			int[] b =exprToAuto(abr.subTrees.get(1));
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
		case CONCAT :{
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
		case ETOILE:{
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
	//printeur d'automate
	public int  test(RegExTree a) {
		Automate test = new Automate(a);
		for (int i =0;i<259;i++) {
			for (int j =0;j<259;j++) {
				if(automate[i][j]!= -1){
					System.out.println("automate["+i+"]["+j+"]="+automate[i][j] );
					
				}
			}
		}
		for (int i =0;i<259;i++) {
			for (int j =0;j<3;j++) {
				if(startEndEps[i][j]!= -1){
					System.out.println("startEnd["+i+"]["+j+"]="+startEndEps[i][j] );
					
				}
			}
		}
		System.out.println(epsilon);
		return test.start;
		
	}
	//rajoute tous les etats atteignable par des epsilon transitions à partir du rassemblement d'etat e,à chaque fois qu'on rajoute un état on fait un appel récursif pour reverifier si on peut rajouter d'autre etat
	private Set<Integer> addEpsilon(Set<Integer> e,int len) {
		Set<Integer> d=(Set<Integer>) ((HashSet) e).clone();
		for(int i:e) {
			
			d.addAll( epsilon.get(i)) ;
		}
		ArrayList<Integer> l = new ArrayList<Integer>(d);
		if(l.size()==len) {
			return d;
		}else {
			return addEpsilon(d,d.size());
		}
		
	}
	//cree la version determinste de l'automate, prend en parametre un regroupement d'etat
	void determ(int starte) {
		int courant;
		Set<Integer> d;
		//initialisation,verification si on est dans etat de depart ou de fin et si on est au debut de la determinisation
		if(etat.get(starte)==null) {
			
			d=new HashSet<Integer> ((Collection<? extends Integer>) epsilon.get(starte).clone());
			d.add(starte);
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
		
		//on va parcourir l'automate afin de creer les regroupement d'etat accesible à partir d'un regroupement d'etat
		deter.put(courant, new HashMap());
		deterfacile.put(courant,new HashMap());
		ArrayList<Integer>nb=new ArrayList<Integer>();//on sauvegarde tous les nouveaux regroupement d'etat creer
		//Pour tout les etat du regroupement d'etat on regarde toute les transitions et si une transition existe on crer un nouveau regroupement d'etat à partir de l'etat d'arriver et les epsilons transitions 
		for (int i:d) {
			
			for (int j =0;j<259;j++) {
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
		//appel recursif sur tous les nouveaux etats 
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
	//methode de parcours du texte
	 ArrayList<Coordinates> parcours2(String r) {
		 ArrayList<String> res=new ArrayList<>();
		 ArrayList<Coordinates> res2 = new ArrayList<>();
		 char[]liste =r.toCharArray();
		 String courant="";
		 int etat_courant=deterstart;
		 int e=0;
		
		 for (int i:liste) {
			 //on regarde si on peut une transition dans l'etat de depart
			 if(deter.get(etat_courant).containsKey(i)|| deter.get(etat_courant).containsKey(257)) {
				 if(deter.get(etat_courant).containsKey(257)) {
					 etat_courant= numEtat(deter.get(etat_courant).get(257));
					 }
				 else
					 etat_courant= numEtat(deter.get(etat_courant).get(i));
					courant=courant+(char)i;
					if(etatF.get(etat_courant)[1]==1) {
						res2.add(new Coordinates(e,e));
						res.add(courant);
					}
				 for(int j=e+1;j<liste.length;j++) {
					 //on cree un sous parcours du reste du texte 
					 if( deter.get(etat_courant).get((int)liste[j])==null && !deter.get(etat_courant).containsKey(257)) {
						 courant="";
						 etat_courant=deterstart;
						 break;
					
					 }else {
						 if(deter.get(etat_courant).containsKey(257))
							 etat_courant= numEtat(deter.get(etat_courant).get(257));
						 else
							 etat_courant= numEtat(deter.get(etat_courant).get((int)liste[j]));
						courant=courant+liste[j];
						if(etatF.get(etat_courant)[1]==1) {
							res2.add(new Coordinates(e,j));
							res.add(courant);
						}
							
					 }
						
				 }
				 courant="";
				 etat_courant=deterstart;
			 }
			

			 e++;
		 }

		 //System.out.println(res);
		 return res2;

	 }
}
	 
