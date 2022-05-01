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
    private ArrayList<Monitor> monTirolina = new ArrayList<>();
    private Queue<Ninno> colaTirolina = new ConcurrentLinkedQueue();
    private Lock lockTirolina = new ReentrantLock();
    private Condition monitorTirolina = lockTirolina.newCondition();
    private Condition actividadesMonitor = lockTirolina.newCondition();
    
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
            Escritor.addMsg(ninno.getMiId() + " se empieza a preparar en la TIROLINA");
            sleep(1000);
            estadoTirolina++;
            Escritor.addMsg(ninno.getMiId() + " se monta en la TIROLINA");
            sleep(3000);
            estadoTirolina++;
            Escritor.addMsg(ninno.getMiId() + " llega al fin de la TIROLINA");
            sleep(500);
            estadoTirolina=0;
            ninnoTirolina.remove(ninno);
            ninno.substractActividad(estadoTirolina);
            monTirolina.get(0).substractActividad();
            actividadesMonitor.signal();
        } catch (InterruptedException ex) {
            Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lockTirolina.unlock();
        }
        Escritor.addMsg(ninno.getMiId() + " abandona la TIROLINA");
    }
    
    public void tirolina(Monitor mon) {
        lockTirolina.lock();
        try {
            monTirolina.add(mon);
            monitorTirolina.signal();
            Escritor.addMsg(mon.getMiId() + " llega a la TIROLINA");
            while (mon.getContadorActividades()>0){
                actividadesMonitor.await();
            }
            monTirolina.remove(mon);
            Escritor.addMsg(mon.getMiId() + " abandona la TIROLINA");
        } catch (InterruptedException ex) {
            Logger.getLogger(Tirolina.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lockTirolina.unlock();
        }
    }
    
    public String getCola(){
        String msg = "";
        for (Ninno ninno:colaTirolina){
            msg += ninno.getMiId() + " ";
        }
        return msg;
    }

    public String getNinno(){
        String msg = "";
        for (Ninno ninno:ninnoTirolina){
            msg += ninno.getMiId() + " ";
        }
        return msg;
    }

    public String getMon(){
        String msg = "";
        for (Monitor mon:monTirolina){
            msg += mon.getMiId() + " ";
        }
        return msg;
    }
    
    public int getEstadoTirolina(){
        return estadoTirolina;
    }
    
}
