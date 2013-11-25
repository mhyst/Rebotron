/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rebotron;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author mhyst
 */
public class LogicaJuego {
    // Control del nivel
    private int nivel;
    private Nivel[] niveles;
    
    // Control de posición del jugador
    private int x;
    private int y;
    private int despx = 0;
    private int despy = 0;

    // Control de monedas
    private int monedas = 0;
    private int monedasNivel = 0;
    
    // Segundos transcurridos desde el inicio de un nivel
    private int segundos;
    
    // Desplazamiento añadido a la bola del jugador
    // Define la velocidad del jugador;
    public static final int DESPL = 8;
    
    // Fases del juego
    public static final int FASE_INICIAL = 0;
    public static final int FASE_JUEGO = 1;
    public static final int FASE_RESUMEN = 2;
    public static final int FASE_GANADOR = 3;
    public static final int FASE_PAUSE = 4;
    public static final int FASE_PRERESUMEN = 5;
    public static final int DIST_EFECTO_ARMA = 200;
    private int fase;
    
    // Banderas de estado
    private boolean fin;
    private boolean finJuego;
    private boolean finNivel;
    private boolean ganador;
    private boolean pausa = false;
    private boolean armaActiva = false;
    private boolean escudo = false;
    
    // Música de fondo y timer para música
    private MakeSound msMusic;
    private Timer timerMusic;
    
    // Imágenes del juego
    private BufferedImage gameover = null;
    private BufferedImage monedasIcon = null;
    private BufferedImage moneda = null;
    private BufferedImage ready = null;
    private BufferedImage bolajugador = null;
    private BufferedImage bolamala = null;
    //private BufferedImage bolaiman = null;
    private BufferedImage bolaimanoff = null;
    private BufferedImage bolaimanon = null;
    private BufferedImage imgSuperior = null;
    private BufferedImage imgEscudo = null;
    private BufferedImage imgMarcaJugador = null;
    
    // Zonas visuales del juego (Canvas)
    private VisorSuperior superior;
    private VisorLateral lateral;
    private Pantalla tablero;
    private VisorEstado estado;
    
    private QuadTreeBolon qt = null;

    // Array e índice de selección de música
    public static String[] musica;
    public static int idMusica;
    
    // Cálculos de colisiones
    public static int cc = 0;
    
    
    public LogicaJuego() {
        pausa = false;
        finNivel = false;
        ganador = false;
        fin = false;
        timerMusic = new Timer(true);

        loadImages();
        loadNiveles();
        loadMusic();
    }
    
    public void setTablero(Pantalla p) {
        tablero = p;
    }
    
    public void setEstado(VisorEstado ve) {
        estado = ve;
    }
    
    public void setLateral(VisorLateral vl) {
        lateral = vl;
    }
    
    public void setSuperior(VisorSuperior vs) {
        superior = vs;
    }
    
    public int getFase() {
        return fase;
    }
    
