import java.util.Scanner;

import Opcion1.GeneradorReferencias;
import Opcion2.LeerArchivoReferencias;
import Opcion2.PageTable;
import Opcion2.ThreadReader;
import Opcion2.ThreadUpdater;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Bienvenido al simulador de memoria virtual: ");

        //Menu:
        Scanner scanner = new Scanner(System.in);
        int eleccion;

        do {
            System.out.println("\nMenú:");
            System.out.println("1. Generación de las referencias");
            System.out.println("2. Calcular datos buscados");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            eleccion = scanner.nextInt();
            
            switch (eleccion) {
                case 1:
                    System.out.println("Opción de Generar referencias:");
                    System.out.println("Ingrese el tamaño de página:");
                    int tamPagina = scanner.nextInt();
                    System.out.println("Ingrese el nombre del archivo de la imagen:");
                    String nomImagen = scanner.next();
                    GeneradorReferencias generadorReferencias = new GeneradorReferencias(tamPagina, nomImagen);
                    String nombreArchivoFinal = generadorReferencias.generarReferencias();
                    System.out.println("El nombre del archivo generado es: "+ nombreArchivoFinal);

                    break;
                case 2:
                    System.out.println("Has seleccionado la Opción 2.");
                    
                    System.out.println("Ingrese el número de marcos de página:");
                    int numMarcos = scanner.nextInt();
                    System.out.println("Ingrese el nombre del archivo de referencias:");
                    String archivoReferencias = scanner.next();


                    // Leer el archivo de referencias
                    LeerArchivoReferencias leerArchivoReferencias = new LeerArchivoReferencias();
                    leerArchivoReferencias.leerArchivoReferencias(archivoReferencias);
                    String[] referencias = leerArchivoReferencias.getReferencias().toArray(new String[0]);

                    // Ejecutar la simulación
                    PageTable pageTable = new PageTable(numMarcos);
                    ThreadReader reader = new ThreadReader(pageTable, referencias);
                    ThreadUpdater updater = new ThreadUpdater(pageTable);

                    reader.start();
                    updater.start();

                    // Esperar a que los hilos terminen
                    reader.join();
                    updater.join();

                    System.out.println("Simulación terminada.");
                    System.out.println("El número de fallos de pagina obtenido fue: " + Integer.toString(pageTable.getMiss()));
                    System.out.println("El número de hits de pagina obtenido fue: " + Integer.toString(pageTable.getHits()));
                    System.out.println("El total de referencias fue: " + Integer.toString(pageTable.getHits()+pageTable.getMiss()));

                    break;
                case 3:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida, intenta de nuevo.");
            }
        } while (eleccion != 3);

        scanner.close();
    }

    
}
