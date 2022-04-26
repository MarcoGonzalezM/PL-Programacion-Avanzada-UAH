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

    public Monitor(int p_id, Campamento p_campamento) {
        this.id = "" + p_id;
        this.campamento = p_campamento;
    }
    
    public void abrirEntrada(){
    
    }


}
