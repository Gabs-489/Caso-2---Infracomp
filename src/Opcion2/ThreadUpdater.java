package Opcion2;
public class ThreadUpdater extends Thread {
    private final PageTable pageTable;

    public ThreadUpdater(PageTable pageTable) {
        this.pageTable = pageTable;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Espera 1 ms (simulando que el hilo se ejecuta cada milisegundo)
                Thread.sleep(1); 
                if(!pageTable.isContinuar()){
                    break;
                }            
                synchronized (pageTable) {
                    pageTable.actualizarEstado();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
