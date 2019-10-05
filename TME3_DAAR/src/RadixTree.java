import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class RadixTree implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8797546353613135978L;
	static int alphabet_size = 28;
	private Node root;
	
	class Edge implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2569119177374717148L;
		private String tag;
		private Node orig;
		private Node dest;

		public Edge(String tag, Node orig, Node dest) {
			this.tag = tag;
			this.orig = orig;
			this.dest = dest;
		}

		public String getTag() {
			return tag;
		}

		public void setOrig(Node orig) {
			this.orig = orig;
		}

		public void setDest(Node dest) {
			this.dest = dest;
		}

		public Node getOrig() {
			return orig;
		}

		public Node getDest() {
			return dest;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}
		
		public String substractGreatestCommonPrefix (String s) {
			
			int i = 0;
			while (i < tag.length() && s.charAt(i) == tag.charAt(i)) {
				i++;
			}
			return s.substring(i);
		}
		
		public String toString() {
			if(dest == null) {
				return tag;
			}
			return tag + " " + dest;
		}
		
	}
	
	class Node implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7469752664608457546L;
		private ArrayList<Coordinates> location;
		private Edge[] edges;
		
		public Node() {
			this.location = null;
			this.edges = new Edge[alphabet_size];
			for(int i = 0; i < alphabet_size; i++) {
				edges[i] = new Edge("", this, null);
			}
		}
		
		public void setLocation(ArrayList<Coordinates> l) {
			if(location != null) {
				System.out.println("Warning: inserting a word already in the tree");;
			}
			location = l;
		}
		
		public ArrayList<Coordinates> getLocation(){
			return location;
		}
		
		public Edge getEdge(String s) {
			char c = s.charAt(0);
			int pos_edge;
			// in second to last position
			if (c == '-') {
				pos_edge = 26;
			}
			else {
				//in last pos
				if (c == '\'') {
					pos_edge = 27;
				}
				else {
					pos_edge = c - 'a';
				}
			}
			return edges[pos_edge];
		}
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append('(');
			for (Edge edge: edges) {
				sb.append(edge.toString());
				sb.append(',');
			}
			sb.append(')');
			return sb.toString();
		}

	}
	
	public RadixTree() {
		this.root = new Node();
		
	}
	
	public Node insertWordUtil(Node n, String w, Node to_insert) {
		if (w.isEmpty()) {
			return to_insert;
		}
		else {
			// in depth insertion
			Edge dir = n.getEdge(w);
			String t = dir.getTag();
			
			//i is the length of the greatest common prefix
			int i = 0;
			int min_len = Math.min(t.length(), w.length());
			while (i < min_len && w.charAt(i) == t.charAt(i)) {
				i++;
			}
			// no tag on edge thus no word yet
			if(i == 0) {
				dir.setTag(w);
				Node res = insertWordUtil(dir.getDest(), "", to_insert);
				dir.setDest(res);
				return res;
			}
			
			String suffix_tag = t.substring(i);
			String suffix_w = w.substring(i);
			if (suffix_tag.isEmpty()) {
				return insertWordUtil(dir.getDest(), suffix_w, to_insert);
			}
			
			// cutting previous prefix
			Node to_move = dir.getDest();
			Node new_node;
			
			if (suffix_w.isEmpty()) {
				new_node = to_insert;
			}
			else {
				new_node= new Node();
			}
			
			dir.setDest(new_node);
			dir.setTag(t.substring(0, i));
			insertWordUtil(new_node, suffix_tag, to_move);
			return insertWordUtil(new_node, suffix_w, to_insert);
		}
	}

	public void insertWord (String w, ArrayList<Coordinates> c) {
		Node n = new Node();
		n.setLocation(c);
		Node res = insertWordUtil(root, w, n);
		return;
	}
	
	public String toString() {
		return root.toString();
	}
	
	public void saveRadixTree(String filename) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(filename);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
        System.out.println("Serialized data is saved in " + filename);
	}
	
	public static RadixTree readRadixTree(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        RadixTree rd = (RadixTree) in.readObject();
        in.close();
        fileIn.close();
        return rd;
	}
	
	
}
