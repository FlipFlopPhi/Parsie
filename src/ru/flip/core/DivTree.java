/**
 * 
 */
package ru.flip.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Vizu
 *
 */
public class DivTree {
	
	private String address;
	private Element root;
	
	public DivTree(String name) {
		address = name;
	}
	
	public void addRoot(Element e) {
		root = e;
	}
	
	/**
	 * Returns a list of all leaves of this tree. 
	 *  A leaf is an Element without any children.
	 */
	public List<Element> getLeaves(){
		List<Element> leaves = new LinkedList<Element>();
		
		return null;
		
	}
	
	public String getAddress() {return address;}
	public Element getRoot() {return root;}
	
	public void toHTML(File file) {
		try {
			file.delete();
			file.createNewFile();
			Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			writer.write("<html>\n");
			writer.write("<head></head>\n");
			writer.write("<body>\n");
			writeElement(writer, root);
			writer.write("</body>\n");
			writer.write("</html>");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void writeElement(Writer writer, Element element) throws IOException {
		String div = "<div class=\""+element.id+"\" style=\"";
		div += "width:"+(element.getWidth()-2)+"px; " + "height:"+(element.getHeight()-2)+"px;";
		div += "position: absolute; background-color: #ffffff;";
		div += "left:"+(element.getMarginLeft()-1)+"px;" + "top:"+(element.getMarginTop()-1)+"px;";
		div += "border: 1px solid black;\">\n";
		writer.write(div);
		for(Element child : element.getChildren())
			writeElement(writer, child);
		writer.write("</div>\n");
	}
}
