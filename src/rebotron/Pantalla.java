/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rebotron;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author mhyst
 */
public class Pantalla extends Canvas {
    
    private LogicaJuego logicaJuego;
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final int BALLSIZE = 15;
    
    public Pantalla(LogicaJuego lj) {
        logicaJuego = lj;
    }
    @Override            
    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        BufferedImage bi = new BufferedImage(this.getWidth(),
                                             this.getHeight(),
                                             BufferedImage.TYPE_INT_RGB);
        Graphics buff = bi.getGraphics();

        ArrayList<Bolon> bolas = logicaJuego.getNivel().getBolasJuego();
        switch (logicaJuego.getFase()) {
            case LogicaJuego.FASE_INICIAL:
                buff.drawImage(logicaJuego.getReady(), 0, 0, this);
                buff.setColor(Color.white);
                buff.setFont(new Font("Courier New", Font.BOLD, 24));
                buff.drawString("Nivel "+(logicaJuego.getNivelId()+1), 110, 60);
                buff.setColor(Color.blue);
                buff.drawImage(logicaJuego.getBolaJugador(), logicaJuego.getX(), logicaJuego.getY(), this);
                buff.drawImage(logicaJuego.getImgMarcaJugador(), logicaJuego.getX()-30, logicaJuego.getY()-30, this);
                break;
            case LogicaJuego.FASE_JUEGO:
            case LogicaJuego.FASE_PAUSE:
                buff.drawImage(logicaJuego.getNivel().getFondo(), 0, 0, this);
                /*buff.setColor(Color.black);
                for (Caja c : logicaJuego.getNivel().getCajas()) {
                    buff.fillRect(c.getX(), c.getY(), c.getW(), c.getH());
                }*/
                //buff.fillRect(250, 150, 100, 100);
                //buff.setColor(Color.blue);
                //buff.fillOval(logicaJuego.getX(), logicaJuego.getY(), BALLSIZE, BALLSIZE);
                buff.drawImage(logicaJuego.getBolaJugador(), logicaJuego.getX(), logicaJuego.getY(), this);
                if (logicaJuego.isEscudo()) {
                    buff.drawImage(logicaJuego.getImgEscudo(), logicaJuego.getX()-30, logicaJuego.getY()-30, this);
                }
                
                //logicaJuego.colisiones();
                for (Bolon b : bolas) {
                    /*if (!logicaJuego.isPause())
                        b.nextMove();*/
                    if (b instanceof Moneda) {
                        //buff.setColor(Color.yellow);
                        buff.drawImage(logicaJuego.getMoneda(), b.getX(), b.getY(), this);
                    } else if (b instanceof BolonPerseguidor) {
                        if (b.visible()) {
                            //buff.setColor(Color.black);
                            if (((BolonPerseguidor)b).isOn()) {
                                buff.drawImage(logicaJuego.getBolaImanOn(), b.getX(), b.getY(), this);
                            } else {
                                buff.drawImage(logicaJuego.getBolaImanOff(), b.getX(), b.getY(), this);
                            }
                                
                            //buff.fillOval(b.getX(), b.getY(), BALLSIZE, BALLSIZE);
                        }
                    } else {
                        if (b.visible()) {
                            buff.drawImage(logicaJuego.getBolaMala(), b.getX(), b.getY(), this);
                        }
                    }
                    //buff.fillOval(b.getX(), b.getY(), BALLSIZE, BALLSIZE);
                }
                break;
            case LogicaJuego.FASE_PRERESUMEN:
                buff.drawImage(logicaJuego.getNivel().getFondo(), 0, 0, this);
                //buff.setColor(Color.blue);
                //buff.fillOval(logicaJuego.getX(), logicaJuego.getY(), BALLSIZE, BALLSIZE);
                buff.drawImage(logicaJuego.getBolaJugador(), logicaJuego.getX(), logicaJuego.getY(), this);
                //logicaJuego.colisiones(bolas);
                for (Bolon b : bolas) {
                    /*if (!logicaJuego.isPause())
                        b.nextMove();*/
                    if (b instanceof Moneda) {
                        //buff.setColor(Color.yellow);
                        buff.drawImage(logicaJuego.getMoneda(), b.getX(), b.getY(), this);
                    }  else if (b instanceof BolonPerseguidor) {
                            buff.drawImage(logicaJuego.getBolaImanOff(), b.getX(), b.getY(), this);
                            //buff.setColor(Color.black);
                            //buff.fillOval(b.getX(), b.getY(), BALLSIZE, BALLSIZE);
                    } else {
                        //buff.setColor(Color.red);
                        buff.drawImage(logicaJuego.getBolaMala(), b.getX(), b.getY(), this);
                    }
                    //buff.fillOval(b.getX(), b.getY(), BALLSIZE, BALLSIZE);
                }
                break;
            case LogicaJuego.FASE_RESUMEN:
                buff.setColor(Color.black);
                buff.fillRect(0, 0, WIDTH, HEIGHT);
                buff.setColor(Color.white);
                buff.setFont(new Font("Courier New", Font.BOLD, 24));
                buff.drawString("Fin Nivel "+(logicaJuego.getNivelId()+1), 90, 150);
                if (logicaJuego.isFin()) {
                    buff.drawString("Esta vez ha perdido", 30, 90);
                } else {
                    buff.drawString("Monedas del nivel: "+(logicaJuego.getNivel().getMonedas()), 90, 170);
                    buff.drawString("Monedas recogidas: "+(logicaJuego.getMonedasNivel()), 90, 190);
                }   
                break;
            case LogicaJuego.FASE_GANADOR:                
                buff.setColor(Color.black);
                buff.fillRect(0, 0, WIDTH, HEIGHT);
                buff.setColor(Color.white);
                buff.setFont(new Font("Courier New", Font.BOLD, 24));
                buff.drawString("Has ganado", 90, 150);
                break;
        }
        g.drawImage(bi, 0, 0, this);
    }    
}
