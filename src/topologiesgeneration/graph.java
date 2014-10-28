package topologiesgeneration;

class cArc { //classe Arco
	
	int adj, weight;	
}

public class graph {

	int nodes; 											//guarda numero de nodos/arestas.
	int nLinks; //armazena o número de ligações do grafo
	DoublyLinkedList links = new DoublyLinkedList(); 	//guarda os links no formato i,j,i,j,...
	cArc[][] arcs = new cArc[nodes][nodes]; 			//cria a matriz de arestas (adjacencia).
	DoublyLinkedList transp = new DoublyLinkedList(); 	//lista dos transponders
	int degree[] =  new int[nodes];
	
	public graph(int n) {
		
		nodes = n;
		arcs = new cArc[nodes][nodes];
		degree = new int[nodes];
	}

	/**
	 * Inicializa um grafo.
	*/
	public void initGraph(int n) {	//init [g] with (n x n) elements
		
		nodes = n;
		for(int i = 0; i < n; i++){

			degree[i] = 0;
			
			for(int j = 0;j < n; j++) {
				
				arcs[i][j] = new cArc();
				arcs[i][j].adj = 0; //inicialization of the columns.
				arcs[i][j].weight = 9999;
			}
		}
		this.nLinks = 0;
	}

	/**
	 *Define um link (bidirecional), (origem, destino, peso).
	 */

	public void setArc(int src, int dst, int w) throws Exception{
		
		// System.out.println("1origem: "+src+"destino: "+dst+"w: "+w);
		arcs[src][dst].adj = 1;
		
		// System.out.println("2origem: "+src+"destino: "+dst+"w: "+w);
		arcs[dst][src].adj = 1;
		
//		System.out.println("origem: "+src+"destino: "+dst+"w: "+w+"\n");
		
		
		arcs[src][dst].weight = w;
		arcs[dst][src].weight = w;

		/**
		 * Atualiza o grau dos nós
		 */
		degree[src]= degree[src]+1;
		degree[dst] = degree[dst]+1;
		
		this.nLinks++;
		
//		System.out.println(" origemDegree: "+degree[src]+" destinoDegree: "+degree[dst]+"\n");
	}
	
	public int getNumberNodes() {
		return this.nodes;
	}

	public int getArc(int src,int dst) throws Exception{

		/**
		 * Testa se já tem ligação entre dois nós
		 */
		
		if (arcs[src][dst].weight != 9999 || arcs[dst][src].weight != 9999) 
		{
			return 0;
		}
		return 1;
	}

	public int getDegree(int src) {
		return this.degree[src];
	}

	public void setDegree(int src) throws Exception{

		this.degree[src] = 0;
	}
	/**
	 *Imprime a topologia, dado o n�mero de n�s. (matriz [g]).
	 */
	public void printTopology() {
		
		System.out.println("Network Topology [g]");
		// System.out.println("------------------------------------");
		
		for(int i = 0; i < nodes; i++) {
			
			for(int j = 0; j < nodes; j++) {
				
				System.out.print(""+arcs[i][j].adj+"\t");
			}
			System.out.println();
		}
		// System.out.println("------------------------------------");
	}

	public void removeEdges(int nodes) {
		
		for(int i=0;i< this.nodes-1;i++) {
			for(int j=i;j< this.nodes;j++){
				this.arcs[i][j].adj = 0;
				this.arcs[i][j].weight = 9999;
				
				this.arcs[j][i].adj = 0;
				this.arcs[j][i].weight = 9999;
			}
			this.degree[i] = 0;
		}
		this.nLinks = 0;
	}
	
	/**
	 *Imprime a topologia, dado o n�mero de n�s. (matriz [g]).
	 */
	
	public void printWeights() {
		
		// System.out.println("Network Topology [g]");
		// System.out.println("------------------------------------");
		
		for(int i = 0; i < nodes; i++) {
			
			for(int j = 0; j < nodes; j++) {
				
				System.out.print("["+arcs[i][j].weight+"]\t");
			}
			// System.out.println();
		}
		// System.out.println("------------------------------------");
	}

	/**
	 *get para numero de links/arestas.
	 */
	public int getNumLinks()throws Exception {
	
		
		return this.nLinks;
	}
	

	/**
	 *Preenche o vetor de links i,j,i,j,....
	 */
	public DoublyLinkedList getLinks()throws Exception {
		
		for(int i = 0; i < nodes; i++) {
			
			for(int j = 0; j < nodes; j++) {
				
				if((i < j) && (arcs[i][j].adj == 1))
				{
					//entao existe o link
					links.addTwo(i,j);
				}
			}
		}
		return links;
	}
	/**
	 *Preenche a lista dos transponder inicialmente com 1 para c�lculo do transponder.
	 */
	public void initTransp(int n) {
		
		for(int i = 0; i < n; i++) {
			
			for(int j = 0;j < n; j++) {
				
				if(j > i) 
				{
					transp.addNode(1);//mudar para 0
				}		
			}
		}
	}

	/**
	 *atualiza valores da lista da quantidade de transponder.
	 */
	public void attTransp(int pos, int v) { // colocar num for, quando j > i ai vai colocando os numeros
		
		transp.upTransp(pos,v);
	}

	/**
	 *Retorna lista do transponder.
	 */
	public DoublyLinkedList retLT() {
		
		return transp;
	}
}
