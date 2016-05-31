/**
 * 
 */
package ru.flip.compare;

import java.util.List;

import flip.util.Pair;
import flip.util.Util;
import ru.flip.core.DivTree;
import ru.flip.core.Element;

/**
 * @author Vizu
 *
 */
public class StrictEditComparer extends Comparer{

	public final static int INSERTION_COST = 1;
	public final static int DELETION_COST = INSERTION_COST;
	public final static int SUBSTITUTION_COST = 1;
	
	@Override
	public double getDistance(DivTree tree0, DivTree tree1) {
		return getDistance(tree0.getRoot(), tree1.getRoot());
	}
	
	/**
	 * Calculates how much it would cost to create or remove the given element.
	 */
	public int getCost(Element element) {
		int cost = INSERTION_COST;
		Element[] children = element.getChildren();
		for(Element child : children) 
			cost += getCost(child);
		return cost;
	}
	
	public double getDistance(Element branch0, Element branch1){
		//System.out.println("comparing "+branch0 +" with "+branch1);
		int delta = 0;
		if (!branch0.isSameSizeAs(branch1))
			delta += SUBSTITUTION_COST;
		
		Element[] leaves0;
		Element[] leaves1;
		if (branch0.getNrOfChildren()>=branch1.getNrOfChildren()) {
			leaves0 = branch0.getChildren();
			leaves1 = branch1.getChildren();
		} else {
			leaves1 = branch0.getChildren();
			leaves0 = branch1.getChildren();
		}
		if (leaves0.length==0)
			return delta;
		
		for(int i=0; i<leaves0.length; i++) {
			int min = getCost(leaves0[i]);
			for(int j=0; j<leaves1.length; j++)
				min = (int) Math.min(min, getDistance(leaves0[i],leaves1[j]));
			delta+=min;
		}
		
		return delta;
	}
	
	private int[] createIncreasingArray(int upTo) {
		int[] array = new int[upTo];
		for(int n=0; n<upTo;n++)
			array[n] = n;
		return array;
	}

	@Override
	public DivTree createAverage(DivTree tree0, int weight0, DivTree tree1, int weight1) {
		
		return null;
	}
	
	protected DivTree createAverage(DivTree tree0, DivTree tree1) {
		return null;
	}

	
}
