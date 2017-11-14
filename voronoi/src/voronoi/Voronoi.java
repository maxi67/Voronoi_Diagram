
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
	        	  
	        	  if(pointCount == 0) {
	        		  JLbl_msg.setText("沒有點...");
	        		  return;
	        	  }
	        	  
	        	  if(run) {
	        		  JFrame JF = new JFrame();
	        		  JLbl_msg.setText("請按下清除後再操作");
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
		        			}
		        	  		
							ReadFile(f);
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
	        	  
	        	  if(run) {
	        		  try {
		        		  outputFile();
		        	  } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
		        	  }
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
		
		while(TwoP.length() == 0 || TwoP.charAt(0) == '#') 
			TwoP = br.readLine();
		
		//點數
		pointCount = Integer.parseInt(TwoP);

		//clean canvas
		Graphics g = panelDraw.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(100, 100, 1000, 1000);
		
		//遇到0 -> EOF
		if(pointCount == 0) {	
			readReady = false;
			JLbl_msg.setText("EOF");
			br.close();
			pointCount = 0;
			return;
		}

		for(int i = 0; i < pointCount; i++) {
			
			TwoP = br.readLine();
			while(TwoP.length() == 0 || TwoP.charAt(0) == '#') 
				TwoP = br.readLine();
			
			strs = TwoP.split(" "); //以逗號區隔，形成數個陣列資料
			Point p = new Point();
			p.x = Integer.parseInt(strs[0]);
			p.y = Integer.parseInt(strs[1]);
			
		//	g.setColor(Color.BLACK);
          //  g.fillOval(p.x, p.y, 4, 4);
            
            updatePointMsg(p, p.x, p.y, i);
            JLbl_msg.setText("按執行顯示圖形，或先按清除後再按Enter繼續");
		}
		
		
	}

	private void outputFile() throws IOException{
		File saveFile = null;
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showSaveDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION){
	    	saveFile = fileChooser.getSelectedFile();
	    }
	    BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile.getAbsolutePath()));
		bw.write(totals.replace("\n", "\r\n"));
        bw.close();
        JOptionPane.showMessageDialog(null,"檔案儲存成功","提示",JOptionPane.PLAIN_MESSAGE);
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
	//	System.out.println("1 "+panelDraw.line_list.size());
	}
	
	//新增線
	public void updateLineMsg(double a_x, double a_y, double b_x, double b_y) {
		
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
	//	System.out.println("2 "+panelDraw.line_list.size());	
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
	
	public void draw() {
		Graphics g = panelDraw.getGraphics();
		String msg = "";
		JLbl_msg.setText("可以輸出檔案，或按清除後繼續");
		
	//	int c_count = panelDraw.point_list.size(); //畫布上看得到的點數
		
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
					updateLineMsg(L.midpointX(), 0, L.midpointX(), 1000);
				
				else if(L.interceptX() == 0) //水平線
					updateLineMsg(0, L.midpointY(), 1000, L.midpointY());
				
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
					
					Line c = toBoundX(l); //延長到畫布邊界
						
					updateLineMsg(c);
				}
				break;
				
			case 3:
				PDouble p0 = new PDouble();
				PDouble p1 = new PDouble();
				PDouble p2 = new PDouble();
				
				p0.x = panelDraw.point_list.get(0).x;
				p0.y = panelDraw.point_list.get(0).y;
				p1.x = panelDraw.point_list.get(1).x;
				p1.y = panelDraw.point_list.get(1).y;
				p2.x = panelDraw.point_list.get(2).x;
				p2.y = panelDraw.point_list.get(2).y;
				
				Line sortingLine[] = getLineList(p0, p1, p2);

				
				Line mvline[] = new Line[3]; //三邊中垂線
				Line c;
	            for(int i = 0;i < 3;i++){
	            	mvline[i] = mvLine(sortingLine[i].p_a, sortingLine[i].p_b);
	            }
				if((p1.y - p0.y) * (p2.x - p0.x) == (p2.y - p0.y) * (p1.x - p0.x))
					for(int i = 0;i < 2;i++) {
						c = toBoundX(mvline[i]);
						updateLineMsg(c);
					}
		
				break;
				
			default:
				JLbl_msg.setText("尚待開發..");
				break;
		}
		button_output.setEnabled(true);
	}
	
	private Line[] getLineList(PDouble pa, PDouble pb, PDouble pc){ 		//	把三角形的邊從大到小排好  回傳為從小到大
        Line sortingLine[] = new Line[3];
        
        ArrayList<Line> lineList = new ArrayList<>();
        lineList.add(new Line(pa, pb));
        lineList.add(new Line(pb, pc));
        lineList.add(new Line(pc, pa));
        
        for(int i = 0;i < lineList.size(); i++){		
        	Line max = new Line();
        	
	        for(int j = i;j < lineList.size();j++){
	        	if(max.getLength() < lineList.get(j).getLength()){
	        		max = lineList.get(j);
	        	}
	        }
	        lineList.remove(max);
	        lineList.add(i, max);
        }
        
        sortingLine[0] = lineList.get(2);
        sortingLine[1] = lineList.get(1);
        sortingLine[2] = lineList.get(0);

        return sortingLine;
    }
	
	// 找中垂線
	private Line mvLine(PDouble a,PDouble b) { 
        PDouble c = new PDouble();
        PDouble v = new PDouble();
        double temp;
        c.x =  (a.x + b.x) / 2 ;
        c.y =  (a.y + b.y) / 2 ;
        v.x =  b.x - a.x;
        v.y = b.y -a.y;
        
        temp = v.x;
        v.x = v.y;
        v.y = temp;
        
        v.x *= -1;
        v.x += c.x;
        v.y += c.y;
        return new Line(c,v);
    }
	
	private Line toBoundX(Line line) {
		int maxX = 900;
        int maxY = 900;
        int minX = 0;
        int minY = 0;
        Line tempL;
        
        if((line.p_a).x  - (line.p_b).x == 0){	// 中垂線為垂直線的狀況
            PDouble down = new PDouble( (line.p_a).x , (double)maxY); 
            PDouble top = new PDouble( (line.p_a).x , (double)minY); 
            tempL = new Line(top, down); 
            return tempL;
        }
        
        else {
        	double m = ((line.p_a).y - (line.p_b).y) / ((line.p_a).x - (line.p_b).x);
        	// Y = m * (X - x) + y
            maxY = (int) (m * (maxX - (line.p_a).x) + (line.p_a).y);
            minY = (int) (m * (minX - (line.p_a).x) + (line.p_a).y);

            PDouble left = new PDouble();
            left.x = (double)minX; left.y = (double)minY;
            
            PDouble right = new PDouble();
            right.x = (double)maxX; right.y = (double)maxY;
            
            tempL = new Line(left, right); 
            tempL = toBoundY(tempL);
            return tempL;
        }
	}	
	
	private Line toBoundY(Line line){
		int maxY = 900;
		int minY = 0;
        
        double m =  ((line.p_a).y - (line.p_b).y) / ((line.p_a).x - (line.p_b).x) ;	
        
        int X;
        if(line.p_a.y < 0 ){	 // X = (Y - y) / m + x 
            X = (int) (((minY - (line.p_a).y)) / m + (line.p_a).x);     
            line.p_a.x = X;
            line.p_a.y = minY;
        }   
        
        if(line.p_a.y > 900){	
            X = (int) (((maxY - (line.p_a).y)) / m + (line.p_a).x);     
            line.p_a.x = X;
            line.p_a.y = maxY;
        }
        
        if(line.p_b.y < 0 ){	
            X = (int) (((minY - (line.p_b).y)) / m + (line.p_b).x);    
            line.p_b.x = X;
            line.p_b.y = minY;
        }   
        
        if(line.p_b.y > 900){	
            X = (int) (((maxY - (line.p_b).y)) / m + (line.p_b).x);     
            line.p_b.x = X;
            line.p_b.y = maxY;
        }
        return line;
	}

	//主程式
	public static void main(String argv[]){
		Voronoi app = new Voronoi("Term Project");
	}
}

