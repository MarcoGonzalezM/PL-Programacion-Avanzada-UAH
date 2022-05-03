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
    private ServerSocket servidor;
    private Socket conexion;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private Campamento campamento;
    
    public Respuesta(ServerSocket p_servidor, Campamento p_campamento){
        try{
            conexion = servidor.accept();
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
            switch (numConsulta){
                case 1 -> {
                    //campamento.get
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Respuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
