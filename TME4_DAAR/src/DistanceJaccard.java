import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DistanceJaccard {
	public double distanceJaccard(Set<String> f1, Set<String> f2) {
		
		// u_i U v_i
		Set<String> union = new HashSet<>();
		union.addAll(f1);
		union.addAll(f2);
		
		// |(u_i U v_i)|
		double size_u = union.size();
		
		//u_i \ v_i
		Set<String> f1_diff_f2 = new HashSet<>();
		f1_diff_f2.addAll(f1);
		f1_diff_f2.removeAll(f2);
		
		// v_i \ u_i
		Set<String> f2_diff_f1 = new HashSet<>();
		f2_diff_f1.addAll(f2);
		f2_diff_f1.removeAll(f1);
		
		//(u_i \ v_i) U (v_i \ u_i)
		f1_diff_f2.addAll(f2_diff_f1);
		
		//|(u_i \ v_i) U (v_i \ u_i)|
		double size_u_diff = f1_diff_f2.size();
		
		return size_u_diff/size_u;
	}
	
	static public double distanceJaccard(Map<String, Integer> f1, Map<String,Integer> f2) {
		double max =0;
		double diff =0;
		Set<String> union = new HashSet<>();
		for(String e:f2.keySet()) {
			union.add(e);
		}
		for(String e:f1.keySet()) {
			union.add(e);
		}
		for (String e : union) {
			int k1=0;
			int k2=0;
			if (f1.get(e)!=null) {
				k1=f1.get(e);
			}
			if (f2.get(e)!=null) {
				k2=f2.get(e);
			}
			diff+=Math.abs(k1- k2);
			max+=Math.max(k1, k2);
			
			
		}
		return diff/max;
	}
	public static void main (String []arg) {
		Map<String,Integer> w = new HashMap<>();
		Map<String,Integer> s = new HashMap<>();
		s.put("bien",1);
		s.put("dormir",1);
		s.put("matin",1);
		w.put("lève",1);
		w.put("matin",1);
		DistanceJaccard a = new DistanceJaccard();
		System.out.println((a.distanceJaccard(s, w)));
		System.out.println(a.distanceJaccard(s.keySet(), w.keySet()));
	
	}
}
