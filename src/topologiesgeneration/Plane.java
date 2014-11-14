package topologiesgeneration;
import java.util.Random;
import java.lang.Math;
import java.security.SecureRandom;
/**
 *Funções para o Plano.
 */

public class Plane {

	int width;
	int height;
	int side;
	int X ; 				//lados do plano
	int heightR, widthR;	//altura e largura em unidade de cada regiao
	int[][]area ;
	int []nodos ;			//guarda região que o nó pertence 
	DoublyLinkedList nPos;
	DoublyLinkedList region;
	DoublyLinkedList nNodesReg;
	int nNodes;
	int minDegree;
	int maxDegree;
	
	/**
	 *Atribui valor a area do plano quadrado. Construtor
	 */
	public Plane(int width,int height,int n,int side,int min, int max,int nNodes) {
		
		X = side;
		this.area = new int[side][side];
		this.nNodes = nNodes;
		
		this.nPos = new DoublyLinkedList();				//posicao dos nos na regiao
		this.region = new DoublyLinkedList();			//limites de cada regiao
		this.nNodesReg = new DoublyLinkedList();		//numero de nos em cada regiao
			
		if(n == 0)
		{
			this.width = width;
			this.height = height;
		}

		this.initPlan();
		this.minDegree = min;
		this.maxDegree = max;
	}
	/**
	 *Inicializa o plano atribuindo zero a todas as posições.
	 */
	public void initPlan() { 						// n = lado de um quadrado de uma área N X N
		
		for(int i = 0; i < side-1; i++) { 
			
			for(int j = i+1;j < side; j++) {
				
				area[i][j] = area[j][i] = 0;  
			}
		}
	}
	
	/**
	 *Imprime Plano.
	 */
	
	public void printPlan() {
		
		int n = X;
		// System.out.println("--------------- Plan ---------------");
		// System.out.println("------------------------------------");
		
		for(int i = 0; i < n; i++) {
			
			for(int j = 0;j < n;j++) {
				
				System.out.print("\t"+area[i][j]+"\t");
			}
			System.out.println();
		}
		// System.out.println("------------------------------------");
	}
	
	/**
	 *set nodos.
	 */
	
	public void setNode(int i, int j, int Y) {

		for(int u = i-Y; u <= (i+Y); u++) {
			
			for(int v = j-Y; v <= j+Y; v++) {
				
				if(u >= 0 && v >= 0 && u < X && v < X)
				{
					if(area[u][v] != 1) // para não colocar o 2 em cima de um nó já inserido
					{
						area[u][v] = 2; //cria a zona de bloqueio
					}
				}
			}
		}
		
		area[i][j] = 1; //coloca o nó na posição i,j
		nPos.addTwo(i,j); //coloca a posição i e j do nó
 	}
 	
	
	public void setRegions(int R, int Y, int S, int N)  throws Exception{

		int X = area.length;
		int primos[] = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71}; // 20 números
		
		boolean prime = false; // verifica se é primo
		int temp = 0;
		DoublyLinkedList fact = new DoublyLinkedList(); // lista que irá guardar os numeros primos divisiveis pelos números de regiões
		int num = 3;
		boolean nextPar = true;
		int auxR = R;
		int A = 1;
		int Sa = S;
		int auxF = 0;

