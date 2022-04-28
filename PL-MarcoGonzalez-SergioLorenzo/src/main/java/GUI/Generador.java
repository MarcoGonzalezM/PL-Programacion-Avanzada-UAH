package GUI;

import Modelo.Campamento;
import Modelo.Monitor;
import Modelo.Ninno;

/**
 *
 * @author sergi
 */
public class Generador extends Thread{
    private final int AFORO = 50;
    private final int N_MONITORES = 4;
    private final int N_NINNOS = 20000;
    Campamento campamento;
    Monitor m;
    Ninno n;
    public Generador(){
        campamento = new Campamento(AFORO);
    }
    @Override
    public void run(){
        for (int i=0;i<N_MONITORES;i++){
            m = new Monitor(i,campamento,N_MONITORES);
            m.start();
        }
        for (int i=0;i<N_NINNOS;i++){
            n = new Ninno(i,campamento);
            n.start();
        }
    }
}
