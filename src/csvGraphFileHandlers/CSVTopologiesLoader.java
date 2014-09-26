package csvGraphFileHandlers;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;


public class CSVTopologiesLoader{
	private int nTopologies = 0;
	private int nNodes;
	
	private ReadCSVTopologiesFile topologyFile;
	private String filePath;
	private ArrayList<Graph> graphs = new ArrayList<Graph>(0);
	
	public CSVTopologiesLoader(String linkFilePath, int nNodes) throws FileNotFoundException{
		filePath = linkFilePath;
		topologyFile = new ReadCSVTopologiesFile(linkFilePath);
		nTopologies = topologyFile.tam();
		this.nNodes = nNodes;
		fillGraphsList();
		
	}
	
//	methods	

	private Graph createGraph(ArrayList<String[]> edges, int topology_id){
		int i;
		String graphName = filePath.substring(filePath.lastIndexOf("\\")+1, filePath.lastIndexOf("."));
		Graph graph = new SingleGraph(graphName+"["+topology_id+"]");			
		String linkName;
		
		for(i=0;i<nNodes;i++){
			graph.addNode(""+i);
		}
		for(i=0;i<edges.size();i++){
			linkName = edges.get(i)[0]+" - "+edges.get(i)[1];				
			graph.addEdge(linkName, edges.get(i)[0], edges.get(i)[1]);
			
			// System.out.println(linkName+" "+edges.get(i)[0]+" "+edges.get(i)[1]);
		}
		
		loadGraphDegrees(graph);
		
		return graph;
	}
	
	private void fillGraphsList(){
		int i;
		for(i = 0 ; i < nTopologies;i++){
			graphs.add(createGraph(topologyFile.getTopologies().get(i), i));
//			graphs.get(i).display(true); // PARA VISUALIZAR OS GRAFOS CRIADOS DESCOMENTE AQUI!!
		}
		
	}

	public ArrayList<Graph> getGraphs(){
		return this.graphs;
	}
	
	private void loadGraphDegrees(Graph graph){
		BetweennessCentrality bc = new BetweennessCentrality();
		int dMin = graph.getNode(0).getDegree();
		int dMax = graph.getNode(0).getDegree();
		double bcMin,bcMax;
		double dMed=0;
		double bcMed=0;
		double tmp;
		int i;
		
		bc.init(graph);
		bc.setUnweighted();
		bc.compute();
		
		bcMin=graph.getNode(0).getAttribute("Cb");
		bcMax=graph.getNode(0).getAttribute("Cb");
		
		for(i=1;i<graph.getNodeCount();i++){
			tmp = graph.getNode(i).getDegree();
			dMed+=tmp;
			if(dMin>tmp){
				dMin = (int)tmp;
			}
			if(dMax<tmp){
				dMax=(int)tmp;
			}
			
			tmp = graph.getNode(i).getAttribute("Cb");
			bcMed += tmp;
			if(bcMin>tmp){
				bcMin=tmp;				
			}
			if(bcMax<tmp){
				bcMax=tmp;
			}
		}
		dMed/=graph.getNodeCount();
		bcMed/=graph.getNodeCount();
		graph.addAttribute("dMin", dMin);
		graph.addAttribute("dMax", dMax);
		graph.addAttribute("dMed", dMed);
		graph.addAttribute("bcMin", bcMin);
		graph.addAttribute("bcMax", bcMax);
		graph.addAttribute("bcMed", bcMed);

		
	}
}

