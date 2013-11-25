/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rebotron;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author mhyst
 */
public class Nivel {
    private ArrayList<Bolon> bolasJuego;
    private ArrayList<Bolon> bolas;
    private ArrayList<Caja> cajas;
    private long tiempo;
    private int monedas;
    private int nbolas; //Bolas adicionales
    private BufferedImage fondo = null;
    private String fondoFile;
    
    public Nivel(String filename, long t, int monedas, int nbolas) {
        bolasJuego = new ArrayList<Bolon>();
        bolas = new ArrayList<Bolon>();
        cajas = new ArrayList<Caja>();
        tiempo = t;
        this.monedas = monedas;
        this.nbolas = nbolas;
        fondoFile = filename;
        loadImage();
    }
    
    public void addBola(Bolon b) {
        bolas.add(b);
    }
    
    public ArrayList<Bolon> getBolas() {
        return bolas;
    }
    
    public void addCaja(Caja c) {
        cajas.add(c);
    }
    
    public ArrayList<Caja> getCajas() {
        return cajas;
    }
    
    public void iniciarNivel() {
        bolasJuego = new ArrayList<Bolon>();
        bolasJuego.addAll(bolas);
    }

    public ArrayList<Bolon> getBolasJuego() {
        return bolasJuego;
    }

    
    public long getTiempo() {
        return tiempo;
    }

    public int getMonedas() {
        return monedas;
    }

    public int getNumBolas() {
        return nbolas;
    }
    
    private void loadImage() {
        try {
            fondo = ImageIO.read(new File(fondoFile));
        } catch (IOException ex) {
            Logger.getLogger(LogicaJuego.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BufferedImage getFondo() {
        return fondo;
    }
}
