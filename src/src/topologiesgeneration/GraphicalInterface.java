package topologiesgeneration;

import javax.swing.*;

public class GraphicalInterface {

		public int opc(){
			
			int op = JOptionPane.showConfirmDialog(null,"Deseja calcular o h medio e h medio linha?");
			
			if (op == JOptionPane.YES_OPTION) 
			{
				return 1;
			}
			else if(op == JOptionPane.NO_OPTION)
			{
				return 2;
			}
			return 0;
		}
	
/*	public static void main (String [] args){
		interfaceGraf a;
		a = new interfaceGraf();
		a.opc();
	}
*/
}
