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
public class BolonPerseguidor extends Bolon {
    private boolean on = false;
    
    public BolonPerseguidor(int x, int y) {
        super(x,y);
    }
    
    public BolonPerseguidor(ArrayList<Bolon> bolas, ArrayList<Caja> cajas) {
        super(bolas, cajas);
    }
    
    public boolean isOn() {
        return on;
    }
    
    @Override
    public void nextMove() {
        int jx = Rebotron.lj.getX();
        int jy = Rebotron.lj.getY();
        
        if (distancia(jx,jy) < 130) {
            on = true;
            // Generar nuevos desx y desy
            int dx, dy;
            dx = Math.abs(getX() - jx);
            dy = Math.abs(getY() - jy);
            if (dx == 0) {
                dy = 3;
            } else if (dy == 0) {
                dx = 3;
            } else {
                if (dx > dy) {
                    dx = 3;
                    dy = 2;
                } else {
                    dy = 3;
                    dx = 2;
                }
                /*dx *= 2;
                dy *= 2;*/
                if (jx > getX()) {
                    if (dx < 0) dx *= -1;
                } else {
                    if (dx > 0) dx *= -1;
                }
                if (jy > getY()) {
                    if (dy < 0) dy *= -1;
                } else {
                    if (dy > 0) dy *= -1;
                }
                desx = dx;
                desy = dy;
                /*if (desx > 2) desx = 2;
                if (desy > 2) desy = 2;*/
            }
        } else {
            on = false;
        }
        //setX(getX()+desx);
        //setY(getY()+desy);
        super.nextMove();
    }
}
