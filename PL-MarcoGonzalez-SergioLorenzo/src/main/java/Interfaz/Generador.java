package Interfaz;

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
    private int n_monitores, n_ninnos, n_actividades_ninnos, n_actividades_monitores;
    private Campamento campamento;
    private Monitor m;
    private Ninno n;
    public Generador(Campamento p_campanento, int  p_n_monitores, int p_n_ninnos, int p_n_actividades_ninnos, int p_n_actividades_monitores){
        campamento = p_campanento;
        n_monitores = p_n_monitores;
        n_ninnos = p_n_ninnos;
        n_actividades_monitores = p_n_actividades_monitores;
        n_actividades_ninnos = p_n_actividades_ninnos;
    }
    @Override
    public void run(){
        for (int i=1;i<=n_monitores;i++){
            m = new Monitor(i,n_monitores, n_actividades_monitores,campamento);
            m.start();
        }
        for (int i=0;i<n_ninnos;i++){
            n = new Ninno(i, n_actividades_ninnos, campamento);
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
