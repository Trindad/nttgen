package topologiesgeneration;

import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.graph.Graph;

public class Measures {
	
	DoublyLinkedList path1 = new DoublyLinkedList();
	DoublyLinkedList path2 = new DoublyLinkedList();
	DoublyLinkedList cust = new DoublyLinkedList(); 

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
					cust = new DoublyLinkedList();
					pc = sub.dijkstra(top, i, N);
					M1 = sub.pathM(i,j, pc, N);
					for(int x=0;x<cust.size();x++){
						pc[x]=cust.retElement(x);
					}
					cust = new DoublyLinkedList(); 
					sub.transCost(M1,pc,top,j,N);
					pc = sub.dijkstra(top,i,N);
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
	public double hAvg(DoublyLinkedList list) {
		
		double h = 0, listS = list.size(); /*D = ((N *(N-1))/2)*/
		int som = 0;
		
		for(int i = 0; i < listS; i++) {
				som += list.retElement(i);// soma os valores da matriz superior
		}
		
		h = ((double)(1/listS)*som); // faz a mÃ©dia
		
		return h;				
	}
	
	/**
	 * Calcula o valor h medio linha para uma topologia.
	 */
	public double[] callH(DoublyLinkedList path) {
		
		double hs [] = new double [2];
		
	//	hops(top,N);
		hs[0] = hAvg(path1);
		hs[1] = hAvg(path2);
		
		return hs;	
	}
	
/*
 * 
 * EXCENTRICIDADE
 * 
 
 public int dijkstraExcentricidade(DoublyLinkedList top1, int s, int d, int N){
		int []S = new int[N];
		int []p = new int[N];
		int []c = new int [N];
		int a=s, auxC=0, topS= top1.size(), cost=N;
		boolean v=true;

		for(int k=0;k<N;k++) {c[k]=N;}	
		for(int k=0;k<N;k++) {S[k]=-1;}
		for(int k=0;k<N;k++) {p[k]=-1;}
		
		c[a]=0;
		S[a]=1;
		
		while(v==true){
			for(int k=0;k<N;k++){
				if(S[k]==-1){
					for(int i=0;i<topS;i++){				
						if(top1.retElementTwo(i)==k && top1.retElementOne(i)==a){
							if(c[k]>c[a]+top1.retElementThree(i)){
								c[k]=c[a]+top1.retElementThree(i);
								p[k]=a;	
							}
						}
					}
				}
			}	
			
			cost=N;
			for(int i=0;i<N;i++){
				if(S[i]!=1){
					if(cost>=c[i]){ 
						cost=c[i];
						auxC=i; //recebe o nó do custo minimo
					}
				}
			}
			a=auxC;
			
			// verifica se já foi alterado o custo para todos os nós
			for(int i=0;i<S.length;i++){
				if(S[i]==-1){ // se não foram, continua no laço
					v=true;
					break;
				}else{ // se todos foram, sai do laço
					v=false; 
				}
			}
			
			S[a]=1;
		}	
		
		DoublyLinkedList M = new DoublyLinkedList();
		int t=d;
		
		for(int i=0;i<p.length;i++){
			d=p[t];
			M.addTwo(d,t);		
			t=d;
			
			if(d==s){break;}
		}
		
		int k=M.size();
		return k;		
	}
 
 public void mHops (DoublyLinkedList top1, DoublyLinkedList top2, int N){
		int a=0,k=0;
		
		for(int i=0;i<N;i++){
			for(int j=a;j<N;j++){
				if(i!=j){
					k=dijkstraExcentricidade(top1,i,j,N);
					top2.addNode(i,j,k);
					top2.addNode(j,i,k);
				}
			}
			a++;
		}
	}
	
	public void excentricidade (DoublyLinkedList top2, DoublyLinkedList excent, int N){		
		int max=0;
		for(int i=0; i<N; i++){
			for(int j=0; j<top2.size(); j++){
				if(i==top2.retElementOne(j)){
					if(top2.retElementThree(j)>max){
						max=top2.retElementThree(j);
					}
				}
			}
			excent.addTwo(max,i);
			max=0;
		}
		excent.displayTwo();
	}	
	
	public float eMedia (DoublyLinkedList excent){	
		float p, listE=excent.size();
		int som=0;
	
		for(int i=0; i<listE; i++){
			som+= excent.retElementOne(i);
		}
		p=((float)(1/listE)*som); // faz a média
		return p;
	}
	
	public void eCentro (DoublyLinkedList excent, DoublyLinkedList excentC){	
		int min=9999;
	
		for(int j=0; j<excent.size(); j++){
			if(excent.retElementOne(j)<min){
				min=excent.retElementOne(j);
			}
		}
		for(int j=0; j<excent.size(); j++){
			if(excent.retElementOne(j)==min){
				excentC.addTwo(min,j);
			}
		}
		min=9999;
	}	
	
	public void eDiametro (DoublyLinkedList excent, DoublyLinkedList excentD){
		int max=0;		
		for(int j=0; j<excent.size(); j++){
			if(excent.retElementOne(j)>max){
				max=excent.retElementOne(j);
			}
		}
		for(int j=0; j<excent.size(); j++){
			if(excent.retElementOne(j)==max){
				excentD.addTwo(max,j);
			}
		}
		max=0;
	}
 

 * 
 * 
 * EXCENTRICIDADE
 * 
 * 
 * */	
	
	
	/**
	 *Calculo do transponder.
	 */
	public int calcTransp(DoublyLinkedList transp, int h) { //1 para h mÃ©dio, outro valor para h'
		
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
		
		for(i = 0;i < graph.getNodeCount(); i++) {
			
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