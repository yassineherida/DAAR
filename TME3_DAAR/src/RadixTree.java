import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class RadixTree {
	static int alphabet_size = 28;
	private Node root;
	
	class Edge {
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
		
	}
	
	class Node {
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
	
	
}
