import Opcion1.GeneradorReferencias;
import Opcion2.LeerArchivoReferencias;
import Opcion2.PageTable;
import Opcion2.ThreadReader;
import Opcion2.ThreadUpdater;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Pruebas {
    public static void main(String[] args) throws Exception {

        // Escenarios definidos incluyendo ambas imágenes, tamaños y marcos
        List<String> imagenes = Arrays.asList("imag1.bmp", "imag2.bmp");
        int[] tamanosPagina = {512, 1024, 2048, 4096};
        int[] marcosAsignados = {4, 6, 8, 12};

        // Archivo CSV de salida con la ingormación de cada combinación realizada y lso outputs correspondientes
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultados_simulacion.csv"))) {
            writer.write("Imagen,TamPagina,Marcos,Referencias,Hits,Misses,PorcentajeHits,TiempoHits(ns),TiempoMisses(ns),TiempoTotal(ns)\n");

            for (String imagen : imagenes) {
                for (int tamPagina : tamanosPagina) {
                    // Generar archivo de referencias
                    GeneradorReferencias generador = new GeneradorReferencias(tamPagina, imagen);
                    String archivoReferencias = generador.generarReferencias();

                    for (int marcos : marcosAsignados) {
                        LeerArchivoReferencias lector = new LeerArchivoReferencias();
                        lector.leerArchivoReferencias(archivoReferencias.substring(archivoReferencias.lastIndexOf("/") + 1));

                        String[] referencias = lector.getReferencias().toArray(new String[0]);
                        PageTable pageTable = new PageTable(marcos);
                        ThreadReader reader = new ThreadReader(pageTable, referencias);
                        ThreadUpdater updater = new ThreadUpdater(pageTable);

                        reader.start();
                        updater.start();

                        reader.join();
                        updater.join();

                        int hits = pageTable.getHits();
                        int misses = pageTable.getMiss();
                        int total = hits + misses;
                        double porcentajeHits = (hits * 100.0) / total;
                        long tiempoHits = hits * 50L;
                        long tiempoMisses = misses * 10_000_000L;
                        long tiempoTotal = tiempoHits + tiempoMisses;

                        writer.write(String.format("%s,%d,%d,%d,%d,%d,%.2f,%d,%d,%d\n",
                                imagen, tamPagina, marcos, total, hits, misses,
                                porcentajeHits, tiempoHits, tiempoMisses, tiempoTotal));

                        System.out.printf("%s - Pagina: %d - Marcos: %d - Hits: %d - Misses: %d\n",
                                imagen, tamPagina, marcos, hits, misses);
                    }
                }
            }

            System.out.println("\nTodas las simulaciones han terminado. Revisa resultados_simulacion.csv para los datos.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
