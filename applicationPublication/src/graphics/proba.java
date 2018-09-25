package graphics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class proba {

	public static void main(String[] args) {
		/*LinkedList<String> l1 = new LinkedList<>();
		l1.addLast("0 0 0");
		l1.addLast("0 0 1");
		l1.addLast("0 1 0");
		l1.addLast("0 1 1");
		l1.addLast("1 0 0");
		l1.addLast("1 0 1");
		l1.addLast("1 1 0");
		l1.addLast("1 1 1");
		
		LinkedList<String> l2 = new LinkedList<>();
		l2.addLast("1 1 1");
		
		LinkedList<String> l3 = new LinkedList<>();
		l3.addLast("0 0 0");*/
		
		LinkedList<String> l1 = new LinkedList<>();
		l1.addLast("0 0 0");
		l1.addLast("1 1 1");
		
		LinkedList<LinkedList<String>> liste = new LinkedList<>();
		liste.addLast(l1);
		liste.addLast(l1);
		liste.addLast(l1);
		liste.addLast(l1);
		liste.addLast(l1);
		liste.addLast(l1);
		liste.addLast(l1);
		liste.addLast(l1);
		
		List<Set<String>> sets = new ArrayList<Set<String>>();
		for (int i = 0; i < liste.size(); i++) {
			sets.add(new HashSet<String>(liste.get(i)));
		}
		Set<List<String>> cartesianSet = com.google.common.collect.Sets.cartesianProduct(sets);
		
		System.out.println(cartesianSet.size());
	}
}
