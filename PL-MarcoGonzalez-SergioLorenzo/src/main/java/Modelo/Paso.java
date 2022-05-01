/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Paso {
    private static boolean detenido = false;
    private static Lock lock = new ReentrantLock();
    private static Condition detener = lock.newCondition();
    
    public static void mirar(){
        try{
            lock.lock();
            while(detenido){
                detener.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Paso.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.unlock();
        }
    }
    
    public static void reanudar(){
        try{
            lock.lock();
            detenido = false;
            detener.signalAll();
        } finally {
            lock.unlock();
        }
    }
   
    public static void detener(){
        try{
            lock.lock();
            detenido = true;
        } finally {
            lock.unlock();
        }
    }
}
