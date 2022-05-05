/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Interfaz.Escritor;
import static java.lang.Thread.sleep;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class ZonaComun {
    private Escritor escritor;
    private Paso paso;
    private CopyOnWriteArrayList<Ninno> ninnoZonaComun = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Monitor> monZonaComun = new CopyOnWriteArrayList<>();

    public ZonaComun(Escritor p_escritor, Paso p_paso){
        escritor = p_escritor;
        paso = p_paso;
    }
    
    public void descansar(Ninno ninno){
        paso.mirar();
        ninnoZonaComun.add(ninno);
        escritor.addMsg(ninno.getMiId() + " inicia su descanso");
        paso.mirar();
        try {
            sleep((int) (2000 + 2000*Math.random()));
        } catch (InterruptedException ex) {
            Logger.getLogger(ZonaComun.class.getName()).log(Level.SEVERE, null, ex);
        }
        paso.mirar();
        ninnoZonaComun.remove(ninno);
        escritor.addMsg(ninno.getMiId() + " finaliza su descanso");
    }
    
    public void descansar(Monitor mon){
        paso.mirar();
        monZonaComun.add(mon);
        escritor.addMsg(mon.getMiId() + " inicia su descanso");
        paso.mirar();
        try {
            sleep((int) (1000 + 1000*Math.random()));
        } catch (InterruptedException ex) {
            Logger.getLogger(ZonaComun.class.getName()).log(Level.SEVERE, null, ex);
        }
        paso.mirar();
        monZonaComun.remove(mon);
        escritor.addMsg(mon.getMiId() + " finaliza su descanso");
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
