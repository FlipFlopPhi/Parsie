/**
 * 
 */
package ru.flip.compare;

import java.util.LinkedList;
import java.util.List;

import ru.flip.Main;
import ru.flip.compare.DistanceMap.Node;
import ru.flip.core.DivTree;

/**
 * @author Vizu
 *
 */
public abstract class Clusterer {

	protected class ColoredNode {
		protected Node node;
		protected List<ColoredNode> cluster;
		
		protected ColoredNode(Node node) {
			this.node = node;
			cluster = new LinkedList<>();
		}
		
		/**
		 * Returns an array containing all the nodes in the cluster of this node.
		 */
		protected DivTree[] getNodes() {
			DivTree[] out = new DivTree[cluster.size()];
			int i=0;
			for(ColoredNode node : cluster) {
				out[i] = node.node.getTree();
				i++;
			}
			return out;
		}
	}
	
	protected List<List<ColoredNode>> clusters;
	protected DistanceMap map;
	protected List<ColoredNode> coloredNodes;
	
	protected Clusterer(DistanceMap map) {
		this.map = map;
		clusters = new LinkedList<>();
		coloredNodes = new LinkedList<>();
		for(Node node : map.getNodes())
			coloredNodes.add(new ColoredNode(node));
	}
	
	/**
	 * 
	 */
	public void clean() {
		clusters.clear();
		for(ColoredNode node: coloredNodes) {
			node.cluster = new LinkedList<>();
		}
	}
	
	public final void cluster() {
		clean();
		findClusters();
	}
	
	protected abstract void findClusters();
	
	public final int getAmountOfClusters() {
		return clusters.size();
	}
	
	public void printClusters() {
		int colorsUsed = clusters.size();
		for(int i=0; i < colorsUsed; i++) {
			List<ColoredNode> cluster = clusters.get(i);
			System.out.println("Cluster "+i+" contains the following websites:");
			for(ColoredNode colNode: cluster) {
				DivTree tree = colNode.node.getTree();
				System.out.println(tree.getAddress());
			}
		}
	}
	
	public DivTree[] getCluster(int i) {
		DivTree[] cluster = new DivTree[clusters.get(i).size()];
		int j=0;
		for(ColoredNode node : clusters.get(i)) {
			cluster[j] = node.node.getTree();
			j++;
		}
		return cluster;
	}
}
