/*******************************************************/
/* $LAN=JAVA$                                          */
/* Term Project - Voronoi Diagram Algorithm            */
/* Author: ���a��	                                       */
/* Student ID: M063040005                              */
/* Version: 2017/11/15                                 */
/*******************************************************/
package voronoi;

import java.awt.*;
import java.awt.event.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import voronoi.Panel;

public class Voronoi extends JFrame implements ActionListener, ItemListener {

	private JFileChooser fc; //�}���ɮ�
	private File f; //�ɮ�
	private BufferedReader br;
	private BufferedWriter bw;
	
    JMenu Menu_main, Menu_action;
    JMenuBar JBar;
    JButton clear;
    JMenuItem JMItem_open, JMItem_output, JMItem_clean, JMItem_exit, JMItem_STS, JMItem_run, JMItem_test;
	JRadioButton[] JRB;
	JLabel JLbl_msg, JLbl_points;
	JLabel button_step, button_run, button_open, button_output, button_clean, button_exit; //Button
	JPanel panelMsg;
	Panel panelDraw;
	JScrollPane JSP_points;
	JTextArea dataMsg;
	
	private JPanel contentPane;
	ButtonGroup bg;
	
	public final static int MAX = 900;
	public final static int MIN = 0;
	int point[][] = new int[500][2];
	int pointCount = 0;
	int step = -1;
	int part = 0;
	int dir = 1;
	
	ArrayList<Point> point_list = new ArrayList<Point>();
	
	boolean click_mode = true, readReady = false, run = false, isStep = false;
	String points = "", lines = "", totals = "";
	
