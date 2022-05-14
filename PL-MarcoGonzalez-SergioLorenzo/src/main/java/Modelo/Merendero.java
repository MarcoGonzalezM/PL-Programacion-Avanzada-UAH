/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Interfaz.Escritor;
import static java.lang.Thread.sleep;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Merendero {
    private ContadorBandejas bandLimpias, bandSucias;
    private Escritor escritor;
    private Paso paso;
    private Queue<Ninno> colaMerendero = new ConcurrentLinkedQueue();
    private CopyOnWriteArrayList<Ninno> ninnoMerendero = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Monitor> monMerendero = new CopyOnWriteArrayList<>();
    private Semaphore semMerienda;
    
    public Merendero(int p_nBandejas, int p_aforoMer, Escritor p_escritor, Paso p_paso){
        bandLimpias = new ContadorBandejas(0);
        bandSucias = new ContadorBandejas(p_nBandejas);
        semMerienda = new Semaphore(p_aforoMer,true);
        escritor = p_escritor;
        paso = p_paso;
    }
    
    public void merendar (Ninno ninno){
        paso.mirar();
        colaMerendero.offer(ninno);
        escritor.addMsg(ninno.getMiId() + " se pone a la cola de MERENDERO");
        paso.mirar();
        try {
            semMerienda.acquire();
            colaMerendero.remove(ninno);
            ninnoMerendero.add(ninno);
            paso.mirar();
            bandLimpias.extraerBandeja();
            paso.mirar();
            escritor.addMsg(ninno.getMiId() + " se sienta a comer en el MERENDERO");
            sleep(7000);
            paso.mirar();
            bandSucias.annadirBandeja();
            paso.mirar();
            ninnoMerendero.remove(ninno);
            escritor.addMsg(ninno.getMiId() + " abandona el MERENDERO");
        } catch (InterruptedException ex) {
            Logger.getLogger(Merendero.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            semMerienda.release();
        }
    }
    
    public void merendero (Monitor mon){
        paso.mirar();
        monMerendero.add(mon);
        escritor.addMsg(mon.getMiId() + " llega al MERENDERO");
        paso.mirar();
        while(mon.getContadorActividades() > 0){
            bandSucias.extraerBandeja();
            paso.mirar();
            try {
                sleep((int) (3000 + 2000*Math.random()));
            } catch (InterruptedException ex) {
                Logger.getLogger(Merendero.class.getName()).log(Level.SEVERE, null, ex);
            }
            paso.mirar();
            bandLimpias.annadirBandeja();
            mon.substractActividad();
            escritor.addMsg(mon.getMiId() + " limpia una bandeja");
        }
        paso.mirar();
        monMerendero.remove(mon);
        escritor.addMsg(mon.getMiId() + " abandona el MERENDERO");
    }

    public int getBandLimpias() {
        return bandLimpias.get();
    }

    public int getBandSucias() {
        return bandSucias.get();
    }

    public String getCola() {
        String msg = "";
        for (Ninno ninno:colaMerendero){
            msg += ninno.getMiId() + " ";
        }
        return msg;
    }

    public String getNinno() {
        String msg = "";
        for (Ninno ninno:ninnoMerendero){
            msg += ninno.getMiId() + " ";
        }
        return msg;    
    }
    public int cuantosNinnosMerienda(){
        return ninnoMerendero.size();
    }

    public String getMon() {
        String msg = "";
        for (Monitor mon:monMerendero){
            msg += mon.getMiId() + " ";
        }
        return msg;    
    }
}
