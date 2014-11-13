package topologiesgeneration;

class Node {   
	
	int element1,element2, element3; 
	int element; // nodo para uma lista com um elemento só  
	Node next;
	Node prev; 

	Node (int x,int y, int z) { // construtor para três elementos 
		
		element1 = x; 
		element2 = y;
		element3 = z;      
		next = null; 
		prev = null;    
	}
	Node (int x,int y) { // construtor para dois elementos 
		
		element1 = x; 
		element2 = y;      
		next = null; 
		prev = null;    
	} 
	Node (int elem) { // construtor para um elemento
		
		element = elem;     
		next = null; 
		prev = null;    
	} 
}
class NodeDouble { // nó para um double
	
	NodeDouble nextDouble;
	NodeDouble prevDouble; 
	double elementDouble;
	
	NodeDouble (double elemDouble) { // construtor para um elemento double
		elementDouble = elemDouble;     
		nextDouble = null; 
		prevDouble = null;    
	}      
}     
    
public class DoublyLinkedList {
	
	Node first, last;  
	NodeDouble firstDouble, lastDouble;    
	int numberNos;   

	DoublyLinkedList() {  
		
		first = null;     
		last = null;    
		firstDouble = null;
		lastDouble = null; 
		numberNos = 0;     
	}     

	/**
	 * Verifica se a lista esta vazia caso esteja retorna 1 , método para inteiro
	 */
	public int empty() { 
		
		if (first == null && last == null) 
		{
			return 1;
		}
		return 0;
	}

	/**
	 * Verifica se a lista esta vazia caso esteja retorna 1, método para ponto flutuante
	 */
	
	public int emptyDouble() { 
		
		if (firstDouble == null && lastDouble == null){
			return 1;
		}
		return 0;
	}        

	/**
	 * Retorna número de nós.
	 */
	public int size() {
		
		return numberNos;
	}

	/**
	 * Adiciona nodo na lista em inteiro.
	 */
	public void addNode(int elem) {  
		
		numberNos++;     
		Node newNo = new Node(elem);  
		
		if (empty() == 1){     
			first = newNo;
			last = newNo;     
		}
		else
		{     
			newNo.prev = last;
			last.next = newNo;     
			last = newNo;
		}      
	}

	/**
	 * Adiciona nodo na lista em ponto flutuante.
	 */
	public void addDouble(double elementDouble) {
		
		numberNos++;     
		NodeDouble newN = new NodeDouble(elementDouble);
		
		if (emptyDouble() == 1)
		{     
			firstDouble = newN;
			lastDouble = newN;     
		}
		else
		{     
			newN.prevDouble = lastDouble;
			lastDouble.nextDouble = newN;     
			lastDouble = newN;
		}      
	}
	/**
	 * Adiciona dois nós.
	 */
	public void addTwo(int x,int y) {
		
		numberNos++;     
		Node newNo = new Node(x,y);   
		
		if (empty() == 1)
		{     
			first = newNo;
			last = newNo;     
		}
		else
		{     
			newNo.prev = last;
			last.next = newNo;     
			last = newNo;
		}    
	}

	/**
	 * Adiciona três nodos.
	 */
	public void addNode(int x,int y, int z) {  
		
		numberNos++;     
		Node newNo = new Node(x,y,z);    
		
		if (empty() == 1){     
			first = newNo;
			last = newNo;     
		}
		else
		{     
			newNo.prev = last;
			last.next = newNo;     
			last = newNo;
		}    
	}

	/**
	 * Remove um nodo.
	 */
	public void removeNode(int elem) {
		
		Node noTemp = first; 
		
		while (noTemp !=null && noTemp.element != elem) {
			
				noTemp = noTemp.next;     
		}   
		
		if(noTemp != null)
		{   
			if (first.element == noTemp.element)
			{ 
				first = first.next;     
				numberNos--;     
			}
			else
			{     
				noTemp.prev.next = noTemp.next; 
				
				if(noTemp.next != null)
				{
					noTemp.next.prev = noTemp.prev; 
				}
				numberNos--;     
			}     
			if(noTemp == last)
			{     
				last = noTemp.prev;
			}  
			noTemp = null;
		}   
	}        

