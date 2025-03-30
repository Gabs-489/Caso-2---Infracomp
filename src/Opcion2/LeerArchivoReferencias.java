package Opcion2;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LeerArchivoReferencias {
    private final List<String> configuracion;
    private final List<String> referencias;
    
    public LeerArchivoReferencias() {
        this.configuracion = new ArrayList<>();
        this.referencias = new ArrayList<>();
    }


    public void leerArchivoReferencias(String archivoReferencias) {
        String directorioProyecto = System.getProperty("user.dir");
        Path pathAR = Paths.get(directorioProyecto+"/src/"+archivoReferencias);

        try (BufferedReader reader = new BufferedReader(new FileReader(pathAR.toString()))) {
            String line;
            int contador = 0;
            while ((line = reader.readLine()) != null) {
                // Filtra y agrega solo las líneas que contienen referencias válidas
                if (contador < 5) {
                    this.configuracion.add(line);
                }
                else if (line.startsWith("Imagen") || line.startsWith("SOBEL_X") || line.startsWith("SOBEL_Y") || line.startsWith("Rta")) {
                    this.referencias.add(line);
                }
                contador ++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<String> getConfiguracion() {
        return configuracion;
    }


    public List<String> getReferencias() {
        return referencias;
    }

    


}
