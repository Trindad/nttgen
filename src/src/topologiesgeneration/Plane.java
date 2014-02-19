package topologiesgeneration;
import java.util.Random;

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
	DoublyLinkedList nPos ;
	DoublyLinkedList region;
	DoublyLinkedList nNodesReg;
	int nNodes;
	
	/**
	 *Atribui valor a area do plano quadrado. Construtor
	 */
	public Plane(int width,int height,int side) {
		
		X = side;
		area = new int[side][side];				//plano
		nPos = new DoublyLinkedList();			//posicao dos nos na regiao
		region = new DoublyLinkedList();		//limites de cada regiao
		nNodesReg = new DoublyLinkedList();		//numero de nos em cada regiao
		
		this.width = width;
		this.height = height;
		
		this.initPlan();
	}
	/**
	 *Inicializa o plano atribuindo zero a todas as posições.
	 */
	public void initPlan() { 						// n = lado de um quadrado de uma área N X N
		
		for(int i = 0; i < side; i++) { 
			
			for(int j = 0;j < side;j++) {
				
				area[i][j] = 0;  
			}
		}
	}
	
	/**
	 *Imprime Plano.
	 */
	
	public void printPlan() {
		
		int n = X;
		System.out.println("--------------- Plan ---------------");
		System.out.println("------------------------------------");
		
		for(int i = 0; i < n; i++) {
			
			for(int j = 0;j < n;j++) {
				
				System.out.print(area[i][j]);
			}
			System.out.println();
		}
		System.out.println("------------------------------------");
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
 	
	/**
	 *set para Região.
	 */
	
	public void setRegion(int R, int Y, int S, int N)throws Exception {
		
			
		widthR = (int) Math.floor(X/width); 	//largura de cada regiao
		heightR = (int) Math.floor(X/height); 	//altura de cada regiao
		
		limit(heightR, widthR, R, width);  				//determina os limites das regioes
		setNodeReg(heightR, widthR, R, Y, S, N); 	//determina o número de nós por região
		sortNpos(R, Y);
	} 		
	
	/*
	 *Limite para Região 
	 */
	
	private void limit(int hR, int wR, int R, int A) {
		
		int K = height;
		//System.out.println("K: "+K);
		int temp = 1;
		@SuppressWarnings("unused")
		int auxMinI, auxMaxI, auxMinJ, auxMaxJ;
		
		if(wR == X)
		{ 								//se a divisão for apenas num sentido
      		region.addNode(0); 			//minimo valor possivel para i
      		region.addNode(X - 1); 		//maximo valor possivel para i
      		region.addNode(0); 			//minimo valor possivel para j
      		region.addNode(hR - 1); 	//maximo valor possivel para j
      		int auxMax = (hR - 1);
      		temp = temp + 1;
      		
  			while(temp <= R) { 
  				//limites para as restantes regioes
  				region.addNode(0); 				//min i
  				region.addNode(X - 1); 			//max i
  				region.addNode(auxMax + 1);  	//min j
  				region.addNode((temp*hR)-1); 	//max j
  				auxMax = (temp*hR)-1;
  				temp = temp + 1;
      		}
      }
      else
      { 
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
 					
  					if(temp == R)
  						
  						break;
  					else
  						temp++;
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
				
			/**
			 * Verifica se o máximo é menor que 1 ou seja 0
			 * e que o número de nós é maior que 0
			 * então o máximo passa a ser 1
			 */
			if(max < 1 && missing > 0) 
			{
				max = 1;
			}
			//System.out.println("max:"+max);
			if((max*R) >= N)
			{ 
				//entao os nós cabem nas regioes
				
				/**
				 * então o número de nós por região será igual, um nó por região
				 */
				
				if(S == 0)
				{ 
					//System.out.println("Distribuição uniforme");
					
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
								
								nNodesReg.update(auxR,1);
								allocated = allocated +1;
								auxR = auxR +1;
							}
							if(allocated == N)
							{
								all = true;
							}
						}
					}
				}
				else
				{ 
					System.out.println("Distribuição aleatoria dos nós");
					/**
					 * a colocacao dos nós sera aleatória
					 * inicialmente o número de nós por região será 0 (zero)
					 */
					int j = 0,count;
					
					while(j < R) {
						
						nNodesReg.addNode(0);
						j++;
					}
					
					while(missing > 0) {
						
						count = 0;
						
						while(count < R)
						{
							auxR = random(0,R-1);
							
							//System.out.println("AQUI RAN: "+auxR+"missing: "+missing);
							
							//System.out.println("N:"+N+"max: "+max);
							if(N <= max)
							{ 
								//System.out.println("Numero de nós é menor ou igual a max: "+max);
								//entao cabem todos na regiao
								if(missing == 1)
								{
									//falta um nó somente
									nNodesReg.update(auxR,1);
									missing-=1;
								}
								else
								{
									
									randInt = random(0, missing);
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
									if(nNodesReg.update(auxR,1)!= 0)
									{
										nNodesReg.update(auxR,1);
									}
									missing-=1;
								}
								else
								{
									
	                 				randInt = random(0, auxN); //pode ficar em ciclo aqui
	                 				
	                        		if( (missing - randInt) >= 0 )
	                        		{
										nNodesReg.update(auxR,randInt);
	                            		missing = missing - randInt;
	                        		}
	                        		else
	                        		{
										nNodesReg.update(auxR,missing);
	                        			missing = 0;
	                        			break;
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
		
		Random rn = new Random(); 
		
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
		
		int rand, u1, u2, a,b,x, listSize,p = 0, blockSize;
		
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
				//se tiver algum nó pra inserir na regiao
				if(toBlock.empty() != 1)
				{ 
					// verifica se tem elementos em toBlock
					
					blockSize = toBlock.size();
					x = 0;
					
					for(int i = 0;i <= blockSize; i++) {
						
						a = toBlock.retElementOne(x);
						b = toBlock.retElementTwo(x);
						
						if(plan[auxR].searchTwo(a,b) == true)
						{ 
															// verifica se há algum elemente da região que está numa área que não pode ser adicionado nó
							plan[auxR].removeNode(a,b);		// se estiver, remove a região da lista
							toBlock.removeNode(a,b);		// e remove de toBlock
						}
						else
						{
							x++;							// se não estiver, continua procurando na lista do toBlock se há outras área da região auxR bloqueados	
						}					
					}
				}		
				for(int auxNno = 0; auxNno < nNodesReg.retElement(auxR); auxNno++) {//para o numero de nos de cada regiao
					
					listSize = (plan[auxR].size())-1;
					
					//System.out.println("list Size: "+listSize);
					
					rand = random(0,listSize);//sorteia uma
//					System.out.println("Rand: "+rand);
					
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
	public double randomD(int min, int max){
		
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
	public int getEuclidianDistance(int u1, int u2, int v1, int v2){
		
		int d = (int)Math.sqrt((u1-v1)*(u1-v1) + (u2 - v2)*(u2 - v2));
		return d;
	}

	/**
	 *Probabilidade de Waxman.
	 */
	public int waxman(graph rede, int i, int j, int d, double alfa, double beta, double L, int mSpath){
		// L = comprimento máximo de um link
		double probWax = (alfa*(double)Math.exp(-d/(L*beta))); // alpha*exp(-d/(beta*L));

		double temp = randomD(0,1)*0.75f;

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
		else
		{
			return 1;//nao colocou pq origem = destino
		}
	} 
	
	/**
	 *Set link/aresta
	 */
	public void setLink(graph rede, int L, double alfa, double beta, int mSpath){
		
		int auxSrc, auxDst = 0;
		int u1 = 0, u2 = 0, v1 = 0, v2 = 0;
		boolean segue = false;
		int N = this.nNodes-1;
		
		auxSrc = random(0, N);//selecciona um nó de origem
		
		while(segue == false) {//enquanto a origem n for diferente do destino
			
			/**
			 * Para cada nó u verifica se v tem a menor distância comparado ao anterior, do contrário continua
			 */
			auxDst = random(0, N);//selecciona um destino
			
			if(auxSrc != auxDst)
			{
				u1 = nPos.retElementOne(auxSrc);//coordenada i da origem
				u2 = nPos.retElementTwo(auxSrc);//coordenada j da origem
				v1 = nPos.retElementOne(auxDst);//coordenada i do destino
				v2 = nPos.retElementTwo(auxDst);//coordenada j do destino
				segue = true;
			}
		}
		int dist = getEuclidianDistance(u1, u2, v1, v2);
		
		
		if(auxSrc < N && auxDst < N && auxSrc != auxDst)
		{
			waxman(rede,auxSrc,auxDst,dist,alfa,beta,L,mSpath);
		}
		else
		{
			setLink(rede,L,alfa,beta,mSpath);
		}
		
	}
	
	public int buscaVerticeMaisProximo(int origem, int minimo, int maximo) {
		
		/*
		 * Obtem o valor das coordenadas dos nodos
		 */
		int xOrigem = nPos.retElementOne(origem);
		int yOrigem = nPos.retElementTwo(origem);	
		
		int destino = origem;
		int destinoAtual = origem;
		int count = minimo;

		while(count <= maximo) {
			
			destinoAtual = random(minimo,maximo);
			
			int raioAtual = 0,raioAnterior = 1; /*controle*/
			
			int xDestino = nPos.retElementOne(destinoAtual);
			int yDestino = nPos.retElementTwo(destinoAtual);
			
			for(int k = 1; k <= this.X; k++) {
				
				for(int x =  xOrigem-k; x <= xOrigem+k; x++) {
					
					for(int y = yOrigem-k; y <= yOrigem+k; y++) {
						
						/*
						 * Testa se as coordenadas encontradas são iguais ao nó candidato a destino 
						*/
						if(xDestino == x && y == yDestino)
						{
							if(raioAtual < raioAnterior)
							{
								destino = destinoAtual;
								raioAnterior = raioAtual;
								
								break;
							}
							raioAtual++;
						}
						
					}
				}
			}
			count++;
		}
		//System.out.println("Dst: "+destino);
		return destino;
	}

	/**
	 * Verifica se nó é adjacente
	 * @return valor do nó destino se for adjacente do contrario origem
	 */
	public int adjacencia(int destino, int origem) {
		
		
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
	public int nearestNode(int origem, int minimo, int maximo) {
		
		//System.out.println("Busca nó mais próximo: "+origem);
		
		/*
		 * Testa possibilidades de nós adjacentes  
		*/
		for(int i = minimo; i < maximo; i++) {
			
			int destino = random(minimo,maximo);
			
			/*
			 * Verifica se destino temporário é adjacente/vizinho ao nó origem
			 * o nó de adjacente a origem pode estar em oito posiveis coordenada.
			 */
			if(adjacencia(origem,destino) == destino)
			{
				//System.out.println("Adjacencia");
				return destino;
			}
		}
		/*
		* Caso não encontre nó  adjacente vai buscar o mais próximo da origem
		* faz expanção da busca a partir da origem 
		*/
		return buscaVerticeMaisProximo(origem,minimo,maximo);
		
	}
	/**
	 *Set aneis.
	 */
	public void setRing(graph rede, int N, int R, int L, int mSpath){
		this.nNodes = N;
		int u1, u2, v1, v2, auxSize;
		int dist, p = 0;
		int src, destino;
		
		DoublyLinkedList labelNo = new DoublyLinkedList();
		
		//cria uma lista com o numero do no
		for(int j = 0; j < N; j++) {
			
			labelNo.addNode(j);
		}
		/*
		 * for each region
		*/

		for(int auxR = 0; auxR < R; auxR++) {
			
			DoublyLinkedList aux = new DoublyLinkedList();
			src = labelNo.retElement(0);
			//System.out.println("src: "+src);
			
			destino = src;
			
			/*
			 * Dificilmente entra nesse só se ocorrer a nova modificação onde pode ter a distribuição dos nós randomicamente
			*/
			
			if((int)nNodesReg.retElement(auxR) > 0)
			{
				//System.out.println("PROBLEMA");
				if((int)nNodesReg.retElement(auxR) > 1)
				{
					//System.out.println("PROBLEMA 1");
					/*
					 * se houver mais q um no/vertice em uma região
					 */
					
					for(int k = 0; k < (int)nNodesReg.retElement(auxR); k++) {//para cada no da regiao
						
						aux.addNode(src);//lista auxiliar que contém os nós com ligações já feitas
						//System.out.println("PROBLEMA 3");
						int min = aux.retElement(0);
						int max = (aux.retElement(0)+nNodesReg.retElement(auxR))-1;
						
						if(max > N)
						{
							break;
						}
						//System.out.println("PROBLEMA AQUI: "+max+min);
						if(k == nNodesReg.retElement(auxR)-1)
						{
							/*
							 * se for a última ligação
							*/
							destino =  aux.retElement(0);
							//System.out.println(destino);
							
						}
						else
						{		
							
							if(destino == src)
							{
								while(true)
								{
									
									if(max == N && max-1 == min)
									{
										destino = src-1;
										break;
									}
									int temp = random(min,max);
									
									if(temp < N && src != temp)
									{
										destino = temp;
										break;
									}
								}
							}
							while(p == 1)
							{
								
								//o destino deve ser diferente da origem
								
								//System.out.println("CAIU AUQI: "+destino+p+aux.size()+src);
								/**
								 * Prioridade nós adjacentes
								 */
								
								for(int x = 0; x < aux.size(); x++) {
									
									//System.out.println("AQUI: "+aux.retElement(x));
									if(destino == aux.retElement(x))
									{
										//verifica se o nó sorteado já tem uma ligação ou é ele próprio
										p = 1;
										break;
									}
									else
									{
										//System.out.println("OLHA SÒ");
										p = 0;
									}
								}
							}
						}
						do
						{
							if(destino == src)
							{
								while(true)
								{
									int temp = random(min,max);
									
									if(temp < N && src != temp)
									{
										destino = temp;
										break;
									}
								}
							}
							u1 = nPos.retElementOne(src);
							u2 = nPos.retElementTwo(src);
							v1 = nPos.retElementOne(destino);
							v2 = nPos.retElementTwo(destino);
							
							dist = getEuclidianDistance(u1, u2, v1, v2);
							
						}
						while(waxman(rede,src,destino,dist,1,1,L,mSpath) != 0);//enquanto n inserir um link
						
						labelNo.removeNode(src);
						
						src = destino;//src recebe a nova origem
						
						if(nNodesReg.retElement(auxR) == 2)
						{
							//se houver só dois nós, a ligação será feita uma vez
							
							labelNo.removeNode(src);
							k++;
						}
					}
				}
			}
			auxSize = aux.size();
			
			if(aux.empty() != 1)
			{
				//System.out.println("AQUI DEU CREPR");
				//remove os elementos em aux
				
				for(int j = 0; j < auxSize; j++){
					
					int x = 0;		
					int a = aux.retElement(x);
					aux.removeNode(a);
				}
			}	
		}
	}

	/*
	 *Interliga os anéis em cada região.
	*/	
	public void setRingReg(graph rede, int N, int L, int mSpath) {
		
		
		int R = (int)nNodesReg.size();
		
		int src = 0, auxS = 0, x = 0;
	
		int u1, u2, v1, v2 , v11 , v22 , dist, dist1, destino = 0, dst1, src1, srcAux = 0, dstAux = 0;

		/*
		 * Número de regiões maior que 1 
		 */
		if(R > 1)
		{
			for(int i = 0; i < R; i++) {
				
				if(i == 0)
				{ 
					/*
					*se for primeira regiao, a regiao comeca nPos[0]
					*/
					auxS = 0;
				}
				else
				{
					auxS += nNodesReg.retElement(i-1);//auxS indica o início de cada regiao em nPos
					
					if(auxS < 0 || auxS >= N)
					{
						auxS = 0;
					}
				}
				/*
				 * Verifica a existência de nós na região
				 */
				if(nNodesReg.retElement(i) > 0)
				{
					if(nNodesReg.retElement(i) == 1)
					{ 
						//existe 1 nó na regiao
						src = auxS;
						/*
						 * Coordenadas x,y 
						 */
						u1 = nPos.retElementOne(src); 
						u2 = nPos.retElementTwo(src);
						
						
						do
						{ 
							//encontrar o destino
							do
							{
								/*
								 * Deve ser mudado, buscar nó da região vizinha 
								*/
								//System.out.println("ERRO provavel 91");
								while(true)
								{
									int temp = nearestNode(src,0, N-1);//sortear destino
									
									if(temp < N)
									{
										destino  = temp;
										break;
									}
								} 	
								//System.out.println("Destino __: "+destino);
							}
							while(destino == src);
							
							v1   = nPos.retElementOne(destino);
							v2   = nPos.retElementTwo(destino);
							
							dist = getEuclidianDistance(u1, u2, v1, v2);
						}
						while(waxman(rede,src,destino,dist,1,1,L,mSpath) != 0);//ate colocar um link

						do
						{
							do
							{
								while(true)
								{
									int temp = nearestNode(src,0, N-1);//sortear destino
									
									if(temp < N)
									{
										dst1 = temp;
										break;
									}
								}
								//System.out.println("Destino: "+dst1);
							}
							while(dst1 == destino || dst1 == src);
							
							v11   = nPos.retElementOne(dst1);
							v22   = nPos.retElementTwo(dst1);
							dist1 = getEuclidianDistance(u1, u2, v11, v22);
						
						}
						while(waxman(rede,src,dst1,dist1,1,1,L,mSpath)!= 0);
					
					}
				
					else if(nNodesReg.retElement(i) == 2)
					{ 
						/*
						 * existe 2 nós na regiao, ligados em barra
						*/
						src = auxS;
					
						u1 = nPos.retElementOne(src); //origem
						u2 = nPos.retElementTwo(src);//origem

						do
						{ //encontrar o destino
							do
							{
								//System.out.println("ERRO provavel 856");
								while(true) 
								{
									int temp  = nearestNode(src,0, N-1);//sortear destino
									if(temp < N) destino = temp; break;
								}
								
							}
							while(destino == src || destino == src+1);
							
							v1   = nPos.retElementOne(destino);
							v2   = nPos.retElementTwo(destino);
							
							dist = getEuclidianDistance(u1, u2, v1, v2);
						
						}
						while(waxman(rede,src,destino,dist,1,1,L,mSpath) != 0);//ate colocar um link
							
						int srcTemp = src+1;
						
						if(srcTemp >= N) 
						{
							//System.out.println("ERRO provavel");
							srcTemp = srcTemp - 2;
						}
						src = srcTemp;
						
						//System.out.println("SRC -->  "+src);
						u1 = nPos.retElementOne(src); //origem
						u2 = nPos.retElementTwo(src);//origem

						do
						{	
							do
							{
								//System.out.println("ERRO provavel 89");
								while(true) {
									//System.out.println("ERRO provavel 1");
									int temp = nearestNode(src,0, N-1);//sortear destino
									
									if(temp < N)
									{
										dst1  = temp;
										break;
									}
									
								}
								
							}
							while(dst1 == src || dst1 == src-1);
							
							v11   = nPos.retElementOne(dst1);
							v22   = nPos.retElementTwo(dst1);
							
							dist1 = getEuclidianDistance(u1, u2, v11, v22);
							
						}
						while(waxman(rede,src,dst1,dist1,1,1,L,mSpath) != 0);

					}
					else
					{
						//existe um anél na regiao
						DoublyLinkedList vSrc = new DoublyLinkedList();
						DoublyLinkedList vDst = new DoublyLinkedList();
						
						//preencher os vectores vSrc e vDst
						//System.out.println("ERRO provavel 561");
						for(int j = 0; j < (int)nPos.size(); j++) {
							
							if(j == auxS)
							{
								for(int k = auxS; k < (auxS+nNodesReg.retElement(i)); k++) {
									
									vSrc.addTwo(nPos.retElementOne(k),nPos.retElementTwo(k));
									j++;
								}
								j--;
							}
							else
							{
								vDst.addTwo(nPos.retElementOne(j),nPos.retElementTwo(j));
							}
						}
					
						int srcSize= vSrc.size();
						int dstSize = vDst.size();
						//System.out.println("ERRO provavel 78");
						while(true) {
							
							int origemTemp = random(0,srcSize-1);
							
							if(origemTemp < N)
							{
								src = origemTemp;
								break;
							}
							//System.out.println("ERRO provavel 3");
						}
						
						
						u1 = vSrc.retElementOne(src); //origem
						u2 = vSrc.retElementTwo(src); //origem

						do
						{ //encontrar o destino
							//System.out.println("ERRO provavel 153");
							while(true) {
								
								int Temp = nearestNode(src,0,dstSize-1); //sortear destino;
								
								if(Temp < N)
								{
									destino = Temp;
									break;
								}
								//System.out.println("ERRO provavel 4");
							}
							
							v1   = vDst.retElementOne(destino);
							v2   = vDst.retElementTwo(destino);
							dist = getEuclidianDistance(u1, u2, v1, v2);
							//System.out.println("ERRO provavel 19");
							for(int n = 0; n  < nPos.size(); n++) {
								
								if(nPos.retElementOne(n) == u1 && nPos.retElementTwo(n) == u2 && n < N)
								{									
									srcAux = n;
									break;
								}
							}

							for(int n = 0;  n < nPos.size(); n++) {
								
								if(nPos.retElementOne(n) == v1 && nPos.retElementTwo(n) == v2 && n < N )
								{
									dstAux = n;
									
									break;
								}
							}	
											
						}
						while(waxman(rede,srcAux,dstAux,dist,1,1,L,mSpath) != 0);//ate colocar um link /aresta

						do
						{
							do
							{
								//System.out.println("ERRO provavel 16");
								while(true)
								{
									src1  = random(0, srcSize-1);
									dst1  = random(0, dstSize-1);//sortear destino
									
									if(src1 < N && dst1 < N) break;
								}
								
							}
							while(src == src1 && destino == dst1);

							u1 = vSrc.retElementOne(src1); 		//origem
							u2 = vSrc.retElementTwo(src1);		//origem
							
							v11   = vDst.retElementOne(dst1);
							v22   = vDst.retElementTwo(dst1);
							
							dist1 = getEuclidianDistance(u1, u2, v11, v22);

							for(int n = 0; n < nPos.size(); n++) {
								//System.out.println("ERRO provavel 11");
								if(nPos.retElementOne(n) == u1 && nPos.retElementTwo(n) == u2 && n < N)
								{
									srcAux = n;
									break;
								}
							}

							for(int n = 0; n < nPos.size(); n++){
								//System.out.println("ERRO provavel 10");
								if(nPos.retElementOne(n) == v11 && nPos.retElementTwo(n) == v22 && n < N)
								{
									dstAux = n;
									break;	
								}
							}
	
						}
						while(waxman(rede,srcAux,dstAux,dist1,1,1,L,mSpath) != 0);
									
						if(vSrc.empty() != 1)
						{
							//System.out.println("ERRO provavel 9");
							int SrcSize = vSrc.size();
							
							for(int p = 0; p < SrcSize; p++) {
								
								vSrc.removeNode(vSrc.retElementOne(x),vSrc.retElementTwo(x));// é removido todos os elementos de nós origem
							}
						}	
						if(vDst.empty() != 1)
						{
							//System.out.println("ERRO provavel 5");
							int DstSize = vDst.size();
							
							for(int p = 0; p < DstSize; p++)
							{
								vDst.removeNode(vDst.retElementOne(x),vDst.retElementTwo(x));// é removido todos os elementos de nós destino
							}
						}
					}
				}
			}
		}	
	}
}