	/**
	 * Remove dois nodos.
	 */	
	public void removeNode(int x,int y) {
		
		Node noTemp = first; 
		
		while (noTemp != null && (noTemp.element1 != x || noTemp.element2 != y)) {       
				noTemp = noTemp.next;     
		}    
		if(noTemp != null)
		{   
			if (first.element1 == noTemp.element1 && first.element2 == noTemp.element2)
			{     
				first = first.next;     
				numberNos--;     
			}
			else
			{     
				noTemp.prev.next = noTemp.next; 
				if(noTemp.next != null)
				{
					noTemp.next.prev = noTemp.prev;  
				}
				numberNos--;     
			}     
			if(noTemp == last) 
			{     
				last = noTemp.prev;
			}
			noTemp = null; 
		}
	}          
    
	/**
	 *Remove 3 nodos.
	 */
	public void removeNode(int x,int y, int z) {
		
		Node noTemp = first; 
		
		/**
		 * Busca nodo na posição caso encontra um dos 3 atribui valores a temporario 
		 * */
		while ( noTemp != null && (noTemp.element1 != x || noTemp.element2 != y || noTemp.element3 != z) ) {     
			
				noTemp = noTemp.next;     
		}    
		if(noTemp != null)
		{ 
			
			if (first.element1 == noTemp.element1 && first.element2 == noTemp.element2 && first.element3 == noTemp.element3) 
			{     
				first = first.next;     
				numberNos--;    
				
			}
			else
			{     
				noTemp.prev.next = noTemp.next; 
				if(noTemp.next != null)
				{
					noTemp.next.prev = noTemp.prev;    
				}
				numberNos--;     
			}     
			if(noTemp == last) 
			{     
				last = noTemp.prev;
			}
			noTemp = null; 
		}
	}          
    
	/**
	 * Faz uma busca por um elemento da lista caso encontre retorna verdadeiro.
	 */ 
    public boolean searchOne(int elem) {
    	
		Node noTemp = first;   
		
		while (noTemp != null) {  
			
			if(noTemp.element == elem)
			{     
				return true;    
			} 
			noTemp = noTemp.next;     
		}
		return false;         
	} 
    
    /**
	 * Faz uma busca por dois elementos caso os dois retorna verdadeiro do contrario falso .
	 */  
	public boolean searchTwo(int x,int y) {     
		
		Node noTemp = first;     
		
		while (noTemp != null) { 
			
			if(noTemp.element1 == x && noTemp.element2 == y)
			{     
				return true;    
			} 
			noTemp = noTemp.next;     
		}
		return false;         
	}     
	
	/**
	 * Display para um nodo.
	 */	
	public void displayOne() {
		
		Node noTemp = first;
		
		if(noTemp == null)
		{
			// System.out.println("Lista Vazia");
		}
		else
		{
			while(noTemp != null) {     
				// System.out.print(noTemp.element + " ");   
				noTemp = noTemp.next;     
			} 	
		}
		// System.out.println();	   
	} 

	/**
	 * Display para um nodo.
	 */
	public void displayDouble() {  
		
		NodeDouble noTemp = firstDouble;
		
		if(noTemp == null)
		{
			// System.out.println("Lista Vazia");
			
		}
		else
		{
			while(noTemp !=null) {     
				// System.out.println(noTemp.elementDouble + " ");   
				noTemp = noTemp.nextDouble;     
			} 	
		}
		// System.out.println();	   
	} 
	/**
	 * Display para dois nodos.
	 */
	public void displayTwo() { 
		
		Node noTemp = first;
		
		if(noTemp == null)
		{
			// System.out.println("Lista Vazia");
			
		}
		else
		{
			while(noTemp != null) { 
				
				// System.out.println("["+noTemp.element1+"]["+noTemp.element2+"]");     
				noTemp = noTemp.next;     
			} 	
		}
		// System.out.println();
	}

