/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rebotron;

/**
 *
 * @author mhyst
 */
public class Point {
    private int x, y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public String toString() {
        return "("+x+", "+y+")";
    }
}
