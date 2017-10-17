
/*******************************************************/
/* $LAN=JAVA$                                          */
/* Term Project - Voronoi Diagram Algorithm            */
/* Author:                                             */
/* Student ID:                                         */
/* Version: 2017/10/16                                 */
/*******************************************************/


package voronoi;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.*;

public class Voronoi extends JFrame implements ActionListener, ItemListener {

	private JFileChooser fc; //�}���ɮ�
	private File f; //�ɮ�
	private BufferedReader br;
	
    JMenu Menu_main, Menu_action;
    JMenuBar JBar;
    JMenuItem JMItem_open, JMItem_output, JMItem_clean, JMItem_exit, JMItem_STS, JMItem_run, JMItem_test;
	JRadioButton[] JRB;
	JLabel JLbl_msg, JLbl_points;
	JPanel panelMsg, panelDraw;
	JScrollPane JSP_points;
	
	ButtonGroup bg;
	
	int x, y, pointCount = 0, test;
	int pointX[] = new int[1000];
	int pointY[] = new int[1000];
	boolean click_mode = true, readReady = false;
	
	
	//Constructor
	public Voronoi(String title) {
		
			fc = new JFileChooser();
			
			//�إߥ\����
	        setJMenuBar(getFunctionMenuBar());
	        
	        JLbl_msg = new JLabel();
	        JLbl_msg.setText("There are "+ pointCount + " points now.");
	        JLbl_msg.setPreferredSize(new Dimension(500, 100));
	        JLbl_msg.setFont(new Font("�з���", Font.CENTER_BASELINE, 25));
	        
	        panelMsg = new JPanel(); //Message Panel
	        panelDraw = new JPanel(); //Drawing Panel
	        
	        //Msg--------------------------------------
	        panelMsg.setPreferredSize(new Dimension(100, 100));
	        panelMsg.setBackground(Color.YELLOW);
	        
	        JRB = new JRadioButton[2];
	        JRB[0] = new JRadioButton("�ƹ��I���Ҧ�", true);
	        JRB[0].setBackground(Color.YELLOW );
	        JRB[0].setFont(new Font("�з���", Font.CENTER_BASELINE, 25));
	        JRB[0].addItemListener(this);
	        
	        JRB[1] = new JRadioButton("�ɮ׶פJ�Ҧ�", false);
	        JRB[1].setBackground(Color.YELLOW );
	        JRB[1].setFont(new Font("�з���", Font.CENTER_BASELINE, 25));
	        JRB[1].addItemListener(this);
	        
	        ButtonGroup bg = new ButtonGroup();
	        bg.add(JRB[0]);
	        bg.add(JRB[1]);
	        
	        panelMsg.add(JLbl_msg);
	        panelMsg.add(JRB[0]);
	        panelMsg.add(JRB[1]);
	        
	        //Draw--------------------------------------
	        panelDraw.setBorder(BorderFactory.createMatteBorder(
	        	    2,2,2,2,Color.green)); //border
	        
	        panelDraw.addMouseListener(new MouseAdapter(){
		         public void mousePressed(MouseEvent e){  //�갵�ƹ����I���ƥ�
			            Graphics g = panelDraw.getGraphics();
			            int x = e.getX();
			            int y = e.getY();
			            
			            if(click_mode) {
			            	g.setColor(Color.BLACK);
				            g.fillOval(x, y,4, 4);
				            pointX[pointCount] = x;
				            pointY[pointCount] = y;
				            pointCount++;
				            
				            if(pointCount == 1)
				            	JLbl_msg.setText("There are "+ pointCount + " point now.");
				            else
				            	JLbl_msg.setText("There are "+ pointCount + " points now.");
			            }
			            else {
			            	JFrame JF = new JFrame();
			            	JOptionPane.showMessageDialog(JF,"�b�ɮ׶פJ�Ҧ��U�A�ƹ����i�I���e���C\n�ШϥΥ��W�����פJ�ɮץH�~��C",
	                                  "���~", JOptionPane.ERROR_MESSAGE);
			            }
			         }
			      });
	        

	        panelDraw.addKeyListener(new prockey());
	        panelDraw.setFocusable(true);
	        
	        add(panelMsg, BorderLayout.NORTH);
	        add(panelDraw, BorderLayout.CENTER);
	        
	        //Frame(Window) Settings
	        setTitle("Term Project");
			setLocation(200, 200);
			setSize(1000, 1000);
			setLocationRelativeTo(null);
			setResizable(false);
			setVisible(true);
	    	        
	}
	
