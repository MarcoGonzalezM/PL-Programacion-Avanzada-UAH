package Modelo;

import Interfaz.Escritor;
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
    private int vecesUsado = 0;
    private ArrayList<Ninno> ninnoTirolina = new ArrayList<>();
    private ArrayList<Monitor> monTirolina = new ArrayList<>();
    private Queue<Ninno> colaTirolina = new ConcurrentLinkedQueue();
    private Lock lockTirolina = new ReentrantLock();
    private Condition monitorTirolina = lockTirolina.newCondition();
    private Condition actividadesMonitor = lockTirolina.newCondition();
    
    public void tirolina(Ninno ninno){
        Paso.mirar();
        //Instruccion bloqueante A
        colaTirolina.offer(ninno);
        Escritor.addMsg(ninno.getMiId() + " se pone a la cola de la TIROLINA");
        Paso.mirar();
        lockTirolina.lock(); //Instruccion bloqueante B
        //Instruccion desbloqueante A
        try {
            while (monTirolina.size()==0){
                monitorTirolina.await();
            }
            colaTirolina.poll();
            ninnoTirolina.add(ninno);
            Paso.mirar();
            estadoTirolina++;
            Escritor.addMsg(ninno.getMiId() + " se empieza a preparar en la TIROLINA");
            sleep(1000);
            estadoTirolina++;
            Paso.mirar();
            sleep(3000);
            Escritor.addMsg(ninno.getMiId() + " se monta en la TIROLINA");
            Paso.mirar();
            estadoTirolina++;
            Escritor.addMsg(ninno.getMiId() + " llega al fin de la TIROLINA");
            sleep(500);
            Paso.mirar();
            estadoTirolina=0;
            ninnoTirolina.remove(ninno);
            vecesUsado++;
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
        Paso.mirar();
        lockTirolina.lock();
        try {
            monTirolina.add(mon);
            Paso.mirar();
            monitorTirolina.signal();
            Escritor.addMsg(mon.getMiId() + " llega a la TIROLINA");
            while (mon.getContadorActividades()>0){
                actividadesMonitor.await();
            }
            Paso.mirar();
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
    public int cuantosNinnosCola(){
        return colaTirolina.size();
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

    public int getVecesUsado() {
        return vecesUsado;
    }
    
    
    
}
