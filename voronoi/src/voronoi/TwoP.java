package voronoi;

import java.awt.Point;

public class TwoP {
	public PDouble p1;
	public PDouble p2;

	public TwoP(PDouble p1, PDouble p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public double midpointX() {
		return (p1.x + p2.x)/2;
	}
	
	public double midpointY() { // 中點Y座標
		return (p1.y + p2.y)/2;
	}
	
	public Point direction() { // 兩點方向向量
		Point p = new Point((int)(p2.x - p1.x), (int)(p2.y - p1.y));
		return p;
	}
	
	public boolean isVertival() {
		if(p2.y == p1.y)
			return true;
		return false;
	}
	
	public double interceptX() { //X截距
		return p2.x - p1.x;
	}
	
	public static Line getVerticalLine(TwoP L) { //中垂線
		Line l = new Line(); 
		PDouble vir1 = new PDouble();
		PDouble	vir2 = new PDouble();
		
		vir1.x = -(L.p2.y - L.p1.y); 
		vir1.y = L.p2.x - L.p1.x;
		vir2.x =  (L.p2.y - L.p1.y);
		vir2.y =  -(L.p2.x - L.p1.x);

		l.p_a.x = L.midpointX() + vir1.x;
		l.p_a.y = L.midpointY() + vir1.y;
		
		l.p_b.x = L.midpointX() + vir2.x;
		l.p_b.y = L.midpointY() + vir2.y;

		l = l.toBoundX();
		return l;
	}
	
	public double slope() { //斜率
		double s = (double)(p2.y - p1.y)/(p2.x - p1.x);
		return s;
	}
	
}
