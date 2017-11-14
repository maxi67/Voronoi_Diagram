package voronoi;

import java.awt.Point;

public class TwoP {
	public static PDouble p1;
	public static PDouble p2;

	public TwoP(PDouble p1, PDouble p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public double midpointX() {
		return (p1.x + p2.x)/2;
	}
	
	public double midpointY() { // ���IY�y��
		return (p1.y + p2.y)/2;
	}
	
	public Point direction() { // ���I��V�V�q
		Point p = new Point((int)(p2.x - p1.x), (int)(p2.y - p1.y));
		return p;
	}
	
	public boolean isVertival() {
		if(p2.y == p1.y)
			return true;
		return false;
	}
	
	public double interceptX() { //X�I�Z
		return p2.x - p1.x;
	}
	
	public double slope() { //�ײv
		double s = (double)(p2.y - p1.y)/(p2.x - p1.x);
		return s;
	}
	
}
