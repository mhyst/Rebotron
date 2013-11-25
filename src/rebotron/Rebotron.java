/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rebotron;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;

/**
 *
 * @author mhyst
 */
public class Rebotron {
    public static LogicaJuego lj = null;
    
    public static String lpad(String monedas, int n) {
        String res = "";
        int len = monedas.length();
        int esp = n-len;
        for (int i = 0; i < esp; i++) {
            res += " ";
        }
        res += monedas;
        return res;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
        throws Exception {
        
        LogicaJuego logicaJuego = new LogicaJuego();
        lj = logicaJuego;
        Pantalla tablero = new Pantalla(logicaJuego);
        logicaJuego.setTablero(tablero);
        tablero.setSize(Pantalla.WIDTH, Pantalla.HEIGHT);
        VisorSuperior armas = new VisorSuperior(logicaJuego);
        logicaJuego.setSuperior(armas);
        armas.setSize(750,80);
        VisorLateral botones = new VisorLateral(logicaJuego);
        logicaJuego.setLateral(botones);
        botones.setSize(100, 480);
        VisorEstado estado = new VisorEstado(logicaJuego);
        logicaJuego.setEstado(estado);
        estado.setSize(750,50);
        JPanel pTab = new JPanel();
        pTab.add(tablero);
        JPanel pArm = new JPanel();
        pArm.add(armas);
        JPanel pBot = new JPanel();
        pBot.add(botones);
        JPanel pEst = new JPanel();
        pEst.add(estado);
        JFrame frm = new JFrame("Rebotron 0.0.3") {
            public void paint(Graphics g) {
                g.setColor(Color.darkGray);
                g.fillRect(0, 0, 800, 800);
            }
        };
        frm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP)
                    lj.setDespy(-LogicaJuego.DESPL);
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    lj.setDespy(LogicaJuego.DESPL);
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    lj.setDespx(-LogicaJuego.DESPL);
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    lj.setDespx(LogicaJuego.DESPL);
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                if (!lj.isPause()) {
                    if (e.getKeyCode() == KeyEvent.VK_UP)
                        lj.setDespy(0);
                    if (e.getKeyCode() == KeyEvent.VK_DOWN)
                        lj.setDespy(0);
                    if (e.getKeyCode() == KeyEvent.VK_LEFT)
                        lj.setDespx(0);
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                        lj.setDespx(0);
                    if (e.getKeyCode() == KeyEvent.VK_SPACE)
                        lj.activarArma();
                    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                        lj.toggleEscudo();
                    }
                }
                if (e.getKeyChar() == 'n' && lj.isFin()) {
                    lj.setFin(false);
                }
                if (e.getKeyChar() == 'p') {
                    if (lj.getFase() == LogicaJuego.FASE_JUEGO ||
                        lj.getFase() == LogicaJuego.FASE_PAUSE)
                        lj.togglePause();
                }
            }
        });
        
        frm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                lj.stopMusic();
                //lj.stopFX();
                lj.terminarJuego();
            }
        });
        //frm.setLayout(new BorderLayout());
        frm.add(pArm,BorderLayout.NORTH);
        frm.add(pBot, BorderLayout.WEST);
        frm.add(pTab, BorderLayout.CENTER);
        frm.add(pEst, BorderLayout.SOUTH);

        //frm.setSize(430, 360);
        frm.pack();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);   
        
        logicaJuego.run();
    }    
}
