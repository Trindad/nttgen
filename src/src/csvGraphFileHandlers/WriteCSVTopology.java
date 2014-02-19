package csvGraphFileHandlers;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

@SuppressWarnings("unused")
public class WriteCSVTopology{
	private String targetFile;
	private FileWriter writer;
	private PrintWriter output;
	
	public WriteCSVTopology(String target){
		this.targetFile = target;
		try {
			writer = new FileWriter(targetFile, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void writeTopologyName(String name){
		try {
			writer = new FileWriter(targetFile, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		output = new PrintWriter(writer);
		
		output.println("Simulation Topology "+name);
		output.close();
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeTopologyNodes(Graph graph){

		int i;
		String line;
		try {
			writer = new FileWriter(targetFile, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		output = new PrintWriter(writer);
		for(i=0;i<graph.getNodeCount();i++){
			line = "["+graph.getNode(i).getAttribute("x")+"]";
			line += "\t";
			line += "["+graph.getNode(i).getAttribute("y")+"]";
			
			output.println(line);
		}		
		output.println("----------------------------------------------------");
		output.close();
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void writeTopologyEdges(Graph graph){
		int i;
		String line;
		Node source;
		Node target;
		double a,b;
		try {
			writer = new FileWriter(targetFile, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		output = new PrintWriter(writer);
		output.println("From\tTo\tLength");
		for(i=0;i<graph.getEdgeCount();i++){
			source = graph.getEdge(i).getSourceNode();
			target = graph.getEdge(i).getTargetNode();
			a = Math.pow(((Integer)source.getAttribute("x") - (Integer)target.getAttribute("x")), 2d);
			b = Math.pow(((Integer)source.getAttribute("y") - (Integer)target.getAttribute("y")), 2d);
			line = ""+source;
			line += "\t";
			line += target;
			line += "\t";

//			line += Math.sqrt(a+b);
//			output.println(line);
			output.format("%s%.2f%n", line, Math.sqrt(a+b));
		}
		output.println("----------------------------------------------------");
		output.close();
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}




























