/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rebotron;

/**
 *
 * @author mhyst
 */
public class Area {
    private Point origin;
    private Point dimension;
    
    public Area(Point origin, Point dimension) {
        this.origin = origin;
        this.dimension = dimension;
    }

    public int minBoundaryX() {
        return origin.getX();
    }

    public int maxBoundaryX() {
        return origin.getX()+dimension.getX();
    }

    public int minBoundaryY() {
        return origin.getY();
    }

    public int maxBoundaryY() {
        return origin.getY()+dimension.getY();
    }
    
    public int getWidth() {
        return dimension.getX();
    }
    
    public int getHeight() {
        return dimension.getY();
    }
    
    public boolean containsPoint(Point p) {
        return p.getX() >= minBoundaryX() && p.getX() <= maxBoundaryX() &&
               p.getY() >= minBoundaryY() && p.getY() <= maxBoundaryY();
    }
    
    public boolean intersectsArea(Area other) {
        //R1 = this, R2 = other
        int ox, oy, ow, oh;
        ox = other.getOrigin().getX();
        oy = other.getOrigin().getY();
        ow = other.getDimension().getX();
        oh = other.getDimension().getY();
        int x, y, w, h;
        x = origin.getX();
        y = origin.getY();
        w = dimension.getX();
        h = dimension.getY();
        return (ox >= x && ox <= x+w &&
                oy >= y && oy <= y+h &&
                ox+ow >= x && ox+ow <= x+w &&
                oy+oh >= y && oy+oh <= y+h);
    }

    public Point getDimension() {
        return dimension;
    }

    public void setDimension(Point dimension) {
        this.dimension = dimension;
    }

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }
    
    public String toString() {
        return "Origen: "+origin+" Dimensiones: "+dimension;
    }
    
    public static void main(String[] argv) {
        Area a = new Area(new Point(0,0), new Point(20,100));
        Point p = new Point(21,90);
        
        if (a.containsPoint(p)) {
            System.out.println("El punto está dentro");
        } else {
            System.out.println("El punto está fuera");
        }
    }
}
