/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Interfaz.Escritor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author marco
 */
public class Campamento {
    //ATRIBUTOS (privados) 
    private int nMonitoresMerienda = 0, nMonitoresTirolina = 0, nMonitoresSoga = 0;
    private int nMonitoresDesMer, nMonitoresDesTir, nMonitoresDesSoga;
    private ArrayList<Integer> actividades = new ArrayList<>(Arrays.asList(0,1,2));
    private Escritor escritor;
    private Paso paso;
    private Tirolina tirolina;
    private RegistroNinno registroNinno;
    private Entrada entrada;
    private ZonaComun zonaComun;
    private Merendero merendero;
    private Soga soga;
    
    //CONSTRUCTOR
    public Campamento(int p_huecosDisponibles, int p_nMonitoresDesMer, int p_nMonitoresDesTir, int p_nMonitoresDesSoga, int p_tamEquipo, int p_nBandejas, int p_aforoMerendero, Escritor p_escritor, Paso p_paso){
        nMonitoresDesMer = p_nMonitoresDesMer;
        nMonitoresDesTir = p_nMonitoresDesTir;
        nMonitoresDesSoga = p_nMonitoresDesSoga;
        escritor = p_escritor;
        paso = p_paso;
        entrada = new Entrada(p_huecosDisponibles, escritor, paso);
        soga = new Soga(p_tamEquipo, escritor, paso);
        tirolina = new Tirolina(escritor, paso);
        merendero = new Merendero(p_nBandejas,p_aforoMerendero, escritor, paso);
        zonaComun = new ZonaComun(escritor, paso);
        registroNinno = new RegistroNinno();
    }
    
    public void entrarPuerta1(Ninno ninno){
        registroNinno.annadir(ninno);
        entrada.entrarPuerta1(ninno);
    }
    public void entrarPuerta2(Ninno ninno){
        registroNinno.annadir(ninno);
        entrada.entrarPuerta2(ninno);
    }
    public void salirCampamento(Ninno ninno){
        entrada.salirCampamento(ninno);
    }
    public void abrirCamp1(Monitor mon){
        entrada.abrirCamp1(mon);
    }
    public void abrirCamp2(Monitor mon){
        entrada.abrirCamp2(mon);
    }

    public void usarTirolina(Ninno ninno){
        tirolina.tirolina(ninno);
    }
    
    public void accederTirolina(Monitor mon){
        tirolina.tirolina(mon);
    }
    
    public void usarSoga(Ninno ninno){
        soga.soga(ninno);
    }
    
    public void accederSoga(Monitor mon){
        soga.soga(mon);
    }
    
    public void usarMerendero(Ninno ninno){
        merendero.merendar(ninno);
    }
    
    public void accederMerendero(Monitor mon){
        merendero.merendero(mon);
    }

    public void usarZonaComun(Ninno ninno){
        zonaComun.descansar(ninno);
    }
    
    public void accederZonaComun(Monitor mon){
        zonaComun.descansar(mon);
    }
    
    public synchronized boolean elegirEntrada(boolean elegida, int nMonitores){
        if(elegida){
            if (entrada.getNMonitoresP1()<nMonitores-1) {
                entrada.incrementNMonitoresP1();
                return true;
            }
            else {
                entrada.incrementNMonitoresP2();
                return false;
            }
        }
        else {
            if (entrada.getNMonitoresP2()<nMonitores-1) {
                entrada.incrementNMonitoresP2();
                return false;
            }
            else {
                entrada.incrementNMonitoresP1();
                return true;
            }        
        }
    
    }
    
    
    public synchronized int reservaActividad() {
        int actividad = actividades.get((int) (actividades.size() * Math.random()));
        switch (actividad) {
            case 0 -> {
                nMonitoresDesMer -= 1;
                if (nMonitoresDesMer == 0) {
                    actividades.remove(actividades.indexOf(actividad));
                }
            }
            case 1 -> {
                nMonitoresDesTir -= 1;
                if (nMonitoresDesTir == 0) {
                    actividades.remove(actividades.indexOf(actividad));
                }
            }
            case 2 -> {
                nMonitoresDesSoga -= 1;
                if (nMonitoresDesSoga == 0) {
                    actividades.remove(actividades.indexOf(actividad));
                }
            }
        }
        return actividad;
    }
    public int getNumActividadesNinno(String idNinno){
        return registroNinno.numActividadesNinno(idNinno);
    }

    public int getnMonitoresMerienda() {
        return nMonitoresMerienda;
    }

    public int getnMonitoresTirolina() {
        return nMonitoresTirolina;
    }

    public int getnMonitoresSoga() {
        return nMonitoresSoga;
    }

    public String getCola1() {
        return entrada.getCola1();
    }

    public String getCola2() {
        return entrada.getCola2();
    }
    
    public String getColaT() {
        return tirolina.getCola();
    }
    
    public int getTamColaT() {
        return tirolina.getTamCola();
    }
    
    public int getVecesUsadoT(){
        return tirolina.getVecesUsado();
    }
    
    public String getNinnoTirolina() {
        return tirolina.getNinno();
    }

    public String getMonTirolina() {
        return tirolina.getMon();
    }
    
    public int getEstadoTirolina() {
        return tirolina.getEstadoTirolina();
    }

    public String getColaS() {
        return soga.getCola();
    }
    
    public int getTamColaS(){
        return soga.getTamCola();
    }

    public String getEquipoA() {
        return soga.getColaEquipoA();
    }

    public String getEquipoB() {
        return soga.getColaEquipoB();
    }
    
    public String getMonSoga() {
        return soga.getMon();
    }
    
    public int getBandLimpias() {
        return merendero.getBandLimpias();
    }

    public int getBandSucias() {
        return merendero.getBandSucias();
    }

    public String getColaMerendero() {
        return merendero.getCola();
    }

    public String getNinnoMerendero() {
        return merendero.getNinno();
    }
    
    public int getCuantosNinnosMerienda(){
        return merendero.cuantosNinnosMerienda();
    }

    public String getMonMerendero() {
        return merendero.getMon();
    }

    public String getNinnoZC() {
        return zonaComun.getNinno();
    }

    public String getMonZC() {
        return zonaComun.getMon();
    }

}
