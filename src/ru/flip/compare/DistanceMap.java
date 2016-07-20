/**
 * 
 */
package ru.flip.compare;

import java.util.LinkedList;
import java.util.List;

import flip.util.Pair;
import ru.flip.core.DivTree;

/**
 * @author Vizu
 *
 */
public class DistanceMap {

	public class Node {
		private DivTree tree;
		private List<Edge> edges;
		
		public Node(DivTree tree) {
			this.tree = tree;
			edges = new LinkedList<Edge>();
		}
		
		private double getDistanceTo(DivTree other) {
			for(Edge e : edges) {
				if(e.node0.tree==other | e.node1.tree==other)
					return e.distance;
			}
			return -1;
		}
		
		public Node getNearestNeighbour() {
			Node closest = null;
			double min = Double.MAX_VALUE;
			for(Edge e : edges) {
				if (e.distance < min) {
					min = e.distance;
					if (e.node0==this)
						closest = e.node1;
					else
						closest = e.node0;
				}
			}
			return closest;
		}

		public DivTree getTree() {return tree;}
		
	}
	
	private class Edge {
		private Node node0;
		private Node node1;
		private double distance;
		
		private Edge(Node node0, Node node1, double distance) {
			this.node0 = node0;
			this.node1 = node1;
			this.distance = distance;
			
			node0.edges.add(this);
			node1.edges.add(this);
		}
	}
	
	private Node[] nodes;
	
	public DistanceMap(List<DivTree> trees, Comparer comparer){
		nodes = new Node[trees.size()];
		int len = nodes.length;
		for(int i=0; i<len; i++)
			nodes[i] = new Node(trees.get(i));
		
		for(int i=0; i<len; i++) {
			Node node0 = nodes[i];
			for(int j=i+1; j<len; j++){
				Node node1 = nodes[j];
				new Edge(node0, node1, 
						comparer.getDistance(node0.tree, node1.tree));
			}
		}
	}
	
	/**
	 * This method scans the distance map for the edge between tree0 and tree1,
	 *  and returns the calculated distance between them.
	 * @param tree0
	 * @param tree1
	 * @return the distance between the two divtrees. Returns -1 if no distance has been calculated between the two.
	 */
	public double getDistance(DivTree tree0, DivTree tree1) {
		if(tree0 == tree1) {
			return 0;
		}
		for(Node n : nodes) {
			if (n.tree == tree0) {
				return n.getDistanceTo(tree1);
			}
		}
		return -1;
	}
	
	public void printPairs() {
		for(Node n : nodes) {
			System.out.print(n.tree.getAddress() + " is closest to:");
			Node var0 = n.getNearestNeighbour();
			System.out.println("    "+ var0.tree.getAddress());
		}
	}
	
	public Pair<DivTree> findClosestPair() {
		Edge smallest = null;
		double min = Double.MAX_VALUE;
		for(Node n : nodes) {
			for(Edge e : n.edges) {
				if (e.distance < min) {
					smallest = e;
					min = e.distance;
				}
			}
		}
		
		return new Pair<DivTree> (smallest.node0.getTree(),smallest.node1.getTree());
	}
	
	public void printClosestPair() {
		Edge smallest = null;
		double min = Double.MAX_VALUE;
		for(Node n : nodes) {
			for(Edge e : n.edges) {
				if (e.distance < min) {
					smallest = e;
					min = e.distance;
				}
			}
		}
		System.out.println(smallest.node0.getTree().getAddress()+" and "+smallest.node1.getTree().getAddress()
				+" are the closest pair,\n with a distance of "+min);
		
	}

	public Node[] getNodes() {
		return nodes.clone();
	}
	
	
	
}
