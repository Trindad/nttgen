package topologiesgeneration;

import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.graph.Graph;

public class Measures {
	
	DoublyLinkedList path1 = new DoublyLinkedList();
	DoublyLinkedList path2 = new DoublyLinkedList();

	/**
	 *
	 */
	public void hops(DoublyLinkedList top, int N) {
		
		Suurballe sub = new Suurballe();
		DoublyLinkedList M1 = new DoublyLinkedList();
		DoublyLinkedList M2 = new DoublyLinkedList();
		int pc [] = new int [N];
		DoublyLinkedList w = new DoublyLinkedList ();
		w = sub.saveW(top);
		
		for(int i = 0;i < N; i++) {
			
			for(int j = 0;j < N; j++) {
				
				sub.initCost(top, w, N);
				
				if(i != j && i < j)
				{
					pc = sub.dijkstra(top,i,j,N,1);
					M1 = sub.pathM(i,j, pc, N);
					pc = sub.dijkstra(top,i,j, N, 2);
					sub.transCost(M1,pc,top,j,N);
					pc = sub.dijkstra(top,i,j,N, 1);
					M2 = sub.pathM(i,j,pc, N);
					sub.pathDisj(M1,M2);
					int m1S = M1.size();
					int m2S = M2.size();
					path1.addNode(m1S);
					path2.addNode(m2S);
				}
			}
		}
	}
	
	/**
	 * Calcula valor de h .
	 */
	public double hAvg(DoublyLinkedList list, int N) {
		
		double h = 0, listS = list.size(); /*D = ((N *(N-1))/2)*/
		int som = 0;
		
		for(int i = 0; i < listS; i++) {
				som += list.retElement(i);// soma os valores da matriz superior
		}
		
		h = ((double)(1/listS)*som); // faz a média
		
		return h;				
	}
	
	/**
	 * Calcula o valor h medio linha para uma topologia.
	 */
	public double[] callH(DoublyLinkedList top, int N, int op) {
		
		double hs [] = new double [2];
		
		hops(top,N);
		hs[0] = hAvg(path1,N);
		hs[1] = hAvg(path2,N);
		
		return hs;	
	}
	
	/**
	 *Calculo do transponder.
	 */
	public int calcTransp(DoublyLinkedList transp, int h) { //1 para h médio, outro valor para h'
		
		int som = 0, tSize = transp.size();
		
		for(int i = 0; i < tSize; i++){
			if(h == 1)
			{
				transp.upTransp(i,((transp.retElement(i)*path1.retElement(i))*2));
			}
			else
			{
				transp.upTransp(i,((transp.retElement(i)*path2.retElement(i))*2));
			}
		}
		
		for(int i = 0; i < tSize; i++) {
			
			som = som+transp.retElement(i);
		}
		return som; 
	}	
	
	/**
	 *Carrega os graus do grafo.
	 */
	public static void loadGraphDegrees(Graph graph) {
		
		BetweennessCentrality bc = new BetweennessCentrality();
		int dMin = graph.getNode(0).getDegree();
		int dMax = graph.getNode(0).getDegree();
		double bcMin,bcMax;
		double dMed = 0;
		double bcMed = 0;
		double tmp;
		int i;
		
		bc.init(graph);
		bc.setUnweighted();
		bc.compute();
		
		bcMin = graph.getNode(0).getAttribute("Cb");
		bcMax = graph.getNode(0).getAttribute("Cb");
		
		for(i = 1;i < graph.getNodeCount(); i++) {
			
			tmp = graph.getNode(i).getDegree();
			dMed += tmp;
			
			if(dMin > tmp)
			{
				dMin = (int)tmp;
			}
			if(dMax < tmp){
				dMax = (int)tmp;
			}
			
			tmp = graph.getNode(i).getAttribute("Cb");
			bcMed += tmp;
			
			if(bcMin > tmp)
			{
				bcMin = tmp;				
			}
			
			if(bcMax < tmp)
			{
				bcMax = tmp;
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
