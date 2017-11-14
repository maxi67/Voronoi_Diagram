package voronoi;

import java.util.Comparator;

public class Compare2DArray implements Comparator {
	  public int compare(Object a, Object b) {
		    int aa[] = (int[]) a;
		    int bb[] = (int[]) b;
		    for (int i = 0; i < aa.length && i < bb.length; i++)
		      if (aa[i] != bb[i])
		        return aa[i] - bb[i];
		    return aa.length - bb.length;
		  }
		}

