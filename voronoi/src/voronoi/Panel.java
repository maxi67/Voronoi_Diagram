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
	
	public static int hull = 0;
	public boolean hyper = false, left = false, right = false;
	
	public int pointCount = 0;
	public int lineCount = 0;
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
		for (int i = 0 ;i < pointCount; i++){
			g.setColor(Color.BLACK);
			g.fillOval((int)point_list.get(i).x, (int)point_list.get(i).y, 3, 3);
		}
		
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
		
		//畫邊
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
	
	public void addPoint(Point p) {
		
		Point pI = new Point();
		pI.x = (int)p.x;
		pI.y = (int)p.y;
		
		if (!point_list.contains(pI)) {
        	pointCount++;
        	point_list.add(pI);
        } 
		
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
		
        lineCount++;	
        repaint();
	}
	
	public void getAllLine() {
		
		System.out.println(line_listL.size()+" "+line_listR.size());
		if (!(line_listAll.size() == line_listL.size() + line_listR.size())) {
			
			for (int i = 0; i < line_listL.size(); i++)
				line_listAll.add(line_listL.get(i));
			
			for (int i = 0; i < line_listR.size(); i++)
				line_listAll.add(line_listR.get(i));
		}
	//	for (int i = 0; i < line_listAll.size(); i++) System.out.println(line_listAll.get(i).p_a.x+" "+line_listAll.get(i).p_a.y+" "+line_listAll.get(i).p_b.x+" "+line_listAll.get(i).p_b.y);
		
		//sort
		line_listAll.sort(new LineCmp());
	}
	
	public void reStep() {
		lineCount = 0;
		
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
		pointCount = 0;
		lineCount = 0;
		
		point_list.clear();
		line_listL.clear();
		line_listR.clear();
		line_listAll.clear();
		
		hull = 0;
		hyper = false;
		left = false;
		right = false;
		
		repaint();
	}
	
	
	
}

class LineCmp implements Comparator<Line> {
	@Override
	public int compare(Line l1, Line l2) {
		if ((l1.p_a).x != (l2.p_a).x)
			return (int)((l1.p_a).x - (l2.p_a).x);
		else{
			if ((l1.p_a).y != (l2.p_a).y)
				return (int)((l1.p_a).y - (l2.p_a).y);
			else{
				if ((l1.p_b).x != (l2.p_b).x)
					return (int)((l1.p_b).x - (l2.p_b).x);
				else
					return (int)((l1.p_b).y - (l2.p_b).y);
			}
		}
	}
}
