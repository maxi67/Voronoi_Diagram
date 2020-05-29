package voronoi;

import java.awt.Point;

public class PDouble {
	public double x;
	public double y;
	
	public PDouble(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public PDouble() {
		x = 0.0;
		y = 0.0;
	}
	
	public Point getPoint() {
		Point p = new Point();
		p.x = (int)x;
		p.y = (int)y;
		return p;
	}
	
	public static PDouble getPDouble(Point P) {
		PDouble p = new PDouble();
		p.x = (double)P.x;
		p.y = (double)P.y;
		return p;
	}
	
	public boolean isEqual(PDouble p2) {
		if (p2.x == x)
			if (p2.y == y)
				return true;
			
		return false;
	}
}
