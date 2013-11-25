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
public class TreeQuadNode {
    private TreeQuadNode padre;
    private TreeQuadNode[] hijo;
    private int x, y, h, w;
    private ArrayList<Bolon> bolas;

    public TreeQuadNode(TreeQuadNode padre, int x, int y, int h, int w) {
        this.padre = padre;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        hijo = new TreeQuadNode[4];
        bolas = new ArrayList<Bolon>();
    }
    
    public void colisiones() {
        // TODO: Colisiones recursivo
    }
    
    public void subdivide() {
        // TODO: Repasa el árbol y lo subdivide si en un
        //       nodo hay más de 10 objetos;
        if (bolas.size() > 10) {
            hijo[0] = new TreeQuadNode(this, x, y, w/2, h/2);
            hijo[1] = new TreeQuadNode(this, x+w/2, y, w/2, h/2);
            hijo[2] = new TreeQuadNode(this, x, y+h/2, w/2, h/2);
            hijo[3] = new TreeQuadNode(this, x+w/2, y+h/2, w/2, h/2);
            for (int i = 0; i < hijo.length; i++) {
                for (Bolon b : bolas) {
                    if (hijo[i].perteneceArea(b)) {
                        hijo[i].getBolas().add(b);
                        bolas.remove(b);
                    }
                }
                hijo[i].subdivide();
            }
        }
    }
    
    public void revisionBolas() {
        for (Bolon b : bolas) {
            if (!perteneceArea(b)) {
                padre.getBolas().add(b);
                bolas.remove(b);
            }
        }        
        // Se revisan las bolas que ya no me pertenecen
        // Se revisan las bolas que pueden pertenecer
        // a algún hijo
        for (int i = 0; i < hijo.length; i++) {
            hijo[i].revisionBolas();
            for (Bolon b : bolas) {
                if (hijo[i].perteneceArea(b)) {
                    hijo[i].getBolas().add(b);
                    bolas.remove(b);
                }
            }
        }
        for (Bolon b : bolas) {
            if (!perteneceArea(b)) {
                padre.getBolas().add(b);
                bolas.remove(b);
            }
        }                
    }
    
    private boolean perteneceArea(Bolon b) {
        int left = b.getX();
        int top = b.getY();
        int right = left + Pantalla.BALLSIZE;
        int bottom = top + Pantalla.BALLSIZE;
        return left >= x && top >= y && right <= x+w && bottom <= y+h;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public TreeQuadNode[] getHijo() {
        return hijo;
    }

    public void setHijo(TreeQuadNode[] hijo) {
        this.hijo = hijo;
    }

    public TreeQuadNode getPadre() {
        return padre;
    }

    public void setPadre(TreeQuadNode padre) {
        this.padre = padre;
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
    
    public ArrayList<Bolon> getBolas() {
        return bolas;
    }

    public void setBolas(ArrayList<Bolon> bolas) {
        this.bolas = bolas;
    }
}
