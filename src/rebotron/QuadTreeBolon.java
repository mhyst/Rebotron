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
public class QuadTreeBolon {
    public static final int QT_NODE_CAPACITY = 4;
    
    private Area boundary;
    private ArrayList<Bolon> bolas;
    
    private QuadTreeBolon northWest = null;
    private QuadTreeBolon northEast = null;
    private QuadTreeBolon southWest = null;
    private QuadTreeBolon southEast = null;
    
    public QuadTreeBolon(Area boundary) {
        this.boundary = boundary;
        bolas = new ArrayList<Bolon>();
    }
    
    public boolean insert(Bolon b) {
        if (!boundary.intersectsArea(b.getArea())) {
            return false;
        }
        
        if (bolas.size() < QT_NODE_CAPACITY && northWest == null) {
            bolas.add(b);
            return true;
        }
        
        if (northWest == null) {
            subdivide();
        }
        
        if (northWest.insert(b)) return true;
        if (northEast.insert(b)) return true;
        if (southWest.insert(b)) return true;
        if (southEast.insert(b)) return true;        
        
        bolas.add(b);
        return true;
    }
    
    public void subdivide() {
        int x, y, w, h;
        x = boundary.getOrigin().getX();
        y = boundary.getOrigin().getY();
        w = boundary.getDimension().getX();
        h = boundary.getDimension().getY();
        
        northWest = new QuadTreeBolon(new Area(new Point(x,y), new Point(w/2, h/2)));
        northEast = new QuadTreeBolon(new Area(new Point(x+w/2,y), new Point(w/2, h/2)));
        southWest = new QuadTreeBolon(new Area(new Point(x,y+h/2), new Point(w/2, h/2)));
        southEast = new QuadTreeBolon(new Area(new Point(x+w/2,y+h/2), new Point(w/2, h/2)));
        ArrayList<Bolon> borrar = new ArrayList<Bolon>();
        for (Bolon b : bolas) {
            if (northWest.insert(b)) {
                borrar.add(b);
                continue;
            }
            if (northEast.insert(b)) {
                borrar.add(b);
                continue;
            }
            if (southWest.insert(b)) {
                borrar.add(b);
                continue;
            }
            if (southEast.insert(b)) {
                borrar.add(b);
                continue;
            }                    
        }
        for (Bolon b : borrar) {
            bolas.remove(b);
        }
    }
    
    public ArrayList<Bolon> queryRange(Area range) {
        ArrayList<Bolon> bolasInRange = new ArrayList<Bolon>();
        
        if (!boundary.intersectsArea(range)) {
            return bolasInRange;
        }
        
        for (Bolon b : bolas) {
            if (range.containsPoint(b.getPoint())) {
                bolasInRange.add(b);
            }
        }
        
        if (northWest == null) {
            return bolasInRange;
        }
        
        bolasInRange.addAll(northWest.queryRange(range));
        bolasInRange.addAll(northEast.queryRange(range));
        bolasInRange.addAll(southWest.queryRange(range));
        bolasInRange.addAll(southEast.queryRange(range));
        
        return bolasInRange;
    }
    
    public void colisiones() {
        
        if (northWest == null) {
            checkColisiones(bolas);
        } else {
            ArrayList<Bolon> ini = bolas;
            for (Bolon b : ini) {
                ArrayList<Bolon> pir = queryRange(b.getCollisionArea());
                checkColisiones(pir);
            }
            /*pir.addAll(northWest.bolas);
            pir.addAll(northEast.bolas);
            pir.addAll(southWest.bolas);
            pir.addAll(southEast.bolas);
            
            checkColisiones(pir);*/
            
            northWest.colisiones();
            northEast.colisiones();
            southWest.colisiones();
            southEast.colisiones();
        }
    }
    
    private void checkColisiones(ArrayList<Bolon> balls) {
        Bolon[] b = new Bolon[balls.size()];
        b = balls.toArray(b);
        for (int i = 0; i < b.length; i++) {
            for (int j = i+1; j < b.length; j++) {
                LogicaJuego.cc++;
                if(b[i].distancia(b[j]) <= Pantalla.BALLSIZE ) {
                    if (b[i].reportColision(b[j])) {
                        int desx = b[i].getDesx();
                        int desy = b[i].getDesy();
                        b[i].setDesx(b[j].getDesx());
                        b[i].setDesy(b[j].getDesy());
                        b[j].setDesx(desx);
                        b[j].setDesy(desy);
                        LogicaJuego.playFX("rebote.wav");
                    }
                }
            }
        }        
    }
    
    public ArrayList<Bolon> ajustar() {
        ArrayList<Bolon> local = new ArrayList<Bolon>();
        ArrayList<Bolon> res = new ArrayList<Bolon>();
        for (Bolon b : bolas) {
            if (!boundary.intersectsArea(b.getArea())) {
                local.add(b);
            }
        }
        bolas.removeAll(local);
        res.addAll(local);
        if (northWest != null) {
            res.addAll(northWest.ajustar());
            res.addAll(northEast.ajustar());
            res.addAll(southWest.ajustar());
            res.addAll(southEast.ajustar());
            if (northWest.bolas.size() == 0 &&
                northEast.bolas.size() == 0 &&
                southWest.bolas.size() == 0 &&
                southEast.bolas.size() == 0) {
                northWest = null;
            }
        }
        return res;
    }
    
    public void imprimir() {
        System.out.println(boundary);
        System.out.println("Bolas propias:");
        for (Bolon b : bolas) {
            System.out.println(b);
        }
        if (northWest == null) {
            return;
        }
        System.out.println("NorthWest:");
        northWest.imprimir();
        System.out.println("NorthEast:");
        northEast.imprimir();
        System.out.println("SouthWest:");
        southWest.imprimir();
        System.out.println("SouthEaest:");
        southEast.imprimir();
    }
    
    public static void main(String[] args) {
        QuadTreeBolon qt = new QuadTreeBolon(new Area(new Point(0,0), new Point(640,480)));
        
        ArrayList<Bolon> bolas = new ArrayList<Bolon>();
        ArrayList<Caja> cajas = new ArrayList<Caja>();
        Bolon b;
        for (int i = 0; i < 20; i++) {
           b = new Bolon(bolas, cajas);
           qt.insert(b);
        }
        qt.imprimir();
    }
}