		//busca na lista primos para verificar se o número de regiões é primo
		while(prime == false && temp < 20){
            if(R == primos[temp])
                prime = true;
            else temp = temp + 1;
		}
		if(prime == true){ //se o R for um número primo
			widthR = X;     //a largura de cada região é a largura do plano
			heightR = (int)Math.floor(X/R); //a altura é o menor inteiro da divisão entre o tamanho do plano e o número de regiões
		}
		else
		{ //se o numero de regiões não for primo é necessário factorizá-lo
    		if(auxR == 1)
    			fact.addNode(1);
    	    else{
    	    	while(nextPar == true){ //decomposição de todos os números pares
    	    		if( auxR % 2 == 0){
    	    			auxR = auxR/2; // se auxR for um numero par, só receberá o numero dois no fact
						fact.addNode(2); //inserção no vector fact
    	    		}
    	    	    else{
    	    	    	nextPar = false;
    	    	    }
    	    	}
    	    	while(auxR > 1){  //factorização dos números impares
    	    			if(auxR % num == 0){
    	    				if(auxR != auxF){
								fact.addNode(num); //inserção do primo na lista
								auxF = num;
							}
							auxR = auxR/num;
    	    			}
    	    		    else{
    	    		    	num = num + 2; //avançar para o primo seguinte
    	    		    } 
    	    	} 
			}

			int auxA = (int) Math.ceil(fact.size()/2);
    	    for(int tempA = 0; tempA < auxA; tempA++){
				if(fact.retElement(tempA)!=0){
					A = A * fact.retElement(tempA);  //para decompor em dois números de igual tamanho multiplicaremos metade da lista fact
				}
    	   	}
			int K = R/A;
			widthR = (int) Math.floor(X/A); //largura de cada regiao
			heightR = (int) Math.floor(X/K); //altura de cada regiao
		}
		// System.out.println("R"+R);
		limit(heightR, widthR, R, A);  //determina os limites das regioes
		setNodeReg(heightR, widthR, R, Y, Sa, N); //determina o número de nós por região
		sortNpos(R, Y); //cria lista auxiliar para sortear a posicao dos nodos.
	} 		
	
	/**
	 *set para Região.
	 */
	
	public void setRegion(int R, int Y, int S, int N)throws Exception {
	
		widthR = (int) Math.floor(X/height); 	//largura de cada regiao 
		heightR = (int) Math.floor(X/width); 	//altura de cada regiao

		
		limit(heightR, widthR, R, height);  			//determina os limites das regioes
		setNodeReg(heightR, widthR, R, Y, S, N); 	//determina o número de nós por região

		sortNpos(R, Y); //auxiliar para sortear nós
	} 		
	
	/*
	 *Limite para Região 
	 */
	
	private void limit(int hR, int wR, int R, int A) {
		
		int K = (int)Math.floor(R/A);
	
		int temp = 1;
		@SuppressWarnings("unused")
		int auxMinI, auxMaxI, auxMinJ, auxMaxJ;
		
		if(wR == X)
		{ 						//se a divisão for apenas num sentido
      		region.addNode(0); 			//minimo valor possivel para i
      		region.addNode(X-1); 		//maximo valor possivel para i
      		region.addNode(0); 			//minimo valor possivel para j
      		region.addNode(hR-1); 		//maximo valor possivel para j
      		
      		int auxMax = (hR-1);
      		temp = temp + 1;
      		
  			while(temp <= R) 
  			{ 
  				//limites para as  regioes restantes
  				
  				region.addNode(0); 				//min i
  				region.addNode(X-1); 			//max i
  				
  				region.addNode(auxMax + 1);  	//min j
  				region.addNode((temp*hR)-1); 	//max j
  				
  				auxMax = (temp*hR)-1;
  				temp = temp + 1;
      		}
      }
      else
      { 
    	  // System.out.println("limit bbb");
    	  	//a divisao vai ocorrer nos dois sentidos
      		region.addNode(0);
      		auxMinI = 0;
      		
      		region.addNode(wR - 1);
      		
      		auxMaxI = wR - 1;
      		
      		region.addNode(0);
      		
      		auxMinJ = 0;
      		
      		region.addNode(hR - 1);
      		
      		auxMaxJ = hR - 1;
      		temp = temp + 1;
      		
  			for(int tempK = 1; tempK <= K; tempK++) { 
  				
  				//determinação dos limites nas regioes horizontais
  				for(int tempA = 1; tempA < A; tempA++) { //determinação dos limites nas regioes verticais
  					
  					region.addNode(auxMaxI+1);
  					auxMinI = auxMaxI+1;
  					region.addNode(((tempA+1)*wR)-1);
  					auxMaxI = ((tempA+1)*wR)-1;
  					
 					region.addNode(auxMinJ);
 					region.addNode(auxMaxJ);
 					
  					if(temp == R) {

  						break;
  					}
  						
  					else
  					{
  						temp++;
  					}
  						
  				}
  				if(temp == R)
  				{
  						break;
  				}
  				else
  				{
					region.addNode(0); //nesta altura inicia a segunda linha de regiões
					auxMinI = 0;
					region.addNode(wR - 1);
					auxMaxI = wR - 1;
					region.addNode(((tempK)*hR));
					auxMinJ = ((tempK)*hR);
					region.addNode(((tempK+1)*hR)-1);
					auxMaxJ = ((tempK+1)*hR)-1;
					temp++;
  				}
  			}
      }
		
		// for(int i=0;i < R*4;i++)System.out.println("limit "+region.retElement(i));
		
	}
	

	/**
	 *Set valores para uma Região.
	 */
	private void setNodeReg(int hR, int wR, int R, int Y, int S, int N) throws Exception{// mostra quantos nós haverá em cada região
		
			int missing = N; // N = número de nós; S= distribuição de nós por região
			int allocated = 0;
			boolean all = false;
			int auxR = 0,auxN = 0;
			int randInt = 0;
			
			int max = (int)Math.floor((hR*wR)/(4*Y+4*Y*Y+1)); //maximo de nos que cabem em cada regiao
			// System.out.println("max "+max);
			/**
			 * Verifica se o máximo é menor que 1 ou seja 0
			 * e que o número de nós é maior que 0
			 * então o máximo passa a ser 1
			 */
			if(max < 1 && missing > 0) 
			{
				max = 1;
			}
			if((max*R) >= N)
			{ 
				//entao os nós cabem nas regioes
				
				/**
				 * então o número de nós por região será igual, um nó por região
				 */
				
				if(S == 0)
				{ 
					for(auxR = 0; auxR < R; auxR++){

						nNodesReg.addNode((int)Math.floor(N/R)); //determinação do número de nós
						
						allocated = allocated + (int)Math.floor(N/R);
					}
					if(allocated < N)
					{ 
						/**
						 * se sobrarem nós serão colocados um a um em cada uma das regiões
						 */
						while(all == false) {
							
							auxR = 0;
							
							while(allocated < N && auxR < R) {
								
								auxR = random(0,R-1);
								
								if(auxR < R)
								{
									nNodesReg.update(auxR,1);
									allocated = allocated+1;
								}
								
							}
							if(allocated == N)
							{
								all = true;
								break;
							}
						}
					}
				}
				else
				{ 
					// System.out.println("Distribuição aleatoria dos nós");
					/**
					 * a colocacao dos nós sera aleatória
					 * inicialmente o número de nós por região será 0 (zero)
					 */
					int j = 0;
					
					while(j < R) {
						
						nNodesReg.addNode(0);
						j++;
					}
					
					while(missing > 0) {
						
						
						for(auxR = 0; auxR < R; auxR++){
			
							if(N <= max)
							{ 
								//entao cabem todos na regiao
								if(missing == 1)
								{
									//falta um nó somente
									nNodesReg.update(auxR,1);
									missing = missing-1;
								}
								else
								{
									
									randInt = random(0,missing);
									nNodesReg.update(auxR,randInt);
									missing = missing - randInt;
								}
								if(missing == 0)
								{	
									break;
								}
							}
							else
							{ 
								
								//nao cabem todos os nós na regiao
								
								auxN = nNodesReg.retElement(auxR);
								auxN = max-auxN;
								
								if(auxN == 1)
								{
									//só falta colocar 1 para atingir o máximo
									if(nNodesReg.update(auxR,1) != 0)
									{
										nNodesReg.update(auxR,1);
									}
									missing = missing -1;
								}
								else
								{
									
	                 				randInt = random(0,auxN); //pode ficar em ciclo aqui
	                 				
	                        		if( (missing - randInt) >= 0 )
	                        		{
										nNodesReg.update(auxR,randInt);
	                            		missing = missing - randInt;
	                        		}
	                        		else
	                        		{
										nNodesReg.update(auxR,missing);
	                        			missing = 0;
	                        		}
								}
								if(missing == 0)
								{
									break;
								}
								
							}
							if(missing == 0)
							{
								break;
							}
						}
					}
				}
			}
			else
			{ //entao os nós NAO cabem nas regioes
				
				throw new Exception("The nodes do not fit in the regions!!");	
				
			}
		
	}
	
	/**
	 *Valores randômicos apartir dos limites de maximo e minimo determinado como entrada.
	 */
	
	public int random(int min, int max) {
		
		int nRand;
		
		Random rn = new SecureRandom(); 
		
		if(max != 0 && max > 0)
		{
			max = max+1;
			if (min == 0)
			{
				nRand = min + rn.nextInt(max);
				return nRand;
			}
			else
			{
				nRand = min + (int)((max-min)*Math.random());
				return nRand;
			}
		}
		else
		{
			return 0;
		}
	}

	/**
	 *Ordena Posição dos nós.
	 */
	
	private void sortNpos(int R, int Y) throws Exception{
		
		int rand, u1, u2, a,b,x, listSize = 0,p = 0, blockSize;
		
		DoublyLinkedList[] plan = new DoublyLinkedList[R];
		DoublyLinkedList toBlock = new DoublyLinkedList(); // para "bloquear" as áreas de outras regiões
				
		for(int c = 0; c < R ; c++){  
			
			plan[c] = new DoublyLinkedList(); //inicializa as listas 
		} 

		
		for(int auxR = 0; auxR < R; auxR++) {//para todas as regioes
		
			for(int k = region.retElement(p+2); k <= region.retElement(p+3); k++) { //linhas
				
				for(int j = region.retElement(p);j <= region.retElement(p+1); j++){ //colunas
					
					plan[auxR].addTwo(k,j);
				}
			}
			
			if(nNodesReg.retElement(auxR) > 0)
			{
//				System.out.println("nodos por região "+nNodesReg.retElement(auxR));
				//se tiver algum nó pra inserir na regiao
				if(toBlock.empty() != 1)
				{ 

					// verifica se tem elementos em toBlock
					blockSize = toBlock.size();
					
					x = 0;
					
					for(int i = 0;i <= blockSize; i++) {
						
						a = toBlock.retElementOne(x);						
						b = toBlock.retElementTwo(x);
						// verifica se há algum elemente da região que está numa área que não pode ser adicionado nó
						if(plan[auxR].searchTwo(a,b) == true)
						{ 								
							plan[auxR].removeNode(a,b);//se estiver remove a região da lista	
							toBlock.removeNode(a,b);// e remove de toBlock
						}
						else
						{
							x++;// se não estiver, continua procurando na lista do toBlock se há outras área da região auxR bloqueados								// se não estiver, continua procurando na lista do toBlock se há outras área da região auxR bloqueados	
						}					
					}
				}		
				
				for(int auxNno = 0; auxNno < nNodesReg.retElement(auxR); auxNno++) {//para o numero de nos de cada regiao
					
					
					listSize = (int)(plan[auxR].size())-1;
					
					rand = random(0,listSize);//sorteia uma
					// System.out.println("auxR "+auxR+" n"+rand);
//					this.nodos[auxR][rand] = 1;
					
					u1 = plan[auxR].retElementOne(rand);//determina posicao i do no a colocar
					u2 = plan[auxR].retElementTwo(rand);//determina posicao j do no a colocar	
					
					setNode(u1,u2,Y);	//coloca o nodo
						
					for(int temp = u1-Y; temp <= u1+Y; temp++){
						// remover da lista a posição escolhida e os que estão ao redor da posição
							
						for(int temp1 = u2-Y; temp1 <= u2+Y; temp1++) {
							
							if(temp > region.retElement(p+3) || temp1 > region.retElement(p+1) || temp < region.retElement(p+2) || temp1 < region.retElement(p))
							{
								if(temp >=0 && temp1 >= 0 && temp < X && temp1 < X)
								{
									toBlock.addTwo(temp,temp1); // adiciona na área bloqueada
								}
							}
							if(temp1 >= region.retElement(p) && temp >= region.retElement(p+2) && temp1<= region.retElement(p+1) && temp <= region.retElement(p+3))
							{
								plan[auxR].removeNode(temp,temp1);
							}
						}
					}
				}
			}	
			
			for(int k = region.retElement(p+2);k <= region.retElement(p+3); k++) { //linhas
				
				for(int j = region.retElement(p);j <= region.retElement(p+1); j++) { //colunas
					plan[auxR].removeNode(k,j);
				}
			}
			p+=4;
		}	
		if(toBlock.empty() != 1)
		{
			// se toBlock não estiver vazia
			blockSize = toBlock.size();
			
			for(int i = 0;i < blockSize; i++) {
				x = 0;
				a = toBlock.retElementOne(x);
				b = toBlock.retElementTwo(x);

				toBlock.removeNode(a,b);// é removido todos os seus elementos
			}					
		}			
	}

	/**
	 * Gera valor randomico em ponto flutuante.
	 */
	public double randomD(int min, int max) throws Exception{
		
		Random rn = new Random(); 
		
		if(max != 0)
		{
			return ( ((double)Math.random()/ ((double)rn.nextInt(max)+(double)(1))) );
		}
		else
		{
			return 0;
		}
	}
	/**
	 *Calcula a distancia Euclidiana entre dois pares de vértices/nós.
	 */
	public int getEuclidianDistance(int u1, int u2, int v1, int v2) throws Exception{
		
		int d = (int)Math.sqrt((u1-v1)*(u1-v1) + (u2 - v2)*(u2 - v2));
		return d;
	}
	
	public void link(graph rede,int i, int j,int mSpath, int dist) {
		
		if(i >= rede.getNumberNodes() || j >= rede.getNumberNodes() ) return;
		
		if(rede.getDegree(i) < this.maxDegree && rede.getDegree(j) < this.maxDegree && rede.getArc(i,j) == 1)
		{
			if(mSpath == 0)
			{ 
				//entao h medio será em distancia	
				rede.setArc(i,j,dist);
			}
			else
			{ 
				//entao h medio será em hops
				rede.setArc(i,j,1);
			}
		}
		
		return;
	}

	/**
	 *Probabilidade de Waxman.
	 */
	public int waxman(graph rede, int i, int j, int d, double alfa, double beta, double L, int mSpath) throws Exception{
	
		double probWax = (alfa*(double)Math.exp(-d/(L*beta))); // alpha*exp(-d/(beta*L));

		double temp = randomD(0,1)*0.50f;

		if(i != j)
		{

			if(probWax >= temp)
			{
				if(mSpath == 0)
				{ 
					//entao h medio será em distancia	
					rede.setArc(i,j,d);
				}
				else
				{ 
					//entao h medio será em hops
					rede.setArc(i,j,1);
				}
				return 0;//colocou link
			}
			else
			{
				return 1;//nao colocou link
			}
		}
		
		return 1;//nao colocou pq origem = destino
		
	} 

	public int randomNode(graph rede) {
		
		int node = 0;
		int N = this.nNodes;
		
		int [] nos = new int[N];
		
		int n = 0;
		
		for(int i = 0; i < N ;i++)
		{
			int grau = rede.getDegree(i);
			
			if( grau < this.minDegree)
			{
				nos[n]  = i;
				n++;
			}
		}
		int [] ar = new int[n];
		
		for(int i = 0; i < n ;i++){
			
			ar[i]  = nos[i];
		}
		/**
		* shuffleArray
		 */
		Random rnd = new SecureRandom(); 
		
	    for (int i = ar.length-1; i > 0; i--)
	    {
		      int index = rnd.nextInt(i+1);
		      // Simple swap
			  int a = ar[index];
			  ar[index] = ar[i];
			  ar[i] = a;
		}
	   if(n > 0)  node = ar[random(0,n-1)];
	    
	   return node; 
	}
	
	/**
	 *Set link/aresta
	 */
	public int setLink(graph rede, int L, double alfa, double beta, int mSpath,int controleGrau) throws Exception{

		int auxSrc = -1, auxDst = -1;
		int u1 = 0, u2 = 0, v1 = 0, v2 = 0;
		int grauOrigem = -1,grauDestino = -1;
		int N = rede.getNumberNodes();
		int count = 0;
		
		
		int [] nos = new int[N];
		
		int nMin = 0,n = 0;
		
		for(int i = 0; i < N ;i++)
		{
			int grau = rede.getDegree(i);
			
			if( grau < this.maxDegree)
			{
				nos[n]  = i;
				n++;
			}
			
			if(grau >= this.minDegree)
			{
				nMin++;
			}
		}
		
		int [] ar = new int[n];
		
		for(int i = 0; i < n ;i++){
			
			ar[i]  = nos[i];
			
		}
		
		if(n == 1 && rede.getDegree(ar[0]) < this.minDegree)
		{
			auxSrc = ar[0];
			
			int grau = this.minDegree-1;
			
			auxDst = auxSrc;
			u1 = nPos.retElementOne(auxSrc);//coordenada i da origem
			u2 = nPos.retElementTwo(auxSrc);//coordenada j da origem
			
			while(grau < this.maxDegree || auxDst == auxSrc) 
			{
				
				for(int i = 0; i < N; i++) {
					
					if(rede.getDegree(i) == grau && rede.getArc(auxSrc, i) == 1 && i != auxSrc)
					{
						auxDst = i;
						v1 = nPos.retElementOne(auxDst);//coordenada i do destino
						v2 = nPos.retElementTwo(auxDst);//coordenada j do destino
						
						int dist = getEuclidianDistance(u1, u2, v1, v2);
						
						if(mSpath == 0)
						{ 
							//entao h medio será em distancia	
							rede.setArc(auxSrc,auxDst,dist);
						}
						else
						{ 
							//entao h medio será em hops
							rede.setArc(auxSrc,auxDst,1);
						}
						
						break;
					}
				}
				
				grau++;
			}
			
		}
		else
		{
			
			int max = (this.maxDegree*rede.getNumberNodes())/2;
			
			if(rede.getNumLinks() == max-1)
			{
				auxSrc = auxDst = 0;
				
				while(auxSrc == auxDst || rede.getArc(auxSrc, auxDst) == 0 ) {
					
					auxSrc = random(0,N-1);
					
					auxDst = random(0,N-1);
				}
				
				u1 = nPos.retElementOne(auxSrc);//coordenada i da origem
				u2 = nPos.retElementTwo(auxSrc);//coordenada j da origem
				v1 = nPos.retElementOne(auxDst);//coordenada i do destino
				v2 = nPos.retElementTwo(auxDst);//coordenada j do destino
				
				int dist = getEuclidianDistance(u1, u2, v1, v2);
				
				link(rede,auxSrc,auxDst,mSpath,dist);
			}	
			
			if(ar.length == 2)
			{
				u1 = nPos.retElementOne(ar[0]);//coordenada i da origem
				u2 = nPos.retElementTwo(ar[0]);//coordenada j da origem
				v1 = nPos.retElementOne(ar[1]);//coordenada i do destino
				v2 = nPos.retElementTwo(ar[1]);//coordenada j do destino
				
				int dist = getEuclidianDistance(u1, u2, v1, v2);
				
				if(rede.getArc(ar[0], ar[1]) == 0)
				{
					return -1;
				}
				else
				{
					if(mSpath == 0)
					{ 
						//entao h medio será em distancia	
						rede.setArc(ar[0],ar[1],dist);
					}
					else
					{ 
						//entao h medio será em hops
						rede.setArc(ar[0],ar[1],1);
					}
					
				}
			}
			else
			{
				/**
				* shuffleArray
				 */
				Random rnd = new SecureRandom(); 
				
			    for (int i = ar.length-1; i > 0; i--)
			    {
				      int index = rnd.nextInt(i+1);
				      // Simple swap
					  int a = ar[index];
					  ar[index] = ar[i];
					  ar[i] = a;
				}
			
				if(grauOrigem  == -1 )
				{
					count = 0;
					
					while(count < n) 
					{
						//enquanto a origem n for diferente do destino
						auxSrc =ar[count];
						
						if(rede.getDegree(auxSrc) < this.maxDegree && auxDst !=  auxSrc)
						{
							grauOrigem = rede.getDegree(auxSrc);
							
							break;
						}

						count++;
					}
				}
				
				
				if(grauDestino == -1)
				{
					count = 0;
					
					while(count < n) 
					{
						//enquanto a origem n for diferente do destino
						auxDst = ar[count];//selecciona um destino

						if(rede.getDegree(auxDst) < this.maxDegree && rede.getArc(auxSrc, auxDst) == 1 &&  auxSrc != auxDst)
						{

							grauDestino = rede.getDegree(auxDst);
							break;
						}
						count++;
					}	
				}

				if(auxSrc == -1 || auxDst == -1) return -1;
				
				if(auxSrc != auxDst && rede.getArc(auxSrc, auxDst) == 1)
				{
//					System.out.println("procura destino src "+auxSrc+" dst"+auxDst);
					u1 = nPos.retElementOne(auxSrc);//coordenada i da origem
					u2 = nPos.retElementTwo(auxSrc);//coordenada j da origem
					v1 = nPos.retElementOne(auxDst);//coordenada i do destino
					v2 = nPos.retElementTwo(auxDst);//coordenada j do destino
					
					int dist = getEuclidianDistance(u1, u2, v1, v2);
					
					link(rede,auxSrc,auxDst,mSpath,dist);
					
				}
			}
			
		}

		nMin = 0;
		
		for(int i = N-1; i >= 0 ;i--)
		{	
			if(rede.getDegree(i) >= this.minDegree)
			{
				nMin++;
			}
		}
		
		return nMin;
	}
	
	public int buscaVerticeMaisProximo(int origem, int minimo, int maximo, graph rede)throws Exception {
		/*
		 * Obtem o valor das coordenadas dos nodos
		 */
		// System.out.println("NODE");
		int destino = origem;
		int destinoAtual = origem;
		int aux = 0;
		int [] nos = new int[nNodes];
		
		nos[origem] = 1;
		int distAnt = 9999; /*controle*/
		
		/**
		 * Busca nó mais próximo e que já não foi ligado
		 */
		while(aux < nNodes) {
			
			destinoAtual = randomNode(rede);
			
			int u1 = nPos.retElementOne(origem);//coordenada i da origem
			int u2 = nPos.retElementTwo(origem);//coordenada j da origem
			int v1 = nPos.retElementOne(destinoAtual);//coordenada i do destino
			int v2 = nPos.retElementTwo(destinoAtual);//coordenada j do destino
			
			int dist = getEuclidianDistance(u1,u2,v1,v2);
			
			if(distAnt > dist && rede.getArc(origem, destinoAtual) == 1 && rede.getDegree(destinoAtual) < this.maxDegree && destinoAtual != origem && nodos[destinoAtual] != nodos[origem])
			{
				distAnt = dist;
				destino = destinoAtual;
			}
			
			aux++;
		}
		// System.out.println("destino "+destino+" origem "+origem);
		return destino;
	}

	/**
	 * Verifica se nó é adjacente
	 * retorna valor do nó destino se for adjacente do contrario origem
	 */
	public int adjacencia(int destino, int origem) throws Exception{
		
		/*
		 * Obtem o valor das coordenadas dos nodos
		 */
		int xOrigem = nPos.retElementOne(origem);

		int yOrigem = nPos.retElementTwo(origem);
		
		int xDestino = nPos.retElementOne(destino);
		int yDestino = nPos.retElementTwo(destino);
		
		
		int difx = Math.abs(xOrigem-xDestino);
		int dify = Math.abs(yOrigem-yDestino);
		
		if(difx <= 1 && dify <= 1)
		{
			return destino;
		}
		
		return origem; /*vertice destino não é adjacente*/
	}
	/*
	 * busca nó mais próximo ao passado (origem)
	 * retorna um vetor com as coordenadas x,y do nó
	 */
	public int nearestNode(int origem, int minimo, int maximo, graph rede) throws Exception {
	
		
		if(rede.getDegree(origem) == this.maxDegree) return origem;

		/*
		* Caso não encontre nó  adjacente vai buscar o mais próximo da origem
		* faz expanção da busca a partir da origem 
		*/
		return buscaVerticeMaisProximo(origem,minimo,maximo,rede);
		
	}
	/**
	 *Set aneis.
	 */
	public void setRing(graph rede, int N, int R, int L, int mSpath) throws Exception{
		
		this.nNodes = N;
		int u1, u2, v1, v2;
		int dist;
		int src = 0, dst = 0;
		
		int []origens = new int [N];
		int []destinos = new int [N];
		
		
		for(int j = 0; j < N; j++)
		{
			origens[j] = 0;
			destinos[j] = 0;
		}
		//cria uma lista com o numero do no
		
		
		this.nodos = new int [nNodes];
		
		int []nodesReg = new int [R];
		
		for(int j = 0; j < R; j++) {
			
			nodesReg[j] = 0;
		}
		
		int nP =  R*4;
		int r = 0,i = 0;
		int fim = 0;
		
		while(i < nP) {
			
			/**
			 * Pega as coordenadas x e y iniciais e finais de cada nó
			 */
			int x_init = region.retElement(i);
			i++;
			int x_fim = region.retElement(i);
			i++;
			int y_init = region.retElement(i);
			i++;
			int y_fim = region.retElement(i);
			
			
			for(int j = 0; j < N; j++) {
				
				int xi   = nPos.retElementOne(j);
				int yi   = nPos.retElementTwo(j);
				
				/**
				 * Verifica qual região pertence o nó
				 */
				if(xi >= x_init && xi <= x_fim && yi >= y_init && yi <= y_fim) 
				{
					this.nodos[j] = r;
					nodesReg[r] = nodesReg[r]+1;
				}
			}
			r++;
			i++;
			
		}
		int n = 0;
		
		for(int auxR = 0; auxR < R; auxR++){//para cada regiao
			
			n = 0;
			
			if(nodesReg[auxR] > 0)
			{
				/**
				 * Realiza a ligação entre dois ou mais nós
				 */
				if( nodesReg[auxR] >= 2)
				{
					for(int k = 0; k < nodesReg[auxR];k++){//para cada no da regiao
						
						if( k == 0 || origens[src] == 1)
						{
							while(true)
							{
								
								src = random(0,this.nNodes-1);
								
								if(nodos[src] == auxR && rede.getDegree(src) < this.minDegree)
								{
									fim = src;	
									break;
								}
							}
						}
						
						dst = src;
						
						if(origens[src] == 1)
						{
							continue;
						}
						
						while(true)
						{
							int link = 1;
							
							dst = random(0,this.nNodes-1);
							
							if(dst == fim && n < nodesReg[auxR]-1) 
							{
								continue;
							}
							else if(dst == fim && n == nodesReg[auxR]-1)
							{
								u1 = nPos.retElementOne(src);
								u2 = nPos.retElementTwo(src);
								v1 = nPos.retElementOne(dst);
								v2 = nPos.retElementTwo(dst);
								dist = getEuclidianDistance(u1, u2, v1, v2);
								
								System.out.println("src "+src+" dst "+dst);
								link(rede,src,dst,mSpath,dist);
								
								break;
							}
								
							
							if(nodos[dst] == auxR && dst != src && rede.getDegree(dst) < this.minDegree && destinos[dst] == 0 && rede.getDegree(src) < this.minDegree && origens[src] == 0 && rede.getArc(src, dst) == 1)
							{
//								System.out.println("src "+src+" dst "+dst);
								
								u1 = nPos.retElementOne(src);
								u2 = nPos.retElementTwo(src);
								v1 = nPos.retElementOne(dst);
								v2 = nPos.retElementTwo(dst);
								
								dist = getEuclidianDistance(u1, u2, v1, v2);
								
								link = waxman(rede,src,dst,dist,1,1,L,mSpath);//enquanto n inserir um link
								
							}
							
							if(link == 0) 
							{
								System.out.println("src "+src+" dst "+dst);
								destinos[dst] = 1;
								n++;
								break;
							}
						}
						
						if(origens[src] == 0) 
						{
							origens[src] = 1;
						}
					
						
						src = dst;//src recebe a nova origem
						
					}
				}
			}
		} 
	}

	/*
	 *Interliga os anéis em cada região.
	*/	
	
	public void setRingReg(graph rede, int N, int L, int mSpath,int maxLinks) throws Exception {

		int R = (int)nNodesReg.size();
		
		
		this.nodos = new int [nNodes];
	
		int i = 0;
		
		int nP =  R*4;
		int r = 0;
		
		while(i < nP) {
			
			/**
			 * Pega as coordenadas x e y iniciais e finais de cada nó
			 */
			int x_init = region.retElement(i);
			i++;
			int x_fim = region.retElement(i);
			i++;
			int y_init = region.retElement(i);
			i++;
			int y_fim = region.retElement(i);
			
			
			for(int j = 0; j < N; j++) {
				
				int xi   = nPos.retElementOne(j);
				int yi   = nPos.retElementTwo(j);
				
				/**
				 * Verifica qual região pertence o nó
				 */
				if(xi >= x_init && xi <= x_fim && yi >= y_init && yi <= y_fim) 
				{
					// System.out.println("no "+j+" na região "+r);
					this.nodos[j] = r;
				}
			}
			r++;
			i++;
			
		}
		
		
		
		int []controle = new int[N];
		
		for(i = 0;i < N; i++) {
			controle[i] = 0;
		}
		
		int src,dst,u1,u2,v1,v2;
		
		for(i = 0; i < N; ) {
			
			if(rede.getNumLinks() == maxLinks) return;
			
			src = random(0,N-1);
			
			dst = nearestNode(src,0,N-1,rede);
			
			if(src != dst)
			{
				u1 = nPos.retElementOne(src); 		
				u2 = nPos.retElementTwo(src);		
				
				v1   = nPos.retElementOne(dst);
				v2   = nPos.retElementTwo(dst);
				
				int dist = getEuclidianDistance(u1, u2, v1, v2);
				
				link(rede,src, dst,mSpath, dist);
			}
			if(controle[src] == 0)
			{
				controle[src] = 1;
				i++;
			}
			if(controle[dst] == 0)
			{
				controle[dst] = 1;
				i++;
			}
			

		}
		
	}
}
