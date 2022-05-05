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
    private int nMonitores, contadorActividades, actividadesHastaDescanso;

    public Monitor(int p_id, int p_nMonitores, int p_contadorActividades, Campamento p_campamento) {
        id = "M" + p_id;
        campamento = p_campamento;
        nMonitores = p_nMonitores;
        contadorActividades = p_contadorActividades;
        actividadesHastaDescanso = p_contadorActividades;
    }
    
    public void abrirEntrada(){
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
            }        
        }
    }

    public String getMiId() {
        return id;
    }

    public int getContadorActividades() {
        return contadorActividades;
    }
    
    public void substractActividad(){
        contadorActividades--;
    }

    public void run() {
        abrirEntrada();
        int actividad = campamento.reservaActividad();
        while (true) {
                switch (actividad) {
                    case 0 ->
                        campamento.accederMerendero(this);
                    case 1 ->
                        campamento.accederTirolina(this);
                    case 2 ->
                        campamento.accederSoga(this);
                }
            campamento.accederZonaComun(this);
            contadorActividades = actividadesHastaDescanso;
        }
    }
}
