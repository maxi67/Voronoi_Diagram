package voronoi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;

public class Panel extends JPanel {
	
	public int hull = 0;
	public boolean hyper = false, left = false, right = false;

	public ArrayList<Point> point_list = new ArrayList<Point>();
	public List<Line> line_listL = new ArrayList<Line>();
	public List<Line> line_listR = new ArrayList<Line>();
	public List<Line> line_listAll = new ArrayList<Line>();
	public List<Line> hyperplane_list = new ArrayList<Line>();
	
	public static Point[] con_p, con_p2;
	
	public Panel() {}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		//畫點
		for (int i = 0 ;i < point_list.size(); i++){
			g.setColor(Color.BLACK);
			g.fillOval((int)point_list.get(i).x, (int)point_list.get(i).y, 3, 3);
		}
		
		//Convex Hull
		if (hull > 0) {
			g.setColor(Color.BLUE);
			for (int i = 0; i < con_p.length - 1; i++)
				g.drawLine(con_p[i].x, con_p[i].y, con_p[i+1].x, con_p[i+1].y);
			g.drawLine(con_p[0].x, con_p[0].y, con_p[con_p.length-1].x, con_p[con_p.length-1].y);
			
			if (hull == 2) {
				for (int i = 0; i < con_p2.length - 1; i++)
					g.drawLine(con_p2[i].x, con_p2[i].y, con_p2[i+1].x, con_p2[i+1].y);
				g.drawLine(con_p2[0].x, con_p2[0].y, con_p2[con_p2.length-1].x, con_p2[con_p2.length-1].y);
			}
		}
		
		//HyperPlane
		if (hyper) {
			g.setColor(Color.MAGENTA);
			for (int i = 0; i < hyperplane_list.size(); i++)
				g.drawLine((int)(hyperplane_list.get(i).p_a).x, (int)(hyperplane_list.get(i).p_a).y, (int)(hyperplane_list.get(i).p_b).x, (int)(hyperplane_list.get(i).p_b).y);
		}
		
		//Line(Voronoi)
		if (left) {
			g.setColor(Color.RED);
			for (int i = 0 ;i < line_listL.size(); i++)
				g.drawLine((int)(line_listL.get(i).p_a).x, (int)(line_listL.get(i).p_a).y, (int)(line_listL.get(i).p_b).x, (int)(line_listL.get(i).p_b).y);
		}
		
