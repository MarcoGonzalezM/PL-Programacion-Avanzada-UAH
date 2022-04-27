package Modelo;

import java.util.*;
import javax.swing.JTextField;

/* La clase ListaThreads permite gestionar las listas de threads en los monitores,
con métodos para meter y sacar threads en ella. Cada vez que una lista se modifica,
se imprime su nuevo contenido en el JTextField que toma como parámetro el constructor. */
public class ListaThreads {
    ArrayList<Ninno> lista;
    JTextField tf;
    
    public ListaThreads(JTextField tf) {
        lista=new ArrayList<Ninno>();
        this.tf=tf;
    }
    
    public synchronized void meter(Ninno n) {
        lista.add(n);
        imprimir();
    }
    
    public synchronized void sacar(Ninno n) {
        lista.remove(n);
        imprimir();
    }
    
    public int getLongitud(){
        return lista.size();
    }
    
    public void imprimir() {
        String contenido="";
        for(int i=0; i<lista.size(); i++) {
           contenido=contenido+lista.get(i).getMiId()+" ";
        }
        tf.setText(contenido);
    }
}