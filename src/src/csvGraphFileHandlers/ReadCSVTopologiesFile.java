package csvGraphFileHandlers;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ReadCSVTopologiesFile{
	
	private File file;
	private Map<String, Integer> hashMap = new HashMap<String, Integer>();
	private ArrayList<ArrayList<String[]>> topologies = new ArrayList<ArrayList<String[]>>(0);
	private int tam;
	
	
	public ReadCSVTopologiesFile(String source) throws FileNotFoundException{
		try{
			this.file = new File(source);
			Scanner s = new Scanner(this.file);
			
			s.useDelimiter("\n");
			
			fillTopologiesList(s);
			tam = topologies.size();

		}catch(FileNotFoundException e){
			throw new FileNotFoundException("Link file not found!");
		}
	
	}
	
	private void fillEdgeHashMap(String[] attr){
		int i;
		
		for(i=0;i<attr.length;i++){
			this.hashMap.put(attr[i].toLowerCase(), i);
		}
				
	}
	private ArrayList<String[]> fillEdgeArrayList(Scanner s){
		String[] edge;
		ArrayList<String[]> edges = new ArrayList<String[]>(0);
		
		while(s.hasNextLine()){
			edge = s.nextLine().replace("\"", "").split(",");
			if(edge[edge.length - 1].equals("---")){//separador de topologias
				break;
			}else{
				edges.add(edge);
			}
		}
		return edges;
	
	}
	private void fillTopologiesList(Scanner s){		
		String[] line = s.nextLine().replace("\"", "").split(",");
		
		while(!line[0].equals("END")){// a flag END sinaliza o fim do arquivo de topologias
			fillEdgeHashMap(line);
			topologies.add(fillEdgeArrayList(s));
			line = s.nextLine().replace("\"", "").split(",");
		}
	}
	
	public ArrayList<ArrayList<String[]>> getTopologies(){
		return this.topologies;
	}

	public int tam(){
		return this.tam;
	}

}
