/**
 * 
 */
package flip.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Vizu
 *
 */
public class Util {

	public static String stringify(Iterable array) {
		String out = "[";
		for(Object i: array) {
			out += i.toString() +",";
		}
		out = out.subSequence(0, out.length()-1) +"]";
		return out;
	}
	
	public static String stringify(Object[] array) {
		String out = "[";
		for(Object i: array) {
			out += i.toString() +",";
		}
		out = out.subSequence(0, out.length()-1) +"]";
		return out;
	}
	
	public static String stringify(int[] array) {
		String out = "[";
		for(int i: array) {
			out += i +",";
		}
		out = out.subSequence(0, out.length()-1) +"]";
		return out;
	}
	
	public static List<String> removeOccurencesOf(int rem, int[] array) {
		List<String> out = new LinkedList<String>();
		for(int n : array) {
			if (n!=rem)
				out.add(""+n);
		}
		return out;
	}
}
