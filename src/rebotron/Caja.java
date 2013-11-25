/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rebotron;

/**
 *
 * @author mhyst
 */
public class Caja {
    private int x;
    private int y;
    private int h;
    private int w;
    
    public Caja(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
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
}
