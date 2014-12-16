package front;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import topologiesgeneration.GraphSim;

public class Main {

	private JFrame MainWindow;
	
	private JTextField NodesTxtField; //vertices
	private JTextField DmaxTxtField; //maximo
	private JTextField DminTxtField;//minimo
	private JTextField LTxtField;
	private JTextField LengthTxtField;//R-Length
	private JTextField YTxtField;
	private JTextField XTxtField;
	private JTextField BreadthTxtField;//R-Breadth
	private JTextField nRegionsTxtField;
	private JTextField SimsTxtField;
	
	private JComboBox<String> ScomboBox = new JComboBox<String>();
	private JComboBox<String> CaractcomboBox = new JComboBox<String>();
	private JComboBox<Double> AlphaComboBox = new JComboBox<Double>();
	private JComboBox<Double> BethaComboBox = new JComboBox<Double>();
	
	private JCheckBox chckbxBetweennessCentrality;
	private JCheckBox chckbxRegions;
	private JCheckBox chckbxNodeDegree;
	
	private JButton btnSimulate = new JButton("Simulate");
	
	private JLabel lblStatus = new JLabel("");
	
	public int count;
	/**
	 * Launch the application.
	*/
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
				{
					Main window = new Main();
					window.MainWindow.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		
		/**
		 * Inicializa variaveis 
		*/
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		count = 0;
		Double i;
		
		MainWindow = new JFrame("TOPOLOGIES GENERATION");
		
		//cria e organiza janelas 
		JFrame frame = new JFrame("TOPOLOGIES GENERATION");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MainWindow.setBounds(230,100, 625, 378);
		MainWindow.setTitle("TOPOLOGIES GENERATION");
		MainWindow.setBackground(new Color(234, 234, 234));
		
		MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		/**
		 * Número de nós/vertices do grafo
		 */
		JLabel lblNodes = new JLabel("N");
		lblNodes.setBounds(35, 32, 70, 20);
		lblNodes.setToolTipText("Number of nodes");
		
		NodesTxtField = new JTextField();
		NodesTxtField.setBounds(35, 57, 70, 20);
		NodesTxtField.setText("20");
		NodesTxtField.setColumns(10);
		
		/**
		 * Número máximo de arestas do grafo
		 */
		
		//label
		
		JLabel lblMaxDegree = new JLabel("\u0394(G)");
		lblMaxDegree.setBounds(125, 32, 70, 14);
		lblMaxDegree.setToolTipText("Maximum degree");
		
		//campo 
		DmaxTxtField = new JTextField();
		DmaxTxtField.setBounds(125, 57, 70, 20);
		DmaxTxtField.setColumns(10);
		DmaxTxtField.setText("2");
		
		/**
		 * Número mínimo de arestas do grafo
		 */
		
		JLabel lblMinDegree = new JLabel("\u03B4(G)");
		lblMinDegree.setBounds(215, 32, 70, 14);
		lblMinDegree.setToolTipText("Minimum degree");
		
		DminTxtField = new JTextField();
		DminTxtField.setBounds(215, 57, 70, 20);
		DminTxtField.setColumns(10);
		DminTxtField.setText("2");
		
		/**
		 * Número máximo de links
		 */
		
		JLabel lblL = new JLabel("\u03BD");
		lblL.setBounds(395, 32, 70, 14);
		lblL.setToolTipText("Max link length");
		
		LTxtField = new JTextField();
		LTxtField.setBounds(395, 57, 70, 20);
		LTxtField.setColumns(10);
		LTxtField.setText("6");
		
		/**
		 * Espaçamento entre dois nós
		 */

		JLabel lblY = new JLabel("\u03B9");
		lblY.setBounds(515, 88, 70, 14);
		lblY.setToolTipText("Spacing between 2 nodes");
		
		YTxtField = new JTextField();
		YTxtField.setBounds(515, 109, 70, 20);
		YTxtField.setColumns(10);
		YTxtField.setText("1");
		
		/**
		 * Tipo de distribuição : variado/uniforme
		 */
		JLabel lblS = new JLabel("S");
		lblS.setBounds(395, 88, 100, 14);
		lblS.setToolTipText("Distribution type");
		ScomboBox.setBounds(395, 109, 100, 20);
		ScomboBox.addItem("Uniform");
		ScomboBox.addItem("Varied");
		ScomboBox.setSelectedIndex(1);
		
		/**
		 * Area : comprimento X altura
		 */
		JLabel lblArea = new JLabel("Area");
		lblArea.setBounds(35, 88, 70, 14);
		lblArea.setToolTipText("Size of the area");
		
		XTxtField = new JTextField();
		XTxtField.setBounds(35, 109, 70, 20);
		XTxtField.setColumns(10);
		XTxtField.setText("40");
		
		/**
		 * R : número de regiões da matriz.
		 */
		
		
		JLabel lblRegions = new JLabel("Regions");
		lblRegions.setBounds(380, 155, 80, 24);
		MainWindow.getContentPane().add(lblRegions);

		chckbxRegions = new JCheckBox("Enable flexible regions");
		chckbxRegions.setBounds(396, 185, 300, 23);
		MainWindow.getContentPane().add(chckbxRegions);
		
		chckbxRegions.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					count = 1;
					BreadthTxtField.setEnabled(true);
					LengthTxtField.setEnabled(true);
					BreadthTxtField.setText("4");
					LengthTxtField.setText("10");
					
					nRegionsTxtField.setEnabled(false);
					nRegionsTxtField.setText("0");
					
				} else 
				{
					count = 0;
					nRegionsTxtField.setEnabled(true);
					
					BreadthTxtField.setEnabled(false);
					LengthTxtField.setEnabled(false);
					
					BreadthTxtField.setText("0");
					LengthTxtField.setText("0");
					nRegionsTxtField.setText("1");
				}
				
			}
		});
		
		JLabel lblRBreadth = new JLabel("Breadth");
		lblRBreadth.setBounds(400, 185, 70, 14);
		lblRBreadth.setToolTipText("Number of regions");
		
		BreadthTxtField = new JTextField();
		BreadthTxtField.setBounds(400, 212, 70, 20);
		BreadthTxtField.setColumns(10);
		BreadthTxtField.setText("4");
		BreadthTxtField.setEnabled(false);
		
		if(count == 0)
		{
			BreadthTxtField.setText("0");
			
		}
		else
		{
			BreadthTxtField.setText("4");
		}
		
	
		/**
		 * Comprimento da região 
		 */
		JLabel lblRLength = new JLabel("Length");
		lblRLength.setBounds(500, 185, 70, 14);
		lblRLength.setToolTipText("Number of regions");
		
		LengthTxtField = new JTextField();
		LengthTxtField.setBounds(500, 212, 70, 20);
		LengthTxtField.setColumns(10);
		LengthTxtField.setText("10");
		LengthTxtField.setEnabled(false);
		
		if(count == 0)
		{
			LengthTxtField.setText("0");
			
		}
		else
		{
			LengthTxtField.setText("10");
		}
		/**
		 * Número de regiões 
		 */
		JLabel lblnRegions = new JLabel("Number of regions");
		lblnRegions.setBounds(400, 250, 150, 14);
		lblnRegions.setToolTipText("Number of regions");
		
		nRegionsTxtField = new JTextField();
		nRegionsTxtField.setBounds(400, 278, 70, 20);
		nRegionsTxtField.setColumns(10);
		nRegionsTxtField.setText("1");
		nRegionsTxtField.setEnabled(true);
		
		if(count == 0)
		{
			nRegionsTxtField.setText("1");
		}
		else
		{
			nRegionsTxtField.setText("0");
		}
		
		/**
		 * Numero de simulações.
		 */
			
		JLabel lblNewLabel = new JLabel("\u03C6");
		lblNewLabel.setBounds(125, 88, 70, 14);
		lblNewLabel.setToolTipText("Number of simulations");
		
		SimsTxtField = new JTextField();
		SimsTxtField.setBounds(125, 109, 70, 20);
		SimsTxtField.setText("2");
		SimsTxtField.setColumns(10);
		btnSimulate.setBounds(35, 273, 170, 23);
		
		lblStatus.setBounds(10, 257, 252, 14);
		lblStatus.setEnabled(true);
		MainWindow.getContentPane().add(lblStatus);
		
		
		btnSimulate.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				//simulate
				lblStatus.setText("Simulating...");
				lblStatus.setBounds(230, 245, 320, 80);
				
					if(count == 1)
					{
						int R =  Integer.parseInt( LengthTxtField.getText() ) *  Integer.parseInt( BreadthTxtField.getText() );
						int N =  Integer.parseInt( NodesTxtField.getText() );
						
						/*
						 * Verifica condições de limite para região antes de simular 
						 */
						if( R  > (N* N) || R < 2*N )
						{
							
							lblStatus.setText("Check the value of R it must be : "+2*N + " ≤ R ≤" +N*N);
							lblStatus.setBounds(250, 280, 320, 80);
							lblStatus.setToolTipText("Limit Region.");
							return;
						}
						else
						{
							executaComDelay();
							
						}
					}
					else
					{
				
//						lblStatus.setText("wtf");
						
						int R = Integer.parseInt( XTxtField.getText() );
						int N =  Integer.parseInt( NodesTxtField.getText() );
						
						if( R  > (N * N) || R < 2*N ) 
						{
							lblStatus.setText("Check the value of R it must be : "+2*N + " ≤ R ≤" +N*N);
							lblStatus.setBounds(250, 280, 320, 80);
							lblStatus.setToolTipText("Limit Region.");
							return;
						}
						else
						{
//							lblStatus.setText("wth");
							executaComDelay();
						}
					}
					
					
				
			}
		});
		
		MainWindow.getContentPane().setLayout(null);
		MainWindow.getContentPane().add(lblNodes);
		MainWindow.getContentPane().add(NodesTxtField);
		MainWindow.getContentPane().add(DmaxTxtField);
		MainWindow.getContentPane().add(lblMaxDegree);
		MainWindow.getContentPane().add(DminTxtField);
		MainWindow.getContentPane().add(lblMinDegree);
		MainWindow.getContentPane().add(LTxtField);
		MainWindow.getContentPane().add(lblL);
		MainWindow.getContentPane().add(YTxtField);
		MainWindow.getContentPane().add(lblY);
		MainWindow.getContentPane().add(lblS);
		MainWindow.getContentPane().add(ScomboBox);
		MainWindow.getContentPane().add(XTxtField);
		MainWindow.getContentPane().add(lblArea);
		MainWindow.getContentPane().add(BreadthTxtField);
		MainWindow.getContentPane().add(lblRBreadth);
		MainWindow.getContentPane().add(LengthTxtField);
		MainWindow.getContentPane().add(nRegionsTxtField);
		MainWindow.getContentPane().add(lblnRegions);
		MainWindow.getContentPane().add(lblRLength);
		MainWindow.getContentPane().add(lblNewLabel);
		MainWindow.getContentPane().add(SimsTxtField);
		MainWindow.getContentPane().add(btnSimulate);
		
		JLabel lblCaracterizationType = new JLabel("Caracterization type");
		lblCaracterizationType.setBounds(215, 88, 159, 14);
		MainWindow.getContentPane().add(lblCaracterizationType);
		
		
		CaractcomboBox.setBounds(215, 109, 159, 20);
		CaractcomboBox.addItem("Specific");
		CaractcomboBox.addItem("General");
		CaractcomboBox.setSelectedIndex(1);
		MainWindow.getContentPane().add(CaractcomboBox);
		
		chckbxBetweennessCentrality = new JCheckBox("Betweenness Centrality");
		chckbxBetweennessCentrality.setBounds(35, 185, 300, 23);
		MainWindow.getContentPane().add(chckbxBetweennessCentrality);
		
		JLabel lblMeasures = new JLabel("Measures");
		lblMeasures.setBounds(35, 155, 80, 24);
		MainWindow.getContentPane().add(lblMeasures);
		
		chckbxNodeDegree = new JCheckBox("Node Degree");
		chckbxNodeDegree.setBounds(35, 211, 124, 23);
		MainWindow.getContentPane().add(chckbxNodeDegree);
		
		/**
		 *  parâmetro para probabilidade de waxman. Alfa.
		 */
		JLabel lblAlpha = new JLabel("\u03B1");
		lblAlpha.setToolTipText("Waxman Parameter");
		lblAlpha.setBounds(305, 32, 70, 14);
		MainWindow.getContentPane().add(lblAlpha);
		
		AlphaComboBox.setBounds(305, 57, 70, 20);
		
		for(i = 1d; i <= 10; i++) {
			
			AlphaComboBox.addItem(i/10);
		}
		AlphaComboBox.setSelectedIndex(3);
		MainWindow.getContentPane().add(AlphaComboBox);
		
		
		/**
		 * parâmetro para probabilidade de waxman.Beta.
		 */
		JLabel lblBetha = new JLabel("\u03B2");
		lblBetha.setToolTipText("Waxman Parameter");
		lblBetha.setBounds(485, 32, 70, 14);
		MainWindow.getContentPane().add(lblBetha);
		

		BethaComboBox.setBounds(485, 57, 100, 20);
		
		for(i = 1d;i <= 10; i++){
			BethaComboBox.addItem(i/10);
		}
		BethaComboBox.setSelectedIndex(3);
		MainWindow.getContentPane().add(BethaComboBox);
		

		

	}
	
	private void executaComDelay() {
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	
		            	try 
						{
							simulateGraph();
						}
						catch(Exception e) {
							e.printStackTrace();
							lblStatus.setText("Couldn't generate topology!");
							lblStatus.setBounds(250, 280, 320, 80);
						}
		            }
		        }, 
		        1000
		);
	}
	
	private void simulateGraph() throws Exception{
		
		GraphSim graphGen = new GraphSim();
		
		graphGen.setDmax(Integer.parseInt(DmaxTxtField.getText()));
		graphGen.setDmin(Integer.parseInt(DminTxtField.getText()));
		graphGen.setN(Integer.parseInt(NodesTxtField.getText()));
		graphGen.setL(Integer.parseInt(LTxtField.getText()));
		
		
		graphGen.setLength(Integer.parseInt(LengthTxtField.getText()));
		graphGen.setBreadth(Integer.parseInt(BreadthTxtField.getText()));
		graphGen.setNRegions(Integer.parseInt(nRegionsTxtField.getText()));
		
		graphGen.setY(Integer.parseInt(YTxtField.getText()));
		graphGen.setX(Integer.parseInt(XTxtField.getText()));
		
		/**
		 * Região 
		 */
		graphGen.setR( Integer.parseInt( LengthTxtField.getText() ), Integer.parseInt( BreadthTxtField.getText() ) );
		
		graphGen.setS(ScomboBox.getSelectedIndex());
		graphGen.setAlfa((AlphaComboBox.getSelectedIndex()+1)/10f);
		graphGen.setBeta((BethaComboBox.getSelectedIndex()+1)/10f);
		graphGen.setnSim(Integer.parseInt(SimsTxtField.getText()));
		graphGen.setCaracType(CaractcomboBox.getSelectedIndex());
		
		if(chckbxBetweennessCentrality.isSelected())
		{
			graphGen.setBc(true);
		}
		else
		{
			graphGen.setBc(false);
		}
		if(chckbxNodeDegree.isSelected())
		{
			graphGen.setDegree(true);
		}
		else
		{
			graphGen.setDegree(false);
		}
		
//		lblStatus.setText("jhgjgjghkjj.");
//		lblStatus.setBounds(250, 250, 320, 80);
		
		try 
		{
			graphGen.init();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		

		lblStatus.setText("Done.");
		lblStatus.setBounds(230, 245, 320, 80);
		
		count = 0;
	}
}
