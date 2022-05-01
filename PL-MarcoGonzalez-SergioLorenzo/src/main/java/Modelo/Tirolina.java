package Modelo;

import GUI.Escritor;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Tirolina {
    private int estadoTirolina = 0;
    private ArrayList<Ninno> ninnoTirolina = new ArrayList<>();
    private ArrayList<Ninno> monTirolina = new ArrayList<>();
    private Queue<Ninno> colaTirolina = new ConcurrentLinkedQueue();
    private Lock lockTirolina = new ReentrantLock();
    private Condition monitorTirolina = lockTirolina.newCondition();
    
    public void tirolina(Ninno ninno){
        //Instruccion bloqueante A
        colaTirolina.offer(ninno);
        Escritor.addMsg(ninno.getMiId() + " se pone a la cola de la TIROLINA");
        lockTirolina.lock(); //Instruccion bloqueante B
        //Instruccion desbloqueante A
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
        ninno.substractActividad(estadoTirolina);
    }
    
    public void tirolina(Monitor mon){
        monitorTirolina.signal();
        Escritor.addMsg(mon.getMiId() + " llega a la TIROLINA");
    }
    
    public String getCola(){
        String msg = "";
        for (Ninno ninno:colaTirolina){
            msg += ninno.getMiId();
        }
        return msg;
    }
    
}
