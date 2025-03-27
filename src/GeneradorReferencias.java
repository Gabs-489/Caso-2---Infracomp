import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GeneradorReferencias {

    private Integer tamPagina;
    private String nomImagen;
    private Path pathImagenEntrada;
    private Path pathImagenSalida;
    Map<String, String> pagImagenes = new HashMap<>();

    public GeneradorReferencias (int tamPaginat, String nomImagent){
        this.tamPagina = tamPaginat;
        this.nomImagen = nomImagent;
        String directorioProyecto = System.getProperty("user.dir");
        this.pathImagenEntrada = Paths.get(directorioProyecto+"/src/"+nomImagen);
        System.out.println(pathImagenEntrada);
        
        String archivoRelativo = "/src/filtroSobel"+nomImagent; 
        this.pathImagenSalida = Paths.get(directorioProyecto+archivoRelativo);
    }

    public String generarReferencias(){
        Imagen imagenIn = new Imagen(pathImagenEntrada.toString());
        Imagen imagenOut = new Imagen(pathImagenEntrada.toString());
        FiltroSobel fs = new FiltroSobel(imagenIn, imagenOut);
        fs.applySobel();
        imagenOut.escribirImagen(pathImagenSalida.toString());


        String pathSalida = System.getProperty("user.dir") + "/src/referencias_" + nomImagen;
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathSalida, true))) {
            writer.write("TP="+ Integer.toString(this.tamPagina) + "\n");

            Integer NF = imagenIn.alto;
            writer.write("NF=" + Integer.toString(NF)+ "\n");

            Integer NC = imagenIn.ancho;
            writer.write("NC=" + Integer.toString(NC)+ "\n");

            //Falta el de referencias
            Integer referenciasTotales = ((NF-2)*(NC-2)*3) + ((NF-2)*(NC-2)*9*9);
            writer.write("NR=" + Integer.toString(referenciasTotales)+ "\n");

            Integer totalPaginas =  (int) (Math.ceil((2*(NF*NC*3)+72)/ (double) this.tamPagina));
            writer.write("NP=" + Integer.toString(totalPaginas) + "\n");


            //Poner Imagenes en paginas 
            int pag = 0;
            int desplazamientoPag = 0;

            int pagFiltroX = (int) Math.floor((NF*NC*3)/(double)this.tamPagina);
            int desplazamientoPagFiltroX = (NF*NC*3) - (pagFiltroX*this.tamPagina);

            int pagFiltroY = (int) Math.floor(((NF*NC*3)+(3*3*4))/(double)this.tamPagina);
            int desplazamientoPagFiltroY = ((NF*NC*3)+(3*3*4)) - (pagFiltroY*this.tamPagina);

            int pagRespuesta = (int) Math.floor(((NF*NC*3)+(3*3*4*2))/(double)this.tamPagina);
            int desplazamientoRespuesta = ((NF*NC*3)+(3*3*4*2)+(NC*3+1)) - (pagRespuesta*this.tamPagina);

            // Matriz Imagen
            for (int i = 1; i < NF-1; i++) {
                for (int j = 1; j < NC-1; j++) {
                    for (int ki = -1; ki <= 1; ki++) { 
                        for (int kj = -1; kj <= 1; kj++){
                            //Escribir cada canal
                            for (int g = 0; g < 3; g++){
                                if (desplazamientoPag >= 512){
                                    pag+=1;
                                    desplazamientoPag = 0;
                                }

                                //Encontrar Canal
                                String canal = "r";
                                if (g==1){
                                    canal = "g";
                                }
                                else if(g==2){
                                    canal = "b";
                                }

                                //Ver si ya esta en una pagina
                                
                                String imagLlave = String.format("Imagen[%d][%d].%s", i+ki,j+kj,canal);
                                String mensaje = pagImagenes.get(imagLlave);
                                if (mensaje == null){
                                    mensaje = String.format("Imagen[%d][%d].%s, %d, %d, R", i+ki, j+kj,canal,pag,desplazamientoPag);
                                    desplazamientoPag+=1;
                                    pagImagenes.put(imagLlave, mensaje);
                                }
                                writer.write(mensaje + "\n");
                                
                            }

                            //Escribir Para filtros
                            if (desplazamientoPagFiltroX + 4 >= 512){
                                pagFiltroX+=1;
                                desplazamientoPagFiltroX = 0;
                            }

                            String sobelX = String.format("SOBEL_X[%d][%d], %d, %d, R", 1+ki, 1+kj,pagFiltroX,desplazamientoPagFiltroX);
                            writer.write(sobelX + "\n");
                            writer.write(sobelX + "\n");
                            writer.write(sobelX + "\n");
                            desplazamientoPagFiltroX+=4;

                            if (desplazamientoPagFiltroY + 4 >= 512){
                                pagFiltroY+=1;
                                desplazamientoPagFiltroY = 0;
                            }
                            String sobelY = String.format("SOBEL_Y[%d][%d], %d, %d, R", 1+ki, 1+kj,pagFiltroY,desplazamientoPagFiltroY);
                            writer.write(sobelY + "\n");
                            writer.write(sobelY + "\n");
                            writer.write(sobelY + "\n");
                            desplazamientoPagFiltroY+=4;

                        }
                    }

                    pagFiltroX = (int) Math.floor((NF*NC*3)/(double)this.tamPagina);
                    desplazamientoPagFiltroX = (NF*NC*3) - (pagFiltroX*this.tamPagina);

                    pagFiltroY = (int) Math.floor(((NF*NC*3)+(3*3*4))/(double)this.tamPagina);
                    desplazamientoPagFiltroY = ((NF*NC*3)+(3*3*4)) - (pagFiltroY*this.tamPagina);

                    //Imprimir el del Resultado
                    for(int g = 0; g<3 ; g++){
                        if (desplazamientoRespuesta >= 512){
                            pagRespuesta+=1;
                            desplazamientoRespuesta = 0;
                        }
                        String canal = "r";
                        if (g==1){
                            canal = "g";
                        }
                        else if(g==2){
                            canal = "b";
                        }
                        String respuesta = String.format("Rta[%d][%d].%s, %d, %d, W", i, j,canal,pagRespuesta,desplazamientoRespuesta);
                        writer.write(respuesta + "\n");
                        desplazamientoRespuesta+=1;
                    }
                    
                }
            }

            return pathSalida;
        
        } catch (IOException e) {
            // Manejo de errores
            System.err.println("Error al escribir el archivo: " + e.getMessage());
            return "Error";
        }

        
    }
    
}
