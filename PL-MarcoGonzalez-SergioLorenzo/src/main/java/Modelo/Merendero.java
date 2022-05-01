/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import GUI.Escritor;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Merendero {
    private ContadorBandejas bandLimpias, bandSucias;
    private Queue<Ninno> colaMerendero = new ConcurrentLinkedQueue();
    private CopyOnWriteArrayList<Ninno> ninnoMerendero = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Monitor> monMerendero = new CopyOnWriteArrayList<>();
    private Semaphore semMerienda;
    
    public Merendero(int p_nBandejas, int p_aforoMer){
        bandLimpias = new ContadorBandejas(0);
        bandSucias = new ContadorBandejas(p_nBandejas);
        semMerienda = new Semaphore(20,true);
    }
    
    public void merendar (Ninno ninno){
        colaMerendero.offer(ninno);
        Escritor.addMsg(ninno.getMiId() + " se pone a la cola de MERENDERO");
        try {
            semMerienda.acquire();
            colaMerendero.remove(ninno);
            ninnoMerendero.add(ninno);
            bandLimpias.extraerBandeja(); 
            Escritor.addMsg(ninno.getMiId() + " se sienta a comer en el MERENDERO");
            sleep(7000);
            bandSucias.annadirBandeja();
            ninnoMerendero.remove(ninno);
            Escritor.addMsg(ninno.getMiId() + " abandona el MERENDERO");
        } catch (InterruptedException ex) {
            Logger.getLogger(Merendero.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            semMerienda.release();
        }
    }
    
    public void merendero (Monitor mon){
        monMerendero.add(mon);
        Escritor.addMsg(mon.getMiId() + " llega al MERENDERO");
        while(mon.getContadorActividades() > 0){
            bandSucias.extraerBandeja();
            try {
                sleep((int) (3000 + 2000*Math.random()));
            } catch (InterruptedException ex) {
                Logger.getLogger(Merendero.class.getName()).log(Level.SEVERE, null, ex);
            }
            bandLimpias.annadirBandeja();
            mon.substractActividad();
        }
        monMerendero.remove(mon);
        Escritor.addMsg(mon.getMiId() + " abandona el MERENDERO");
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

    public String getMon() {
        String msg = "";
        for (Monitor mon:monMerendero){
            msg += mon.getMiId() + " ";
        }
        return msg;    
    }
}