	//�غc�l
	public Voronoi(String title) {
		fc = new JFileChooser("./");
		
		//�إߥ\����
        setJMenuBar(getFunctionMenuBar());
        
        contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

        //Panel_Msg--------------------------------------
        initMsgPanel();

        //Data Msg(�I����)----------------------------------
        JScrollPane databoard = new JScrollPane();
        databoard.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        dataMsg = new JTextArea();
        dataMsg.setFont(new Font("",Font.BOLD, 16));
        databoard.setViewportView(dataMsg);
        
        databoard.setBounds(0, 100, 300, 900);
        contentPane.add(databoard);
        
        //Panel_Draw--------------------------------------
        panelDraw = new Panel();
        panelDraw.setBounds(300,100,900,900);
        panelDraw.setBackground(Color.WHITE);
        
        panelDraw.addMouseListener(new MouseAdapter(){
	         public void mousePressed(MouseEvent e){  //�갵�ƹ����I���ƥ�
	        	 
	        	 	if(click_mode) {

	        	 		if(run) {
			        		  JFrame JF = new JFrame();
			        		  JOptionPane.showMessageDialog(JF, "�Ы��U�M����A���s�ާ@�C",
		                              "���~", JOptionPane.ERROR_MESSAGE);
			        		  return;
			        	  }
		            	
		            	Point p = e.getPoint();
		            	pointCount++;      
		                 
		            	updatePointMsg(p, p.x, p.y, panelDraw.point_list.size());

			            if(panelDraw.point_list.size() == 1)
			            	JLbl_points.setText(pointCount + " point");
			            else
			            	JLbl_points.setText(pointCount + " points");
		            }
		            else {
		            	JFrame JF = new JFrame();
		            	JOptionPane.showMessageDialog(JF,"�b�ɮ׶פJ�Ҧ��U�A�ƹ����i�I���e��",
                                  "���~", JOptionPane.ERROR_MESSAGE);
		            }
	        	 
		         }
		      });
        
        panelDraw.addKeyListener(new prockey());
        panelDraw.setFocusable(true);
        
        contentPane.add(panelDraw);
 
        //Frame(Window) Settings
        setTitle("Term Project");
		setLocation(200, 200);
		setSize(1200, 1100);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	//Panel_Msg ��l��
	public void initMsgPanel () {

		panelMsg = new JPanel();

        //�T��JLabel ***************************************
        JLbl_msg = new JLabel();
        JLbl_msg.setText("�i�H�b�e���W���N�I���A�Χ�H�ɮ׶פJ");
        JLbl_msg.setFont(new Font("�з���", Font.CENTER_BASELINE, 18));
        
        JLbl_msg.setBounds(400, 25, 700, 25);
        contentPane.add(JLbl_msg);
        
        //�Ҧ�����(�ƹ��BŪ��) *********************************
        JRB = new JRadioButton[2];
        JRB[0] = new JRadioButton("�ƹ��I���Ҧ�", true);
        JRB[0].setFont(new Font("�з���", Font.CENTER_BASELINE, 25));
        JRB[0].addItemListener(this);
        
        JRB[1] = new JRadioButton("�ɮ׶פJ�Ҧ�", false);
        JRB[1].setFont(new Font("�з���", Font.CENTER_BASELINE, 25));
        JRB[1].addItemListener(this);
        
        bg = new ButtonGroup();
        bg.add(JRB[0]);
        bg.add(JRB[1]);
        
        JRB[0].setBounds(500, 50, 200, 50);
        JRB[1].setBounds(700, 50, 200, 50);
        contentPane.add(JRB[0]);
        contentPane.add(JRB[1]);
        
        //�I�Ƭ���JLabel *************************************
        JLbl_points = new JLabel();
        JLbl_points.setText(pointCount + " points");
        JLbl_points.setFont(new Font("�з���", Font.CENTER_BASELINE, 20));
        
        JLbl_points.setBounds(1000, 60, 100, 30);
        contentPane.add(JLbl_points);
        
        //�\����s�� ******************************************
        JLabel L_run = new JLabel(); 
        L_run.setText("Run");
        L_run.setFont(new Font("�з���", Font.CENTER_BASELINE, 18));
        
        JLabel L_step = new JLabel(); 
        L_step.setText("Step by step");
        L_step.setFont(new Font("�з���", Font.CENTER_BASELINE, 18));
        
        JLabel L_open = new JLabel(); 
        L_open.setText("�פJ");
        L_open.setFont(new Font("�з���", Font.CENTER_BASELINE, 18));
        
        JLabel L_output = new JLabel(); 
        L_output.setText("��X");
        L_output.setFont(new Font("�з���", Font.CENTER_BASELINE, 18));
        
        JLabel L_clean = new JLabel();
        L_clean.setText("�M��");
        L_clean.setFont(new Font("�з���", Font.CENTER_BASELINE, 18));
        
        JLabel L_exit = new JLabel();
        L_exit.setText("���}");
        L_exit.setFont(new Font("�з���", Font.CENTER_BASELINE, 18));
        
        button_step = new JLabel(new ImageIcon("./img/step.png"), SwingConstants.LEFT);
        button_run = new JLabel(new ImageIcon("./img/run.png"), SwingConstants.LEFT);
        button_open = new JLabel(new ImageIcon("./img/folder.png"), SwingConstants.LEFT);
        button_output = new JLabel(new ImageIcon("./img/output.png"), SwingConstants.LEFT);
        button_clean = new JLabel(new ImageIcon("./img/clean.png"), SwingConstants.LEFT);
        button_exit = new JLabel(new ImageIcon("./img/exit.png"), SwingConstants.LEFT);
        
        //Location ***************************************
        //step by step
        button_step.addMouseListener(new MouseAdapter() {
	          @Override
	          public void mouseClicked(MouseEvent e) {
	        	  isStep = true;
	        	  runDraw();
	          }
	        });
        
        //run
        button_run.addMouseListener(new MouseAdapter() {
	          @Override
	          public void mouseClicked(MouseEvent e) {
	        	  isStep = false;
	        	  runDraw();
	          }
	        });
        
        //open
		button_open.addMouseListener(new MouseAdapter() {
	          @Override
	          public void mouseClicked(MouseEvent e) {
	        	  	try {
	        	  		if(!click_mode) {
	        	  			int status = fc.showOpenDialog(null);
		        			
		        			if(status == JFileChooser.APPROVE_OPTION){		
		        				//get selected file
		        				f = fc.getSelectedFile();	
		        				ReadFile(f);
		        			}
		        			else {
		        				JLbl_msg.setText("�ɮ�Ū������");
		        			}
		        	  									
	        	  		}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	          }
	        });
        
		//output
		button_output.addMouseListener(new MouseAdapter() {
	          @Override
	          public void mouseClicked(MouseEvent e) {
	        	  try {
	        		  if(run) {
		        		  int status = fc.showOpenDialog(null);
		        		  
	        			  if(status == JFileChooser.APPROVE_OPTION){	
	        				  f = fc.getSelectedFile();
	        				  outputFile(f);
	        			  }
	        			  else {
	        				  JLbl_msg.setText("�ɮ׿�X����");
	        			  }
	        		  }
	        	  
	        	  } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
		        	}
	        	  
	          }
	        });
		
		//clean
        button_clean.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
        		clean();
          }
        });
        
        //exit
        button_exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	dispose();
				System.exit(0);	//�����{��
            }
          });

        //Location ***************************************
        button_step.setBounds(40, 25, 150, 20);
        L_step.setBounds(80, 25, 150, 25);
        
        button_run.setBounds(240, 25, 100, 20);
        L_run.setBounds(280, 25, 40, 25);
        
        button_open.setBounds(40, 50, 100, 50);
        button_open.setEnabled(false);
        L_open.setBounds(80, 50, 40, 50);
        
        button_output.setBounds(140, 50, 100, 50);
        button_output.setEnabled(false);
        L_output.setBounds(180, 50, 40, 50);
        
        button_clean.setBounds(240, 50, 100, 50);
        L_clean.setBounds(280, 50, 40, 50);
        
        button_exit.setBounds(340, 50, 100, 50);
        L_exit.setBounds(380, 50, 40, 50);
        
        contentPane.add(button_step);
        contentPane.add(L_step);
        contentPane.add(button_run);
        contentPane.add(L_run);
        contentPane.add(button_open);
        contentPane.add(L_open);
        contentPane.add(button_output);
        contentPane.add(L_output);
        contentPane.add(button_clean);
        contentPane.add(L_clean);
        contentPane.add(button_exit);
        contentPane.add(L_exit);
        
        panelMsg.setBounds(0, 0, 1200, 100);
        contentPane.add(panelMsg);   
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
        JMItem_run= new JMenuItem("����", new ImageIcon("./img/run.png"));
        
        JMItem_STS.addActionListener(this);
        JMItem_run.addActionListener(this);
        
        Menu_action.add(JMItem_STS);
        Menu_action.add(JMItem_run);
        
        JBar.add(Menu_main);
        JBar.add(Menu_action);
        
        return JBar;
	}

	//����ƥ�B�z
    class prockey extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            if (e.getKeyCode() == 10 && readReady) { //Enter
					try {
						
						for (int i = point.length - pointCount; i < point.length; i++){
							point[i][0]=0;
							point[i][1]=0;
						}
						
						getData(br);
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}		
            }
        }
    }
	
	//�����s�ƥ�B�z(�Ҧ����)
	public void itemStateChanged(ItemEvent e) { 
		
		//Mode Select
        if(e.getItem() == JRB[0]) { //click
        	clean();
        	JLbl_msg.setText("�i�H���N�I���e��");
        	JMItem_open.setEnabled(false);
        	button_open.setEnabled(false);
        	click_mode = true;
        }
        else if(e.getItem() == JRB[1]) { //file
        	clean();
        	JLbl_msg.setText("��ܶפJ�A�}�Ҵ���");
        	JMItem_open.setEnabled(true);
        	button_open.setEnabled(true);
        	click_mode = false;
        }
    } 
		    
	//�����ƥ�(���)
	public void actionPerformed(ActionEvent e) {
		try{
			
			if (e.getSource() == JMItem_open) { //Open file
				
				int status = fc.showOpenDialog(null);
			
	    		if (status == JFileChooser.APPROVE_OPTION){		
	    			//get selected file
					f = fc.getSelectedFile();	
				}
	    		ReadFile(f);
			}
			
			else if (e.getSource() == JMItem_output){ //Output
				try {
	        		  if (run) {
		        		  int status = fc.showOpenDialog(null);
		        		  
	        			  if (status == JFileChooser.APPROVE_OPTION){	
	        				  f = fc.getSelectedFile();
	        				  outputFile(f);
	        			  }
	        			  else {
	        				  JLbl_msg.setText("�ɮ׿�X����");
	        			  }
	        		  }
	        	  
	        	  } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
		        	}
			}
			
			else if (e.getSource() == JMItem_clean){ //Clean
				clean();
			}
			
			else if (e.getSource() == JMItem_exit){ //Exit
				dispose();
				System.exit(0);	//�����{��
			}
			
			else if (e.getSource() == JMItem_run){ //Run
				

	        	  runDraw();
			}

		}catch (IOException ex) {
			System.out.println(ex);
		}
	}
	
	public void runDraw() {
		JLbl_msg.setText(""+isStep);
		
		if (pointCount == 0) {
	  		  JLbl_msg.setText("�S���I...");
	  		  return;
	  	}

		int count = panelDraw.point_list.size();
		ArrayList<Point> list = panelDraw.point_list;
		
		ArrayList<Point> p = new ArrayList<>();
		ArrayList<Point> p2 = new ArrayList<>();
		
		if (count <= 3)		
			draw(list);
		
		else { //divide 2 parts
			
			for (int i = 0; i < count/2; i++)
				p.add(list.get(i));
			
			for (int i = (count/2); i < count; i++)
				p2.add(list.get(i));
			
			p.sort(new PointCmpY());
			p2.sort(new PointCmpY());
			
			Point[] c_p = new Point[list.size()];
			for (int i = 0; i < list.size(); i++)
				c_p[i] = list.get(i);
			
			if (!isStep) { //run
				if(run) //����G������
					return;
				
				dir = 1; //left
				draw(p);
				
				dir = 2; //right
				draw(p2);
				
			//	panelDraw.drawConvex(ConvexHull.convex_hull(c_p), 3);
				panelDraw.drawHyperPlane(p, p2);
				updateMsg();
			}
			
			else { //Step by Step

				step = (step == 7) ? -1 : step;
				step++;
				run = false;
				
				switch (step) {
					case 0:
						panelDraw.reStep();
						JLbl_msg.setText("������" + p.size() + "�k" + p2.size());
						break;
					case 1:
						dir = 1; //left
						draw(p);
						JLbl_msg.setText("Group 1");
						break;
						
					case 2:
						dir = 2; //right
						draw(p2);
						JLbl_msg.setText("Group 2");
						break;
						
					case 3:
						panelDraw.drawConvex(p, 1);
						JLbl_msg.setText("Group 1 Convex Hull");
						break;
						
					case 4:
						panelDraw.drawConvex(p2, 2);
						JLbl_msg.setText("Group 2 Convex Hull");
						break;
						
					case 5:
						panelDraw.drawConvex(ConvexHull.convex_hull(c_p), 3);
						JLbl_msg.setText("Merge Convex Hull");
						break;
						
					case 6:
						panelDraw.drawHyperPlane(p, p2);
						updateMsg();
						JLbl_msg.setText("HyperPlane");
						break;
						
					case 7:
						panelDraw.hull = 0;
						panelDraw.repaint();
						JLbl_msg.setText("Erase convex hull");
						break;	
						
					default:
						break;
				}
			}
		}
	}

	public void draw(ArrayList<Point> list) {
		String msg = "";
		Line c = new Line();
		
		if (click_mode)
			JLbl_msg.setText("�i�H��X�ɮסA�Ϋ��M�����~��");
		else
			JLbl_msg.setText("�i�H��X�ɮסA�Ϋ�Enter�~��");

		switch(list.size()) {
			case 1:
				msg = "�@���I�L�k�c��Voronoi diagram";
				if(panelDraw.point_list.size() < pointCount)
					msg = msg + "(�����|���I)";
				
				JLbl_msg.setText(msg);
				return;
		
			case 2:
				PDouble pa = new PDouble();
				PDouble pb = new PDouble();

				pa.x = (double)list.get(0).x;
				pa.y = (double)list.get(0).y;
				pb.x = (double)list.get(1).x;
				pb.y = (double)list.get(1).y;

				TwoP L = new TwoP(pa, pb);
				
				if (L.isVertival()) { //�����u
					c.p_a.x = L.midpointX();
					c.p_a.y = 0;
					c.p_b.x = L.midpointX();
					c.p_b.y = 900;
				}	

				else if (L.interceptX() == 0) { //�����u
					c.p_a.x = 0;
					c.p_a.y = L.midpointY();
					c.p_b.x = 900;
					c.p_b.y = L.midpointY();
				}
				
				else 
					c = TwoP.getVerticalLine(L);
				
				updateLineMsg(c);
				break;
				
			case 3:
				PDouble p[] = new PDouble[3];	
				for (int i = 0; i <= 2; i++) { //�T���I
					p[i] = new PDouble();
					p[i].x = (double)(list.get(i).x);
					p[i].y = (double)(list.get(i).y);
				}
				
				Line sortingLine[] = getLineList(p); //�T��q�p��j
				Line mline[] = new Line[3]; //�T�䤤���u
				
	            for (int i = 0; i < 3; i++)
	            	mline[i] = sortingLine[i].mLine();

	            //�@�u���p
				if ((p[1].y - p[0].y) * (p[2].x - p[0].x) == (p[2].y - p[0].y) * (p[1].x - p[0].x)) {
					for (int i = 0; i < 2; i++) {
						c = mline[i].toBoundX();
						updateLineMsg(c);
					}
				}
				
				//�T����
				else {	
					PDouble circumcentre = getCircumcentre(p); //�~��
					int type = getTriangleType(sortingLine); //�T����  0:�U�� 1:�w�� 2:����
					PDouble verticalPoint = null; //��������
					
					//�D��������
	            	if (type == 2)
	            		for (int i = 0; i < 3; i++) //������̪��䪺����I
	            			if (!p[i].isEqual(sortingLine[2].p_a) && !p[i].isEqual(sortingLine[2].p_b)) 
	                        	verticalPoint = p[i]; //��������
   	
	            	for (int i = 0; i < 3; i++) {
            			if (i == 2) {
            				if (type == 1 && (circumcentre.x < 0 ||circumcentre.x > MAX))
            					continue;
            				c = getTriLine(circumcentre, mline[i], verticalPoint, type); //�~��, �����u, ��������, �T���Ϋ��A	
            			}
            			else
            				c = getTriLine(circumcentre, mline[i], verticalPoint, 0);	
            			updateLineMsg(c);
            		}
				}
				break;
				
			default:
				JLbl_msg.setText("�|�ݶ}�o..");
				break;
		}
		updateMsg();
		button_output.setEnabled(true);
	}
	
	//Ū��
	public void ReadFile(File f) throws IOException{
			
		FileReader fileReader = new FileReader(f);	
		br = new BufferedReader(fileReader);

		readReady = true;
		JLbl_msg.setText("�wŪ���ɮסA��Enter�H�~��");
		panelDraw.requestFocus();
		return;
	}
	
	//Ū��X�ɮ�
	public void showPic(String line) throws IOException {
		
		clean();
		String strs[] = line.split(" ");
		
		Point p = new Point(Integer.valueOf(strs[1]), Integer.valueOf(strs[2]));
		pointCount++;
		
		//count++;
		updatePointMsg(p, Integer.valueOf(strs[1]), Integer.valueOf(strs[2]), pointCount);
		
		while ((line = br.readLine()) != null){
			
			String strs2[] = line.split(" ");
			
			if (strs2[0].charAt(0) == 'P') {
				pointCount++;
				p.x = Integer.valueOf(strs2[1]);
				p.y = Integer.valueOf(strs2[2]);
				updatePointMsg(p, Integer.valueOf(strs2[1]), Integer.valueOf(strs2[2]), pointCount);
			}
			
			else if (strs2[0].charAt(0) == 'E') {
				Line l = new Line();
				l.p_a.x = (double)Integer.valueOf(strs2[1]);
				l.p_a.y = (double)Integer.valueOf(strs2[2]);
				l.p_b.x = (double)Integer.valueOf(strs2[3]);
				l.p_b.y = (double)Integer.valueOf(strs2[4]);
				updateLineMsg(l);
			}		
		}
		readReady = false;
	}
	
	//�C��EnterŪ�@�ո��
	public void getData(BufferedReader br) throws IOException {	
		readReady = false;
		String strs[];
		String line = br.readLine();
	
		while (line.length() == 0 || line.charAt(0) == '#')  //���L���ѩδ���Ÿ�
			line = br.readLine();
	
		//Ū��X�ɮ�
		if (line.charAt(0) == 'P') {
				showPic(line);
				return;
		}
		
		pointCount = Integer.parseInt(line); //�I��
		panelDraw.clean(); //clean canvas
		
		//�J��0 -> EOF
		if (pointCount == 0) {	
			readReady = false;
			JLbl_msg.setText("EOF");
			br.close();
			pointCount = 0;
			return;
		}

		for (int i = 0; i < pointCount; i++) {
			
			run = false;
			line = br.readLine();
			while (line.length() == 0 || line.charAt(0) == '#') 
				line = br.readLine();
			
			strs = line.split(" "); //�H�r���Ϲj�A�Φ��ƭӰ}�C���
			Point p = new Point();
			p.x = Integer.parseInt(strs[0]);
			p.y = Integer.parseInt(strs[1]);
			
            updatePointMsg(p, p.x, p.y, i);
            
		}
		readReady = true;
		JLbl_points.setText(pointCount + " points");
        JLbl_msg.setText("��������ܹϧΡA�M��i�H��X�ɮשΫ�Enter�~��");
	}

	//��X�ɮ�
	private void outputFile(File f) throws IOException{
		
	    bw = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
		bw.write(totals.replace("\n", "\r\n"));
        bw.close();
        JOptionPane.showMessageDialog(null, "�ɮפw�x�s","����", JOptionPane.PLAIN_MESSAGE);
	}
	
	//�s�W�I
	public void updatePointMsg(Point p, int new_x, int new_y, int index) {
		point[index][0] = new_x;
        point[index][1] = new_y;
		Arrays.sort(point, new Compare()); //lexical order
		updateMsg();

        panelDraw.addPoint(p);
	}
	
	//�s�W�u
	public void updateLineMsg(Line l) {
		if (l.p_a.x == l.p_b.x && l.p_a.y == l.p_b.y)
			return;

		panelDraw.addLine(l, dir);
		run = true;
	}

	//reset
	public void clean(){
		points = "";
		run = false;
		step = -1;
		isStep = false;
		dataMsg.setText(points);
		
		for (int i = point.length - pointCount; i < point.length; i++){
			point[i][0] = 0;
			point[i][1] = 0;
		}

		pointCount = 0;
		panelDraw.clean();
		
		JLbl_msg.setText("");
		JLbl_points.setText(pointCount + " points");
		button_output.setEnabled(false);
		readReady = true;
	}

	//�e�T������
	private Line getTriLine(PDouble circumcentre, Line mline, PDouble verticalPoint, int type){
    	PDouble start = new PDouble();
        PDouble destination = new PDouble();
        PDouble newDest = new PDouble();
        
        Line tempLine;
        int maxX = MAX;
        int minX = MIN;
        int maxY = MAX;
        int minY = MIN;
        
        if (type == 1){ //�w���T����
            start.x = (mline.p_a).x;
            start.y = (mline.p_a).y;
            destination.x = circumcentre.x;
            destination.y = circumcentre.y;
    	}
        
    	else if (type == 2){ //�����T����
    		start.x = (mline.p_a).x;
            start.y = (mline.p_a).y;
    	}
        
    	else { //�U���T����
    		start.x = circumcentre.x;
            start.y = circumcentre.y;
            destination.x = (mline.p_a).x;
            destination.y = (mline.p_a).y;
    	}
        
        if (type != 2){ //�w�� �U��
        	if (start.x  == destination.x){ //�����u����	
            	PDouble p_a, p_b;
            	if (start.y > destination.y){ //���W
            		p_a = new PDouble(start.x, (double)0);
            		
            		if (type == 1) {
            			tempLine = new Line(destination, p_a);
            			tempLine = tempLine.toBoundX();
            		}
            		else
            			tempLine = new Line(start, p_a);
            	}
            	
            	else { //���U
            		p_b = new PDouble(start.x, MAX);
            		
            		if (type == 1)
            			tempLine = new Line(destination, p_b);
            		else
            			tempLine = new Line(start, p_b);
            	}
            }
        	
            else{ //�����u������
            	double m = (double) (start.y - destination.y) / (start.x - destination.x);
            	
            	if (start.x > destination.x){ //�V��
            		minY = (int) (m * (minX - start.x) + start.y);
            		newDest.x = minX;
            		newDest.y = minY;
            	}
            	
            	else { //�V�k
            		maxY =  (int) (m * (maxX - start.x) + start.y);
            		newDest.x = maxX;
            		newDest.y = maxY;
            	}
            	
            	if (type == 1)
            		tempLine = new Line(destination, newDest); 
            	
            	else 
            		tempLine = new Line(start, newDest); 
            	
            	tempLine = tempLine.toBoundY(tempLine);
            }
        	tempLine.change();
            return tempLine;
        }
        
        else{ //�����T����
        	tempLine = mline.toBoundX();
        	
        	//�����u�V�k
        	if ((start.x > verticalPoint.x && start.y < verticalPoint.y) || (start.x > verticalPoint.x && start.y > verticalPoint.y)){
                if ((tempLine.p_a).x > (tempLine.p_b).x) 
                	tempLine.p_b = start;
                else 
                	tempLine.p_a = start;
            } 
        	
        	else{ //�����u�V��
                if (start.x > verticalPoint.x)
                	tempLine.p_a = start;
                else 
                	tempLine.p_b = start;            
            }
        	return tempLine;
        }
    }
	
	//�T���Ϊ���sort
	private Line[] getLineList(PDouble[] p) { //�̪��פj��p�Ƨ�
        Line line[] = new Line[3];
        
        //�ͦ��T��
        ArrayList<Line> lineList = new ArrayList<>();
        lineList.add(new Line(p[0], p[1]));
        lineList.add(new Line(p[1], p[2]));
        lineList.add(new Line(p[0], p[2]));
        
        for (int i = 0; i < lineList.size(); i++){		
        	Line max = new Line();
        	
	        for (int j = i;j < lineList.size(); j++){
	        	if (max.getLength() < lineList.get(j).getLength()){
	        		max = lineList.get(j);
	        	}
	        }
	        lineList.remove(max);
	        lineList.add(i, max);
        }
        
        line[0] = lineList.get(2);
        line[1] = lineList.get(1);
        line[2] = lineList.get(0);
        return line;
    }
	
	//�T���Ϋ��A  0:�U�� 1:�w�� 2:���� (D = a^2 + b^2 - c^2)
	private int getTriangleType(Line line[]){ 
		//�w��
		if (Math.pow(line[0].getLength(), 2) + Math.pow(line[1].getLength(), 2) - Math.pow(line[2].getLength(), 2) < 0)
			return 1;
		
		//����
		else if (Math.pow(line[0].getLength(), 2) + Math.pow(line[1].getLength(), 2) - Math.pow(line[2].getLength(), 2) == 0)
			return 2;
		
		//�U��
		else
			return 0;
	}
	
	//�T���Υ~��
	private PDouble getCircumcentre(PDouble[] p){
    	PDouble res = new PDouble();
    	res.x = ((p[1].y - p[0].y) * (p[2].y * p[2].y - p[0].y * p[0].y + p[2].x * p[2].x - p[0].x * p[0].x) - (p[2].y - p[0].y)
                * (p[1].y * p[1].y - p[0].y * p[0].y + p[1].x * p[1].x - p[0].x * p[0].x))
                / (2 * (p[2].x - p[0].x) * (p[1].y - p[0].y) - 2 * ((p[1].x - p[0].x) * (p[2].y - p[0].y)));
    	
    	res.y = ((p[1].x - p[0].x) * (p[2].x * p[2].x - p[0].x * p[0].x + p[2].y * p[2].y - p[0].y * p[0].y) - (p[2].x - p[0].x)
    	        * (p[1].x * p[1].x - p[0].x * p[0].x + p[1].y * p[1].y - p[0].y * p[0].y))
    	        / (2 * (p[2].y - p[0].y) * (p[1].x - p[0].x) - 2 * ((p[1].y - p[0].y) * (p[2].x - p[0].x)));    	
    	return res;
    }
	
	//��s������ܪO
	public void updateMsg() {
		points = "";
		lines = "";
        
		for (int i = point.length - pointCount; i < point.length; i++) 
        	points = points + "P " + point[i][0] + " " + point[i][1] + "\n";
        
        if (panelDraw.line_listL.size() != 0) {
        	lines = "";
    		panelDraw.getAllLine();
    		for (int i = 0; i < panelDraw.line_listAll.size(); i++) {
    			lines = lines + "E " + (int)panelDraw.line_listAll.get(i).p_a.x + " " + (int)panelDraw.line_listAll.get(i).p_a.y + " "
            						 + (int)panelDraw.line_listAll.get(i).p_b.x + " " + (int)panelDraw.line_listAll.get(i).p_b.y + "\n";
            }
        }
		totals = points + lines;
		dataMsg.setText(totals);
	}
	
	//�D�{��
	public static void main(String argv[]){
		Voronoi app = new Voronoi("Term Project");
	}
}

