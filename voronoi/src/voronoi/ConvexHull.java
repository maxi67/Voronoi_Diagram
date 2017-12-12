package voronoi;

import java.awt.Point;

import java.util.Arrays;
import java.util.Comparator;

public class ConvexHull {

	public static long cross(Point O, Point A, Point B) {
		return (A.x - O.x) * (B.y - O.y) - (A.y - O.y) * (B.x - O.x);
	}
	
	public static Point[] convex_hull(Point[] P) {

		if (P.length > 1) {
			int n = P.length, k = 0;
			Point[] H = new Point[2 * n];
			PointCmpX arr = new PointCmpX();
			Arrays.sort(P, arr);

			// Build lower hull
			for (int i = 0; i < n; ++i) {
				while (k >= 2 && cross(H[k - 2], H[k - 1], P[i]) <= 0)
					k--;
				H[k++] = P[i];
			}

			// Build upper hull
			for (int i = n - 2, t = k + 1; i >= 0; i--) {
				while (k >= t && cross(H[k - 2], H[k - 1], P[i]) <= 0)
					k--;
				H[k++] = P[i];
			}
			
			if (k > 1) // remove non-hull vertices after k; remove k - 1 which is a duplicate
				H = Arrays.copyOfRange(H, 0, k - 1);
			
			return H;
		} else if (P.length <= 1) {
			return P;
		} else {
			return null;
		}
	}
}

class PointCmpX implements Comparator<Point> {
    public int compare(Point a, Point b) {
    	if (a.x == b.x)
    		return a.y - b.y;
    	else
    		return a.x - b.x;
    }
}

class PointCmpY implements Comparator<Point> {
    public int compare(Point a, Point b) {
    	if (a.y == b.y)
    		return a.x - b.x;
    	else
    		return a.y - b.y;
    }
}
