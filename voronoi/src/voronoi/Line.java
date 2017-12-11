package voronoi;

public class Line {
	public PDouble p_a = new PDouble();
	public PDouble p_b = new PDouble();
	private double Length;
	
	public Line(PDouble pa, PDouble pb) {
			p_a.x = pa.x;
			p_a.y = pa.y;
			p_b.x = pb.x;
			p_b.y = pb.y;
			Length = Math.sqrt((Math.pow((p_a.x - p_b.x), 2) + Math.pow((p_a.y - p_b.y), 2))); //線段長 
	}

	//內部線延伸到X軸邊界
	public Line toBoundX() {
	
		int maxX = 900;
		int maxY = 900;
		int minX = 0;
		int minY = 0;
		
		Line tempL;
        if (p_a.x  - p_b.x == 0){	// 垂直線	
            PDouble a = new PDouble( (p_a).x , (double)maxY); 
            PDouble b = new PDouble( (p_a).x , (double)minY); 
            tempL = new Line(a, b); 
            return tempL;
        }
        
        else {
        	double m = ((p_a).y - (p_b).y) / ((p_a).x - (p_b).x);
        	// Y = m * (X - x) + y
            maxY = (int) (m * (maxX - (p_a).x) + (p_a).y);
            minY = (int) (m * (minX - (p_a).x) + (p_a).y);

            PDouble left = new PDouble();
            left.x = (double)minX; left.y = (double)minY;
            
            PDouble right = new PDouble();
            right.x = (double)maxX; right.y = (double)maxY;
            
            tempL = new Line(left, right); 
            tempL = toBoundY(tempL);
            return tempL;
        }
		
	}
	
	//內部線延伸到Y軸邊界
	public Line toBoundY(Line line){

		int minY = 0;
		int maxY = 900;
		
        double m =  ((line.p_a).y - (line.p_b).y) / ((line.p_a).x - (line.p_b).x) ;	
        
        int X;
        if (line.p_a.y < minY ){	 // X = (Y - y) / m + x 
            X = (int) (((minY - (line.p_a).y)) / m + (line.p_a).x);     
            line.p_a.x = X;
            line.p_a.y = minY;
        }   
        
        if (line.p_a.y > maxY){	
            X = (int) (((maxY - (line.p_a).y)) / m + (line.p_a).x);     
            line.p_a.x = X;
            line.p_a.y = maxY;
        }
        
        if (line.p_b.y < minY ){	
            X = (int) (((minY - (line.p_b).y)) / m + (line.p_b).x);    
            line.p_b.x = X;
            line.p_b.y = minY;
        }   
        
        if (line.p_b.y > maxY){	
            X = (int) (((maxY - (line.p_b).y)) / m + (line.p_b).x);     
            line.p_b.x = X;
            line.p_b.y = maxY;
        }
        return line;
	}
	
	//超過邊界值的拉回來
	public void change() { 
		double m = ((p_a).y - (p_b).y) / ((p_a).x - (p_b).x);
		if (p_a.x < 0) {
			p_a.y = p_b.y - p_b.x * m;
			p_a.x = 0;
		}
		
		if (p_a.x > 900) {
			p_a.y = p_b.y + (900-p_b.x) * m;
			p_a.x = 900;
		}
		
		if (p_b.x < 0) {
			p_b.y = p_a.y - p_a.x * m;
			p_b.x = 0;
		}
		
		if (p_b.x > 900) {
			p_b.y = p_a.y + (900-p_a.x) * m;
			p_b.x = 900;
		}
		
		
		if (p_a.y < 0) {
			p_a.x = p_b.x - p_b.y / m;
			p_a.y = 0;
		}
		
		if (p_a.y > 900) {
			p_a.x = (900 - p_b.y)/m + p_b.x;
			p_a.y = 900;
		}
		
		if (p_b.y < 0) {
			p_b.x = p_a.x - p_a.y / m;
			p_b.y = 0;
		}
		
		if (p_b.y > 900) {
			p_b.x = (900 - p_a.y)/m + p_a.x;
			p_b.y = 900;
		}
		
		

	}
	
	//中垂線
	public Line mLine() { 
		double temp;
        PDouble c = new PDouble(); //兩點中點
        PDouble v = new PDouble(); //法向量
        
        c.x =  (p_a.x + p_b.x) / 2 ;
        c.y =  (p_a.y + p_b.y) / 2 ;
        
        //先取方向向量
        v.x = p_b.x - p_a.x;
        v.y = p_b.y - p_a.y;
        
        //法向量
        temp = v.x;
        v.x = v.y;
        v.y = temp;
        v.x *= -1;
        
        v.x += c.x;
        v.y += c.y;
        return new Line(c, v);
    }
	
	public double getLength(){
    	return Length;
    }
	
	public Line() {}
}
