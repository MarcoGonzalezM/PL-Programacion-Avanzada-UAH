/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Interfaz.Escritor;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Entrada {
    private int huecosDisponibles;
    private AtomicInteger nMonitoresP1 = new AtomicInteger(0);
    private AtomicInteger nMonitoresP2 = new AtomicInteger(0);
    private boolean alternancia = false;
    private ArrayList<Ninno> colaEntrada1 = new ArrayList<>();
    private ArrayList<Ninno> colaEntrada2 = new ArrayList<>();
    private CountDownLatch cdl1 = new CountDownLatch(1);
    private CountDownLatch cdl2 = new CountDownLatch(1);
    private Lock lockEntrada = new ReentrantLock();
    private Condition puerta1 = lockEntrada.newCondition();
    private Condition puerta2 = lockEntrada.newCondition();
    
    public Entrada(int p_huecosDisponibles){
        huecosDisponibles = p_huecosDisponibles;
    }
    
    public void entrarPuerta1(Ninno ninno){
        try {
            Paso.mirar();
            colaEntrada1.add(ninno);
            Escritor.addMsg(ninno.getMiId() + " entra a la cola de entrada 1");
            Paso.mirar();
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
            Paso.mirar();
            colaEntrada1.remove(ninno);
            huecosDisponibles--;
        } catch(InterruptedException ex){
            Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);        
        }
        finally{
            lockEntrada.unlock();
            Escritor.addMsg(ninno.getMiId() + " entra al campamento por la puerta 1");
        }
    }
    public void entrarPuerta2(Ninno ninno){
        try {
            Paso.mirar();
            colaEntrada2.add(ninno);
            Escritor.addMsg(ninno.getMiId() + " entra a la cola de entrada 2");
            Paso.mirar();
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
            Paso.mirar();
            colaEntrada2.remove(ninno);
            huecosDisponibles--;
            
        } catch(InterruptedException ex){
            Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);        
        } finally{
            lockEntrada.unlock();
            Escritor.addMsg(ninno.getMiId() + " entra al campamento por la puerta 2");
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
        Paso.mirar();
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
        nMonitoresP1.getAndIncrement();
        Escritor.addMsg(mon.getMiId() + " entra al campamento por la puerta 1");
    }
    public synchronized void abrirCamp2(Monitor mon){
        Paso.mirar();
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
        nMonitoresP2.getAndIncrement();
        Escritor.addMsg(mon.getMiId() + " entra al campamento por la puerta 2");
    }

    public int getNMonitoresP1() {
        return nMonitoresP1.get();
    }

    public int getNMonitoresP2() {
        return nMonitoresP2.get();
    }
    
    public String getCola1() {
        String msg = "";
        for (Ninno ninno:colaEntrada1){
            msg += ninno.getMiId() + " ";
        }
        return msg;
    }

    public String getCola2() {
        String msg = "";
        for (Ninno ninno:colaEntrada2){
            msg += ninno.getMiId() + " ";
        }
        return msg;
    }   
}