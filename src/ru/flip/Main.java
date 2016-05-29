package ru.flip;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import flip.util.Pair;
import ru.flip.compare.*;
import ru.flip.core.DivTree;
import ru.flip.core.Element;

public class Main {

	public static void main(String[] args) {
		
		List<DivTree> trees = new LinkedList<DivTree>();
		
        try {
			File file = new File("DB.txt");
			System.out.println("Database file has been opened.");
			FileReader fr = new FileReader(file);
			BufferedReader tr = new BufferedReader(fr);
			String s = null;
			while((s = tr.readLine())!= null) {
				if (s.substring(0, 2).equals("$<")) {
					String name = s.substring(2,s.length()-1);
					System.out.println("Tree with name:\n  "+name+"\nfound.");
					DivTree tree = new DivTree(name);
					tree.addRoot(parseElement(tr, 0));
					trees.add(tree);
					System.out.println("Tree has been added to the current set of trees.");
				}
			}
			tr.close();
			fr.close();
			System.gc();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(trees.size() +" trees have been loaded in.");
        
        Comparer comp = new LooseEditComparer();
        /*block: {
        	DivTree tree0 = trees.get(0);
        	DivTree tree1 = trees.get(1);
        	DivTree avg = comp.createAverage(tree0, 1, tree1, 1);
        	double d = comp.getDistance(tree0, tree1);
        	if (comp.getDistance(avg, tree0) > d | comp.getDistance(avg, tree1) > d) {
        		System.out.println("Ho eens even");
        		return;
        	}
        }*/
        DistanceMap map = new DistanceMap(trees, comp);
        System.out.println("Distances between the trees have been calculated.");
        // System.out.println(comp.getDistance(trees.get(0), comp.createAverage(trees.get(0),1,trees.get(0),1)));
        Clusterer clusterAlgorithm = new ClustererKMeans(map, 7, 15, comp);
        clusterAlgorithm.cluster();
        System.out.println("Trees have been succesfully clustered.");
        clusterAlgorithm.printClusters();
        System.out.println("Loading generated websites.");
        for(int i=0; i < clusterAlgorithm.getAmountOfClusters(); i++) {
        	DivTree[] cluster = clusterAlgorithm.getCluster(i);
        	if (cluster.length == 0) {
        		System.out.println("Cluster "+i+" is empty.");
        		continue;
        	}
        	DivTree average = comp.createAverage(cluster);
        	showTreeInBrowser(average, ""+i, false);
        }
        
        map.printPairs();
        map.printClosestPair();
        //map.cluster();
        //map.printPairs();
		//System.out.println(map.getDistance(trees.get(0), trees.get(1)));
        System.out.println("Jobs done");
	}
	
	public static Element parseElement(BufferedReader reader, int depth) throws IOException {
		String s = reader.readLine();
		int pre = 2*depth;
		int end = pre;
		float[] param = new float[4];
		for(int i=0; i<4; i++) {
			while(s.charAt(end)!=',')
				end++;
			if (pre==end)
				param[i] = 0;
			else
				param[i] = Float.parseFloat(s.substring(pre, end));
			end++;
			pre=end;
		}
		String name;
		while(s.charAt(end)!=',')
			end++;
		name = s.substring(pre, end);
		end++;
		pre=end;
		int nrOfChildren;
		while(s.charAt(end)!=':')
			end++;
		//System.out.println(s.substring(pre, end));
		nrOfChildren = Integer.parseInt(s.substring(pre, end));
		Element element = new Element(param[0],param[1],param[2],param[3],name,nrOfChildren);
		for(int i=0; i<nrOfChildren; i++)
			element.addElement(parseElement(reader, depth +1));
		
		return element;
		
	}
	
	public static void showTreeInBrowser(DivTree tree,String name, boolean showRealSite){
		File file = new File("gen/tempSite/temp"+name+".html");
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        tree.toHTML(file);
        try {
        	if (showRealSite) 
        		Desktop.getDesktop().browse(new URI(tree.getAddress()));
			Desktop.getDesktop().browse(file.toURI());
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
