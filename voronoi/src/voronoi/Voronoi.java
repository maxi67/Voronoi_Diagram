
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import voronoi.Panel;

public class Voronoi extends JFrame implements ActionListener, ItemListener {

	private JFileChooser fc; //開啟檔案
	private File f; //檔案
	private BufferedReader br;
	private BufferedWriter bw;
	
    JMenu Menu_main, Menu_action;
    JMenuBar JBar;
    JButton clear;
    JMenuItem JMItem_open, JMItem_output, JMItem_clean, JMItem_exit, JMItem_STS, JMItem_run, JMItem_test;
	JRadioButton[] JRB;
	JLabel JLbl_msg, JLbl_points;
	JLabel button_run, button_open, button_output, button_clean, button_exit; //Button
	JPanel panelMsg;
	Panel panelDraw;
	JScrollPane JSP_points;
	JTextArea dataMsg;
	
	private JPanel contentPane;
	ButtonGroup bg;
	
	public final static int MAX = 900;
	public final static int MIN = 0;
	int pointCount = 0;
	int point[][] = new int[500][2];
	
	ArrayList<Point> point_list = new ArrayList<Point>();
	
	boolean click_mode = true, readReady = false, run = false;
	String points = "", lines = "", totals = "";
	
	//建構子
	public Voronoi(String title) {
		
			fc = new JFileChooser("./");
			
			//建立功能選單
	        setJMenuBar(getFunctionMenuBar());
	        
	        contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);
	
	        //Panel_Msg--------------------------------------
	        initMsgPanel();

