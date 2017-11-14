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
			Length = Math.sqrt((Math.pow((p_a.x - p_b.x), 2) + Math.pow((p_a.y - p_b.y), 2)));
		}
	
	public double getLength(){
    	return Length;
    }
	
	public Line() {}
}
