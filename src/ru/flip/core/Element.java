/**
 * 
 */
package ru.flip.core;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Vizu
 *
 */
public class Element {
	private float width, height, marginLeft, marginTop;
	private Element[] elements;
	private Element parent;
	public String id;
	
	public Element(float width, float height, float marginLeft, float marginTop, int nrOfChildren) {
		this(width, height, marginLeft, marginTop, "", nrOfChildren);
	}

	public Element(float width, float height, float x, float y, String name, int nrOfChildren) {
		this.width = width;
		this.height = height;
		this.marginLeft = x;
		this.marginTop = y;
		id = name;
		elements = new Element[nrOfChildren];
	}

	public boolean addElement(Element newElement) {
		for(int i=0; i < elements.length; i++) {
			if (elements[i]==null) {
				newElement.parent = this;
				elements[i] = newElement;
				return true;
			}
		}
		return false;
	}
	
	//Getters for the main values
	public float getWidth() {return width;}
	public float getHeight() {return height;}
	public float getMarginLeft() {return marginLeft;}
	public float getMarginTop() {return marginTop;}
	public Element getParent() {return parent;}
	
	/**
	 * Returns the amount of children elements, this element has.
	 */
	public int getNrOfChildren() {
		return elements.length;
	}
	
	/**
	 *  Returns a copy of the array of children of this element.
	 */
	public Element[] getChildren() {
		return elements.clone();
	}
	
	
	/**
	 * Returns how deep this element is in this tree.
	 */
	public int getDepth() {
		if (parent==null)
			return 1;
		return parent.getDepth() + 1;
	}
	
	/**
	 * Returns a list of all leaves of this tree. 
	 *  A leaf is an Element without any children.
	 */
	public List<Element> getLeaves(){
		int len = elements.length;
		if (len == 1) {
			return elements[0].getLeaves();
		} else {
			List<Element> leaves = new LinkedList<Element>();
			if (len==0)
				leaves.add(this);
			else{
				for(int i=0; i<len; i++)
					leaves.addAll(elements[i].getLeaves());
			}
			return leaves;
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Element))
			return false;
		Element element = (Element) object;
		if (width != element.getWidth() | height != element.getHeight()
				| marginLeft != element.getMarginLeft() 
				| marginTop != element.getMarginTop()
				| elements.length != element.getNrOfChildren())
			return false;
		for(Element e : elements) {
			boolean hasMatch = false;
			for(Element other : element.getChildren()) {
				if (other.equals(e)) {
					hasMatch = true;
					break;
				}
			}
			if (!hasMatch)
				return false;
		}
		return true;
	}

	public String toString() {
		String out = width +", "+height+", "+marginLeft+", "+marginTop;
		return out;
	}

	/**
	 * Checks whether this element has the same measurements as the othe element.
	 * @param other
	 * @return true when all measurements are exactly the same.
	 */
	public boolean isSameSizeAs(Element other) {
		return (width == other.getWidth() & height == other.getHeight()
				& marginLeft == other.getMarginLeft() 
				& marginTop == other.getMarginTop());
	}
}
