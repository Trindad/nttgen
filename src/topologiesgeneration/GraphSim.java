
package topologiesgeneration;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.implementations.SingleGraph;

import csvGraphFileHandlers.WriteCSVGraphIndexes;
import csvGraphFileHandlers.WriteCSVTopology;
import java.io.File;

public class GraphSim {
	
	private int N; // numero de nos/vertices
	private int Dmax;
	private int Dmin;
	private int L;
	private int Y;
	private int S;//numero de simulações
	private int X; //area
	private int R; //numero de regiões
	private int length;
	private int breadth;
	private double alfa;
	private double beta;
	private int nSim; /*número de simulações*/
	private int caracType;
	
	private int h = 1;
	private int sim;
	private int aux = 0;
	private int auxS = 0;
	private DoublyLinkedList w = new DoublyLinkedList ();
	private Suurballe s = new Suurballe();
	private Plane net1ptr;
	private boolean sobrevivente;
	
	private graph rede;
	private Measures m;
	private DoublyLinkedList top;
	private Graph graph = new SingleGraph("Topologies");
	private WriteCSVTopology graphWritter;
	private WriteCSVGraphIndexes indexWritter;
	
	private boolean betweenCentraly = false; /*betweens centrality*/
	private boolean degree = false;
	private int count;
	public String fileSerial = fileCreation();
	
