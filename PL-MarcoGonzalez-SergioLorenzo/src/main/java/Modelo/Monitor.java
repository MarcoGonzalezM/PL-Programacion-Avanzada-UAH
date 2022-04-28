/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author marco
 */
public class Monitor extends Thread{
    //ATRIBUTOS (prvivados)
    private String id;
    private Campamento campamento;
    private int nMonitores;

    public Monitor(int p_id, int p_nMonitores, int contadorActividades, Campamento p_campamento) {
        this.id = "" + p_id;
        this.campamento = p_campamento;
        this.nMonitores = p_nMonitores;
    }
    
    public synchronized void abrirEntrada(){
        boolean entrada = Math.random()<0.5;
        if(entrada){
            if (campamento.getnMonP1()<nMonitores-1) {
                campamento.abrirCamp2(this);
            }
            else {
                campamento.abrirCamp1(this);
            }
        }
        else {
            if (campamento.getnMonP2()<nMonitores-1) {
                campamento.abrirCamp1(this);
            }
            else {
                campamento.abrirCamp2(this);
            }        }
    }
    
    public synchronized void seleccionarActividad(){
        int actividades[] = {0,1,2};
        int actividad = actividades[(int) (actividades.length * Math.random())];
        campamento.reservaActividad(actividad);

    }

    public String getMiId() {
        return id;
    }

    public void run(){
        
        abrirEntrada();
    }

}
