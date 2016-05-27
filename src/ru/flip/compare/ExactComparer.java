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
public class ExactComparer extends Comparer{

	@Override
	public double getDistance(DivTree tree0, DivTree tree1) {
		if (tree0.getRoot().equals(tree1.getRoot()))
			return 0;
		return 1;
	}

	@Override
	public double getDistance(Element element0, Element element1) {
		if (element0.equals(element1))
			return 0;
		return 1;
	}

	@Override
	public DivTree createAverage(DivTree tree0, int weight0, DivTree tree1, int weight1) {
		// TODO Auto-generated method stub
		return null;
	}

}
