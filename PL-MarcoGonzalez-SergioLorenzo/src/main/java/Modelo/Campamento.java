/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.Queue;
import java.util.concurrent.*;
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
    ListaThreads colaEntrada1 , colaEntrada2;
    CountDownLatch cdl1 = new CountDownLatch(1);
    CountDownLatch cdl2 = new CountDownLatch(1);
    Semaphore semEntrada;
    Lock lockCola1 = new ReentrantLock();
    Lock lockCola2 = new ReentrantLock();
    
    //CONSTRUCTOR
    public Campamento(int p_aforo){
        //colaEntrada1 = new ListaThreads() por terminar
        //colaEntrada2 = new ListaThreads() por terminar
        semEntrada= new Semaphore(p_aforo, true);
    }

    public void llegaNinno(Ninno ninno){
        boolean entrada = Math.random()<0.5; 
        if (entrada){
            colaEntrada1.meter(ninno);
            try {
                cdl1.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!(semEntrada.tryAcquire())&& (colaEntrada1.getLongitud()>0) && (colaEntrada2.getLongitud()>0)) {
                lockCola1.lock();
            }
            try {
                semEntrada.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);
            }
            lockCola2.unlock();
            colaEntrada1.sacar(ninno);
        } else {
            colaEntrada2.meter(ninno);
            try {
                cdl2.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!(semEntrada.tryAcquire())&& (colaEntrada1.getLongitud()>0) && (colaEntrada2.getLongitud()>0)) {
                lockCola2.lock();
            }
            try {
                semEntrada.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);
            }
            lockCola1.unlock();
            colaEntrada2.sacar(ninno);
        }
        
        
    }
    
    public void abrirCamp(Monitor mon){
        
    }

}