	/**
	 * Display para três nodos.
	 */
	public void displayThree() { 
		
		Node noTemp = first;
		
		if(noTemp == null)
		{
			// System.out.println("Lista Vazia");
		}
		else
		{
			while(noTemp !=null) {     
				// System.out.println("["+noTemp.element1+"]["+noTemp.element2+"]["+noTemp.element3+"]");     
				noTemp = noTemp.next;     
			} 	
		}
		// System.out.println();
	}

	/**
	 * Retorna um elemento inteiro.
	 */	
	public int retElement(int elem) {
		
		Node noTemp = first; 
		int i = 0;
		
		while(noTemp != null){    
			if(i == elem) 
			{
				return noTemp.element;
			}
			noTemp = noTemp.next; 
			i++;    
		}
		return 0;   
	}

	/**
	 *Retorna um elemento (ponto flutuante).
	 */
	public double retElementDouble(int elem) {
		
		NodeDouble noTemp = firstDouble; 
		int i = 0;
		
		while(noTemp!= null) { 
			
			if(i == elem) 
			{
				return noTemp.elementDouble;    
			}
			noTemp = noTemp.nextDouble; 
			i++;    
		}
		return 0;   
	}
	
	/**
	 *Retorna o elemento 1 do nodo.
	 */
	public int retElementOne(int elem1) {

		Node noTemp = first; 
		
		int i = 0;
		
		while(noTemp!= null) {   

			if(i == elem1) 
			{
				return noTemp.element1;   
			}
			noTemp = noTemp.next; 
			i++;    
		}
		return 0;   
	} 

	/**
	 *retorna o elemento 2 do nodo.
	 */	
	public int retElementTwo(int elem2) {
		
		Node noTemp = first; 
		int i = 0;
		
		while(noTemp!= null) { 
			
			if(i == elem2) 
			{
				return noTemp.element2; 
			}
			noTemp = noTemp.next; 
			i++;    
		}
		return 0;   
	} 

	/**
	 *Retorna o elemento 3 do nodo.
	 */
	public int retElementThree(int elem3) {
		
		Node noTemp = first; 
		int i = 0;
		
		while(noTemp!= null) { 
			
			if(i == elem3) 
			{
				return noTemp.element3;
			}
			noTemp = noTemp.next; 
			i++;    
		}
		return 0;   
	} 
	
	/**
	 *Atualiza o valor do element.
	 */
	public int update(int elem,int x) { 
		
		Node noTemp = first; 
		int i = 0;
		
		while(noTemp!= null) {
			
			if(i == elem)
			{     
				noTemp.element = noTemp.element+x;
				return noTemp.element;
			}        
			noTemp = noTemp.next;    
			i++; 
		}
		return 0; 
	}
	
	/**
	 * Atualiza o valor do element3.
	 */
	public void updateTwo(int elem,int x){
		
		Node noTemp = first; 
		int i = 0;
		
		while(noTemp!= null) {
			
			if(i == elem) 
			{     
				noTemp.element3 = noTemp.element3+x;
			}        
			noTemp = noTemp.next;    
			i++; 
		}
	}

	/**
	 *Atualiza valor de element3 atribuindo x.
	 */
	public void updateThree(int elem,int x) {

		//three
		Node noTemp = first; 
		int i = 0;
		
		while(noTemp!= null){
			if(i == elem)
			{     
				noTemp.element3 = x;
			}        
			noTemp = noTemp.next;    
			i++; 
		}
	}

	/**
	 *Atualiza valor do element para x.
	 */
	public void upTransp(int elem,int x) {
		
		Node noTemp = first; 
		int i = 0;
		
		while(noTemp!= null) {
			
			if(i == elem)
			{     
				noTemp.element = x;
			}        
			noTemp = noTemp.next;    
			i++; 
		}
	}
}
