/**
 * 
 */
package ru.flip.compare;

import ru.flip.core.DivTree;
import ru.flip.core.Element;

/**
 * @author Vizu
 *
 */
public abstract class Comparer {

	public final boolean compare(DivTree tree0, DivTree tree1) {
		System.out.println(getDistance(tree0, tree1));
		return (getDistance(tree0, tree1)==0);
	}

	public double getDistance(DivTree tree0, DivTree tree1) {
		return getDistance(tree0.getRoot(), tree1.getRoot());
	}

	public abstract double getDistance(Element element0, Element element1);
	
	public DivTree createAverage(DivTree[] trees) {
		DivTree newAverage = trees[0];
		for(int i=1; i<trees.length; i++) {
			newAverage = createAverage(newAverage, i, trees[i], 1);
		}
		return newAverage;
	}
	
	public abstract DivTree createAverage(DivTree tree0, int weight0, DivTree tree1, int weight1);
}
