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
public class Moneda extends Bolon {
    
    public Moneda (int x, int y) {
        super(x,y);
        
    }
    
    public Moneda (ArrayList<Bolon> bolas, ArrayList<Caja> cajas) {
        super(bolas, cajas);
    }
}
