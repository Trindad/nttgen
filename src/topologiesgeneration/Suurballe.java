package topologiesgeneration;
public class Suurballe {
	DoublyLinkedList path1 = new DoublyLinkedList();
	DoublyLinkedList path2 = new DoublyLinkedList();
	DoublyLinkedList cust = new DoublyLinkedList(); 
	static DoublyLinkedList T1 = new DoublyLinkedList();
	static DoublyLinkedList  T2 = new DoublyLinkedList();
	DoublyLinkedList Mfim1 = new DoublyLinkedList();
	DoublyLinkedList Mfim2 = new DoublyLinkedList();

	/**
	 *Algoritmo de Dijkstra, procura o menor caminho dado um grafo.
	 */
	public int[] dijkstra(DoublyLinkedList top, int s, int N) {
		
		int []S = new int[N];
		int []p = new int[N];
		int []c = new int [N];
		int []nS = new int [N];
		int a = s, auxC = 0, topS = top.size(), cost=N;
		
		boolean v = true, flag = false;

		for(int k = 0;k < N; k++) {
			c[k] = N;
			S[k] =-1;
			p[k] =-1;
			nS[k]=99999; // usado quando não é encontrado caminho
		}
		
		c[a] = 0;
		S[a] = 1;
		
		while(v == true) {
			
			for(int k = 0;k < N; k++){
				if(S[k] == (-1) )
				{
					for(int i = 0;i < topS; i++){				
						if(top.retElementTwo(i) == k && top.retElementOne(i) == a)
						{
							if( c[k] > c[a]+top.retElementThree(i) )
							{
								c[k] = c[a]+top.retElementThree(i);
								p[k] = a;
							}
							flag = true;
						}
						if(flag == true) break;
					}
					flag = false;
				}
			}	
			
			cost = N;
			for(int i = 0;i < N;i++){
				if(S[i] != 1)
				{
					if(cost >= c[i])
					{ 
						cost = c[i];
						auxC = i; //recebe o nÃ³ do custo minimo
					}
				}
			}
			a = auxC;
			
			// verifica se jÃ¡ foi alterado o custo para todos os nÃ³s
			for(int i = 0; i < S.length; i++){
				if(S[i] == -1)
				{ // se nÃ£o foram, continua no laÃ§o
					v = true;
					break;
				}
				else
				{ // se todos foram, sai do laÃ§o
					v = false; 
				}
			}
			
			S[a] = 1;
	
		}	

		for(int k = 0;k < c.length; k++) {
			if(c[k] == N)
			{	
				
				return nS; // Retorna nS apenas para verificar que nÃ£o encontrou um segundo caminho
			}
		}
		
		for(int i=0; i<c.length; i++){
			cust.addNode(c[i]);
		}
		
		return p;// Retorna os precedentes
		
	}
	
