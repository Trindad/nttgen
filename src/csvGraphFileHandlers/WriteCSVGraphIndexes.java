package csvGraphFileHandlers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

@SuppressWarnings("unused")
public class WriteCSVGraphIndexes {
	private String targetFile;
	private FileWriter writer;
	private PrintWriter output;
	private boolean bc;
	private boolean degree;
	private int type;
	private DecimalFormat formatador = new DecimalFormat("#0.00");
	
	public WriteCSVGraphIndexes(String target, int type){
		this.targetFile = target;
		this.type = type;
		
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
	
	public void writeIndexes(Graph graph){
		if(type == 1){
			writeGeneralIndexes(graph);
		}else{
			writeNodeIndexes(graph);
		}
	}

	private void writeGeneralIndexes(Graph graph){
		try{
			File f = new File(targetFile);
			FileWriter writer = new FileWriter(f, true);
			PrintWriter output = new PrintWriter(writer);
			int i;
			String line;
			
			line = graph.getAttribute("top")+";"+graph.getNodeCount()+";"+graph.getEdgeCount()+";";
			line += formatador.format(graph.getAttribute("h"))+";"+formatador.format(graph.getAttribute("h'"))+";";
			if(degree){
				
				line += graph.getAttribute("dMin")+";"+graph.getAttribute("dMax")+";"+formatador.format(graph.getAttribute("dMed"))+";";
			}
			if(bc){
				line += formatador.format(graph.getAttribute("bcMin"))+";"+formatador.format(graph.getAttribute("bcMed"))+";"+formatador.format(graph.getAttribute("bcMax"))+";";
			}
					
			output.println(line);
			
			output.close();
			writer.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void writeNodeIndexes(Graph graph){
		try{
			File f = new File(targetFile);
			FileWriter writer = new FileWriter(f, true);
			PrintWriter output = new PrintWriter(writer);
			int i;
			String line;
			Node node;
			
			for(i=0;i<graph.getNodeCount();i++){
				node = graph.getNode(i);
				line = node.getId()+";"+node.getAttribute("x")+";"+node.getAttribute("y")+";";
				if(degree){
					line += node.getDegree()+";";
				}
				if(bc){
					line += formatador.format(node.getAttribute("Cb"))+ ";";
				}
				output.println(line);

			}
			output.println("----------------------------------------------------");
			
			output.close();
			writer.close();
			
		}catch(IOException e){
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
		
		output.println("Topology"+name);
		output.close();
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void WriteCab(){		
		try {
			writer = new FileWriter(targetFile, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		output = new PrintWriter(writer);
		
		String line = "";
		if(type == 0){
			line = "id;x;y;";
			if(degree){
				line+="degree;";
			}
			if(bc){
				line+="bc;";
			}
		}else{
			line = "Id;nNodes;nEdges;h;h';";
			if(degree){
				line+="dMin;dMax;dMed;";
			}
			if(bc){
				line+="bcMin;bcMed;bcMax;";
			}
		}
		

		
		output.println(line);
		output.close();
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void setBc(boolean bc){
		this.bc = bc;		
	}
	
	public void setDegree(boolean dg){
		this.degree=dg;
	}
	
	public String getTargetFile() {
		
		return targetFile;
	}
}
