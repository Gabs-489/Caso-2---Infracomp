class ThreadReader extends Thread {
    private final PageTable pageTable;
    private final String[] references; // Lista de referencias

    public ThreadReader(PageTable pageTable, String[] references) {
        this.pageTable = pageTable;
        this.references = references;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < references.length; i++) {
                String reference = references[i];
                // Aquí procesas la referencia (asumiendo que la referencia tiene el formato adecuado)
                int pageNumber = Integer.parseInt(reference.split(",")[1]); // Extraemos el número de la página

                // Simula el acceso a la página y la actualización de la tabla
                if (!pageTable.accessPage(pageNumber)) { // Fallo de página
                    pageTable.replacePage(pageNumber);  // Reemplazo de página
                }

                // Cada 10000 referencias, el hilo espera 1 ms
                if (i % 10000 == 0) {
                    Thread.sleep(1); // Espera 1 ms
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}