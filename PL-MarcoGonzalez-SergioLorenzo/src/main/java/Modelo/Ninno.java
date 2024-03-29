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
public class Ninno extends Thread implements Comparable<Ninno>{
    //ATRIBUTOS (privados)
    private String id;
    private int contActividades, totalActividades;
    private Campamento campamento;
    
    public Ninno(int p_id, int p_contActividades, Campamento p_campamento){
        id = ""+100000+p_id;
        id = "N"+id.substring(1);
        contActividades = p_contActividades;
        totalActividades = p_contActividades;
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
    
    public void seleccionarActividad(){
        boolean meriendaDisp = contActividades<=12;
        int k=0;
        if (meriendaDisp) k=1;
        int actividad = (int) ((2 + k) * Math.random());
        switch (actividad){
            case 0 -> campamento.usarTirolina(this);
            case 1 -> campamento.usarSoga(this);
            case 2 -> campamento.usarMerendero(this);
        }
        campamento.usarZonaComun(this);
    }
    
    public void salirCamp(){
        campamento.salirCampamento(this);
        campamento.calificar(this);
    }
    
    public void substractActividad(int num){
        contActividades-=num;
    }
    
    public int actividadesRealizadas(){
        return totalActividades - contActividades;
    }
    
    public boolean equals(Ninno ninno) {
        return Integer.valueOf(id.substring(1)).equals(Integer.valueOf(ninno.getMiId().substring(1)));
    }
    public boolean equals(String idNinno) {
        return Integer.valueOf(id.substring(1)).equals(Integer.valueOf(idNinno.substring(1)));
    }
    
    @Override
    public int compareTo(Ninno ninno) {
        //a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
        return Integer.valueOf(id.substring(1)).compareTo(Integer.valueOf(ninno.getMiId().substring(1)));
    }
    public int compareTo(String idNinno) {
        //a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
        return Integer.valueOf(id.substring(1)).compareTo(Integer.valueOf(idNinno.substring(1)));
    }
    
    public void run(){
    entrarCamp();
    while (contActividades>0){
        seleccionarActividad();
    }
    salirCamp();
    } 
}
