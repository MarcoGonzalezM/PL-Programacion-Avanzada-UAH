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
    private int estadoTirolina = 0;
    ArrayList<Ninno> colaEntrada1 = new ArrayList<>();
    ArrayList<Ninno> colaEntrada2 = new ArrayList<>();
    ArrayList<Ninno> ninnoTirolina = new ArrayList<>();
    ArrayList<Ninno> monTirolina = new ArrayList<>();
    Queue<Ninno> colaTirolina = new ConcurrentLinkedQueue();
    Queue<Ninno> colaSoga = new ConcurrentLinkedQueue();
    Queue<Ninno> colaMerendero = new ConcurrentLinkedQueue();
    CountDownLatch cdl1 = new CountDownLatch(1);
    CountDownLatch cdl2 = new CountDownLatch(1);
    Lock lockEntrada = new ReentrantLock();
    Lock lockTirolina = new ReentrantLock();
    Condition monitorTirolina = lockTirolina.newCondition();
    Condition puerta1 = lockEntrada.newCondition();
    Condition puerta2 = lockEntrada.newCondition();
    
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
    
    public int tirolina(Ninno ninno){
        colaTirolina.offer(ninno);
        Escritor.addMsg(ninno.getMiId() + " se pone a la cola de la TIROLINA");
        lockTirolina.lock();
        try {
            while (monTirolina.size()==0){
                monitorTirolina.await();
            }
            colaTirolina.poll();
            ninnoTirolina.add(ninno);
            estadoTirolina++;
            sleep(1000);
            estadoTirolina++;
            sleep(3000);
            estadoTirolina++;
            sleep(500);
            estadoTirolina=0;
        } catch (InterruptedException ex) {
            Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        ninnoTirolina.remove(ninno);
        lockTirolina.unlock();
        Escritor.addMsg(ninno.getMiId() + " abandona la TIROLINA");
        return 1;
    }
    
    public void tirolina(Monitor mon){
        monitorTirolina.signal();
        Escritor.addMsg(mon.getMiId() + " llega a la TIROLINA");
    }
    
    public void reservaActividad(int actividad){
        switch (actividad){
            // case 0 -> contActividades-=campamento.soga(this);
            // case 1 -> contActividades-=campamento.soga(this);
            // case 2 -> contActividades-=campamento.merienda(this);
        }
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
        String msg = "";
        for (Ninno ninno:colaTirolina){
            msg += ninno.getMiId();
        }
        return msg;
    }
    
    

}
