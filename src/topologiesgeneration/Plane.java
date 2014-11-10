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
	DoublyLinkedList nPos;
	DoublyLinkedList region;
	DoublyLinkedList nNodesReg;
	int nNodes;
	int minDegree;
	int maxDegree;
	
	/**
	 *Atribui valor a area do plano quadrado. Construtor
	 */
	public Plane(int width,int height,int n,int side,int min, int max) {
		
		X = side;
		this.area = new int[side][side];
		
		
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
		// System.out.println("max "+max+" min"+min);
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
 	
	
	public void setRegions(int R, int Y, int S, int N){
		
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
		else{ //se o numero de regiões não for primo é necessário factorizá-lo
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
		
		limit(heightR, widthR, R, A);  //determina os limites das regioes
		setNodeRegions(heightR, widthR, R, Y, Sa, N); //determina o número de nós por região
		sortNpositions(R, Y); //cria lista auxiliar para sortear a posicao dos nodos.
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
		
		int K = (int) Math.floor(R/A);
	
		int temp = 1;
		@SuppressWarnings("unused")
		int auxMinI, auxMaxI, auxMinJ, auxMaxJ;
		
		if(wR == X)
		{ 								//se a divisão for apenas num sentido
      		region.addNode(0); 			//minimo valor possivel para i
      		region.addNode(X-1); 		//maximo valor possivel para i
      		region.addNode(0); 			//minimo valor possivel para j
      		region.addNode(hR-1); 	//maximo valor possivel para j
      		int auxMax = (hR-1);
      		temp = temp + 1;
      		
  			while(temp <= R) 
  			{ 
  				//limites para as restantes regioes
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
	
	private void setNodeRegions(int hR, int wR, int R, int Y, int S, int N){// mostra quantos nós haverá em cada região
		int missing = N; // N = número de nós; S= distribuição de nós por região
		int allocated = 0;
		boolean all = false;
		int auxR,auxN=0;
		int randInt = 0;
		int max = (int)Math.floor((hR*wR)/(4*Y+4*Y*Y+1)); //maximo de nos que cabem em cada regiao

		if((max*R)>=N){ //entao os nós cabem nas regioes
			if(S == 1){ //então o número de nós por região será igual, um nó por região
				for(auxR = 0; auxR < R; auxR++){
					nNodesReg.addNode((int)Math.floor(N/R)); //determinação do número de nós
					allocated = allocated + (int)Math.floor(N/R);
				}
				if(allocated < N){ //se sobrarem nós serão colocados um a um em cada uma das regiões
					while(all == false){
						auxR = 0;
						while(allocated < N && auxR < R){
							nNodesReg.update(auxR,1);
							allocated = allocated +1;
							auxR = auxR +1;
						}
						if(allocated == N)
							all = true;
					}
				}
			}else{ //a colocacaoo dos nós sera aleatória
				for(int j = 0; j < R; j++){
					nNodesReg.addNode(0);
				}
				while(missing > 0){
					for(auxR = 0; auxR < R; auxR++){
						if(N <= max){ //entao cabem todos na regiao
							if(missing==1){ //falta um nó somente
								nNodesReg.update(auxR,1);
								missing-=1;
							}else{
								randInt = random(0, missing);
								nNodesReg.update(auxR,randInt);
								missing = missing - randInt;
							}//endif
							if(missing==0){break;}
						}else{ //nao cabem todos os nós na regiao
							auxN = nNodesReg.retElement(auxR);
							auxN = max-auxN;
							if(auxN==1){//só falta colocar 1 para atingir o máximo
								if(nNodesReg.update(auxR,1)!=0){
									nNodesReg.update(auxR,1);
								}
								missing-=1;
							}else{
                 				randInt = random(0, auxN); //pode ficar em ciclo aqui
                        		if((missing - randInt) >= 0){
									nNodesReg.update(auxR,randInt);
                            		missing = missing - randInt;
                        		}else{
									nNodesReg.update(auxR,missing);
                        			missing=0;
                        		}
							}
							if(missing==0){break;}
						}
					if(missing==0){break;}
					}
				}
			}
		}else{ //entao os nós NAO cabem nas regioes
			System.out.println("Os nos não cabem nas regioes!!");
		}
		
//		System.out.print("nNodesReg: ");
//		nNodesReg.display1();
		
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

	private void sortNpositions(int R, int Y){
		int rand, u1, u2, a,b,x, listSize,p=0,X=area.length, blockSize;
		DoublyLinkedList[] plan = new DoublyLinkedList[R];
		DoublyLinkedList toBlock = new DoublyLinkedList(); // para "bloquear" as áreas de outras regiões
				
		for(int c=0;c<R;c++){  
				plan[c] = new DoublyLinkedList(); //inicializa as listas 
		} 

		for(int auxR=0; auxR<R; auxR++){//para todas as regioes
			for(int k= region.retElement(p+2);k<=region.retElement(p+3);k++){ //linhas
				for(int j=region.retElement(p);j<=region.retElement(p+1);j++){ //colunas
					plan[auxR].addTwo(k,j);
				}
			}
// 			System.out.println("----------------- 1 ----------------");
// 			plan[auxR].display2();						
			if(nNodesReg.retElement(auxR)>0){//se tiver algum nó pra inserir na regiao
				if(toBlock.empty()!=1){ // verifica se tem elementos em toBlock
					blockSize = toBlock.size();
					x=0;
					for(int i=0;i<=blockSize;i++){
						a = toBlock.retElementOne(x);
						b = toBlock.retElementTwo(x);
						if(plan[auxR].searchTwo(a,b)==true){ // verifica se há algum elemente da região que está numa área que não pode ser adicionado nó
							plan[auxR].removeNode(a,b);// se estiver, remove a região da lista
							toBlock.removeNode(a,b);// e remove de toBlock
						}else{
							x++;// se não estiver, continua procurando na lista do toBlock se há outras área da região auxR bloqueados	
						}					
					}
				}		
				for(int auxNno=0; auxNno < nNodesReg.retElement(auxR); auxNno++){//para o numero de nos de cada regiao
					listSize=(plan[auxR].size())-1;
					rand = random(0,listSize);//sorteia uma
//					System.out.println("Rand: "+rand);
					u1 = plan[auxR].retElementOne(rand);//determina posicao i do no a colocar
					u2 = plan[auxR].retElementTwo(rand);//determina posicao j do no a colocar	
					System.out.println("["+u1+"] "+"["+u2+"]");	
					setNode(u1,u2,Y);//coloca o no

					for(int temp=u1-Y; temp<=u1+Y; temp++){// remover da lista a posição escolhida e os que estão ao redor da posição
						for(int temp1=u2-Y; temp1<=u2+Y; temp1++){
							if(temp > region.retElement(p+3) || temp1 > region.retElement(p+1) || temp < region.retElement(p+2) || temp1 < region.retElement(p)){
								if(temp >=0 && temp1>=0 && temp <X && temp1 <X){
									toBlock.addTwo(temp,temp1); // adiciona na área bloqueada
								}
							}
							if(temp1 >=region.retElement(p) && temp >= region.retElement(p+2) && temp1<=region.retElement(p+1) && temp<=region.retElement(p+3)){
								plan[auxR].removeNode(temp,temp1);
							}
						}
					}
				}
			}	
			
			for(int k= region.retElement(p+2);k<=region.retElement(p+3);k++){ //linhas
				for(int j=region.retElement(p);j<=region.retElement(p+1);j++){ //colunas
					plan[auxR].removeNode(k,j);
				}
			}
			p+=4;
		}	
		if(toBlock.empty()!=1){// se toBlock não estiver vazia
			blockSize = toBlock.size();
			for(int i=0;i<blockSize;i++){
				x=0;
				a = toBlock.retElementOne(x);
				b = toBlock.retElementTwo(x);
				toBlock.removeNode(a,b);// é removido todos os seus elementos
			}					
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
					//System.out.println("blockSize"+blockSize);
					x = 0;
					
					for(int i = 0;i <= blockSize; i++) {
						
						a = toBlock.retElementOne(x);						
						b = toBlock.retElementTwo(x);
						
						if(plan[auxR].searchTwo(a,b) == true)
						{ 
															// verifica se há algum elemente da região que está numa área que não pode ser adicionado nó
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
					
					listSize = (plan[auxR].size())-1;
					
					rand = random(0,listSize);//sorteia uma
						
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
			
			if( grau < this.maxDegree)
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
			
//			System.out.println("v-"+i+" grau "+grau);
			if(grau >= this.minDegree)
			{
				nMin++;
			}
		}
		
		int [] ar = new int[n];
		
		for(int i = 0; i < n ;i++){
			
			ar[i]  = nos[i];
			
		}
		System.out.println("nNos "+n);
		
		if(n == 1 && rede.getDegree(ar[0]) < this.minDegree)
		{
//			System.out.println("v "+ar[0]+" grau "+rede.getDegree(ar[0]));
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
					System.out.println("procura destino src "+auxSrc+" dst"+auxDst);
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
							if(raioAtual < raioAnterior && rede.getArc(origem,destino) == 1 && (rede.getDegree(origem) < this.maxDegree && rede.getDegree(destino) < this.maxDegree ))
							{
								
								/**
								 * Verifica se já não existe ligação
								 */
							
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
	
		/*
		 * Testa possibilidades de nós adjacentes  
		*/
		for(int i = minimo; i < maximo; i++) {
			
			int destino = 0,temp = 0;

			while(true) {

				temp = random(minimo,maximo);

				/**
				 * Verifica se já não existe ligação
				 */
				if (rede.getArc(origem,temp) == 1 && rede.getDegree(origem) < this.maxDegree || rede.getDegree(temp) < this.maxDegree ) 
				{
					destino = temp;
					break;
				}
			}
			
			/*
			 * Verifica se destino temporário é adjacente/vizinho ao nó origem
			 * o nó de adjacente a origem pode estar em oito posiveis coordenada.
			 */
			if(adjacencia(origem,destino) == destino && rede.getArc(origem,destino) == 1 &&  rede.getDegree(destino) < this.maxDegree && rede.getDegree(origem) < this.maxDegree) 
			{
				return destino;
			}
		}
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
		
		int u1, u2, v1, v2, auxSize;
		int dist, p = 0;
		int src, dst;
		
		DoublyLinkedList labelNo = new DoublyLinkedList();
		
		//cria uma  lista com o numero do no
		for(int j = 0; j < N; j++)
		{
			labelNo.addNode(j);
		}
		
		for(int auxR = 0; auxR < R; auxR++){
			//para cada regiao
			DoublyLinkedList aux = new DoublyLinkedList();
			
			src = labelNo.retElement(0);
			
			dst = src;
			
			if((int)nNodesReg.retElement(auxR) > 0){
				
				if((int)nNodesReg.retElement(auxR) > 1){//se houver mais q um no
					
					
					for(int k = 0; k < (int)nNodesReg.retElement(auxR);k++){//para cada no da regiao
						
						aux.addNode(src);//lista auxiliar que contém os nós com ligações já feitas
						
						int min = aux.retElement(0);
						int max = (aux.retElement(0)+nNodesReg.retElement(auxR))-1;
						
						if(max >= rede.getNumberNodes()) max = rede.getNumberNodes()-1;
						if(min == max) min = min-1;
				
						if(k == nNodesReg.retElement(auxR)-1)
						{//se for a última ligação
							
							dst = aux.retElement(0);//a ligação é feita com o nó inicial
						}
						else
						{
							
							while(dst == src || p == 1){//o destino deve ser diferente da origem
								
								dst = nearestNode(src,min,max,rede);//sortea destino
								
								if(rede.getDegree(dst) >= this.maxDegree) 
								{
									labelNo.removeNode(dst);
								}
								if(rede.getDegree(src) >= this.maxDegree) 
								{
									labelNo.removeNode(src);
								}
								
								if(dst == src || rede.getArc(src, dst) == 0 || rede.getDegree(dst) >= this.maxDegree)
								{
									
									continue;
								}
								
									
								int tam = aux.size();
								
								for(int x = 0; x < tam; x++){
									
									/**
									 * verifica se o nó sorteado já tem uma ligação ou é ele próprio
									 */
									if(dst == aux.retElement(x))
									{
										p = 1; 
										break;
									}
									else
									{
										p = 0;
									}
								
								}
								
							}
						}
							
						u1 = nPos.retElementOne(src);
						u2 = nPos.retElementTwo(src);
						v1 = nPos.retElementOne(dst);
						v2 = nPos.retElementTwo(dst);
							
						dist = getEuclidianDistance(u1, u2, v1, v2);
					
						waxman(rede,src,dst,dist,1,1,L,mSpath);//enquanto n inserir um link
						
						labelNo.removeNode(src);
						
						src = dst;//src recebe a nova origem
						
						if(nNodesReg.retElement(auxR) == 2)
						{//se houver só dois nós, a ligação será feita uma vez
							labelNo.removeNode(src);
							k++;
						}
					}
				}
				
			}
		
			auxSize = aux.size();
			
			/**
			 * remove os elementos em aux
			 */
			if(aux.empty() != 1)
			{
				for(int j = 0;j < auxSize;j++){
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
	
	public void setRingReg(graph rede, int N, int L, int mSpath,int maxLinks) throws Exception {
			
			
			int R = (int)nNodesReg.size();
			
			int src = 0, auxS = 0, x = 0;
		
			int u1, u2, v1, v2 , v11 , v22 , dist, dist1, destino = 0, dst1 = 0, src1 = 0, srcAux = 0, dstAux = 0;
			
			int aux = 0;
			/*
			 * NÃºmero de regiÃµes maior que 1 
			 */
			if(R > 1)
			{
				for(int i = 0; i < R; i++) {
					
					if(rede.getNumLinks() == maxLinks) return;
					System.out.println("RINGREG FOR");
					
					if(i == 0)
					{ 
						/*
						*se for primeira regiao, a regiao comeca nPos[0]
						*/
						auxS = 0;
					}
					else
					{
						auxS += nNodesReg.retElement(i-1);//auxS indica o inÃ­cio de cada regiao em nPos
						
						if(auxS < 0 || auxS >= N)
						{
							auxS = 0;
						}
					}
					/*
					 * Verifica a existÃªncia de nÃ³s na regiÃ£o
					 */
					if(nNodesReg.retElement(i) > 0)
					{
						if(nNodesReg.retElement(i) == 1)
						{
							System.out.println("OIEEE");
							//existe 1 nÃ³ na regiao
							aux = 0;
							
							while(aux < N) {
								
								src = randomNode(rede);
								
								if(rede.getDegree(src) < this.maxDegree) break;
								
								aux++;
							}
							if(rede.getDegree(src) >= this.maxDegree ) continue;
							
								/*
								 * Coordenadas x,y 
								 */
								u1 = nPos.retElementOne(src); 
								u2 = nPos.retElementTwo(src);
								
							
								//encontrar o destino
								aux = 0;
								/*
								 * Deve ser mudado, buscar nÃ³ da regiÃ£o vizinha 
								*/
								System.out.println("ERRO provavel 91");
								int temp = src;
								
								while(aux < N)
								{
									temp = nearestNode(src,0, N-1,rede);//sortear destino
									
									if(temp < N)
									{
										destino  = temp;
										break;
									}
									temp = src;
									aux++;
								} 
								if(temp != src)
								{
									System.out.println("Destino __: "+destino);
									
									
									v1   = nPos.retElementOne(destino);
									v2   = nPos.retElementTwo(destino);
									
									dist = getEuclidianDistance(u1, u2, v1, v2);
									link(rede,src, destino,mSpath,dist);
	
								}
								aux = 0;
								temp = src;
								
							while(aux < N)
							{
								temp = nearestNode(src,0, N-1,rede);//sortear destino
								
								if(temp < N)
								{
									dst1 = temp;
									break;
								}
								temp = src;
								aux++;
							}
							
							System.out.println("Destinox: "+dst1);
							
							if(src != temp)
							{
								v11   = nPos.retElementOne(dst1);
								v22   = nPos.retElementTwo(dst1);
								dist1 = getEuclidianDistance(u1, u2, v11, v22);
							

								link(rede,srcAux, dstAux,mSpath,dist1);
							}
							
						}
						
						if(rede.getNumLinks() == maxLinks) return;
					
						else if(nNodesReg.retElement(i) == 2)
						{ 
							/*
							 * existe 2 nÃ³s na regiao, ligados em barra
							*/
							src = auxS;
						
							u1 = nPos.retElementOne(src); //origem
							u2 = nPos.retElementTwo(src);//origem

							aux = 0;
							
							System.out.println("ERRO provavel 856");
							
							destino = src;
							
							while(aux < N) 
							{
								int temp  = nearestNode(src,0, N-1,rede);//sortear destino
								
								if(temp < N && destino != src) 
								{
									destino = temp; 
									break;
								}
								temp = src;
								aux++;
							}
								
							if(destino != src)
							{
								v1   = nPos.retElementOne(destino);
								v2   = nPos.retElementTwo(destino);
								
								dist = getEuclidianDistance(u1, u2, v1, v2);
							

								link(rede,src, destino,mSpath,dist);
									
							}
							
							if(rede.getNumLinks() == maxLinks) return;
							
							int srcTemp = src+1;
							
							while(srcTemp > N || rede.getDegree(srcTemp) >= this.maxDegree) 
							{
								
								srcTemp = random(0,N-1);
							}
							
							src = srcTemp;
							
							System.out.println("SRC -->  "+src);
							u1 = nPos.retElementOne(src); //origem
							u2 = nPos.retElementTwo(src);//origem
							
							aux = 0;
							
							dst1 = src;
							
							System.out.println("ERRO provavel 89");
							while(aux < N) {
								//System.out.println("ERRO provavel 1");
								int temp = nearestNode(src,0, N-1,rede);//sortear destino
								
								if(temp < N)
								{
									dst1  = temp;
									break;
								}
								dst1 = src;
								aux++;
								
							}
							
							if(src != dst1)
							{
								v11   = nPos.retElementOne(dst1);
								v22   = nPos.retElementTwo(dst1);
								
								dist1 = getEuclidianDistance(u1, u2, v11, v22);

								link(rede,src, dst1,mSpath,dist1);
							}
							if(rede.getNumLinks() == maxLinks) return;
						}
						else
						{
							//existe um anÃ©l na regiao
							DoublyLinkedList vSrc = new DoublyLinkedList();
							DoublyLinkedList vDst = new DoublyLinkedList();
							
							//preencher os vectores vSrc e vDst
							
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
							
							System.out.println("ERRO provavel 78");
							aux = 0;
							
							if(rede.getNumLinks() == maxLinks) return;
							
							while(aux < N) {
								
								int origemTemp = random(0,srcSize-1);
								
								if(origemTemp < N && rede.getDegree(origemTemp) < this.maxDegree)
								{
									src = origemTemp;
									break;
								}
								aux++;
								
								System.out.println("ERRO provavel 3");
							}
							
							if(rede.getNumLinks() == maxLinks) return;
							
							if(src >= 0)
							{
								u1 = vSrc.retElementOne(src); //origem
								u2 = vSrc.retElementTwo(src); //origem

								 //encontrar o destino
								System.out.println("ERRO provavel 153");
								aux = 0;
								while(aux < N) {
									
									int Temp = nearestNode(src,0,dstSize-1,rede); //sortear destino;
									
									if(Temp < N)
									{
										destino = Temp;
										break;
									}
									aux++;
									System.out.println("ERRO provavel 4");
								}
								
								v1   = vDst.retElementOne(destino);
								v2   = vDst.retElementTwo(destino);
								dist = getEuclidianDistance(u1, u2, v1, v2);
								
								if(rede.getNumLinks() == maxLinks) return;
								
								System.out.println("ERRO provavel 19");
								
								for(int n = 0; n  < nPos.size(); n++) {
									
									if(nPos.retElementOne(n) == u1 && nPos.retElementTwo(n) == u2 && rede.getDegree(n) < this.maxDegree && n < N)
									{									
										srcAux = n;
										break;
									}
								}

								for(int n = 0;  n < nPos.size(); n++) {
									
									if(nPos.retElementOne(n) == v1 && nPos.retElementTwo(n) == v2 && rede.getDegree(n) < this.maxDegree && n < N && n != srcAux)
									{
										dstAux = n;
										
										break;
									}
								}	
								
								link(rede,srcAux, dstAux,mSpath,dist);
								
								if(rede.getNumLinks() == maxLinks) return;
							}
							
							if(rede.getNumLinks() == maxLinks) return;

							aux = 0;
							
							src1 = dst1 = 0;
							
							while(aux < N)
							{
								src1  = random(0, srcSize-1);
								dst1  = nearestNode(srcSize,0, dstSize-1,rede);//sortear destino
								
								if(src1 < N && dst1 < N) break;
								
								aux++;
							}
							
							if(src1 != dst1)
							{
								

								u1 = vSrc.retElementOne(src1); 		//origem
								u2 = vSrc.retElementTwo(src1);		//origem
								
								v11   = vDst.retElementOne(dst1);
								v22   = vDst.retElementTwo(dst1);
								
								dist1 = getEuclidianDistance(u1, u2, v11, v22);
								
								link(rede,src, dst1,mSpath,dst1);
							
								if(rede.getNumLinks() == maxLinks) return;
								
								for(int n = 0; n < nPos.size(); n++) {
									
									if(nPos.retElementOne(n) == u1 && nPos.retElementTwo(n) == u2 && rede.getDegree(n) < this.maxDegree && n < N)
									{
										srcAux = n;
										break;
									}
								}
	
								for(int n = 0; n < nPos.size(); n++){
									
									if(nPos.retElementOne(n) == v11 && nPos.retElementTwo(n) == v22 && rede.getDegree(n) < this.maxDegree && n < N && n != srcAux)
									{
										dstAux = n;
										break;	
									}
								}
			
								link(rede,srcAux,dstAux,mSpath,dist1);
								
								if(rede.getNumLinks() == maxLinks) return;
							}	
							
							if(rede.getNumLinks() == maxLinks) return;
							
							if(vSrc.empty() != 1)
							{
								
								int SrcSize = vSrc.size();
								
								for(int p = 0; p < SrcSize; p++) {
									
									vSrc.removeNode(vSrc.retElementOne(x),vSrc.retElementTwo(x));// Ã© removido todos os elementos de nÃ³s origem
								}
							}	
							if(vDst.empty() != 1)
							{
								
								int DstSize = vDst.size();
								
								for(int p = 0; p < DstSize; p++)
								{
									vDst.removeNode(vDst.retElementOne(x),vDst.retElementTwo(x));// Ã© removido todos os elementos de nÃ³s destino
								}
							}
						}
					}
				}
			}	
		}
	}
