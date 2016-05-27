/**
 * 
 */
package ru.flip.compare;

import ru.flip.compare.DistanceMap.Node;

/**
 * @author Vizu
 *
 */
public class ClustererKNN extends Clusterer {
	
	private int k;
	
	public ClustererKNN(DistanceMap map, int k) {
		super(map);
		this.k = k;
	}

	@Override
	protected void findClusters() {
		for (ColoredNode colNode: coloredNodes) {
			colNode.cluster.add(colNode);
			clusters.add(colNode.cluster);
		}
		for(ColoredNode colNode: coloredNodes) {
			Node var0 = colNode.node.getNearestNeighbour();
			for(ColoredNode other : coloredNodes) {
				if (other.node == var0) {
					if (other.cluster.equals(colNode.cluster))
						break;
					other.cluster.addAll(colNode.cluster);
					clusters.remove(colNode.cluster);
					colNode.cluster = other.cluster;
					break;
				}
			}
		}
	}
}