	/**
	 *Inicia simulação e verificação das topologias assim como a gravação das mesmas em arquivo.
	 */
	public void init() throws IOException, Exception{
		
		int i;		
		int numLink = 0, mSpath = 1; 
		/*
		 * Limite máximo é mínimo calculados 
		 */
		int Lmax = (int)((N * Dmax)/2);
		int Lmin = (int)((N * Dmin)/2);

		String fileSerial = fileCreation();
		
		graphWritter = new WriteCSVTopology(fileSerial+"_topology.txt");
		top = new DoublyLinkedList();
		
		for(i = 0; i < N; i++) {
			
			graph.addNode(i+"");
			
		}
		
		if(caracType == 1)
		{
			indexWritter = new WriteCSVGraphIndexes(fileSerial+"_general_indexes.csv", caracType);	
			indexWritter.setBc(betweenCentraly);
			indexWritter.setDegree(degree);
			indexWritter.WriteCab();
		}
		else
		{
			indexWritter = new WriteCSVGraphIndexes(fileSerial+"_specific_indexes.csv", caracType);	
			indexWritter.setBc(betweenCentraly);
			indexWritter.setDegree(degree);
		}
		
		
		
		for(sim = 0; sim < nSim; sim++) {
			
			rede = new graph(N);
			
			graphWritter.writeTopologyName(""+(sim+1) );
			net1ptr = new Plane(getBreadth(), getLength(),X,Dmin,Dmax);
			
			m = new Measures();
			
			sobrevivente = false;
			top = new DoublyLinkedList();
			
			aux = 0;
			count = 0;

			rede.initGraph(N);
			net1ptr.setRegion(R,Y,S,N);
			
		
			/*
			 * Ring Graph
			*/
//			System.out.println("numero de links"+rede.getNumLinks());
			net1ptr.setRing(rede,N,R,L,mSpath);
		
//			System.out.println("numero de links"+rede.getNumLinks());
			net1ptr.setRingReg(rede,N,L,mSpath,Lmax);			

			
//			System.out.println("numero de links"+rede.getNumLinks());
			
			/*
			 *  adiciona ligações até atingir o limite minimo
			 */
			numLink = rede.getNumLinks();
			
			int controle = 0;
			int temp = 0;
			
			while(numLink < Lmin) {
				
				temp = net1ptr.setLink(rede,L,alfa,beta,mSpath,controle);// coloca novo link
				
				if(temp == N && controle < Dmax) controle++;
				
				numLink = rede.getNumLinks();// atualiza numLink
			}
			
			int tempAnt = 0;
			
			
			while(numLink <= Lmax) {//enquanto não tiver o número máximo
				
				temp = 0;
				
				if(numLink < Lmax)
				{
					temp = net1ptr.setLink(rede,L,alfa,beta,mSpath,controle);//coloca uma ligação
					if(temp == N && controle < Dmax) controle++;
					if(temp == -1) break;
				}	
				if( numLink < rede.getNumLinks() || numLink == Lmax || (temp == N && tempAnt < temp))
				{// se deu certo, aumento o número e aqui faz o teste
//					System.out.println("Verifica rede sobrevivente");
					tempAnt = temp;
					numLink = rede.getNumLinks();// atualiza numLink
					
					
					graph.addAttribute("top","Topology ["+(sim+1)+":"+count+"]");
					procedure();
					count++;
					
					if(count > N ) 
					{
						break;
					}
					
					
					if(numLink == Lmax )
					{
						break;
					}
					if(count == N/2) break;
				}
				
			}
			
			if(numLink > Lmax)
			{ 
				System.out.println("Number Link > Limit maximun");
			}
			
			for(i = 0; i < graph.getEdgeCount();i++){// retira as ligações do grafo para a proxima simulação				
				graph.removeEdge(i);
				
			}
//			rede.printTopology();
			numLink = 0;//reinicia numLink com 0 para verificar a proxima topologia
		}	
		
	}  

private void procedure() throws Exception {
		
		int i, j=0;
		double[] hs = new double [2];
		if(!sobrevivente)
		{
			/**
			 * se a rede até então não for sobrevivente
			 */
			for(i = 0; i < N; i++) {
				
				for(j = i+1; j < N; j++) {
					
					if(rede.arcs[i][j].adj == 1)
					{
						//System.out.println("i: " + i +" j:"+ j);
						top.addNode(i,j,rede.arcs[i][j].weight);//adiciona o nó origem e destino e peso na váriavel top
						top.addNode(j,i,rede.arcs[j][i].weight);
					}
				}
				
			}
//			for(i = 0; i < graph.getEdgeCount(); i++){	
//				System.out.println("oi>>> "+graph.getEdge(i));
//			}
//			
			w = s.saveW(top);//salva o peso original da topologia
		
			hs = s.twoPaths(top,N, w, 0);
			if(hs[0]!=999999 && hs[1]!=999999)
			{
				/**
				 *  testa se é sobrevivente
				 */
				
				sobrevivente = true;
				auxS = 1;
			}
			else
			{
				System.out.println("Not Recorded Topology");
				
				//deleta arquivos que não possuem topologias sobreviventes
				
			//	File txt = new File(graphWritter.getTargetFile());
			//	txt.delete();
				
			//	File csv = new File(indexWritter.getTargetFile());
			//	csv.delete();
				
				top.displayThree();
				top = new DoublyLinkedList ();
				
			//	throw new Exception("Couldn't generate topology");
			}
		}
		if(sobrevivente)
		{
		
			/**
			 *  se ela ja for sobrevivente só será necessário adicionar os links
			 */
//			System.out.println("Sobrevivente");
			
			if(aux == 0)
			{// só será executado uma vez
				
				
				
				for(i = 0; i < (int)net1ptr.nPos.size();i++) {
					
					if(i < N)
					{
						int posX = net1ptr.nPos.retElementOne(i); // cordenada X
					
						int posY = +net1ptr.nPos.retElementTwo(i);// coordenada y
						
						org.graphstream.graph.implementations.SingleNode no = graph.getNode(i);
						
						if (no != null && i < N) {
							
							graph.getNode(i).addAttribute("xy", posX, posY);
							
							graph.getNode(i).addAttribute("x", posX);
							graph.getNode(i).addAttribute("y", posY);
						}
					}
				}
				
				graphWritter.writeTopologyNodes(graph);
				aux = 1;
			}
			
			int cont=0; boolean auxX=false;
			for(i = 0; i < N; i++){	
				
				for(j = i+1; j < N; j++){
					
					if(rede.arcs[i][j].adj == 1){// se existir adjacencia				
						try
						{
							// tenta adicionar a ligação no grafo
							graph.addEdge(i+"-"+j, i, j);

							for(cont=0;cont<top.size();cont++){
								// verifica se a ligação já está em "top"
								if(i==top.retElementOne(cont) && j==top.retElementTwo(cont)){
									auxX=true; // se estiver sai
									break;
								}
							}
							if(auxX==false)//se não estiver adiciona ligação em "top"
							{
								top.addNode(i,j,rede.arcs[i][j].weight);
								top.addNode(j,i,rede.arcs[j][i].weight);
								
							}
							
							auxX=false;
						}
						catch(IdAlreadyInUseException IdE)
						{
							
						}
						
					}
				}
			}
			graphWritter.writeTopologyName("["+(sim+1)+":"+count+"]");
			graphWritter.writeTopologyEdges(graph);

			Measures.loadGraphDegrees(graph);
			
			w = new DoublyLinkedList();
			w = s.saveW(top);
			if(caracType == 1)
			{	if(auxS != 1){
					hs = s.twoPaths(top,N, w, 1);
				}else{
					auxS = 0;
				}
			
				//c = getExcCentro();
				graph.addAttribute("h", hs[0]);
				graph.addAttribute("h'", hs[1]);
				//graph.addAttribute("c", c);
				indexWritter.writeIndexes(graph);
			} 
			else
			{	
				indexWritter.writeTopologyName("["+(sim+1)+":"+count+"]");
				indexWritter.WriteCab();
				indexWritter.writeIndexes(graph);
			}
			System.out.println("Gravou topologia");	
		}
	}	
	
