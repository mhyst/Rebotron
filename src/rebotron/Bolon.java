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
public class Bolon {
    private int x;
    private int y;
    protected int desx;
    protected int desy;
    private boolean inicio;
    private long creationMillis;
    private long colisionMillis;
    private boolean colision;
    private Bolon oldColision = null;
    
    public Bolon(int x, int y) {
        this.x = x;
        this.y = y;
        desx = Bolon.getDir();
        desy = Bolon.getDir();
        inicio = true;
        creationMillis = System.currentTimeMillis();
        colisionMillis = 0;
        colision = false;
    }
    
    public Bolon(ArrayList<Bolon> bolas, ArrayList<Caja> cajas) {
        this(0,0);
        posicionarBola(bolas, cajas);
    }
    
    public Point getPoint() {
        return new Point(x, y);
    }
    
    public Area getArea() {
        Area a = new Area(new Point(x,y), new Point(Pantalla.BALLSIZE, Pantalla.BALLSIZE));
        return a;
    }
    
    public Area getCollisionArea() {
        Area a = new Area(new Point(x-50,y-50), new Point(Pantalla.BALLSIZE+100, Pantalla.BALLSIZE+100));
        return a;
    }

    public boolean isInicio() {
        return inicio;
    }
    
    public boolean visible() {
        if (!inicio) return true;
        boolean res = false;
        long t = System.currentTimeMillis();
        long r = t - creationMillis;
        if (r > 3000) {
            inicio = false;
            res = true;
        } else {
            res = !(r % 250 < 150);
        }
        return res;
    }
    
    public boolean reportColision(Bolon b) {
        long t = System.currentTimeMillis();
        long desp = t - colisionMillis;
        if (b != oldColision || desp > 500) {
            oldColision = b;
            colision = true;
            colisionMillis = t;
            b.colision = true;
            b.colisionMillis = t;
            b.oldColision = this;
        } else {
            colision = false;
        }
        return colision;
    }
    
    public boolean isColision() {
        return colision;
    }

    public int getDesx() {
        return desx;
    }

    public void setDesx(int desx) {
        this.desx = desx;
    }

    public int getDesy() {
        return desy;
    }

    public void setDesy(int desy) {
        this.desy = desy;
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
    
    
    
    public static int getDir() {
        int r = (int) (Math.random() * 3)+1;
        int s = (int) (Math.random() * 2)+1;
        return s == 1 ? r : -r;
    }
    
    public void nextMove() {
        int aux;
        
        x += desx;
        y += desy;
        
        if (x < 0) {
            x = 0;
            if (desy > 0) {
                aux = desx;
                desx = desy;
                desy = -aux;
            } else {
                aux = desx;
                desx = -desy;
                desy = aux;                
            }
        } else if (x+Pantalla.BALLSIZE > Pantalla.WIDTH) {
            x = Pantalla.WIDTH - Pantalla.BALLSIZE;
            if (desy > 0) {
                aux = desx;
                desx = -desy;
                desy = aux;
            } else {
                aux = desx;
                desx = desy;
                desy = -aux;                
            }
        } else if (y < 0) {
            y = 0;
            if (desx > 0) {
                aux = desx;
                desx = -desy;
                desy = aux;
            } else {
                aux = desx;
                desx = desy;
                desy = -aux;
            }
        } else if (y+Pantalla.BALLSIZE > Pantalla.HEIGHT) {
            y = Pantalla.HEIGHT - Pantalla.BALLSIZE;
            if (desx > 0) {
                aux = desx;
                desx = desy;
                desy = -aux;
            } else {
                aux = desx;
                desx = -desy;
                desy = aux;                
            }
        }         
    }
    
    public double distancia(Bolon b) {
        double c1 = Math.abs(getX()-b.getX());
        double c2 = Math.abs(getY()-b.getY());
        return Math.sqrt(Math.pow(c1, 2)+Math.pow(c2, 2));
    }
    
    public double distancia(int x, int y) {
        double c1 = Math.abs(getX()-x);
        double c2 = Math.abs(getY()-y);
        return Math.sqrt(Math.pow(c1, 2)+Math.pow(c2, 2));
    }
    
    private boolean posicionOcupada(ArrayList<Bolon> bolas, int x, int y) {
        boolean res = false;
        for (Bolon b : bolas) {
            if (b.distancia(x, y)<25) {
                res = true;
            }
        }
        return res;
    }
    
    private boolean posicionCaja(ArrayList<Caja> cajas) {
        boolean res = false;
        for (Caja c : cajas) {
            if (colisionaCaja(c.getX(), c.getY(), c.getW(), c.getH())) {
                res = true;
                break;
            }
        }
        return res;
    }
    
    protected void posicionarBola(ArrayList<Bolon> bolas, ArrayList<Caja> cajas) {
        x = (int) (Math.random() * Pantalla.WIDTH);
        y = (int) (Math.random() * Pantalla.HEIGHT);
        while(posicionOcupada(bolas, x, y) || posicionCaja(cajas)) {
            x = (int) (Math.random() * Pantalla.WIDTH);
            y = (int) (Math.random() * Pantalla.HEIGHT);    
        }
    }
    
    public boolean colisionaCaja(int x, int y, int w, int h) {
        int left, right, top, bottom;
        int r_left, r_right, r_top, r_bottom;
        left = this.x;
        right = this.x + Pantalla.BALLSIZE;
        top = this.y + Pantalla.BALLSIZE;
        bottom = this.y;
        
        r_left = x;
        r_right =  x + w;
        r_top = y + h;
        r_bottom = y;
        
        return right >= r_left && left <= r_right && top >= r_bottom && bottom <= r_top;
    }
    
    public void affectarDireccion(int ox, int oy) {
        int dirx = x - ox;
        int diry = y - oy;
        
        if (Math.abs(dirx) > Math.abs(diry)) {
            dirx = 3 * (int)Math.signum(dirx);
            diry = 2 * (int)Math.signum(diry);
        } else {
            diry = 3 * (int)Math.signum(diry);
            dirx = 2 * (int)Math.signum(dirx);
        }
        /*desx = dirx*2;
        desy = diry*2;*/
        desx = dirx;
        desy = diry;
        //System.out.println("ox: "+ox+" oy: "+oy+" x: "+x+" y: "+y+" dx: "+desx+" dy: "+desy);
    }
    
    public String toString() {
        return "("+x+", "+y+")";
    }
}