	/***��@����B�z��k***/
    class prockey extends KeyAdapter
    {
        public void keyPressed(KeyEvent e)
        {
            if (e.getKeyCode() == 10 && readReady) //Enter
            {
            	try {
            		
            		panelDraw.repaint();
            		
            		//�Ȱ�0.25��===========================
//        			try
//        			{
//        				Thread.sleep(1000);
//        			}catch(Exception e2){}
        			
					getData(br);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        }
    }
		
	//�إߥ\����
	public JMenuBar getFunctionMenuBar() {
		
		JBar = new JMenuBar();
		
        Menu_main = new JMenu("�\��");
        Menu_main.setFont(new Font("�з���", Font.CENTER_BASELINE, 20));
        Menu_action = new JMenu("�ʧ@");
        Menu_action.setFont(new Font("�з���", Font.CENTER_BASELINE, 20));

        //Menu_main =========
        JMItem_open = new JMenuItem("�פJ�ɮ�...", new ImageIcon("./img/folder.png"));
        JMItem_output = new JMenuItem("��X��r��", new ImageIcon("./img/output.png"));
        JMItem_clean = new JMenuItem("�M�ŵe��", new ImageIcon("./img/clean.png"));
        JMItem_exit = new JMenuItem("���}�{��", new ImageIcon("./img/exit.png"));
        
        JMItem_open.setEnabled(false);
        
        JMItem_open.addActionListener(this);
        JMItem_output.addActionListener(this);
        JMItem_clean.addActionListener(this);
        JMItem_exit.addActionListener(this);
             
        Menu_main.add(JMItem_open);
        Menu_main.add(JMItem_output);
        Menu_main.add(JMItem_clean);
        Menu_main.add(JMItem_exit);
        
        //Menu_action =========
        JMItem_STS = new JMenuItem("Step by Step");
        JMItem_run = new JMenuItem("Run");
        JMItem_test = new JMenuItem("Test");
        
        JMItem_STS.addActionListener(this);
        JMItem_run.addActionListener(this);
        JMItem_test.addActionListener(this);
        
        Menu_action.add(JMItem_STS);
        Menu_action.add(JMItem_run);
        Menu_action.add(JMItem_test);
        
        JBar.add(Menu_main);
        JBar.add(Menu_action);
        
        return JBar;
	}

	//�����s�ƥ�
	public void itemStateChanged(ItemEvent e) { 
		
		//Mode Select
        if(e.getItem() == JRB[0]) { //click
        	clean();
        	JMItem_open.setEnabled(false);
        	click_mode = true;
        }
        else if(e.getItem() == JRB[1]) { //file
        	clean();
        	JMItem_open.setEnabled(true);
        	click_mode = false;
        }
    } 
		    
	//�����ƥ�
	public void actionPerformed(ActionEvent e) {
		try{
			
			if(e.getSource() == JMItem_open) { //Open file
				
				int status = fc.showOpenDialog(null);
			
	    		if(status == JFileChooser.APPROVE_OPTION){		
	    			//get selected file
					f = fc.getSelectedFile();	
				}
	    		ReadFile(f);
			}
			
			else if(e.getSource() == JMItem_clean){ //Clean
				clean();
			}
			
			else if(e.getSource() == JMItem_exit){ //Exit
				dispose();
				System.exit(0);	//�����{��
			}
			
			else if(e.getSource() == JMItem_test){ 
				showPoints();
			}
			
		}catch(IOException ex) {
			System.out.println(ex);
		}
	}
	
	//Ū��
	public void ReadFile(File f) throws IOException{
			
		FileReader fileReader = new FileReader(f);
			
		br = new BufferedReader(fileReader);

		readReady = true;
		JLbl_msg.setText("Open finished. " + readReady);
		panelDraw.requestFocus();
		return;
	}
	
	public void getData(BufferedReader br) throws IOException {
		
		test++;
		readReady = false;
		String strs[];
		String line;
		line = br.readLine();
		pointCount = Integer.parseInt(line);
		
		if(pointCount == 0) {
			JLbl_msg.setText("EOF");
			br.close();
			return;
		}
		
		Graphics g = panelDraw.getGraphics();
		for(int i = 0; i < pointCount; i++) {
			line = br.readLine();
			strs = line.split(" "); //�H�r���Ϲj�A�Φ��ƭӰ}�C���
			pointX[i] = Integer.parseInt(strs[0]);
			pointY[i] = Integer.parseInt(strs[1]);
			g.setColor(Color.BLACK);
            g.fillOval(pointX[i], pointY[i], 4, 4);
            JLbl_msg.setText(test+" ");
		}
		
		readReady = true;
	//	JLbl_msg.setText(test+" ");
	}
	
	public void showPoints() {
		JFrame Frame_points = new JFrame();
		Frame_points.setTitle("All Points");
		Frame_points.setSize(400, 1000);
		Frame_points.setLocationRelativeTo(null);
		Frame_points.setResizable(false);
		Frame_points.setVisible(true);
		
		StringBuilder str = new StringBuilder("<html><body>");
		
		JLbl_points = new JLabel();
		for(int i = 0; i < pointCount; i++) {
			str.append(pointX[i] + ", " + pointY[i] + "<br>");
		}
		
		str.append("</body></html>");
		JLbl_points.setText(String.valueOf(str));
		JLbl_points.setFont(new Font("�з���", Font.CENTER_BASELINE, 25));
		
		JPanel jp = new JPanel();
		jp.add(JLbl_points, BorderLayout.NORTH);
		JSP_points = new JScrollPane(jp);
	
	//	JSP_points = new JScrollPane(JLbl_points);
		Frame_points.add(JSP_points);
	}
	
	//reset
	public void clean(){
		repaint();
		pointCount = 0;
		JLbl_msg.setText("There are "+ pointCount + " points now.");
	}
	
	//�D�{��
	public static void main(String argv[]){
		Voronoi app = new Voronoi("Term Project");
	}
}
