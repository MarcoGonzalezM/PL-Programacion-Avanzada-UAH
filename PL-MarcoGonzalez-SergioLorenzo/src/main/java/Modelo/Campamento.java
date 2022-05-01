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
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Campamento {
    //ATRIBUTOS (privados)
    private int huecosDisponibles;
    private boolean alternancia = false;
    private int nMonitoresP1 = 0, nMonitoresP2 = 0, nMonitoresMerienda = 0, nMonitoresTirolina = 0, nMonitoresSoga = 0;
    private int nMonitoresDesMer, nMonitoresDesTir, nMonitoresDesSoga;
    private int[] actividades = {0,1,2};
    private Tirolina tirolina = new Tirolina();
    private Entrada entrada = new Entrada();
    private ZonaComun zonaComun = new ZonaComun();
    private Merendero merendero = new Merendero();
    private Soga soga = new Soga();
    private ArrayList<Ninno> colaEntrada1 = new ArrayList<>();
    private ArrayList<Ninno> colaEntrada2 = new ArrayList<>();
    private Queue<Ninno> colaMerendero = new ConcurrentLinkedQueue();
    private CountDownLatch cdl1 = new CountDownLatch(1);
    private CountDownLatch cdl2 = new CountDownLatch(1);
    private Lock lockEntrada = new ReentrantLock();
    private Condition puerta1 = lockEntrada.newCondition();
    private Condition puerta2 = lockEntrada.newCondition();
    
    //CONSTRUCTOR
    public Campamento(int p_huecosDisponibles, int p_nMonitoresDesMer, int p_nMonitoresDesTir, int p_nMonitoresDesSoga){
        huecosDisponibles = p_huecosDisponibles;
        nMonitoresDesMer = p_nMonitoresDesMer;
        nMonitoresDesTir = p_nMonitoresDesTir;
        nMonitoresDesSoga = p_nMonitoresDesSoga;
    }
    public void entrarPuerta1(Ninno ninno){
        try {
            colaEntrada1.add(ninno);
            Escritor.addMsg(ninno.getMiId() + " entra a la cola de entrada 1");
            cdl1.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        lockEntrada.lock();
        try{
            while(huecosDisponibles == 0)
            {
                puerta1.await();
            }
            colaEntrada1.remove(ninno);
            huecosDisponibles--;
        } catch(InterruptedException ex){
            Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);        
        }
        finally{
            lockEntrada.unlock();
            Escritor.addMsg(ninno.getMiId() + " entra al campamento");
        }
    }
    public void entrarPuerta2(Ninno ninno){
        try {
            colaEntrada2.add(ninno);
            Escritor.addMsg(ninno.getMiId() + " entra a la cola de entrada 2");
            cdl2.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        lockEntrada.lock();
        try{
            while(huecosDisponibles == 0)
            {
                puerta2.await();
            }
            colaEntrada2.remove(ninno);
            huecosDisponibles--;
            
        } catch(InterruptedException ex){
            Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);        
        } finally{
            lockEntrada.unlock();
            Escritor.addMsg(ninno.getMiId() + " entra al campamento");
        }
    }
    public void salirCampamento(Ninno ninno){
        lockEntrada.lock();
        try {
            huecosDisponibles++;
            if(colaEntrada1.size() > 0 && colaEntrada2.size() > 0){
                // Si hay niños esperando en las dos entradas (alternancia)
                if(!alternancia)
                {
                    puerta2.signal();
                    alternancia = true;
                }
                else{
                    puerta1.signal();
                    alternancia = false;
                }
            }
            else{
                if(colaEntrada1.size() > 0)
                    puerta1.signal();
                else if (colaEntrada2.size() > 0)
                    puerta2.signal();
            }
        } finally {
            lockEntrada.unlock();
            Escritor.addMsg(ninno.getMiId() + " sale del campamento");
        }
    }
    
    public synchronized void abrirCamp1(Monitor mon){
        if (cdl1.getCount()>0){
            long time = (long) (500 + 500 * Math.random());
            try {
                sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);
            }
            cdl1.countDown();
            Escritor.addMsg(mon.getMiId() + " abre la puerta 1");
        }
        nMonitoresP1++;
        Escritor.addMsg(mon.getMiId() + " entra al campamento por la puerta 1");
    }
    public synchronized void abrirCamp2(Monitor mon){
        if (cdl2.getCount()>0){
            long time = (long) (500 + 500 * Math.random());
            try {
                sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);
            }
            cdl2.countDown();
            Escritor.addMsg(mon.getMiId() + " abre la puerta 2");
        }
        nMonitoresP2++;
        Escritor.addMsg(mon.getMiId() + " entra al campamento por la puerta 2");
    }
    
    public void usarTirolina(Ninno ninno){
        tirolina.tirolina(ninno);
    }
    
    public void accederTirolina(Monitor mon){
        tirolina.tirolina(mon);
    }
    
    public void usarSoga(Ninno ninno){
        soga.soga(ninno);
    }
    
    public void accederSoga(Monitor mon){
        soga.soga(mon);
    }
    
    public void usarMerendero(Ninno ninno){
        merendero.merendar(ninno);
    }
    
    public void accederMerendero(Monitor mon){
        merendero.merendero(mon);
    }

    public void usarZonaComun(Ninno ninno){
        zonaComun.descansar(ninno);
    }
    
    public void accederZonaComun(Monitor mon){
        zonaComun.descansar(mon);
    }
    
    public synchronized int reservaActividad(){
        int actividad = actividades[(int) (actividades.length * Math.random())];
        switch (actividad){
            case 0 -> nMonitoresDesMer-=1;
            case 1 -> nMonitoresDesTir-=1;
            case 2 -> nMonitoresDesSoga-=1;
        }
        return actividad;
    }

    public int getnMonP1() {
        return nMonitoresP1;
    }

    public int getnMonP2() {
        return nMonitoresP2;
    }

    public int getnMonitoresMerienda() {
        return nMonitoresMerienda;
    }

    public int getnMonitoresTirolina() {
        return nMonitoresTirolina;
    }

    public int getnMonitoresSoga() {
        return nMonitoresSoga;
    }
    
    public int getnMonitoresDesMer() {
        return nMonitoresDesMer;
    }

    public int getnMonitoresDesTir() {
        return nMonitoresDesTir;
    }

    public int getnMonitoresDesSoga() {
        return nMonitoresDesSoga;
    }
    

    public String getCola1() {
        String msg = "";
        for (Ninno ninno:colaEntrada1){
            msg += ninno.getMiId();
        }
        return msg;
    }

    public String getCola2() {
        String msg = "";
        for (Ninno ninno:colaEntrada2){
            msg += ninno.getMiId();
        }
        return msg;
    }
    
    public String getColaT() {
        return tirolina.getCola();
    }
    
    

}
