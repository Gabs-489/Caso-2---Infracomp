package Opcion2;
import java.util.HashMap;
import java.util.Map;

public class PageTable {
    private final Map<Integer, Pagina> tablaPaginas; // Tabla de páginas
    private final int numMarcos;   // Número de marcos de página
    private boolean continuar = true;
    private int hits = 0;   // Número de marcos de página
    private int miss = 0;   // Número de marcos de página

    public PageTable(int numFrames) {
        this.tablaPaginas = new HashMap<>(numFrames);
        this.numMarcos = numFrames;
    }
    
    
    // Método sincronizado para acceder y modificar la tabla de páginas
    public synchronized boolean accessPage(int pageNumber) {
        if (this.tablaPaginas.containsKey(pageNumber)) {
            this.hits++; 
            Pagina p = tablaPaginas.get(pageNumber);
            p.setReferenciada(true);
            return true; // Hit, la página está en RAM
        }else{
            this.miss++; 
            return false; // Miss, la página no está en RAM
        }
    }


    //Agregar Pagina a la tabla de paginas 
    public synchronized void agregarPagina(int pageNumber,int marco, boolean modifica){
        Pagina nuevaPagina = new Pagina(pageNumber, marco);
        nuevaPagina.setReferenciada(true);
        if (modifica){
            nuevaPagina.setModificada(modifica);
        }
        tablaPaginas.put(pageNumber, nuevaPagina);
    }

    // Método sincronizado para reemplazar una página en RAM
    public synchronized void manejoFalloPagina(int pageNumber, boolean modifica) {
        if (this.tablaPaginas.size() < numMarcos){ //Si hay espacio la agrega
            int marco = tablaPaginas.size();
            agregarPagina(pageNumber,marco,modifica);
        }else{
            reemplazarPagLRU(pageNumber,modifica);
        }
    }

    //Encuentra la pagina con la que se va a remplazar usando el NRU
    public synchronized void reemplazarPagLRU(int pageNumber,boolean modifica){
        int pagEliminar = -1;
        int marco = -1;
        boolean[][] priorityOrder = {{false, false}, {false, true}, {true, false}, {true, true}};
        for (boolean[] priority : priorityOrder) {
            for (Map.Entry<Integer, Pagina> entry : tablaPaginas.entrySet()) {
                Pagina pagina = entry.getValue();
                if (pagina.referenciada == priority[0] && pagina.modificada == priority[1]) {
                    pagEliminar = entry.getKey(); 
                    marco = entry.getValue().marco;
                    break;
                }
            }
        }
        tablaPaginas.remove(pagEliminar);
        agregarPagina(pageNumber,marco,modifica);
    }


    public synchronized void actualizarEstado(){
        for (Pagina pagina : tablaPaginas.values()) {
            pagina.setReferenciada(false);
        }
    }


    public int getHits() {
        return hits;
    }


    public int getMiss() {
        return miss;
    }


    public synchronized boolean isContinuar() {
        return continuar;
    }


    public synchronized void setContinuar(boolean continuar) {
        this.continuar = continuar;
    }

    
}