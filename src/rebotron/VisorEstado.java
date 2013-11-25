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

/**
 *
 * @author mhyst
 */
public class VisorEstado extends Canvas{
    private LogicaJuego logicaJuego;
    
    public VisorEstado(LogicaJuego lj) {
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
        buff.setColor(Color.black);
        buff.fillRect(0, 0, 740, 50);

        switch(logicaJuego.getFase()) {
            case LogicaJuego.FASE_INICIAL:
                break;
            case LogicaJuego.FASE_JUEGO:
                buff.setFont(new Font("Courier New", Font.BOLD, 38));
                buff.setColor(Color.yellow);
                buff.drawString(Rebotron.lpad(""+logicaJuego.getSegundos(),2), 690, 35);
                break;
            case LogicaJuego.FASE_RESUMEN:
                break;
            case LogicaJuego.FASE_PAUSE:
                buff.setFont(new Font("Arial", Font.BOLD, 38));
                buff.setColor(Color.white);
                buff.drawString("PAUSE", 490, 35);                
                break;
        }
        if (logicaJuego.isFin()) {
            buff.drawImage(logicaJuego.getGameover(),0,0,this);
            buff.setFont(new Font("Curier New", Font.BOLD, 14));
            buff.setColor(Color.white);
            if (logicaJuego.isGanador()) {
                buff.drawString("Ha ganado el juego", 150, 10);
            }
            buff.drawString("Press \"n\" to play again...", 150, 30);
        }
        g.drawImage(bi, 0, 0, this);
    }
}
