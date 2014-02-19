package topologiesgeneration;

class cArc { //classe Arco
	
	int adj, weight;	
}

public class graph {

	int nodes; 											//guarda numero de nodos/arestas.
	DoublyLinkedList links = new DoublyLinkedList(); 	//guarda os links no formato i,j,i,j,...
	cArc[][] arcs = new cArc[nodes][nodes]; 			//cria a matriz de arestas (adjacencia).
	DoublyLinkedList transp = new DoublyLinkedList(); 	//lista dos transponders

	public graph(int n) {
		
		nodes = n;
		arcs = new cArc[nodes][nodes];
	}

	/**
	 * Inicializa um grafo.
	*/
	public void initGraph(int n) {	//init [g] with (n x n) elements
		
		nodes = n;
		for(int i = 0; i < n; i++){
			
			for(int j = 0;j < n; j++) {
				
				arcs[i][j] = new cArc();
				arcs[i][j].adj = 0; //inicialization of the columns.
				arcs[i][j].weight = 9999;
			}
		}
	}

	/**
	 *Define um link (bidirecional), (origem, destino, peso).
	 */

	public void setArc(int src, int dst, int w) {
		
		//System.out.println("1origem: "+src+"destino: "+dst+"w: "+w);
		arcs[src][dst].adj = 1;
		
		//System.out.println("2origem: "+src+"destino: "+dst+"w: "+w);
		arcs[dst][src].adj = 1;
		
		//System.out.println("3origem: "+src+"destino: "+dst+"w: "+w);
		
		arcs[src][dst].weight = w;
		arcs[dst][src].weight = w;
	}

	/**
	 *Imprime a topologia, dado o n�mero de n�s. (matriz [g]).
	 */
	public void printTopology() {
		
		System.out.println("Network Topology [g]");
		System.out.println("------------------------------------");
		
		for(int i = 0; i < nodes; i++) {
			
			for(int j = 0; j < nodes; j++) {
				
				System.out.print("["+arcs[i][j].adj+"]\t");
			}
			System.out.println();
		}
		System.out.println("------------------------------------");
	}

	/**
	 *Imprime a topologia, dado o n�mero de n�s. (matriz [g]).
	 */
	
	public void printWeights() {
		
		System.out.println("Network Topology [g]");
		System.out.println("------------------------------------");
		
		for(int i = 0; i < nodes; i++) {
			
			for(int j = 0; j < nodes; j++) {
				
				System.out.print("["+arcs[i][j].weight+"]\t");
			}
			System.out.println();
		}
		System.out.println("------------------------------------");
	}

	/**
	 *get para numero de links/arestas.
	 */
	public int getNumLinks() {
		
		int links = 0;
		
		for(int i = 0; i < nodes; i++) {
			
			for(int j = 0; j < nodes; j++) {
				
				links += arcs[i][j].adj;
			}
		}
		
		return ((int)links/2);
	}
	

	/**
	 *Preenche o vetor de links i,j,i,j,....
	 */
	public DoublyLinkedList getLinks() {
		
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
