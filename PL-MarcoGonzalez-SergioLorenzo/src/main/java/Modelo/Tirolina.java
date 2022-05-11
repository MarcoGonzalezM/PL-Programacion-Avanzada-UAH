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
    private Escritor escritor;
    private Paso paso;
    private Lock lockTirolina = new ReentrantLock();
    private Condition monitorTirolina = lockTirolina.newCondition();
    private Condition primeroCola = lockTirolina.newCondition();
    private Condition actividadesMonitor = lockTirolina.newCondition();
    
    public Tirolina(Escritor p_escritor, Paso p_paso){
        escritor = p_escritor;
        paso = p_paso;
    }
    
    public void tirolina(Ninno ninno){
        paso.mirar();
        colaTirolina.offer(ninno);
        escritor.addMsg(ninno.getMiId() + " se pone a la cola de la TIROLINA");
        paso.mirar();
        lockTirolina.lock();
        try {
            while (monTirolina.size()==0 || !ninno.equals(colaTirolina.peek())){
                if (monTirolina.size()==0) monitorTirolina.await();
                if (!colaTirolina.peek().equals(ninno))primeroCola.await();
            }
            colaTirolina.poll();
            ninnoTirolina.add(ninno);
            paso.mirar();
            estadoTirolina++;
            escritor.addMsg(ninno.getMiId() + " se empieza a preparar en la TIROLINA");
            sleep(1000);
            estadoTirolina++;
            paso.mirar();
            sleep(3000);
            escritor.addMsg(ninno.getMiId() + " se monta en la TIROLINA");
            paso.mirar();
            estadoTirolina++;
            escritor.addMsg(ninno.getMiId() + " llega al fin de la TIROLINA");
            sleep(500);
            paso.mirar();
            estadoTirolina=0;
            ninnoTirolina.remove(ninno);
            vecesUsado++;
            ninno.substractActividad(1);
            monTirolina.get(0).substractActividad();
            actividadesMonitor.signal();
            primeroCola.signalAll();
        } catch (InterruptedException ex) {
            Logger.getLogger(Campamento.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lockTirolina.unlock();
        }
        escritor.addMsg(ninno.getMiId() + " abandona la TIROLINA");
    }
    
    public void tirolina(Monitor mon) {
        paso.mirar();
        lockTirolina.lock();
        try {
            monTirolina.add(mon);
            paso.mirar();
            monitorTirolina.signalAll();
            escritor.addMsg(mon.getMiId() + " llega a la TIROLINA");
            while (mon.getContadorActividades()>0){
                actividadesMonitor.await();
            }
            paso.mirar();
            monTirolina.remove(mon);
            escritor.addMsg(mon.getMiId() + " abandona la TIROLINA");
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
    public int getTamCola(){
        return colaTirolina.size();
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
