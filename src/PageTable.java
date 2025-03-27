class PageTable {
    private final int[] pageTable; // Tabla de páginas
    private final int numFrames;   // Número de marcos de página

    public PageTable(int numFrames) {
        this.numFrames = numFrames;
        this.pageTable = new int[numFrames]; // Suponemos que el índice representa el marco de página
        for (int i = 0; i < numFrames; i++) {
            pageTable[i] = -1; // Inicializamos la tabla con -1 (sin datos cargados)
        }
    }

    // Método sincronizado para acceder y modificar la tabla de páginas
    public synchronized boolean accessPage(int pageNumber) {
        for (int i = 0; i < numFrames; i++) {
            if (pageTable[i] == pageNumber) {
                return true; // Hit, la página está en RAM
            }
        }
        return false; // Miss, la página no está en RAM
    }

    // Método sincronizado para reemplazar una página en RAM
    public synchronized void replacePage(int pageNumber) {
        for (int i = 0; i < numFrames; i++) {
            if (pageTable[i] == -1) {  // Si hay espacio disponible
                pageTable[i] = pageNumber;
                return;
            }
        }

        // Si no hay espacio, reemplazamos la primera página (algoritmo de reemplazo)
        pageTable[0] = pageNumber;
    }
}