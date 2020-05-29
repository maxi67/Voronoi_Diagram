package voronoi;

import java.awt.Point;
import java.util.Comparator;

//Point[][] ��x->y�q�p��j 
public class Compare implements Comparator<Object> {
	  public int compare(Object a, Object b) {
		    int aa[] = (int[]) a;
		    int bb[] = (int[]) b;
		    for (int i = 0; i < aa.length && i < bb.length; i++)
		      if (aa[i] != bb[i])
		        return aa[i] - bb[i];
		    return aa.length - bb.length;
	  }
}

//ArrayList<Point> / Point[] ��x->y�q�p��j 
class PointCmpX implements Comparator<Point> {
    public int compare(Point a, Point b) {
    	if (a.x == b.x)
    		return a.y - b.y;
    	else
    		return a.x - b.x;
    }
}

//ArrayList<Point> / Point[] ��y->x�q�p��j 
class PointCmpY implements Comparator<Point> {
    public int compare(Point a, Point b) {
    	if (a.y == b.y)
    		return a.x - b.x;
    	else
    		return a.y - b.y;
    }
}

//ArrayList<Line> ��p_a.x -> p_a.y -> p_b.x -> p_b.y�q�p��j 
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

