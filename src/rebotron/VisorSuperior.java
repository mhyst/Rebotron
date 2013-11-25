/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rebotron;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author mhyst
 */
public class VisorSuperior extends Canvas {
    private LogicaJuego logicaJuego;
    
    public VisorSuperior(LogicaJuego lj) {
        logicaJuego = lj;
    }
    @Override            
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage bi = new BufferedImage(this.getWidth(),
                                             this.getHeight(),
                                             BufferedImage.TYPE_INT_RGB);
        Graphics buff = bi.getGraphics();
        //buff.setColor(new Color(0x40,0x40,0x40));
        //buff.fillRect(0, 0, 750, 80);
        buff.drawImage(logicaJuego.getImgSuperior(), 0, 0, this);

        switch(logicaJuego.getFase()) {
            case LogicaJuego.FASE_INICIAL:
                break;
            case LogicaJuego.FASE_JUEGO:
                break;
            case LogicaJuego.FASE_RESUMEN:
                break;
        }
        g.drawImage(bi, 0, 0, this);
    }    
}
