package front;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private JTextField SimsTxtField;
	
	private JComboBox<String> ScomboBox = new JComboBox<String>();
	private JComboBox<String> CaractcomboBox = new JComboBox<String>();
	private JComboBox<Double> AlphaComboBox = new JComboBox<Double>();
	private JComboBox<Double> BethaComboBox = new JComboBox<Double>();
	
	private JCheckBox chckbxBetweennessCentrality;
	private JCheckBox chckbxNodeDegree;
	
	private JButton btnSimulate = new JButton("Simulate");
	
	private JLabel lblStatus = new JLabel("");
	
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
		
		Double i;
		
		MainWindow = new JFrame("Topologies Generator");
		
		//cria e organiza janelas 
		JFrame frame = new JFrame("Topologies Generation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MainWindow.setBounds(230,100, 630, 378);
		MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		/**
		 * Número de nós/vertices do grafo
		 */
		JLabel lblNodes = new JLabel("N");
		lblNodes.setBounds(35, 32, 70, 20);
		lblNodes.setToolTipText("Number of nodes");
		
		NodesTxtField = new JTextField();
		NodesTxtField.setBounds(35, 57, 70, 20);
		NodesTxtField.setText("10");
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
		DmaxTxtField.setText("4");
		
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
		XTxtField.setText("20");
		
		/**
		 * R : número de regiões da matriz.
		 */
		
		
		JLabel lblRegions = new JLabel("Regions");
		lblRegions.setBounds(380, 155, 80, 24);
		MainWindow.getContentPane().add(lblRegions);
		
		JLabel lblRBreadth = new JLabel("Breadth");
		lblRBreadth.setBounds(400, 187, 70, 14);
		lblRBreadth.setToolTipText("Number of regions");
		
		BreadthTxtField = new JTextField();
		BreadthTxtField.setBounds(400, 210, 70, 20);
		BreadthTxtField.setColumns(10);
		BreadthTxtField.setText("1");
		
		/**
		 * Comprimento da região 
		 */
		JLabel lblRLength = new JLabel("Length");
		lblRLength.setBounds(500, 187, 70, 14);
		lblRLength.setToolTipText("Number of regions");
		
		LengthTxtField = new JTextField();
		LengthTxtField.setBounds(500, 210, 70, 20);
		LengthTxtField.setColumns(10);
		LengthTxtField.setText("20");
		
		/**
		 * Numero de simulações.
		 */
			
		JLabel lblNewLabel = new JLabel("\u03C6");
		lblNewLabel.setBounds(125, 88, 70, 14);
		lblNewLabel.setToolTipText("Number of simulations");
		
		SimsTxtField = new JTextField();
		SimsTxtField.setBounds(125, 109, 70, 20);
		SimsTxtField.setText("1");
		SimsTxtField.setColumns(10);
		btnSimulate.setBounds(35, 273, 170, 23);
		
		
		btnSimulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//simulate
				
					int R =  Integer.parseInt( LengthTxtField.getText() ) *  Integer.parseInt( BreadthTxtField.getText() );
					int N =  Integer.parseInt( NodesTxtField.getText() );
					
					/*
					 * Verifica condições de limite para região antes de simular 
					 */
					if( R  > (N* N) || R < 2*N ) {
						
						lblStatus.setText("Check the value of R it must be : "+2*N + " ≤ R ≤" +N*N);
						lblStatus.setBounds(35, 210, 334, 100);
						lblStatus.setToolTipText("Limit Region.");
						return;
					}
					else
					{
						lblStatus.setText("");
						lblStatus.setEnabled(false);
						try {
							simulateGraph();
						}
						catch(Exception e) {
							System.err.println(e);
							e.printStackTrace();
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
		

		lblStatus.setEnabled(false);
		lblStatus.setBounds(10, 257, 252, 14);
		MainWindow.getContentPane().add(lblStatus);

	}
	
	private void simulateGraph() throws Exception{
		
		GraphSim graphGen = new GraphSim();
		
		graphGen.setDmax(Integer.parseInt(DmaxTxtField.getText()));
		graphGen.setDmin(Integer.parseInt(DminTxtField.getText()));
		graphGen.setN(Integer.parseInt(NodesTxtField.getText()));
		graphGen.setL(Integer.parseInt(LTxtField.getText()));
		graphGen.setLength(Integer.parseInt(LengthTxtField.getText()));
		graphGen.setBreadth(Integer.parseInt(BreadthTxtField.getText()));
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
		if(chckbxNodeDegree.isSelected())
		{
			graphGen.setDegree(true);
		}
		
		lblStatus.setEnabled(true);
		
		
		try 
		{
			graphGen.init();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		

		lblStatus.setText("Simulation done!");
	}
}
