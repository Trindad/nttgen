package topologiesgeneration;
public class Suurballe {

	/**
	 *Algoritmo de Dijkstra, procura o menor caminho dado um grafo.
	 */
	public int[] dijkstra(DoublyLinkedList top, int s, int d, int N, int aux) throws Exception{
		
		int []S = new int[N];
		int []p = new int[N];
		int []c = new int [N];
		int a = s, auxC = 0, topS = top.size(), cost=N;
//		System.out.println("topS"+topS);
		boolean v = true;

		for(int k = 0;k < N; k++) {
			c[k] = N;
		}	
		
		for(int k = 0;k < N; k++) {
			S[k] =-1;
		}
		
		for(int k = 0;k < N; k++) {
			p[k] =-1;
		}
		
		c[a] = 0;
		S[a] = 1;
		
		while(v == true) {
			
			for(int k = 0;k < N; k++){
				if(S[k] == (-1) )
				{
					
					for(int i = 0;i < topS; i++){	
//						System.out.println("----------------------------------------Inicio\n");
//						try{
//							if(i > N*2) {
//								throw new Exception();
//							}
//						}
//						catch(Exception e){
//							e.printStackTrace();
//						}
						if(top.retElementTwo(i) == k && top.retElementOne(i) == a)
						{
							if( c[k] > c[a]+top.retElementThree(i) )
							{
								c[k] = c[a]+top.retElementThree(i);
								p[k] = a;	
							}
						}
//						System.out.println("----------------------------------------Fim\n");
					}
				}
			}	
			
			cost = N;

			for(int i = 0;i < N;i++){
				if(S[i] != 1)
				{
					if(cost >= c[i])
					{ 
						cost = c[i];
						auxC = i; //recebe o nó do custo minimo
					}
				}
			}
			a = auxC;
			
			// verifica se já foi alterado o custo para todos os nós
			for(int i = 0; i < S.length; i++){
				if(S[i] == -1)
				{ // se não foram, continua no laço
					v = true;
					break;
				}
				else
				{ // se todos foram, sai do laço
					v = false; 
				}
			}
			
			S[a] = 1;
	
		}	

		for(int k = 0;k < c.length; k++) {
			if(c[k] == N)
			{	
				return S; // Retorna S apenas para verificar que não encontrou um segundo caminho
			}
		}
		
		if(aux == 1)
		{
			return p;// Retorna os precedentes
		}
		return c; // Retorna o custo
	}
	
	/**
	 * Faz a transformação dos pesos dos nós para ser escolhido um segundo caminho diferente pelo Dijkstra	.
	 */
	public void transCost(DoublyLinkedList M1, int []c, DoublyLinkedList top, int d, int N) throws Exception{
		
		int M1S = M1.size(), topS = top.size();
		int []status = new int[topS];
		
		for(int i = 0;i < topS; i++){
			status[i] = 0;
		}
		for(int i = 0;i < topS; i++) {
			
			for(int k = 0;k < M1S; k++){
				if( top.retElementOne(i) == M1.retElementOne(k) && top.retElementTwo(i) == M1.retElementTwo(k) )
				{
					
					status[i] = 1;
					break;
				}
				else if(top.retElementOne(i) == M1.retElementTwo(k) && top.retElementTwo(i) == M1.retElementOne(k))
				{
					status[i] = 2;
					break;
				}
			}
		}

		for(int i = 0;i < topS;i = i+2) {
			
			if(status[i] == 0)
			{
				top.updateTwo(i,c[top.retElementOne(i)]-c[top.retElementTwo(i)]);
				top.updateTwo(i+1,c[top.retElementOne(i+1)]-c[top.retElementTwo(i+1)]);
			}
			else if(status[i] == 1)
			{
				top.updateThree(i,N);
				top.updateThree(i+1,0);
			}
			else
			{
				top.updateThree(i,0);
				top.updateThree(i+1,N);
			}
		}
	}
	
	/**
	 *  Retorna o caminho sugerido pelo Dijkstra.	
	 */
	public DoublyLinkedList pathM(int s,int d, int []p, int N)throws Exception {
		
		DoublyLinkedList M = new DoublyLinkedList();
		int x = 0;
		
		for(int i = 0;i < p.length; i++) {
			
			if(p[i] == 1){
				x++;
			}
		}
		if(x == N)
		{
			return M;
		}
		
		int a = d;
		
		for(int i = 0;i < p.length;i++) {
			
			d = p[a];
			M.addTwo(d,a);		
			a = d;
			
			if(d == s)
			{
				break;
			}
		}
		
		return M;
	}

