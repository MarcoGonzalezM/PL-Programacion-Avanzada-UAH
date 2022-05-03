package Modelo;

import Interfaz.Escritor;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
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
    private ArrayList<Monitor> monSoga = new ArrayList<>();
    private Lock lockSoga = new ReentrantLock();
    private Condition hayMonitor = lockSoga.newCondition();
    private Condition ninnosSuficientes = lockSoga.newCondition();
    private CyclicBarrier barreraSoga;
    
    public Soga(int p_tamEquipo){
        tamEquipo = p_tamEquipo;
        colaSoga = new ArrayBlockingQueue(tamEquipo*2);
        colaSogaEquipoA = new ArrayBlockingQueue(tamEquipo);
        colaSogaEquipoB = new ArrayBlockingQueue(tamEquipo);
        barreraSoga = new CyclicBarrier(1+tamEquipo*2);
    }
    
    public void soga(Ninno ninno){
        Paso.mirar();
        if (colaSoga.size()+colaSogaEquipoA.size()+colaSogaEquipoB.size()<tamEquipo*2){
            try {
                colaSoga.put(ninno);
            } catch (InterruptedException ex) {
                Logger.getLogger(Soga.class.getName()).log(Level.SEVERE, null, ex);
            }
            Escritor.addMsg(ninno.getMiId() + " se pone a la cola de SOGA");
            Paso.mirar();
            lockSoga.lock();
            try{
                ninnosSuficientes.signal();
                while (monSoga.size()==0){    
                    hayMonitor.await();
                }
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
    
    public void soga(Monitor mon) {
        Paso.mirar();
        monSoga.add(mon);
        Escritor.addMsg(mon.getMiId() + " llega a la SOGA");
        Paso.mirar();
        while (mon.getContadorActividades() > 0) {
            lockSoga.lock();
            try {
                hayMonitor.signalAll();
                while (colaSogaEquipoA.size() + colaSogaEquipoB.size() == tamEquipo * 2) {
                    ninnosSuficientes.await();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Soga.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                lockSoga.unlock();
            }
            for (int i = 0; i < tamEquipo * 2; i++) {
                Paso.mirar();
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
            Escritor.addMsg(mon.getMiId() + " inicia el enfrentamiento en SOGA");
            Paso.mirar();
            try {
                sleep(7000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Soga.class.getName()).log(Level.SEVERE, null, ex);
            }
            Paso.mirar();
            if (Math.random() < 0.5) {
                Escritor.addMsg(mon.getMiId() + " termina el enfrentamiento en SOGA a favor del EQUIPO A");
                for (Ninno ninno : colaSogaEquipoA) {
                    ninno.substractActividad(2);
                    colaSogaEquipoA.remove(ninno);
                    Escritor.addMsg(ninno.getMiId() + " gana en SOGA");
                }
                for (Ninno ninno : colaSogaEquipoB) {
                    ninno.substractActividad(1);
                    colaSogaEquipoB.remove(ninno);
                    Escritor.addMsg(ninno.getMiId() + " pierde en SOGA");
                }
            } else {
                Escritor.addMsg(mon.getMiId() + " termina el enfrentamiento en SOGA a favor del EQUIPO B");
                for (Ninno ninno : colaSogaEquipoA) {
                    ninno.substractActividad(1);
                    colaSogaEquipoA.remove(ninno);
                    Escritor.addMsg(ninno.getMiId() + " pierde en SOGA");
                }
                for (Ninno ninno : colaSogaEquipoB) {
                    ninno.substractActividad(2);
                    colaSogaEquipoB.remove(ninno);
                    Escritor.addMsg(ninno.getMiId() + " gana en SOGA");
                }
            }
            Paso.mirar();
            try {
                barreraSoga.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Soga.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BrokenBarrierException ex) {
                Logger.getLogger(Soga.class.getName()).log(Level.SEVERE, null, ex);
            }
            mon.substractActividad();
        }
        monSoga.remove(mon);
        Paso.mirar();
    }
    
    public String getCola(){
        String msg = "";
        for (Ninno ninno:colaSoga){
            msg += ninno.getMiId() + " ";
        }
        return msg;    
    }
    public int cuantosNinnosCola(){
        return colaSoga.size();
    }
    
    public String getColaEquipoA(){
        String msg = "";
        for (Ninno ninno:colaSogaEquipoA){
            msg += ninno.getMiId() + " ";
        }
        return msg;    
    }
    
    public String getColaEquipoB(){
        String msg = "";
        for (Ninno ninno:colaSogaEquipoB){
            msg += ninno.getMiId() + " ";
        }
        return msg;    
    }

    public String getMon(){
        String msg = "";
        for (Monitor mon:monSoga){
            msg += mon.getMiId() + " ";
        }
        return msg;    
    }
}