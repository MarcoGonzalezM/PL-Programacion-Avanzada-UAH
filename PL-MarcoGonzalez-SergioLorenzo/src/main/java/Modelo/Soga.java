package Modelo;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Soga {
    private int tamEquipo;
    private ArrayBlockingQueue<Ninno> colaSoga;
    private ArrayBlockingQueue<Ninno> colaSogaEquipoA;
    private ArrayBlockingQueue<Ninno> colaSogaEquipoB;
    private Lock lockSoga = new ReentrantLock();
    private Condition hayMonitor = lockSoga.newCondition();
    private CyclicBarrier barreraSoga;
    
    public Soga(int p_tamEquipo){
        tamEquipo = p_tamEquipo;
        colaSoga = new ArrayBlockingQueue(tamEquipo*2);
        colaSogaEquipoA = new ArrayBlockingQueue(tamEquipo);
        colaSogaEquipoB = new ArrayBlockingQueue(tamEquipo);
        barreraSoga = new CyclicBarrier(1+tamEquipo*2);
    }
    
    public void usarSoga(Ninno ninno){
        if (colaSoga.offer(ninno)){
            lockSoga.lock();
            try{
                hayMonitor.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Soga.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                lockSoga.unlock();
            }
            try {
                barreraSoga.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Soga.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BrokenBarrierException ex) {
                Logger.getLogger(Soga.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void accederSoga(Monitor mon) {
        //Hacer si hay 10 niños
        hayMonitor.signalAll();
        for (int i = 0; i < tamEquipo * 2; i++) {
            Ninno n;
            try {
                n = colaSoga.take();
                if (Math.random() < 0.5) {
                    if (!colaSogaEquipoA.offer(n)) {
                        colaSogaEquipoB.put(n);

                    }
                } else {
                    if (!colaSogaEquipoB.offer(n)) {
                        colaSogaEquipoA.put(n);
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Soga.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            barreraSoga.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Soga.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
            Logger.getLogger(Soga.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