	/**
	 *  Verifica se existem dois caminhos disjuntos de um nó origem para o nó destino.
	 */
	public int pathDisj(DoublyLinkedList M1, DoublyLinkedList M2)throws Exception {
		
		int k = M1.size(),l = M2.size(), igual = 0,j = 0;
		DoublyLinkedList P1 = new DoublyLinkedList();
		DoublyLinkedList P2 = new DoublyLinkedList();
		
		if(M1.empty() != 1 && M2.empty() != 1) {
			
			for(int i = 0; i < k; i++) {
				
				while(igual == 0 && j < l) {
					if(M1.retElementOne(i) == M2.retElementOne(j) && M1.retElementTwo(i) == M2.retElementTwo(j) || M1.retElementOne(i) == M2.retElementTwo(j) && M1.retElementTwo(i) == M2.retElementOne(j))
					{
						igual = 1;
					}
					j++;
				}
				if(igual == 0)
				{
					P1.addTwo(M1.retElementOne(i), M1.retElementTwo(i));
					j = 0;
				}
				else
				{
					for(int q = j; q < l; q++) {
						
						P1.addTwo(M2.retElementOne(q), M2.retElementTwo(q));
					}
					igual = 0;
					i = k;
				}
			}

			k = M2.size();
			l = M1.size();
			
			for(int i = 0;i < k; i++){
				while(igual == 0 && j < l){
					if(M2.retElementOne(i) == M1.retElementOne(j) && M2.retElementTwo(i) == M1.retElementTwo(j) || M2.retElementOne(i) == M1.retElementTwo(j) && M2.retElementTwo(i) == M1.retElementOne(j)){
						igual = 1;
					}
					j++;
				}
				if(igual == 0)
				{
					P2.addTwo(M2.retElementOne(i), M2.retElementTwo(i));
					j = 0;
				}
				else
				{
					for(int q = j;q < l; q++){
						P2.addTwo(M1.retElementOne(q), M1.retElementTwo(q));
					}
					igual = 0;
					i = k;
				}
			}
		}
		else
		{
			return 2;// Não há dois caminhos disjuntos
		}
		
		if(P1.empty() != 1 && P2.empty() != 1)
		{
			for(int i = 0;i < k; i++) {
				
				for(j = 0;j < l; j++) {
					
					if(P1.retElementOne(i) == P2.retElementOne(j) && P1.retElementTwo(i) == P2.retElementTwo(j) || P1.retElementOne(i) == P2.retElementTwo(j) && P1.retElementTwo(i) == P2.retElementOne(j)){
						return 2;// Não há dois caminhos disjuntos
					}
				}
			}
		}
		
		return 1;
	}
	
	/**
	 * Sequência de métodos que faz o proposito do algoritmo de Suurballe.
	 */
	public int algSuurballe(DoublyLinkedList top, int s, int d, int N) throws Exception{
		
		DoublyLinkedList M1 = new DoublyLinkedList();
		DoublyLinkedList M2 = new DoublyLinkedList();
		int []pc = new int[N];
		int v;
	
		pc = dijkstra(top, s, d, N, 1);
		M1 = pathM(s,d, pc, N);
		pc = dijkstra(top, s, d, N, 2);
		transCost(M1,pc,top,d,N);
		pc = dijkstra(top,s,d,N, 1);
		M2 = pathM(s,d,pc, N);
		v = pathDisj(M1,M2);
	
		return v;
	}

	/**
	 * Verifica se existem dois caminhos para todos os nós.
	 */	
	public int twoPaths (DoublyLinkedList top, int N, DoublyLinkedList w)throws Exception {
		
		int a = 0;
		for(int i = 0;i < N; i++){
			for(int j = a;j < N; j++){
				initCost(top, w, N);
				if(i != j)
				{	
					if(algSuurballe(top,i,j,N) == 2)
					{

						System.out.println("Nó sem dois caminhos disjuntos!\t origem "+i+" destino "+j);
						return 0;
					}
					
				}
			}
			a++;
		}
		return 1;
	}	
	
	/**
	 *  Inicializa a topologia com o peso inicial.
	 */	
	public DoublyLinkedList initCost(DoublyLinkedList top, DoublyLinkedList w, int N) throws Exception{
		
		int topS = w.size();
		
		for(int i = 0; i < topS; i++) {
			
			top.updateThree( i,w.retElement(i) );
		}
		return top;
	}

	/**
	 *  Salva o peso inicial da topologia.
	 */ 
	public DoublyLinkedList saveW(DoublyLinkedList top) throws Exception {
		
		int topS = top.size();
		DoublyLinkedList w = new DoublyLinkedList();
		
		for(int i = 0; i < topS; i++) {
			w.addNode(top.retElementThree(i));
		}
		return w;
	}
}
