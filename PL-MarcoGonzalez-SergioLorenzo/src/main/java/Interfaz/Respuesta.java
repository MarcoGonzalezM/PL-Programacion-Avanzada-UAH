package Interfaz;

import Modelo.Campamento;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergi
 */
public class Respuesta extends Thread{
    private int numConsulta;
    private String ninno;
    private Socket conexion;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private Campamento campamento;
    
    public Respuesta(Socket p_conexion, Campamento p_campamento){
        try{
            conexion = p_conexion;
            entrada = new DataInputStream(conexion.getInputStream());
            salida = new DataOutputStream(conexion.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Respuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        campamento = p_campamento;
    }
    
    @Override
    public void run(){
        try {
            numConsulta = entrada.readInt();
            if (numConsulta == 7){
                ninno = entrada.readUTF();
            }
            int respuesta = 0;
            switch (numConsulta){
                case 1 -> {
                    respuesta = campamento.getTamColaT();
                }
                case 2 -> {
                    respuesta = campamento.getVecesUsadoT();
                }
                case 3 -> {
                    respuesta = campamento.getCuantosNinnosMerienda();
                }
                case 4 -> {
                    respuesta = campamento.getBandSucias();
                }
                case 5 -> {
                    respuesta = campamento.getBandLimpias();
                }
                case 6 -> {
                    respuesta = campamento.getTamColaS();
                }
                case 7 -> {
                    respuesta = campamento.getNumActividadesNinno(ninno);
                }                
            }
            salida.writeInt(respuesta);
        } catch (IOException ex) {
            Logger.getLogger(Respuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
