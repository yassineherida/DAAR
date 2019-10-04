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
			Arrays.fill(edges, new Edge("", this, null));
		}
		
		public void setLocation(ArrayList<Coordinates> l) {
			if(location != null) {
				System.out.println("Warning: inserting a word already in the tree");;
			}
			location = l;
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
	
	public void insertWordUtil(Node n, String w, ArrayList<Coordinates> c) {
		if (w.isEmpty()) {
			n.setLocation(c);
		}
		else {
			// in depth insertion
			Edge dir = n.getEdge(w);
			String t = dir.getTag();
			int i = 0;
			while (i < t.length() && w.charAt(i) == t.charAt(i)) {
				i++;
			}
			if(dir.getDest() == null) {
				dir.setDest(new Node());
				//insertWordUtil(dir.getDest(), t, );
			}
			dir.setTag(t.substring(0, i));
			
			String suffix = t.substring(i);
			new Edge(suffix, orig, dest)
			//insertWordUtil(dir.getDest(), )
		}
	}
	
	public void insertWord (String w, ArrayList<Coordinates> c) {
		insertWordUtil(root, w, c);
	}

	
}
