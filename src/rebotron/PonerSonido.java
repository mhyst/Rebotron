/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rebotron;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mhyst
 */
public class PonerSonido extends TimerTask {
    private MakeSound ms;
    private String archivoSonido;

    public PonerSonido(MakeSound ms, String archivoSonido) {
        this.ms = ms;
        this.archivoSonido = archivoSonido;
    }

    @Override
    public void run() {
        ms.playSound(archivoSonido);
    }
    
}
