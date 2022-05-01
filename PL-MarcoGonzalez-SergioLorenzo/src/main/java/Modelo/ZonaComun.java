/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import GUI.Escritor;
import static java.lang.Thread.sleep;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class ZonaComun {
    private CopyOnWriteArrayList<Ninno> ninnoZonaComun = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Monitor> monZonaComun = new CopyOnWriteArrayList<>();

    public void descansar(Ninno ninno){
        Paso.mirar();
        ninnoZonaComun.add(ninno);
        Escritor.addMsg(ninno.getMiId() + " inicia su descanso");
        Paso.mirar();
        try {
            sleep((int) (2000 + 2000*Math.random()));
        } catch (InterruptedException ex) {
            Logger.getLogger(ZonaComun.class.getName()).log(Level.SEVERE, null, ex);
        }
        Paso.mirar();
        ninnoZonaComun.remove(ninno);
        Escritor.addMsg(ninno.getMiId() + " finaliza su descanso");
    }
    
    public void descansar(Monitor mon){
        Paso.mirar();
        monZonaComun.add(mon);
        Escritor.addMsg(mon.getMiId() + " inicia su descanso");
        Paso.mirar();
        try {
            sleep((int) (1000 + 1000*Math.random()));
        } catch (InterruptedException ex) {
            Logger.getLogger(ZonaComun.class.getName()).log(Level.SEVERE, null, ex);
        }
        Paso.mirar();
        monZonaComun.remove(mon);
        Escritor.addMsg(mon.getMiId() + " finaliza su descanso");
    }
    
    public String getNinno(){
        String msg = "";
        for (Ninno ninno:ninnoZonaComun){
            msg += ninno.getMiId() + " ";
        }
        return msg;    
    }
    
    public String getMon(){
        String msg = "";
        for (Monitor mon:monZonaComun){
            msg += mon.getMiId() + " ";
        }
        return msg;    
    }
}