	        //Data Msg(點邊資料)----------------------------------
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
		         public void mousePressed(MouseEvent e){  //實做滑鼠的點擊事件
		        	 
		        	 	if(click_mode) {

		        	 		if(run) {
				        		  JFrame JF = new JFrame();
				        		  JOptionPane.showMessageDialog(JF, "請按下清除後再重新操作。",
			                              "錯誤", JOptionPane.ERROR_MESSAGE);
				        		  return;
				        	  }
			            	
			            	Graphics g = panelDraw.getGraphics();
			            	Point p = e.getPoint();
			            	pointCount++;      
			                 
			            	updatePointMsg(p, p.x, p.y, panelDraw.pointCount);

				            if(panelDraw.pointCount == 1)
				            	JLbl_points.setText(pointCount + " point");
				            else
				            	JLbl_points.setText(pointCount + " points");
			            }
			            else {
			            	JFrame JF = new JFrame();
			            	JOptionPane.showMessageDialog(JF,"在檔案匯入模式下，滑鼠不可點擊畫布",
	                                  "錯誤", JOptionPane.ERROR_MESSAGE);
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
	
	//Panel_Msg 初始化
	public void initMsgPanel () {

		panelMsg = new JPanel();

        //訊息JLabel---------------------------------------
        JLbl_msg = new JLabel();
        JLbl_msg.setText("可以在畫布上任意點擊，或改以檔案匯入");
        JLbl_msg.setFont(new Font("標楷體", Font.CENTER_BASELINE, 20));
        
        JLbl_msg.setBounds(400, 10, 700, 30);
        contentPane.add(JLbl_msg);
        
        //模式單選紐(滑鼠、讀檔)-----------------------------------
        JRB = new JRadioButton[2];
        JRB[0] = new JRadioButton("滑鼠點擊模式", true);
        JRB[0].setFont(new Font("標楷體", Font.CENTER_BASELINE, 25));
        JRB[0].addItemListener(this);
        
        JRB[1] = new JRadioButton("檔案匯入模式", false);
        JRB[1].setFont(new Font("標楷體", Font.CENTER_BASELINE, 25));
        JRB[1].addItemListener(this);
        
        bg = new ButtonGroup();
        bg.add(JRB[0]);
        bg.add(JRB[1]);
        
        JRB[0].setBounds(500, 50, 200, 50);
        JRB[1].setBounds(700, 50, 200, 50);
        contentPane.add(JRB[0]);
        contentPane.add(JRB[1]);
        
        //點數紀錄JLabel-------------------------------------------
        JLbl_points = new JLabel();
        JLbl_points.setText(pointCount + " points");
        JLbl_points.setFont(new Font("標楷體", Font.CENTER_BASELINE, 20));
        
        JLbl_points.setBounds(1000, 60, 100, 30);
        contentPane.add(JLbl_points);
        
        //功能按鈕們
        JLabel L_run = new JLabel(); 
        L_run.setText("執行");
        L_run.setFont(new Font("標楷體", Font.CENTER_BASELINE, 18));
        
        JLabel L_open = new JLabel(); 
        L_open.setText("匯入");
        L_open.setFont(new Font("標楷體", Font.CENTER_BASELINE, 18));
        
        JLabel L_output = new JLabel(); 
        L_output.setText("輸出");
        L_output.setFont(new Font("標楷體", Font.CENTER_BASELINE, 18));
        
        JLabel L_clean = new JLabel();
        L_clean.setText("清除");
        L_clean.setFont(new Font("標楷體", Font.CENTER_BASELINE, 18));
        
        JLabel L_exit = new JLabel();
        L_exit.setText("離開");
        L_exit.setFont(new Font("標楷體", Font.CENTER_BASELINE, 18));
        
        button_run = new JLabel(new ImageIcon("./img/run.png"), SwingConstants.LEFT);
        button_open = new JLabel(new ImageIcon("./img/folder.png"), SwingConstants.LEFT);
        button_output = new JLabel(new ImageIcon("./img/output.png"), SwingConstants.LEFT);
        button_clean = new JLabel(new ImageIcon("./img/clean.png"), SwingConstants.LEFT);
        button_exit = new JLabel(new ImageIcon("./img/exit.png"), SwingConstants.LEFT);
        
        button_run.addMouseListener(new MouseAdapter() {
	          @Override
	          public void mouseClicked(MouseEvent e) {
	        	  if(run) {
	        		  return;
	        	  }
	        	  
	        	  if(pointCount == 0) {
	        		  JLbl_msg.setText("沒有點...");
	        		  return;
	        	  }
	        	  draw();
	          }
	        });
        
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
		        				JLbl_msg.setText("檔案讀取失敗");
		        			}
		        	  									
	        	  		}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	          }
	        });
        
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
	        				  JLbl_msg.setText("檔案輸出失敗");
	        			  }
	        		  }
	        	  
	        	  } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
		        	  }
	        	  
	          }
	        });
		
        button_clean.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
        		clean();
          }
        });
        
        button_exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	dispose();
				System.exit(0);	//結束程式
            }
          });

        button_run.setBounds(40, 25, 100, 20);
        L_run.setBounds(80, 25, 40, 20);
        
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
	
	//建立功能選單
	public JMenuBar getFunctionMenuBar() {
		
		JBar = new JMenuBar();
		
        Menu_main = new JMenu("功能");
        Menu_main.setFont(new Font("標楷體", Font.CENTER_BASELINE, 20));
        Menu_action = new JMenu("動作");
        Menu_action.setFont(new Font("標楷體", Font.CENTER_BASELINE, 20));

        //Menu_main =========
        JMItem_open = new JMenuItem("匯入檔案...", new ImageIcon("./img/folder.png"));
        JMItem_output = new JMenuItem("輸出文字檔", new ImageIcon("./img/output.png"));
        JMItem_clean = new JMenuItem("清空畫布", new ImageIcon("./img/clean.png"));
        JMItem_exit = new JMenuItem("離開程式", new ImageIcon("./img/exit.png"));
        
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

	//按鍵事件處理
    class prockey extends KeyAdapter
    {
        public void keyPressed(KeyEvent e)
        {
            if (e.getKeyCode() == 10 && readReady) //Enter
            {
					try {
						
						for(int i = point.length - pointCount; i < point.length; i++){
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
	
	//單選按鈕事件處理(模式選擇)
	public void itemStateChanged(ItemEvent e) { 
		
		//Mode Select
        if(e.getItem() == JRB[0]) { //click
        	clean();
        	JLbl_msg.setText("可以任意點擊畫布");
        	JMItem_open.setEnabled(false);
        	button_open.setEnabled(false);
        	click_mode = true;
        }
        else if(e.getItem() == JRB[1]) { //file
        	clean();
        	JLbl_msg.setText("選擇匯入，開啟測資");
        	JMItem_open.setEnabled(true);
        	button_open.setEnabled(true);
        	click_mode = false;
        }
    } 
		    
	//單擊事件(選單)
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
				System.exit(0);	//結束程式
			}
			
			else if(e.getSource() == JMItem_test){ 
			//	showPoints();
			}
			
		}catch(IOException ex) {
			System.out.println(ex);
		}
	}
	
	//讀檔
	public void ReadFile(File f) throws IOException{
			
		FileReader fileReader = new FileReader(f);	
		br = new BufferedReader(fileReader);

		readReady = true;
		JLbl_msg.setText("已讀取檔案，按Enter以繼續");
		panelDraw.requestFocus();
		return;
	}
	
	//每按Enter讀一組資料
	public void getData(BufferedReader br) throws IOException {
		
		
		readReady = false;
		String strs[];
		String TwoP = br.readLine();
		
		while(TwoP.length() == 0 || TwoP.charAt(0) == '#')  //跳過註解或換行符號
			TwoP = br.readLine();
	
		pointCount = Integer.parseInt(TwoP); //點數

		panelDraw.clean(); //clean canvas
		
		//遇到0 -> EOF
		if(pointCount == 0) {	
			readReady = false;
			JLbl_msg.setText("EOF");
			br.close();
			pointCount = 0;
			return;
		}

		for(int i = 0; i < pointCount; i++) {
			
			run = false;
			TwoP = br.readLine();
			while(TwoP.length() == 0 || TwoP.charAt(0) == '#') 
				TwoP = br.readLine();
			
			strs = TwoP.split(" "); //以逗號區隔，形成數個陣列資料
			Point p = new Point();
			p.x = Integer.parseInt(strs[0]);
			p.y = Integer.parseInt(strs[1]);
			
            updatePointMsg(p, p.x, p.y, i);
            
		}
		readReady = true;
		JLbl_points.setText(pointCount + " points");
        JLbl_msg.setText("按執行顯示圖形，然後可以輸出檔案或按Enter繼續");
	}

	//輸出檔案
	private void outputFile(File f) throws IOException{
		
	    bw = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
		bw.write(totals.replace("\n", "\r\n"));
        bw.close();
        JOptionPane.showMessageDialog(null, "檔案已儲存","提示", JOptionPane.PLAIN_MESSAGE);
	}
	
	//新增點
	@SuppressWarnings("unchecked")
	public void updatePointMsg(Point p, int new_x, int new_y, int index) {
		
		Compare2DArray arr = new Compare2DArray();
		point[index][0] = new_x;
        point[index][1] = new_y;
		Arrays.sort(point, arr); //lexical order

        points = "";
        for (int i = point.length - pointCount; i < point.length; i++) {
        	points = points + "P " + point[i][0] + " " + point[i][1] + "\n";
        }
        dataMsg.setText(points);
        panelDraw.addPoint(p);
	}
	
	//新增線
	public void updateLineMsg(Line l) {
		
		if(l.p_a.x == l.p_b.x && l.p_a.y == l.p_b.y)
			return;
		
		panelDraw.addLine(l);
		
		lines = "";
		panelDraw.lineListSort();
		for (int i = 0; i < panelDraw.lineCount; i++) {
			lines = lines + "E " + (int)panelDraw.line_list.get(i).p_a.x + " " + (int)panelDraw.line_list.get(i).p_a.y + " "
        						 + (int)panelDraw.line_list.get(i).p_b.x + " " + (int)panelDraw.line_list.get(i).p_b.y + "\n";
        }
		totals = points + lines;
		dataMsg.setText(totals);
		run = true;
	}
	
	//新增線
	public void updateLineMsg(double a_x, double a_y, double b_x, double b_y) {
		
		if(a_x == b_x && a_y == b_y)
			return;
		
		Line l = new Line();
		l.p_a.x = a_x;
		l.p_a.y = a_y;
		l.p_b.x = b_x;
		l.p_b.y = b_y;
		
		panelDraw.addLine(l);
		
		lines = "";
		panelDraw.lineListSort();
		for (int i = 0; i < panelDraw.lineCount; i++) {
			lines = lines + "E " + (int)panelDraw.line_list.get(i).p_a.x + " " + (int)panelDraw.line_list.get(i).p_a.y + " "
        						 + (int)panelDraw.line_list.get(i).p_b.x + " " + (int)panelDraw.line_list.get(i).p_b.y + "\n";
        }
		
		totals = points + lines;
		dataMsg.setText(totals);
		run = true;
	}

	//reset
	public void clean(){
		points = "";
		run = false;
		dataMsg.setText(points);
		
		for(int i = point.length - pointCount; i < point.length; i++){
			point[i][0]=0;
			point[i][1]=0;
		}

		pointCount = 0;
		panelDraw.clean();
		
		JLbl_msg.setText("");
		JLbl_points.setText(pointCount + " points");
		button_output.setEnabled(false);
		readReady = true;
	}
	
	//畫線
	public void draw() {
		String msg = "";
		Line c;
		
		if(click_mode)
			JLbl_msg.setText("可以輸出檔案，或按清除後繼續");
		else
			JLbl_msg.setText("可以輸出檔案，或按Enter繼續");

		switch(panelDraw.pointCount) {
			case 1:
				msg = "一個點無法構成Voronoi diagram";
				if(panelDraw.pointCount < pointCount)
					msg = msg + "(有重疊的點)";
				
				JLbl_msg.setText(msg);
				return;
		
			case 2:
				PDouble pa = new PDouble();
				PDouble pb = new PDouble();
				pa.x = (double)panelDraw.point_list.get(0).x;
				pa.y = (double)panelDraw.point_list.get(0).y;
				pb.x = (double)panelDraw.point_list.get(1).x;
				pb.y = (double)panelDraw.point_list.get(1).y;
				
				TwoP L = new TwoP(pa, pb);
				
				if(L.isVertival()) //垂直線
					updateLineMsg(L.midpointX(), 0, L.midpointX(), 900);
				
				else if(L.interceptX() == 0) //水平線
					updateLineMsg(0, L.midpointY(), 900, L.midpointY());
				
				else {
					Line l = new Line(); //垂直平分線
					PDouble vir1 = new PDouble(), vir2 = new PDouble();
					
					vir1.x = -(L.p2.y - L.p1.y); 
					vir1.y = L.p2.x - L.p1.x;

					vir2.x =  (L.p2.y - L.p1.y);
					vir2.y =  -(L.p2.x - L.p1.x);

					l.p_a.x = L.midpointX() + vir1.x;
					l.p_a.y = L.midpointY() + vir1.y;
					
					l.p_b.x = L.midpointX() + vir2.x;
					l.p_b.y = L.midpointY() + vir2.y;

					c = l.toBoundX();
					
					updateLineMsg(c);
				}
				break;
				
			case 3:
			
				PDouble p[] = new PDouble[3];

				for(int i = 0; i <= 2; i++) {
					p[i] = new PDouble();
					p[i].x = (double)(panelDraw.point_list.get(i).x);
					p[i].y = (double)(panelDraw.point_list.get(i).y);
				}
				
				Line sortingLine[] = getLineList(p); //三邊從小到大

				Line mline[] = new Line[3]; //三邊中垂線
				
	            for(int i = 0;i < 3;i++){
	            	mline[i] = sortingLine[i].mLine();
	            }
	            
	            //共線情況
				if((p[1].y - p[0].y) * (p[2].x - p[0].x) == (p[2].y - p[0].y) * (p[1].x - p[0].x)) {
					for(int i = 0; i < 2; i++) {
						c = mline[i].toBoundX();
						updateLineMsg(c);
					}
				}
				
				//三角形
				else {
					
					PDouble circumcentre = getCircumcentre(p); //外心
					int type = getTriangleType(sortingLine); //三角形  0:銳角 1:鈍角 2:直角
					PDouble verticalPoint = null; //直角坐標
					
	            	if(type == 2){ //直角三角形	
	            		for(int i = 0;i < 3;i++){
	                        
	            			if(p[i] != sortingLine[2].p_a && p[i] != sortingLine[2].p_b)
	                        	verticalPoint = p[i]; //直角坐標
	                        
	                    }
	            	}
	            	
	            	for(int i = 0; i < 3; i++){
            			if(i == 2) { //外心, 中垂線, 直角坐標, 三角形型態
            				if(type == 1 && (circumcentre.x < 0 ||circumcentre.x > MAX) )
            					continue;
            				c = getTriLine(circumcentre, mline[i], verticalPoint, type);	
            			}
            			else
            				c = getTriLine(circumcentre, mline[i], verticalPoint, 0);
            			
            			updateLineMsg(c);
            		}
				}
		
				break;
				
			default:
				JLbl_msg.setText("尚待開發..");
				break;
		}
		button_output.setEnabled(true);
		
	}
	
	//畫三角形邊
	private Line getTriLine(PDouble circumcentre, Line mline, PDouble verticalPoint, int type){
		
		
    	PDouble start = new PDouble();
        PDouble destination = new PDouble();
        PDouble newDest = new PDouble();
        Line tempLine;
        int maxX = MAX;
        int minX = MIN;
        int maxY = MAX;
        int minY = MIN;
        
        if(type == 1){ //鈍角三角形
            start.x = (mline.p_a).x;
            start.y = (mline.p_a).y;
            destination.x = circumcentre.x;
            destination.y = circumcentre.y;
    	}
        
    	else if(type == 2){ //直角三角形
    		start.x = (mline.p_a).x;
            start.y = (mline.p_a).y;
    	}
        
    	else{ //銳角三角形
    		start.x = circumcentre.x;
            start.y = circumcentre.y;
            destination.x = (mline.p_a).x;
            destination.y = (mline.p_a).y;
    	}
        
        if(type != 2){ //鈍角 銳角
        	if(start.x  == destination.x){ //中垂線垂直
        		
            	PDouble p_a, p_b;
            	if(start.y > destination.y){
            		p_a = new PDouble(start.x, (double)0);
            		if(type == 1) {
            			tempLine = new Line(destination, p_a);
            			tempLine = tempLine.toBoundX();
            		}
            		else
            			tempLine = new Line(start, p_a);
            		
            	}
            	else{
            		
            		p_b = new PDouble(start.x, MAX);
            		if(type == 1)
            			tempLine = new Line(destination, p_b);
            		else
            			tempLine = new Line(start, p_b);
            	}
            }
        	
            else{
            	
            	if(start.x > destination.x){
            		double m = (double) (start.y - destination.y) / (start.x - destination.x);
            		minY = (int) (m * (minX - start.x) + start.y);
            		newDest.x = minX;
            		newDest.y = minY;
            	}
            	else{	
            		double m = (double) (start.y - destination.y) / (start.x - destination.x);
            		maxY =  (int) (m * (maxX - start.x) + start.y);
            		newDest.x = maxX;
            		newDest.y = maxY;
            	}
            	if(type == 1) {
            		tempLine = new Line(destination, newDest); 
            	}
            	else 
            		tempLine = new Line(start, newDest); 
            	
            	tempLine = tempLine.toBoundY(tempLine);
            }
        	
        	tempLine.change();
            return tempLine;
        }
        
        else{ //直角三角形
        	
        	tempLine = mline.toBoundX();
        	if( (start.x > verticalPoint.x && start.y < verticalPoint.y) || (start.x > verticalPoint.x && start.y > verticalPoint.y)){
                if((tempLine.p_a).x > (tempLine.p_b).x) 
                	tempLine.p_b= start;
                else 
                	tempLine.p_a = start;
            } 
        	
        	else{
                if((tempLine.p_a).x > (tempLine.p_b).x)
                	tempLine.p_a = start;
                else 
                	tempLine.p_b = start;            
            }
        	return tempLine;
        }
    }
	
	 //三角形的邊從小到大
	private Line[] getLineList(PDouble[] p){
        Line line[] = new Line[3];
        
        //生成三邊
        ArrayList<Line> lineList = new ArrayList<>();
        lineList.add(new Line(p[0], p[1]));
        lineList.add(new Line(p[1], p[2]));
        lineList.add(new Line(p[0], p[2]));
        
        for(int i = 0;i < lineList.size(); i++){		
        	Line max = new Line();
        	
	        for(int j = i;j < lineList.size(); j++){
	        	if(max.getLength() < lineList.get(j).getLength()){
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
	
	//三角形型態  0:銳角 1:鈍角 2:直角 (D = a^2 + b^2 - c^2)
	private int getTriangleType(Line line[]){ 
	
		//鈍角
		if(Math.pow(line[0].getLength(), 2) + Math.pow(line[1].getLength(), 2) - Math.pow(line[2].getLength(), 2) < 0)
			return 1;
		
		//直角
		else if(Math.pow(line[0].getLength(), 2) + Math.pow(line[1].getLength(), 2) - Math.pow(line[2].getLength(), 2) == 0)
			return 2;
		
		//銳角
		else
			return 0;
	}
	
	//三角形外心
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
	
	//主程式
	public static void main(String argv[]){
		Voronoi app = new Voronoi("Term Project");
	}
}

