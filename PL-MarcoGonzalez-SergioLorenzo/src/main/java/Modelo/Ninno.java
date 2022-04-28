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
public class Ninno extends Thread{
    //ATRIBUTOS (privados)
    private String id;
    private int cont_actividades;
    private Campamento campamento;
    
    public Ninno(int p_id, Campamento p_campamento){
        id = ""+100000+p_id;
        id = "N"+id.substring(1);
        cont_actividades = 0;
        campamento = p_campamento;
    }

    public String getMiId() {
        return id;
    }
    
    
    
    public void entrarCamp(){
        boolean entrada = Math.random()<0.5;
        if (entrada){
            campamento.entrarPuerta1(this);
        }
        else{
            campamento.entrarPuerta2(this);
        }
    }
}