    public int getMonedas() {
        return monedas;
    }
    
    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }
    
    public void terminarJuego() {
        finJuego = true;
        System.exit(0);
    }
    
    public void run() throws InterruptedException {
        ganador = false;
        finJuego = false;
        armaActiva = false;

        //nivel = 3;
        while (true) {
            if (ganador) {
                fase = FASE_GANADOR;
                tablero.repaint();
                Thread.sleep(3000);
                break;
            }
            getNivel().iniciarNivel();
            ArrayList<Bolon> bolas = getNivel().getBolasJuego();
            while (fin && !finJuego)
                Thread.sleep(100);
            
            if (finJuego) {
                break;
            }
            
            fase = FASE_INICIAL;
            while(true) {
                //Bucle de la fase inicial
                posicionarJugador(bolas);
                tablero.repaint();
                Thread.sleep(3000);
                break;
            }
            playMusic(siguienteMusica());
            Thread.sleep(50);

            fase = FASE_JUEGO;
            //x = 190;
            //y = 10;
            
            estado.repaint();
            //firstTime = true;

            //int iteraciones = 0;
            long t = System.currentTimeMillis();
            long t2, lapso, lapsoantm, lapsoantb;
            segundos = (int) getNivel().getTiempo()/1000;
            int segant = 0;
            estado.repaint();
            monedasNivel = 0;
            int monedasRestantes = getNivel().getMonedas();
            int bolasRestantes = getNivel().getNumBolas();
            long periodoMonedas = (getNivel().getTiempo()-5000)/monedasRestantes;
            long periodoBolas = (getNivel().getTiempo()-5000)/bolasRestantes;
            lapsoantm = 0;
            lapsoantb = 0;
            
            // Bucle principal de juego
            while (!fin && !finJuego && !ganador) {
                
                //En caso de pausa esperar
                while (isPause()) {
                    Thread.sleep(100);
                    
                    t += 100;
                }

                //Cada 50 milisegundos se produce un movimiento
                Thread.sleep(50, 0);
                //iteraciones++;
                if (gameover(bolas)) {
                    fin = true;
                    estado.repaint();
                    break;
                }
                if (armaActiva) {
                    armaActiva = false;
                    if (monedas >= 3) {
                        monedas -= 3;
                        estado.repaint();
                        ArrayList<Bolon> afectadas = this.getBolasArea(bolas, x, y);
                        efectoArma(afectadas, x, y);
                    }
                }
                x += despx;
                y += despy;
                if (x < 0) x = Pantalla.WIDTH;
                if (x > Pantalla.WIDTH) x = 0;
                if (y < 0) y = Pantalla.HEIGHT;
                if (y > Pantalla.HEIGHT) y = 0;
                /*ArrayList<Caja> cp = posicionCaja(getNivel().getCajas());
                for (Caja c : cp) {
                    int dx = x, dy = y;
                    if (x < c.getX()+c.getW() && despx < 0) {
                        despx = 0;
                        dx = c.getX()+c.getW()+1;
                    }
                    if (x+Pantalla.BALLSIZE > c.getX() && despx > 0) {
                        despx = 0;
                        dx = c.getX()-Pantalla.BALLSIZE-1;
                    }
                    if (y < c.getY()+c.getH() && despy < 0) {
                        despy = 0;
                        dy = c.getY() + c.getH() + 1;
                    }
                    if (y+Pantalla.BALLSIZE > c.getY() && despy > 0) {
                        despy = 0;
                        dy = c.getY()-Pantalla.BALLSIZE - 1;
                    }
                    
                    if (Math.abs(dx-x) <= DESPL)
                        x = dx;
                    if (Math.abs(dy-y) <= DESPL)
                        y = dy;
                }*/
                
                t2 = System.currentTimeMillis();
                lapso = t2 - t;
                
                if ((int)(lapso/1000) != segant) {
                    segant = (int)(lapso/1000);
                    segundos--;
                    estado.repaint();
                }
                //System.out.println("Lapso: "+lapso+" lam: "+lapsoantm+" periodoM: "+periodoMonedas);
                if (monedasRestantes > 0) {
                    if (lapso - lapsoantm >= periodoMonedas) {
                        lapsoantm = lapso;
                        bolas.add(otraMoneda(bolas, getNivel().getCajas()));
                        monedasRestantes--;
                    }
                }
                if (bolasRestantes > 0) {
                    if (lapso - lapsoantb >= periodoBolas) {
                        lapsoantb = lapso;
                        bolas.add(otraBola(bolas, getNivel().getCajas()));
                        bolasRestantes--;
                    }
                }
                //NextMove de las bolas estaba aquí
                //colisiones();
                toQuadTree();
                colisionesQt();
                
                //System.out.println("CC: "+cc);
                tablero.repaint();
                
                if (lapso > getNivel().getTiempo()) {
                    finNivel = true;
                    break;
                }
                
                for (Bolon b : bolas) {
                    b.nextMove();
                }
                
                if (msMusic.acabado()) {
                    playMusic(siguienteMusica());
                }
            }
            msMusic.stop();
            
            if (!fin) {
                playFX("tachan.wav");
                fase = FASE_PRERESUMEN;
                while (hayBolasPantalla(bolas)) {
                    for (Bolon b : bolas) {
                        moveOut(b);
                    }
                    Thread.sleep(20);
                    tablero.repaint();
                }
            } else {
                playFX("rebote.wav");
            }
            fase = FASE_RESUMEN;
            while (true) {
                tablero.repaint();
                Thread.sleep(3000);
                break;
            }
            if (finNivel) {
                nivel++;
                if (nivel >= niveles.length) {
                    ganador = true;
                    nivel = 0;
                }
                finNivel = false;
            }
        }        
        System.exit(0);
    }
    
    public void activarArma() {
        armaActiva = true;
    }
    
    private ArrayList<Bolon> getBolasArea(ArrayList<Bolon> bolas, int x, int y) {
        ArrayList<Bolon> aux = new ArrayList<Bolon>();
        for (Bolon b : bolas) {
            if (b.distancia(x, y) < DIST_EFECTO_ARMA && !(b instanceof Moneda)) {
                aux.add(b);
            }
        }
        return aux;
    }
    
    private void efectoArma(ArrayList<Bolon> bolas, int ox, int oy) {
        for (Bolon b : bolas) {
            b.affectarDireccion(ox, oy);
        }
    }
    
    
    private boolean colisionaCaja(int x, int y, int w, int h) {
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
    
    private ArrayList<Caja> posicionCaja(ArrayList<Caja> cajas) {
        ArrayList<Caja> res = new ArrayList<Caja>();
        for (Caja c : cajas) {
            if (colisionaCaja(c.getX(), c.getY(), c.getW(), c.getH())) {
                res.add(c);
            }
        }
        return res;
    }    
    
    public int getSegundos() {
        return segundos;
    }
    
    public int getSegundosRestantes() {
        return (int) getNivel().getTiempo() / 1000 - segundos;
    }
    
    public boolean togglePause() {
        msMusic.tooglePause();
        pausa = !pausa;
        if (pausa) {
            fase = FASE_PAUSE;
        } else {
            fase = FASE_JUEGO;
        }
        estado.repaint();        
        return pausa;
    }
    
    public boolean isPause() {
        return pausa;
    }
    
    public boolean toggleEscudo() {
        escudo = !escudo;
        return escudo;
    }
    
    public boolean isEscudo() {
        return escudo;
    }
    
    public int getMonedasNivel() {
        return monedasNivel;
    }
    
    private boolean hayBolasPantalla(ArrayList<Bolon> bolas) {
        boolean hay = false;
        for (Bolon b : bolas) {
            if (b.getX() > 0 && b.getY() > 0) {
                hay = true;
                break;
            }
        }
        return hay;
    }

    private void moveOut(Bolon b) {
        int olx = b.getX();
        int oly = b.getY();
        int nx, ny;
        if (olx > oly) {
            nx = olx-3;
            ny = oly-2;
        } else {
            nx = olx-2;
            ny = oly-3;
        }
        int dx = Math.abs(x - nx);
        int dy = Math.abs(y - ny);

        if (b.distancia(x,y) < 40) {
            if (dx > dy) { 
                nx = olx;
            } else {
                ny = oly;
            }
        }
        /*if (dx < 0 && dy < 0) {
            if (b.distancia(x,y) < 40) {
                if (dx < 40) {
                    nx = olx;
                } else if (dy < 40) {
                    ny = oly;
                } else {
                    //int aux = nx;
                    nx = ny;
                    //ny = nx;
                }
            }
        }*/
        b.setX(nx);
        b.setY(ny);
    }

    public boolean demasiadoCerca(ArrayList<Bolon> bolas, int x, int y) {
        boolean tooClose = false;
        for (Bolon n : bolas) {
            if (n.distancia(x, y) < 100) {
                tooClose = true;
                break;
            }
        }
        return tooClose;
    }
    
    private void posicionarJugador(ArrayList<Bolon> bolas) {
        boolean tooClose = false;
        
        x = (int) (Math.random() * Pantalla.WIDTH);
        y = (int) (Math.random() * Pantalla.HEIGHT);
        while (demasiadoCerca(bolas, x, y)) {
            x = (int) (Math.random() * Pantalla.WIDTH);
            y = (int) (Math.random() * Pantalla.HEIGHT);
        }
    }
    
    public Nivel getNivel() {
        return niveles[nivel];
    }
    
    public void nextLevel() {
        nivel++;
        if (nivel > 5) {
            fin = true;
        }
    }
    
    private void loadNiveles() {
        Nivel n;
        nivel = 0;
        niveles = new Nivel[6];
        n = new Nivel("nivel1.jpg",30000,3,3);
        /*n.addCaja(new Caja(80,80,100,20));
        n.addCaja(new Caja(80,100,20,100));
        n.addCaja(new Caja(480,80,100,20));
        n.addCaja(new Caja(560,100,20,100));
        n.addCaja(new Caja(80,300,20,100));
        n.addCaja(new Caja(80,400,100,20));
        n.addCaja(new Caja(560,300,20,100));
        n.addCaja(new Caja(480,400,100,20));
        n.addCaja(new Caja(240,230,200,20));*/
        for (int i = 0; i < 5; i++) {
            n.addBola(new Bolon(n.getBolas(), n.getCajas()));
        }
        niveles[0] = n;
        
        n = new Nivel("nivel2.jpg",35000,4,3);      
        for (int i = 0; i < 8; i++) {
            n.addBola(new Bolon(n.getBolas(), n.getCajas()));
        }
        niveles[1] = n;

        n = new Nivel("nivel3.jpg",40000,5,3);
        for (int i = 0; i < 10; i++) {
            n.addBola(new Bolon(n.getBolas(), n.getCajas()));
        }
        niveles[2] = n;
        
        n = new Nivel("nivel4.jpg",45000,6,3);       
        for (int i = 0; i < 10; i++) {
            n.addBola(new Bolon(n.getBolas(), n.getCajas()));
        }
        n.addBola(new BolonPerseguidor(n.getBolas(), n.getCajas()));
        niveles[3] = n;
        
        n = new Nivel("nivel5.jpg",50000,7,4);      
        for (int i = 0; i < 14; i++) {
            n.addBola(new Bolon(n.getBolas(), n.getCajas()));
        }
        n.addBola(new BolonPerseguidor(n.getBolas(), n.getCajas()));
        n.addBola(new BolonPerseguidor(n.getBolas(), n.getCajas()));
        niveles[4] = n;        
        
        n = new Nivel("nivel5.jpg",60000,8,5);      

        for (int i = 0; i < 15; i++) {
            n.addBola(new Bolon(n.getBolas(), n.getCajas()));
        }
        n.addBola(new BolonPerseguidor(n.getBolas(), n.getCajas()));
        n.addBola(new BolonPerseguidor(n.getBolas(), n.getCajas()));
        n.addBola(new BolonPerseguidor(n.getBolas(), n.getCajas()));
        niveles[5] = n;        
        
        
        /*for(int i=1; i < niveles.length; i++) {
            Nivel n = new Nivel("nivel"+(i+1)+".jpg",30000,8000,5000);
            int nBolas = (i+1)*5;
            for (int j = 0; j < nBolas; j++) {
                Bolon b = new Bolon(n.getBolas());
                n.addBola(b);
                //n.addBola(otraBola());
            }
            niveles[i] = n;
        }*/
    }
    
    private void loadMusic() {
        idMusica = 0;
        musica = new String[10];
        musica[0] = "musica0.wav";
        musica[1] = "musica1.wav";
        musica[2] = "musica2.wav";
        musica[3] = "musica3.wav";
        musica[4] = "musica4.wav";
        musica[5] = "musica5.wav";
        musica[6] = "musica6.wav";
        musica[7] = "musica7.wav";
        musica[8] = "musica8.wav";
        musica[9] = "musica9.wav";

        msMusic = new MakeSound();        
    }
    
    private void loadImages() {
        try {
            //fondo = ImageIO.read(new File("nivel1.jpg"));
            gameover = ImageIO.read(new File("gameover.jpg"));
            monedasIcon = ImageIO.read(new File("monedas.png"));
            moneda = ImageIO.read(new File("moneda.png"));
            ready = ImageIO.read(new File("ready.jpg"));
            bolajugador = ImageIO.read(new File("bolajugador.png"));
            bolamala = ImageIO.read(new File("bolamala.png"));
            bolaimanoff = ImageIO.read(new File("bolaiman.png"));
            bolaimanon = ImageIO.read(new File("bolaimanon.png"));
            imgSuperior = ImageIO.read(new File("superior.jpg"));
            imgEscudo = ImageIO.read(new File("escudo.png"));
            imgMarcaJugador = ImageIO.read(new File("marca.png"));
        } catch (IOException ex) {
            Logger.getLogger(LogicaJuego.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Bolon otraBola(ArrayList<Bolon> bolas, ArrayList<Caja> cajas) {
        /*int x = (int) (Math.random() * Pantalla.WIDTH);
        int y = (int) (Math.random() * Pantalla.HEIGHT);*/
        int t = (int) (Math.random() * 100);
        Bolon b;
        if (t < 20) {
            b = new Moneda(bolas, cajas);
        } else {
            b = new Bolon(bolas, cajas);
        }
        return b;
    }

    public static Bolon otraMoneda(ArrayList<Bolon> bolas, ArrayList<Caja> cajas) {
        int x = (int) (Math.random() * Pantalla.WIDTH);
        int y = (int) (Math.random() * Pantalla.HEIGHT);
        
        Bolon b = new Moneda(bolas, cajas);
        return b;
    }
    
    public String siguienteMusica() {
        String res = musica[idMusica];
        idMusica++;
        if (idMusica > 9 ) {
            idMusica = 0;
        }
        return res;
    }
    
    private void toQuadTree() {
        ArrayList<Bolon> bolas = getNivel().getBolasJuego();
        qt = new QuadTreeBolon(new Area(new Point(0,0), new Point(640,480)));
        for (Bolon b : bolas) {
            if (!qt.insert(b))
                System.out.println("Bola no se pudo insertar: "+b);
        }
    }
    
    private void updateQuadTree() {
        ArrayList<Bolon> bolas = qt.ajustar();
        for (Bolon b : bolas) {
            if (!qt.insert(b))
                System.out.println("Bola no se pudo insertar: "+b);
        }        
    }
    
    public void colisionesQt() {
        qt.colisiones();
    }
    
    public void colisiones() {
        ArrayList<Bolon> bolas = getNivel().getBolasJuego();
        ArrayList<Caja> cajas = getNivel().getCajas();
        Bolon[] b = new Bolon[bolas.size()];
        b = bolas.toArray(b);
        for (int i = 0; i < b.length; i++) {
            for (int j = i+1; j < b.length; j++) {
                if(b[i].distancia(b[j]) <= Pantalla.BALLSIZE ) {
                    int desx = b[i].getDesx();
                    int desy = b[i].getDesy();
                    b[i].setDesx(b[j].getDesx());
                    b[i].setDesy(b[j].getDesy());
                    b[j].setDesx(desx);
                    b[j].setDesy(desy);
                    playFX("rebote.wav");
                }
            }
            //checkBoxes(b[i], cajas);
        }
    }
    
    private void checkBoxes(Bolon b, ArrayList<Caja> cajas) {
        for (Caja c : cajas) {
            checkBox(b, c.getX(), c.getY(), c.getW(), c.getH());
        }
    }
    
    private void checkBox(Bolon b, int cx, int cy, int w, int h) {
        int cd = cx+w, ci = cx;
        int cs = cy, cinf = cy+h;
        
        int bx = b.getX();
        int by = b.getY();
           
        if (b.colisionaCaja(cx, cy, w, h)) {
            //System.out.println("Colisión");
            int desx = b.getDesx();
            int desy = b.getDesy();
            //int aux;
            if ((desx < 0 && desy > 0) || (desx > 0 && desy > 0)) {
                // Arriba o izquierda
                int bd = bx+Pantalla.BALLSIZE, binf = by+Pantalla.BALLSIZE;
                int d1 = Math.abs(bd-ci), d2 = Math.abs(binf-cs);
                if (d1 < d2) {
                    // Rebota por la izquierda
                    b.setX(ci-Pantalla.BALLSIZE);
                    desx = -desx;
                } else {
                    // Rebota por arriba
                    b.setY(cs-Pantalla.BALLSIZE);
                    desy = -desy;
                }
           } else if ((desx > 0 && desy < 0) || (desx < 0 && desy < 0)) {
                // Abajo o derecha
                int bi = bx, bs = by;
                int d1 = Math.abs(bi-cd), d2 = Math.abs(bs-cinf);
                if ( d1 < d2) {
                    b.setX(cd);
                    desx = -desx;
                } else {
                    b.setY(cinf);
                    desy = -desy;
                }
            }
            
            b.setDesx(desx);
            b.setDesy(desy);
        }
    }
    
    
    public boolean gameover(ArrayList<Bolon> bolas) {
        boolean fin = false;
        for (Bolon b : bolas) {
            if (escudo) {
                if (b.distancia(x, y) <= Pantalla.BALLSIZE+30) {
                    b.affectarDireccion(x, y);
                }
            } else {
                if (b.distancia(x, y) <= Pantalla.BALLSIZE) {
                    if (b instanceof Moneda) {
                        bolas.remove(b);
                        monedas++;
                        monedasNivel++;
                        playFX("moneda.wav");
                    } else if (!b.isInicio()) {
                        fin = true;
                        monedas -= monedasNivel;
                        playFX("fin.wav");

                    }
                    lateral.repaint();
                    break;                
                }
            }
        }
        return fin;
    }
    
    public static void playFX(String file) {
        new AePlayWave(file).start();
        //timerFX.schedule(new PonerSonido(msFX,file), 0);
    }

    public void playMusic(String file) {
        timerMusic.schedule(new PonerSonido(msMusic,file), 0);
    }

    public boolean isGanador() {
        return ganador;
    }
    
    public boolean isFin() {
        return fin;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }    

    public BufferedImage getGameover() {
        return gameover;
    }

    public void setGameover(BufferedImage gameover) {
        this.gameover = gameover;
    }
    
    public BufferedImage getMonedasIcon() {
        return monedasIcon;
    }
    
    public BufferedImage getMoneda() {
        return moneda;
    }

    public BufferedImage getReady() {
        return ready;
    }

    public BufferedImage getBolaJugador() {
        return bolajugador;
    }

    public BufferedImage getBolaMala() {
        return bolamala;
    }
    
    public BufferedImage getBolaImanOn() {
        return bolaimanon;
    }

    public BufferedImage getBolaImanOff() {
        return bolaimanoff;
    }
    
    public BufferedImage getImgSuperior() {
        return imgSuperior;
    }

    public BufferedImage getImgEscudo() {
        return imgEscudo;
    }
    
    public BufferedImage getImgMarcaJugador() {
        return imgMarcaJugador;
    }

    public int getNivelId() {
        return nivel;
    }

    /*public void setNivel(int nivel) {
        this.nivel = nivel;
    }*/

    public Nivel[] getNiveles() {
        return niveles;
    }

    public void setNiveles(Nivel[] niveles) {
        this.niveles = niveles;
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

    public int getDespx() {
        return despx;
    }

    public void setDespx(int despx) {
        this.despx = despx;
    }

    public int getDespy() {
        return despy;
    }

    public void setDespy(int despy) {
        this.despy = despy;
    }
    
    public void stopMusic() {
        msMusic.stop();
    }    
}