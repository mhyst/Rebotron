/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rebotron;

import java.util.ArrayList;

/**
 *
 * @author mhyst
 */
public class QuadTree {
    public static final int QT_NODE_CAPACITY = 4;
    
    private Area boundary;
    private ArrayList<Point> points;
    
    private QuadTree northWest;
    private QuadTree northEast;
    private QuadTree southWest;
    private QuadTree southEast;
    
    public QuadTree(Area boundary) {
        this.boundary = boundary;
        points = new ArrayList<Point>();
    }
    
    public boolean insert(Point point) {
        if (!boundary.containsPoint(point)) {
            return false;
        }
        
        if (points.size() < QT_NODE_CAPACITY) {
            points.add(point);
            return true;
        }
        
        if (northWest == null) {
            subdivide();
        }
        
        if (northWest.insert(point)) return true;
        if (northEast.insert(point)) return true;
        if (southWest.insert(point)) return true;
        if (southEast.insert(point)) return true;        
        
        return false;
    }
    
    public void subdivide() {
        int x, y, w, h;
        x = boundary.getOrigin().getX();
        y = boundary.getOrigin().getY();
        w = boundary.getDimension().getX();
        h = boundary.getDimension().getY();
        
        northWest = new QuadTree(new Area(new Point(x,y), new Point(w/2, h/2)));
        northEast = new QuadTree(new Area(new Point(x+w/2,y), new Point(w/2, h/2)));
        southWest = new QuadTree(new Area(new Point(x,y+h/2), new Point(w/2, h/w)));
        southEast = new QuadTree(new Area(new Point(x+w/2,y+h/2), new Point(w/2, h/w)));
    }
    
    public ArrayList<Point> queryRange(Area range) {
        ArrayList<Point> pointsInRange = new ArrayList<Point>();
        
        if (!boundary.intersectsArea(range)) {
            return pointsInRange;
        }
        
        for (Point p : points) {
            if (range.containsPoint(p)) {
                pointsInRange.add(p);
            }
        }
        
        if (northWest == null) {
            return pointsInRange;
        }
        
        pointsInRange.addAll(northWest.queryRange(range));
        pointsInRange.addAll(northEast.queryRange(range));
        pointsInRange.addAll(southWest.queryRange(range));
        pointsInRange.addAll(southEast.queryRange(range));
        
        return pointsInRange;
    }
}
