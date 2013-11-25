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
public class VisorLateral extends Canvas{
    private LogicaJuego logicaJuego;
    
    public VisorLateral(LogicaJuego lj) {
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
        buff.fillRect(0, 0, 100, 480);
        
        buff.drawImage(logicaJuego.getMonedasIcon(),15,5,this);
        buff.setFont(new Font("Courier New", Font.BOLD, 14));
        buff.setColor(Color.lightGray);
        buff.fillRect(22, 48, 52, 16);
        buff.setColor(Color.black);
        buff.drawString(Rebotron.lpad(""+logicaJuego.getMonedas(),5), 30, 61);

        g.drawImage(bi, 0, 0, this);
    }
}
