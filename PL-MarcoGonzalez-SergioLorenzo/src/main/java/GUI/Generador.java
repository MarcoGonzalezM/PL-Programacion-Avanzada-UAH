package GUI;

import Modelo.Campamento;
import Modelo.Monitor;
import Modelo.Ninno;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergi
 */
public class Generador extends Thread{
    private final int N_MONITORES = 4;
    private final int N_NINNOS = 20000;
    private final int N_ACTIVIDADES_NINNOS = 15;
    private final int N_ACTIVIDADES_MONITORES = 10;
    private Campamento campamento;
    private Monitor m;
    private Ninno n;
    public Generador(Campamento p_campanento){
        campamento = p_campanento;
    }
    @Override
    public void run(){
        for (int i=1;i<=N_MONITORES;i++){
            m = new Monitor(i,N_MONITORES, N_ACTIVIDADES_MONITORES,campamento);
            m.start();
        }
        for (int i=0;i<N_NINNOS;i++){
            n = new Ninno(i, N_ACTIVIDADES_NINNOS, campamento);
            n.start();
            long time = (long) (1000 + 2000*Math.random());
            try {
                sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(Generador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
