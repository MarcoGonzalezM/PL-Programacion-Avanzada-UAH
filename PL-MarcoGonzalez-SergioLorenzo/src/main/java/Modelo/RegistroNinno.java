package Modelo;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author sergi
 */
public class RegistroNinno {
    private ArrayList<Ninno> listaNinnos;
    private Lock cerr = new ReentrantLock();
            
    public RegistroNinno(){
        listaNinnos= new ArrayList<Ninno>();
    }
    
    public void annadir(Ninno ninno){
        cerr.lock();
        try{
            if (listaNinnos.isEmpty()) listaNinnos.add(ninno);
            else{           
                int i = listaNinnos.size()-1;
                while (ninno.compareTo(listaNinnos.get(i)) < 0){
                    i--;
                }
                listaNinnos.add(i+1, ninno);
            }
        } finally {
            cerr.unlock();
        }
    }
    
    private int binarySearch(String idNinno){
        int left = 0, right = listaNinnos.size() - 1;       
        while (left <= right)
        { 
            int mid = left + (right - left) / 2; 
    
            // Check if x is present at mid 
            if (listaNinnos.get(mid).equals(idNinno)) 
                return mid; 
    
            // If x greater, ignore left half 
            if (listaNinnos.get(mid).compareTo(idNinno) < 0) 
                left = mid + 1; 
    
            // If x is smaller, ignore right half 
            else
                right = mid - 1; 
        } 
    
        // if we reach here, then element was 
        // not present 
        return -1;
    }
    
    public int numActividadesNinno(String idNinno){
        cerr.lock();
        try{
            int pos = binarySearch(idNinno);
            if (pos < 0 || idNinno.equals("N")){
                return -1;
            }
            return listaNinnos.get(pos).actividadesRealizadas();
        } finally {
            cerr.unlock();
        }
    }
}
