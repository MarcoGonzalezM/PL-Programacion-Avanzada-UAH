/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author sergi
 */
public class Consulta extends Thread{
    private int numConsulta;
    private String ninno;
    private JTextField textFieldCons;
    private JLabel carga;
    private Socket cliente;
    private DataOutputStream salida;
    private DataInputStream entrada;

    public Consulta(int p_numConsulta, JTextField p_textFieldCons , JLabel p_carga) {
        try{
            cliente = new Socket(InetAddress.getLocalHost(), 6721);
            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE, null, ex);
        }
        numConsulta = p_numConsulta;
        textFieldCons = p_textFieldCons;
        carga = p_carga;
    }
    
    public Consulta(int p_numConsulta, String p_ninno, JTextField p_textFieldCons, JLabel p_carga) {
        try{
            cliente = new Socket(InetAddress.getLocalHost(), 6721);
            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE, null, ex);
        }
        numConsulta = p_numConsulta;
        ninno = p_ninno;
        textFieldCons = p_textFieldCons;
        carga = p_carga;
    }
    
    
    
    @Override
    public void run(){
        try{
            salida.writeInt(numConsulta);
            if (numConsulta == 7) {
                salida.writeUTF(ninno);
            }
            carga.setText("C");
            int numDevuelto = entrada.readInt();
            carga.setText("");
            textFieldCons.setText(numDevuelto+"");
        } catch (IOException ex) {
            Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
