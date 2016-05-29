/**
 * 
 */
package ru.flip.compare;

import java.util.LinkedList;
import java.util.List;
import ru.flip.core.DivTree;
import ru.flip.core.Element;

/**
 * @author Vizu
 *
 */
public class LooseEditComparer extends Comparer{

	public final static int INSERTION_COST = 1;
	public final static int DELETION_COST = INSERTION_COST;
	public final static int SUBSTITUTION_COST = 1;
	
	
	
	/**
	 * Calculates how much it would cost to create or remove the given element.
	 */
	public double getCost(Element element) {
		//double cost = this.getVectorDistance(element,
		//		new Element(0,0,0,0,0));
		double cost = getVectorDistance(element, element.getParent());
		Element[] children = element.getChildren();
		for(Element child : children) 
			cost += getCost(child);
		return cost;
	}
	
	public double getDistance(Element branch0, Element branch1){
		//System.out.println("comparing "+branch0 +" with "+branch1);
		double delta = 0;
		delta += getVectorDistance(branch0, branch1);
		
		Element[] leaves0;
		Element[] leaves1;
		if (branch0.getNrOfChildren()>=branch1.getNrOfChildren()) {
			leaves0 = branch0.getChildren();
			leaves1 = branch1.getChildren();
		} else {
			leaves1 = branch0.getChildren();
			leaves0 = branch1.getChildren();
		}
		for(int i=0; i<leaves0.length; i++) {
			double min = getCost(leaves0[i]);
			for(int j=0; j<leaves1.length; j++)
				min = Math.min(min, getDistance(leaves0[i],leaves1[j]));
			delta+=min;
		} 
		
		return delta;
	}

	/**
	 * @return the ratio between which the vectors of branch0 and branch1 differ.
	 */
	private double getVectorDistance(Element branch0, Element branch1) {
		double w = branch0.getWidth() + branch0.getMarginLeft()
			- (branch1.getWidth() + branch1.getMarginLeft());
		double h = branch0.getHeight() +branch0.getMarginTop() 
			- (branch1.getHeight() + branch1.getMarginTop());
		double delta = Math.sqrt(w*w + h*h);
		double l = Math.sqrt(
				Math.pow(branch0.getHeight() + branch0.getMarginTop(),2)
				+Math.pow(branch0.getWidth() + branch0.getMarginLeft(),2)
				);
		return (delta/l);
	}

	@Override
	public DivTree createAverage(DivTree tree0, int weight0, DivTree tree1, int weight1) {
		DivTree out = new DivTree("Average");
		Element newRoot = createAverage(tree0.getRoot(), weight0, tree1.getRoot(), weight1);
		out.addRoot(newRoot);
		return out;
	}
		
	
	protected Element createAverage(Element branch0, int weight0, Element branch1, int weight1) {
		int total = weight0 + weight1;
		
		Element[] leaves0;
		Element[] leaves1;
		boolean isSwapped = false;
		if (branch0.getNrOfChildren()>=branch1.getNrOfChildren()) {
			leaves0 = branch0.getChildren();
			leaves1 = branch1.getChildren();
		} else {
			leaves1 = branch0.getChildren();
			leaves0 = branch1.getChildren();
			isSwapped = true;
		}
		
		List<Element> newLeaves = new LinkedList<Element>();
		if(leaves0.length!=0) {
			for(int i=0; i<leaves0.length; i++) {
				double min = getCost(leaves0[i]);
				Element other = new Element(0,0,0,0,0);
				for(int j=0; j<leaves1.length; j++) {
					double delta = getDistance(leaves0[i],leaves1[j]);
					if (delta < min) {
						other = leaves1[j];
						min = delta;
					}
				}
				if(isSwapped)
					newLeaves.add(createAverage(leaves0[i], weight1, other, weight0));
				else
					newLeaves.add(createAverage(leaves0[i], weight0, other, weight1));
			}
		}
		
		Element newRoot = new Element(
				(branch0.getWidth()*weight0 + branch1.getWidth()*weight1)/total
				,(branch0.getHeight()*weight0 + branch1.getHeight()*weight1)/total
				,(branch0.getMarginLeft()*weight0 + branch1.getMarginLeft()*weight1)/total
				,(branch0.getMarginTop()*weight0 + branch1.getMarginTop())*weight1/total
				, newLeaves.size()
				);
		for(Element newLeaf : newLeaves) 
			newRoot.addElement(newLeaf);
		
		return newRoot;
	}
}
