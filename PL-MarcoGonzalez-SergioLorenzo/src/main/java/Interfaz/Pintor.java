/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Modelo.Campamento;
import java.util.HashSet;
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

    public Pintor(Campamento campamento,JTextField colaEntrada1, JTextField colaEntrada2, JTextField colaTirolina, 
            JTextField monitorTirolina, JTextField inicioTirolina, JTextField dentroTirolina, JTextField finTirolina, 
            JTextField colaSoga, JTextField monitorSoga, JTextField equipoASoga, JTextField equipoBSoga, 
            JTextField colaMerendero, JTextField monitorMerendero, JTextField bandejasSucias, 
            JTextField bandejasLimpias, JTextField dentroMerendero, JTextField monitoresZC, JTextField ninnosZC) {
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
        monitorTirolina.setText(campamento.getMonTirolina());
        int estadoTirolina = campamento.getEstadoTirolina();
        switch(estadoTirolina){
            case 0-> {
                inicioTirolina.setText("");
                dentroTirolina.setText("");
                finTirolina.setText("");
            }
                case 1->{
                inicioTirolina.setText(campamento.getNinnoTirolina());
                dentroTirolina.setText("");
                finTirolina.setText("");
            }
                case 2->{
                inicioTirolina.setText("");
                dentroTirolina.setText(campamento.getNinnoTirolina());
                finTirolina.setText("");
            }
                case 3->{
                inicioTirolina.setText("");
                dentroTirolina.setText("");
                finTirolina.setText(campamento.getNinnoTirolina());
            }
        }
        colaSoga.setText(campamento.getColaS());
        monitorSoga.setText(campamento.getMonSoga());
        equipoASoga.setText(campamento.getEquipoA());
        equipoBSoga.setText(campamento.getEquipoB());
        colaMerendero.setText(campamento.getColaMerendero());
        monitorMerendero.setText(campamento.getMonMerendero());
        bandejasSucias.setText(""+campamento.getBandSucias());
        bandejasLimpias.setText(""+campamento.getBandLimpias());
        dentroMerendero.setText(campamento.getNinnoMerendero());
        monitoresZC.setText(campamento.getMonZC());
        ninnosZC.setText(campamento.getNinnoZC());
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
