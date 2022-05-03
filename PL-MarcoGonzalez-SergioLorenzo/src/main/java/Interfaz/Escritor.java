/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.*;

/**
 *
 * @author marco
 */
public class Escritor extends Thread{
    private PrintWriter log;
    private static Queue<String> colaMsg;
    
    public Escritor(){
        try {
            log = new PrintWriter(new BufferedWriter(new FileWriter("evolucionCampamento.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Escritor.class.getName()).log(Level.SEVERE, null, ex);
        }
        colaMsg = new ConcurrentLinkedQueue<String>();
    }
    
    public static void addMsg(String msg){
        LocalDateTime evtTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MMMM/dd hh:mm:ss");
        msg +=" - "+dtf.format(evtTime);
        colaMsg.offer(msg);
    }
    
    public void print(){
        if (!colaMsg.isEmpty()){
            log.println(colaMsg.poll());
        }
    }
    
    public void close(){
        log.close();
    }
    
    public void run(){
        while(true){
            print();
        }
    }
}
