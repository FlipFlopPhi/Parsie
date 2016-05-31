/**
 * 
 */
package ru.flip.compare;

import flip.util.Util;
import ru.flip.Main;
import ru.flip.compare.Clusterer.ColoredNode;

/**
 * @author Vizu
 *
 */
public class ClustererKMeans extends Clusterer {

	protected int k;
	protected int maxIterations;
	protected Comparer comparer;
	
	public ClustererKMeans(DistanceMap map, int k, int maxIterations, Comparer comp) {
		super(map);
		this.k = k;
		this.maxIterations = maxIterations;
		comparer = comp;
	}

	/* (non-Javadoc)
	 * @see ru.flip.compare.Clusterer#cluster()
	 */
	@Override
	protected void findClusters() {
		int r = (int) Math.floor(Math.random()) % coloredNodes.size();
		int stepDistance = Math.floorDiv(coloredNodes.size(),k);
		ColoredNode[] centroids = new ColoredNode[k];
		for(int i=0; i<k; i++) {
			centroids[i] = coloredNodes.get((r +i*stepDistance) % coloredNodes.size());
		}
		
		for(int i=0; i<maxIterations; i++) {
			for(ColoredNode node : coloredNodes) {
				ColoredNode centroid = findClosestCentroid(node, centroids);
				centroid.cluster.add(node);
			}
			for(int j=0; j<centroids.length; j++) {
				if (centroids[j].cluster.isEmpty())
					continue;
				ColoredNode newCentroid = new ColoredNode(
						map.new Node(comparer.createAverage(centroids[j].getNodes())));
				centroids[j] = newCentroid;
			}
			
		}
		for(ColoredNode node : coloredNodes) {
			ColoredNode centroid = findClosestCentroid(node, centroids);
			centroid.cluster.add(node);
		}
		for(ColoredNode centroid : centroids) {
			System.out.println("Saving clustter, containing "+centroid.cluster.size() +" elements.");
			clusters.add(centroid.cluster);
		}
	}

	protected final ColoredNode findClosestCentroid(ColoredNode node, ColoredNode[] centroids) {
		double min = Double.MAX_VALUE;
		ColoredNode closest = null;
		for(ColoredNode centroid : centroids) {
			double d = comparer.getDistance(node.node.getTree(), centroid.node.getTree());
			if (d<min) {
				closest = centroid;
				min = d;
			}
		}
		return closest;
	}
}
