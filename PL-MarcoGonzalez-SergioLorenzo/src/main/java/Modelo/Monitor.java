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

    public Monitor(int p_id, Campamento p_campamento, int p_nMonitores) {
        this.id = "" + p_id;
        this.campamento = p_campamento;
        this.nMonitores = p_nMonitores;
    }
    
    public void abrirEntrada(){
        boolean entrada = Math.random()<0.5;
        if(entrada){
            campamento.abrirCamp(this, entrada!=campamento.getnMonP1()<nMonitores-1);
        }
        else {
            campamento.abrirCamp(this, entrada!=campamento.getnMonP2()<nMonitores-1);
        }
    }


}
