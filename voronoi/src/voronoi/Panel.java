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
	
	public int pointCount = 0;
	public int lineCount = 0;
//	public ArrayList<PDouble> point_list = new ArrayList<PDouble>();
	public ArrayList<Point> point_list = new ArrayList<Point>();
	public List<Line> line_list = new ArrayList<Line>();
	
	public Panel() {
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		//畫點
		for(int i = 0 ;i < pointCount; i++){
			g.setColor(Color.BLACK);
			g.fillOval((int)point_list.get(i).x, (int)point_list.get(i).y, 4, 4);
		}
		
		//畫邊
		for(int i = 0 ;i < lineCount;i++){
			g.drawLine((int)(line_list.get(i).p_a).x, (int)(line_list.get(i).p_a).y, (int)(line_list.get(i).p_b).x, (int)(line_list.get(i).p_b).y);
		}
	}
	
	public void addPoint(Point p) {
		
		Point pI = new Point();
		pI.x = (int)p.x;
		pI.y = (int)p.y;
		
		if(!point_list.contains(pI)) {
        	pointCount++;
        	point_list.add(pI);
        } 
        repaint();
	}
	
	public void addLine(Line l) {
		
		if(!line_list.contains(l)) {
        	lineCount++;
        	line_list.add(l);
        } 	 
        repaint();
	}
	
	public void lineListSort() {
		
		line_list.sort(new Comparator<Line>(){
			@Override
			public int compare(Line l1, Line l2) {
				if((l1.p_a).x != (l2.p_a).x)
					return (int)((l1.p_a).x - (l2.p_a).x);
				else{
					if((l1.p_a).y != (l2.p_a).y)
						return (int)((l1.p_a).y - (l2.p_a).y);
					else{
						if((l1.p_b).x != (l2.p_b).x)
							return (int)((l1.p_b).x - (l2.p_b).x);
						else
							return (int)((l1.p_b).y - (l2.p_b).y);
					}
				}
			}
		});
	}
	
	public void clean() {
		pointCount = 0;
		lineCount = 0;
		
		point_list.clear();
		line_list.clear();
		repaint();
	}
	
}
