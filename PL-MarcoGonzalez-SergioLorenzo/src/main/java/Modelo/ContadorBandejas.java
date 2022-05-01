/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class ContadorBandejas {
    
    private int cantidad;

    public ContadorBandejas(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public synchronized void annadirBandeja(){
        cantidad++;
        notify();
    }

    public synchronized void extraerBandeja(){
        while(cantidad==0){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ContadorBandejas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        cantidad--;
    }
    
    public int get(){
        return cantidad;
    }
}
