package csvGraphFileHandlers;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.graphstream.graph.Graph;

@SuppressWarnings("unused")
public class WriteCSVTopologiesIndexes {
	private String targetFile;
	private FileWriter writer;
	private PrintWriter output;
	
	public WriteCSVTopologiesIndexes(String target){
		this.targetFile = target;
	}
	
	public void writeIndexes(ArrayList<Graph> graphs) throws IOException{
		try{
			FileWriter writer = new FileWriter(targetFile);
			PrintWriter output = new PrintWriter(writer);
			int i,j,k;
			
			String line = "";
			for(i=0;i<graphs.size();i++){
				line+=graphs.get(i).getId()+";";
			}
			output.println(line);

			for(i=0;i<graphs.get(0).getNodeCount();i++){	
				line="";
				for(j=0;j<graphs.size();j++){
					line+=graphs.get(j).getNode(i).getAttribute("Cb")+";";
				}
				output.println(line);
			}
				

			
			output.close();
			writer.close();
			
		}catch(IOException e){
			throw new IOException("Target file error!");
		}
	}
}