		if (right) {
			g.setColor(Color.GREEN);
			for (int i = 0 ;i < line_listR.size(); i++)
				g.drawLine((int)(line_listR.get(i).p_a).x, (int)(line_listR.get(i).p_a).y, (int)(line_listR.get(i).p_b).x, (int)(line_listR.get(i).p_b).y);
		}
	}
	
	//新增點資訊
	public void addPoint(Point p) {
		
		if (!point_list.contains(p)) 
        	point_list.add(p);
        	
		//sort
		point_list.sort(new Comparator<Point>(){
			@Override
			public int compare(Point p1, Point p2) {
				if (p1.x != p2.x)
					return p1.x - p2.x;
				else
					return p1.y - p2.y;
			}
		});
		
        repaint();
	}
	
	//新增線段資訊
	public void addLine(Line l, int dir) {
		if (dir == 1) {
			left = true;
			line_listL.add(l);
			line_listL.sort(new LineCmp());
		}
		
		else {
			right = true;
			line_listR.add(l);
			line_listR.sort(new LineCmp());
		}
		
        repaint();
	}
	
	//畫convex hull
	public void drawConvex(Point[] p, int status) {
		
		hull = status;
		if (hull == 1 || hull == 3)
			con_p = p.clone();
		else 
			con_p2 = p.clone();
		repaint();
	}
	
	public void drawConvex(ArrayList<Point> list, int status) {
		hull = status;
		
		if (hull == 1 || hull == 3) {
			con_p = new Point[list.size()];
			for (int i = 0; i < list.size(); i++)
				con_p[i] = list.get(i);
		}
		
		else {
			con_p2 = new Point[list.size()];
			for (int i = 0; i < list.size(); i++)
				con_p2[i] = list.get(i);
		}
			
		repaint();
	}

	public void drawHyperPlane(ArrayList<Point> left, ArrayList<Point> right) {
		hyper = true;
		boolean Lend = false, Rend = false;
		
		int Lindex = 0;
		int LPindex = 0;
		int Rindex = 0;
		int RPindex = 0;
		Point tmpPoint = new Point();
		Point tmpPoint2 = new Point();
		int times = 1;
		int last = 0;
		
		while ((Lend == false) || (Rend == false)){
			Line hp = new Line();
			hp.p_a.x = (double)left.get(LPindex).getX();
			hp.p_a.y = (double)left.get(LPindex).getY();
			hp.p_b.x = (double)right.get(RPindex).getX();
			hp.p_b.y = (double)right.get(RPindex).getY();
		//	System.out.println("hp "+hp.p_a.x+" "+ hp.p_a.y+","+hp.p_b.x+" "+ hp.p_b.y);
			
			hp = hp.mLine().toBoundX().yOrder();
		//	tmpPoint = new Point();
		//	tmpPoint2 = new Point();

			tmpPoint = intersect(line_listL.get(Lindex).p_a.getPoint(), line_listL.get(Lindex).p_b.getPoint(), hp.p_a.getPoint(), hp.p_b.getPoint());
			tmpPoint2 = intersect(line_listR.get(Rindex).p_a.getPoint(), line_listR.get(Rindex).p_b.getPoint(), hp.p_a.getPoint(), hp.p_b.getPoint());
			
			if (tmpPoint.getY() < tmpPoint2.getY() && !Rend) { //左邊先交到
				last = 1;
				if (times == 1) {
					hp.p_b = PDouble.getPDouble(tmpPoint);
					line_listL.get(Lindex).p_b = PDouble.getPDouble(tmpPoint);
					Lindex++;
				}
				else {
					hp.p_a = PDouble.getPDouble(tmpPoint);
					hp.p_b = PDouble.getPDouble(tmpPoint2);
					line_listR.get(Rindex).p_a = PDouble.getPDouble(tmpPoint2);
					Rindex++;
				}
					
				times++;
				hyperplane_list.add(hp);
				
				if (Lindex == line_listL.size()) {
					Lindex--;
					Lend = true;
				}
				if (Rindex == line_listR.size()) {
					Rindex--;
					Rend = true;
				}
				
				LPindex++;				
			}
			
			else { //右邊先交到		
				last = 2;
				if (times == 1) {
					hp.p_b = PDouble.getPDouble(tmpPoint2);
					line_listR.get(Rindex).p_a = PDouble.getPDouble(tmpPoint2);
					Lindex++;
				}
				else {
					hp.p_a = PDouble.getPDouble(tmpPoint2);
					hp.p_b = PDouble.getPDouble(tmpPoint);
					line_listL.get(Lindex).p_b = PDouble.getPDouble(tmpPoint);
					Rindex++;
				}
				
				times++;
				hyperplane_list.add(hp);
				
				if (Lindex == line_listL.size()) {
					Lindex--;
					Lend = true;
				}
				if (Rindex == line_listR.size()) {
					Rindex--;
					Rend = true;
				}
				
				RPindex++;
			}	
				
		}
		
		Line hp = new Line();
		hp.p_a.x = (double)left.get(left.size()-1).getX();
		hp.p_a.y = (double)left.get(left.size()-1).getY();
		hp.p_b.x = (double)right.get(right.size()-1).getX();
		hp.p_b.y = (double)right.get(right.size()-1).getY();
		hp = hp.mLine().toBoundX().yOrder();
		hp.p_a = (last == 1) ? PDouble.getPDouble(tmpPoint2) : PDouble.getPDouble(tmpPoint);
			
		hyperplane_list.add(hp);
		repaint();
	}
	
	public void getAllLine() {
		line_listAll.clear();
		
		for (int i = 0; i < line_listL.size(); i++)
			line_listAll.add(line_listL.get(i));
		
		for (int i = 0; i < line_listR.size(); i++)
			line_listAll.add(line_listR.get(i));
		
		for (int i = 0; i < hyperplane_list.size(); i++)
			line_listAll.add(hyperplane_list.get(i));

		//sort
		line_listAll.sort(new LineCmp());
	}
	
	//兩線段交點
	private Point intersect(Point a1, Point a2, Point b1, Point b2){ 
        Point noPoint = new Point();
        noPoint.x = -1;
        noPoint.y = -1;
        
        Point Va = new Point();
        Va.x = a2.x - a1.x;	//A向量
        Va.y = a2.y - a1.y;
        
        Point Vb = new Point();
        Vb.x = b2.x - b1.x;	//B向量
        Vb.y = b2.y - b1.y;
        
        Point Vc = new Point();
        Vc.x = b1.x - a1.x;
        Vc.y = b1.y - a1.y;
     
        // 兩線平行，交點不存在(這邊不會有重疊的可能)
        if (Va.x * Vb.y - Va.y * Vb.x == 0) 
        	return noPoint;
        
        // 找交點 面積原理
        Point p = new Point();	//	交點
        p.x = a1.x + Va.x * (Vc.x * Vb.y - Vc.y * Vb.x) / (Va.x * Vb.y - Va.y * Vb.x);
        p.y = a1.y + Va.y * (Vc.x * Vb.y - Vc.y * Vb.x) / (Va.x * Vb.y - Va.y * Vb.x);
        return p;
    }
	
	public void reStep() {
		line_listL.clear();
		line_listR.clear();
		line_listAll.clear();
		
		hull = 0;
		hyper = false;
		left = false;
		right = false;
		
		repaint();
	}
	
	public void clean() {
		point_list.clear();
		line_listL.clear();
		line_listR.clear();
		line_listAll.clear();
		hyperplane_list.clear();
		
		hull = 0;
		hyper = false;
		left = false;
		right = false;
		
		repaint();
	}
}