	/**
	 *Cria arquivo.
	 */
	private String fileCreation() {
		
		File dir;
		Date date = new Date();
		SimpleDateFormat dateFormat;
		String fileSerial;
		String local;
		
		local = System.getProperty("user.dir")+"/sims";
		
		dir = new File(local);
		
		dir.mkdir();
		
		dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		fileSerial = dateFormat.format(date);
		
		dateFormat = new SimpleDateFormat("HH-mm-ss");
		fileSerial += " "+dateFormat.format(date);
		
		// System.out.println(fileSerial);
		
		return local+"/"+fileSerial;
		
		
		
	}
		
	
	/**
	 * @return the length
	 */
	public int getLength()throws Exception {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length)throws Exception {
		this.length = length;
	}

	/**
	 * @return the breadth
	 */
	public int getBreadth() throws Exception{
		return breadth;
	}
	
	/**
	 * @return the R
	 */
	public int getR()throws Exception {
		return R;
	}
	
	/**
	 * @return the R
	 */
	public int getN()throws Exception {
		return N;
	}

	/**
	 * @param breadth the breadth to set
	 */
	public void setBreadth(int breadth) {
		this.breadth = breadth;
	}

	/**
	 *para adquirir os h's da topologia.
	 */
//	public double[] getH()throws Exception {
//		
//		return m.callH(top,N,h);
//	}
	
	/**
	 *Para adquirir os transponders da topologia.
	 */
	public int[] getTransp()throws Exception{
		
		DoublyLinkedList t = new DoublyLinkedList();
		int som[] = new int[2], tSize;
		Measures m = new Measures();
		graph rede = new graph(N);
		
		m.hops(top,N);
		rede.initTransp(N);
		
		t = rede.retLT();
		som[0]=m.calcTransp(t,1);
		tSize=t.size();
		
		for(int i = 0; i < tSize; i++) {
			
			t.upTransp(i,1);                          
		}
		som[1] = m.calcTransp(t,2);
		return som;
	}

	
	
	
	/**
	 *Set.
	 */
	public void setN(int n)throws Exception {
		
		N = n;
	}

	public void setDmax(int dmax)throws Exception {
		Dmax = dmax;
	}

	public void setDmin(int dmin)throws Exception {
		
		Dmin = dmin;
	}

	public void setL(int l) throws Exception{
		L = l;
	}

	public void setY(int y)throws Exception {
		Y = y;
	}

	public void setS(int s) throws Exception{
		
		S = s;
	}

	public void setX(int x)throws Exception {
		
		X = x;
	}

	public void setR(int length,int breadth) throws Exception{
		
		/**
		 * Calcula quantas regiões tera o plano 
		 * */
		
		R = length*breadth; /*numero de regiões*/
		
	}

	public void setAlfa(double alfa) throws Exception{
		
		this.alfa = alfa;
	}

	public void setBeta(double beta) throws Exception{
		
		this.beta = beta;
	}
	
	public void setnSim(int i) throws Exception{
		
		this.nSim = i;
	}
	
	public void setCaracType(int type)throws Exception {
		
		this.caracType = type;
	}

	public void setBc(boolean bc)throws Exception {
		
		this.betweenCentraly = bc;
	}
	
	public void setDegree(boolean degree) throws Exception{
		
		this.degree = degree;
	}
}