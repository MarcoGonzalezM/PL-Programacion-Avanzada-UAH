/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author marco
 */
public class Campamento {
    //ATRIBUTOS (privados)
    private int huecosDisponibles;
    private boolean alternancia = false;
    private int nMonitoresP1;
    private int nMonitoresP2;
    ArrayList<Ninno> colaEntrada1 = new ArrayList<>();
    ArrayList<Ninno> colaEntrada2 = new ArrayList<>();
    CountDownLatch cdl1 = new CountDownLatch(1);
    CountDownLatch cdl2 = new CountDownLatch(1);
    Lock lockEntrada = new ReentrantLock();
    Condition puerta1 = lockEntrada.newCondition();
    Condition puerta2 = lockEntrada.newCondition();
    
    //CONSTRUCTOR
    public Campamento(int p_huecosDisponibles){
        huecosDisponibles = p_huecosDisponibles;
    }
    public void entrarPuerta1(Ninno ninno){
        lockEntrada.lock();
        try{
            colaEntrada1.add(ninno);
            while(huecosDisponibles == 0)
            {
                puerta1.await();
            }
            colaEntrada1.remove(ninno);
            huecosDisponibles--;
            
        }
        catch(InterruptedException e){}
        finally{
            lockEntrada.unlock();
        }
    }
    public void entrarPuerta2(Ninno ninno){
        lockEntrada.lock();
        try{
            colaEntrada2.add(ninno);
            while(huecosDisponibles == 0)
            {
                puerta2.await();
            }
            colaEntrada2.remove(ninno);
            huecosDisponibles--;
            
        }
        catch(InterruptedException e){}
        finally{
            lockEntrada.unlock();
        }
    }
    public void salir_museo(Ninno ninno){
        lockEntrada.lock();
        try {
            huecosDisponibles++;
            if(colaEntrada1.size() > 0 && colaEntrada2.size() > 0){
                //ninnos esperando en las dos entradas (alternancia)
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
        }
    }
    
    public synchronized void abrirCamp(Monitor mon, boolean puerta){
        //sepparar en abrir camp1 y camp2
    }

    public int getnMonP1() {
        return nMonitoresP1;
    }

    public int getnMonP2() {
        return nMonitoresP2;
    }
    
    

}