	/**
	 * Faz a transformaÃ§Ã£o dos pesos dos nÃ³s para ser escolhido um segundo caminho diferente pelo Dijkstra	.
	 */
	public void transCost(DoublyLinkedList M1, int []c, DoublyLinkedList top, int d, int N) {
		
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
	public DoublyLinkedList pathM(int s,int d, int []p, int N) {
		
		DoublyLinkedList M = new DoublyLinkedList();
		int a = d;
		
		if(p[0] == 99999){
			return M;
		}
		
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
	 *  Verifica se existem dois caminhos disjuntos de um nÃ³ origem para o nÃ³ destino.
	 */
	public int pathDisj(DoublyLinkedList M1, DoublyLinkedList M2) {
		
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
			//System.out.println("Caminho 01: ");
			//P1.display2();
			//System.out.println("Caminho 02: ");
			//P2.display2();
		}
		else
		{
			return 2;// NÃ£o hÃ¡ dois caminhos disjuntos
		}
		
		if(P1.empty() != 1 && P2.empty() != 1)
		{
			for(int i = 0;i < k; i++) {
				
				for(j = 0;j < l; j++) {
					
					if(P1.retElementOne(i) == P2.retElementOne(j) && P1.retElementTwo(i) == P2.retElementTwo(j) || P1.retElementOne(i) == P2.retElementTwo(j) && P1.retElementTwo(i) == P2.retElementOne(j)){
						return 2;// NÃ£o hÃ¡ dois caminhos disjuntos
					}
				}
			}
		}
		
		Mfim1=P1;
		Mfim2=P2;
		T1 = new DoublyLinkedList();
		T2 = new DoublyLinkedList();
		T1=P1;
		T2=P2;
		
		return 1;
	}
	
	/**
	 * SequÃªncia de mÃ©todos que faz o proposito do algoritmo de Suurballe.
	 */
	public int algSuurballe(DoublyLinkedList top, int s, int d, int N) {
		
		DoublyLinkedList M1 = new DoublyLinkedList();
		DoublyLinkedList M2 = new DoublyLinkedList();
		int []pc = new int[N];
		int v, maxH=0;
	
		cust = new DoublyLinkedList();
		pc = dijkstra(top, s, N);
		M1 = pathM(s,d, pc, N);
		for(int i=0;i<cust.size();i++){
			pc[i]=cust.retElement(i);
		}
		cust = new DoublyLinkedList(); 
		transCost(M1,pc,top,d,N);
		pc = dijkstra(top,s,N);
		M2 = pathM(s,d,pc, N);
		v = pathDisj(M1,M2);
	
		int m1S = Mfim1.size();
		int m2S = Mfim2.size();
		
		if(m2S>=maxH){
			maxH=m2S;
		}
		
		for(int i=0;i<maxH-1; i++){
			
			Mfim1 = new DoublyLinkedList();
			Mfim2 = new DoublyLinkedList();
			v = pathDisj(M1,M2);
			M1=T1;
			M2=T2;
		}
		
		m1S = Mfim1.size();
		m2S = Mfim2.size();
		path1.addNode(m1S);
		path2.addNode(m2S);
		
		return v;
		
		
	}

	/**
	 * Verifica se existem dois caminhos para todos os nÃ³s.
	 */	
	public double[] twoPaths (DoublyLinkedList top, int N, DoublyLinkedList w,int op) {
		Measures m = new Measures ();
		double hs [] = new double [2];
		path1 = new DoublyLinkedList();
		path2 = new DoublyLinkedList();
		if(op==0){
			int a = 0;
			for(int i = 0;i < N; i++){
				for(int j = a;j < N; j++){
					initCost(top, w, N);
					if(i != j)
					{
						//System.out.println(i+"--->"+j);
						if(algSuurballe(top,i,j,N) == 2)
						{
							//System.out.println("No sem dois caminhos disjuntos!\n");
							hs[0]=999999;
							hs[1]=999999;
							return hs;
						}
					}
				}
				a++;
			}
		}else{
			int a = 0;
			for(int i = 0;i < N; i++){
				for(int j = a;j < N; j++){
					initCost(top, w, N);
					if(i != j)
					{
						//System.out.println(i+"--->"+j);
						algSuurballe(top,i,j,N);
					}
				}
				a++;
			}
		} 
//		System.out.println("***");
//		path1.displayOne();
//		System.out.println("---");
//		path2.displayOne();
		hs[0]=m.hAvg(path1);
		hs[1]=m.hAvg(path2);

		return hs;
	}	

	
	/**
	 *  Inicializa a topologia com o peso inicial.
	 */	
	public DoublyLinkedList initCost(DoublyLinkedList top, DoublyLinkedList w, int N) {
		
		int topS = w.size();
		
		for(int i = 0; i < topS; i++) {
			
			top.updateThree( i,w.retElement(i) );
		}
		return top;
	}

	/**
	 *  Salva o peso inicial da topologia.
	 */ 
	public DoublyLinkedList saveW(DoublyLinkedList top) {
		
		int topS = top.size();
		DoublyLinkedList w = new DoublyLinkedList();
		
		for(int i = 0; i < topS; i++) {
			w.addNode(top.retElementThree(i));
		}
		return w;
	}
}