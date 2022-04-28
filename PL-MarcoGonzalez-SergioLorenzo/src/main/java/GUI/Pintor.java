/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Modelo.Campamento;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

/**
 *
 * @author marco
 */
public class Pintor extends Thread{
    // Esta clase se encargará de actualizar la interfaz con la información en vivo del campamento
    private Campamento campamento;
    private JTextField colaEntrada1;
    private JTextField colaEntrada2;
    private JTextField colaTirolina;
    private JTextField monitorTirolina;
    private JTextField inicioTirolina;
    private JTextField dentroTirolina;
    private JTextField finTirolina;
    private JTextField colaSoga;
    private JTextField monitorSoga;
    private JTextField equipoASoga;
    private JTextField equipoBSoga;
    private JTextField colaMerendero;
    private JTextField monitorMerendero;
    private JTextField bandejasSucias;
    private JTextField bandejasLimpias;
    private JTextField dentroMerendero;
    private JTextField monitoresZC;
    private JTextField ninnosZC;

    public Pintor(Campamento campamento,JTextField colaEntrada1, JTextField colaEntrada2, JTextField colaTirolina, JTextField monitorTirolina, JTextField inicioTirolina, JTextField dentroTirolina, JTextField finTirolina, JTextField colaSoga, JTextField monitorSoga, JTextField equipoASoga, JTextField equipoBSoga, JTextField colaMerendero, JTextField monitorMerendero, JTextField bandejasSucias, JTextField bandejasLimpias, JTextField dentroMerendero, JTextField monitoresZC, JTextField ninnosZC) {
        this.campamento = campamento;
        this.colaEntrada1 = colaEntrada1;
        this.colaEntrada2 = colaEntrada2;
        this.colaTirolina = colaTirolina;
        this.monitorTirolina = monitorTirolina;
        this.inicioTirolina = inicioTirolina;
        this.dentroTirolina = dentroTirolina;
        this.finTirolina = finTirolina;
        this.colaSoga = colaSoga;
        this.monitorSoga = monitorSoga;
        this.equipoASoga = equipoASoga;
        this.equipoBSoga = equipoBSoga;
        this.colaMerendero = colaMerendero;
        this.monitorMerendero = monitorMerendero;
        this.bandejasSucias = bandejasSucias;
        this.bandejasLimpias = bandejasLimpias;
        this.dentroMerendero = dentroMerendero;
        this.monitoresZC = monitoresZC;
        this.ninnosZC = ninnosZC;
    }
    
    public void pintar(){
        colaEntrada1.setText(campamento.getCola1());
        colaEntrada2.setText(campamento.getCola2());
        colaTirolina.setText(campamento.getColaT());
        monitorTirolina = monitorTirolina;
        inicioTirolina = inicioTirolina;
        dentroTirolina = dentroTirolina;
        finTirolina = finTirolina;
        colaSoga = colaSoga;
        monitorSoga = monitorSoga;
        equipoASoga = equipoASoga;
        equipoBSoga = equipoBSoga;
        colaMerendero = colaMerendero;
        monitorMerendero = monitorMerendero;
        bandejasSucias = bandejasSucias;
        bandejasLimpias = bandejasLimpias;
        dentroMerendero = dentroMerendero;
        monitoresZC = monitoresZC;
        ninnosZC = ninnosZC;
    }
    
    public void run(){
        while(true){
            pintar();
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Pintor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
