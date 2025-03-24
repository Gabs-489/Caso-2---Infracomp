import java.util.Scanner;

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
