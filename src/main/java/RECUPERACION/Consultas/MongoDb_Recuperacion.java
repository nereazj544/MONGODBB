package RECUPERACION.Consultas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.print.Doc;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

public class MongoDb_Recuperacion {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // Scanner para la lectura de datos.

        while (true) {

            System.err.println("\n Selecciona una opcion:");
            System.out.println("1- Crear JSON de las coleciones llamasdas 'Jugadores' y 'equipos'");
            System.out.println("2- Visualizar datos.");
            System.out.println("3- Insertar datos por teclado.");
            System.out.println("4- Insertar datos por archivo. (NO FUNCIONA-EL CODIGO SE ENCUENTRA COMENTADO)");
            System.out.println("5- Borrar");
            System.out.println("6- Salir");

            int e = sc.nextInt(); // Cuando el usuario ponga el numero saltara el caso en el switch

            switch (e) {
                case 1:
                    CrearJSON(sc); // * Llama al metodo para crear archivos JSON
                    break;
                case 2:
                    VisualizarDatos(sc); // * Llama al metodo para visualizar los datos
                    break;
                case 3:
                    InsertarDatosScanner(sc); // * Llama al metodo para insertar los datos
                    break;
                // case 4:
                //     InsertarDatosTXT(sc);
                //     break;
                case 5:
                    Borrar(sc); // * Llama al metodo para borrar datos
                    break;

                case 6:
                    System.err.println("Adios :)"); // * Mensaje de despedida
                    System.exit(0); // Salir del programa
                default:
                    break;
            }

        }
    }

    // ! Crear JSON
    /*
     * -------METODOS PARA CREAR JSON DE LAS COLECCIONES: JUGADORES Y EQUIPOS-------
     */
    private static void CrearJSON(Scanner sc) {
        while (true) { // Se usa un While para preguntarle al usuario que cual coleccion quiere que se
                       // pase a JSON
            System.out.println("¿Para que coleccion quiere hacer el JSON?\n");
            System.out.println("1- Jugadores");
            System.out.println("2- Equipos");
            System.out.println("3- Salir");

            int num = sc.nextInt();

            switch (num) {
                case 1:
                    JugadoresJSON(); // Llamada al metodo de Jugadores
                    break;
                case 2:
                    EquiposJSON(); // Llamada al metodo de Equipos
                    break;
                case 3:
                    System.exit(0);

                default:
                    System.err.println("Error");
            }
        }
    }

    private static void JugadoresJSON() {

        // ! Conexion con MongoDB
        MongoClient m = new MongoClient();
        MongoDatabase database = m.getDatabase("IE_Recu"); // !Se selecciona la Base de datos la cual se llama "IE_Recu"
        MongoCollection<Document> jugadoresCollection = database.getCollection("Jugadores"); // !La coleccion

        // Especificacion de la ruta donde se va a guardar el JSON
        File file = new File("src\\main\\java\\RECUPERACION\\Consultas\\Doc\\Jugadores-MONGODB.json");
        FileWriter fic;
        System.err.println("------------");
        System.out.println("Ruta donde se creara el archivo: " + file); // !Muestor la ruta donde se guardara el doc.
        System.err.println("------------");

        try {
            fic = new FileWriter(file);
            BufferedWriter f = new BufferedWriter(fic);

            // los documentos de la coleccion de jugaodres y se escriben en el archivo JSON
            List<Document> c = jugadoresCollection.find().into(new ArrayList<Document>());

            for (int i = 0; i < c.size(); i++) {
                System.out.println("Elemnto: " + i + ", " + c.get(i).toString());
                f.write(c.get(i).toJson());
                f.newLine();
            }
            f.close(); // Se cierra el BufferedWriter

        } catch (Exception e) {
            e.printStackTrace();
        }

        m.close(); // Se cierra la conexion
    }

    private static void EquiposJSON() {

        // Conexion con MongoDB
        MongoClient m = new MongoClient();
        MongoDatabase database = m.getDatabase("IE_Recu");
        MongoCollection<Document> EquiposCollection = database.getCollection("Equipos");

        // Especificamos la ruta donde se guardara el JSON
        File file = new File("src\\main\\java\\RECUPERACION\\Consultas\\Doc\\Equipos-MONGODB.json");
        FileWriter fic;
        System.out.println("Ruta donde se creara el archivo: " + file); // Mostramos donde se guardo
        try {
            fic = new FileWriter(file);
            BufferedWriter f = new BufferedWriter(fic);

            System.err.println("------------");
            List<Document> c = EquiposCollection.find().into(new ArrayList<Document>());

            for (int i = 0; i < c.size(); i++) {
                System.out.println("Elemnto: " + i + ", " + c.get(i).toString());
                f.write(c.get(i).toJson());
                f.newLine();
            }
            f.close(); // Cerramos BufferedWriter

        } catch (Exception e) {
            e.printStackTrace();
        }

        m.close(); // Cerramos conexion MongoDB
    }

    /*
     * -------METODOS PARA INSERTAR DATOS (SCANNER) DE LAS COLECCIONES: JUGADORES Y
     * EQUIPOS-------
     */

    // ! Scanner = Datos
    private static void InsertarDatosScanner(Scanner sc) {
        while (true) {
            System.err.println("¿A que coleccion le quieres añadir datos? (POR TECLADO)");
            System.out.println("1- Jugadores");
            System.out.println("2- Equipos");
            System.out.println("3- Salir");

            int num = sc.nextInt();
            switch (num) {
                case 1:
                    JugadoresSc(sc);
                    break;
                case 2:
                    EquiposSc(sc);
                    break;
                case 3:
                    System.exit(0);

                default:
                    break;
            }
        }

    }

    private static void JugadoresSc(Scanner sc) {
        try {
            // ! Conexion con MongoDB
            MongoClient m = new MongoClient();
            MongoDatabase database = m.getDatabase("IE_Recu");
            // !Se selecciona la Base de datos la cual se llama "IE_Recu"

            System.out.println("====================================");
            System.out.println("Datos a insertar: Nombre, Posicion, Equipo");
            // Mostramos por pantalla los parametros que vamos a ir introduciendo por
            // teclado

            System.out.println("Enter para continuar\n");
            String e1 = sc.nextLine();

            System.out.println("====================================");
            System.out.println("Nombre del jugador: ");
            String nombre = sc.nextLine();

            System.out.println("Posicion en la que juega: ");
            String posicion = sc.nextLine();

            System.out.println("Equipo en el que juega: ");
            String equipo = sc.nextLine();

            Document d = new Document();
            // !Insertamos los datos en las correspondientes datos en el MongoDb
            d.put("nombre", nombre);
            d.put("posiciones", posicion);
            d.put("Equipos", equipo);

            MongoCollection<Document> c = database.getCollection("Jugadores");
            c.insertOne(d); // Se insertan los nuevos campos dentro de la coleccion
            System.out.println("Insertado Correctamente.");

            m.close(); // Se cierra conexion con MongoDB
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void EquiposSc(Scanner sc) {
        try {
            // ! Conexion con MongoDB
            MongoClient m = new MongoClient();
            MongoDatabase database = m.getDatabase("IE_Recu");
            // !Se selecciona la Base de datos la cual se llama "IE_Recu"

            System.out.println("====================================");
            System.out.println("Datos a insertar: Nombre, Liga, Entrenador, Capitan, Jugadores");
            // Mostramos por pantalla los parametros que vamos a ir introduciendo por
            // teclado

            System.out.println("Enter para continuar");
            String e1 = sc.nextLine();

            System.out.println("Nombre del Equipo: ");
            String nombreequ = sc.nextLine();

            System.out.println("Liga: ");
            String liga = sc.nextLine();

            System.out.println("Entrenador del Equipo: ");
            String entrenador = sc.nextLine();

            System.out.println("Capitan del Equipo: ");
            String capitan = sc.nextLine();
            System.out.println("Jugadores del Equipo: ");
            String jugadores = sc.nextLine();

            Document d = new Document();
            d.put("nombre", nombreequ);
            d.put("liga", liga);
            d.put("Entrenador", entrenador);
            d.put("Capitan", capitan);
            d.put("Jugadores", jugadores);

            MongoCollection<Document> c = database.getCollection("Equipos");
            c.insertOne(d); // Se insertan los nuevos campos dentro de la coleccion
            System.out.println("Insertado Correctamente.");

            m.close(); // Se cierra la conexion
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * -------METODOS PARA BORRAR DATOS-------
     */
    private static void Borrar(Scanner sc) {
        // Conexion con MongoDB
        MongoClient m = new MongoClient();
        MongoDatabase db = m.getDatabase("IE_Recu");

        // Solicitamos la coleccion que queremos hacer las modificaciones
        while (true) {
            System.out.println("¿A qué colección le quieres borrar datos? (POR TECLADO)");
            System.out.println("1- Jugadores");
            System.out.println("2- Equipos");
            System.out.println("3- Salir");

            String choice = sc.nextLine();

            int num;
            try {
                num = Integer.parseInt(choice); // Conversion de la opcion a un nº entero
                if (num < 1 || num > 3) {
                    // Se verifica si el nº esta dentro del rango valido
                    System.err.println("Error: Introduce un número válido (1, 2, o 3).");
                    continue; // Se vuelve al inicio del bucle para solicitar nuevamente la opcion
                }
            } catch (NumberFormatException e) {
                // Si se produce un error en el formato del nº
                System.err.println("Error: Introduce un número válido (1, 2, o 3).");
                continue;
            }

            switch (num) {
                case 1:
                    JugadoresBorrar(sc, db); // Metodo de los jugadores
                    break;
                case 2:
                    EquiposBorrar(sc, db); // Metodo del equipo
                    break;
                case 3:
                    System.exit(0); // salir
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private static void JugadoresBorrar(Scanner sc, MongoDatabase db) {
        System.out.println("Introduce el ID del jugador que quieras borrar: ");
        MongoCollection<Document> jugadores = db.getCollection("Jugadores");

        String id = sc.nextLine(); // Se lee el ID introduccido

        if (!isId(id)) {
            // Se verifica si es valido, si no lo es se muestra el mensje de error
            System.err.println("No es valido");

        }

        DeleteResult dr = jugadores.deleteOne(Filters.eq("_id", new ObjectId(id)));
        // Se elimina el jugador de la coleccion usando el ID

        if (dr.getDeletedCount() == 1) { // Si se ha elimidado el jugador correctamente
            System.out.println("Se ha borrado el jugador con el ID: " + id);

        } else { // si no se ha encontrado ningun jugador
            System.out.println("No se ha encontrado ningun jugador");
        }

    }

    private static void EquiposBorrar(Scanner sc, MongoDatabase db) {
        MongoCollection<Document> Equipos = db.getCollection("Equipos");

        System.out.println("Introduce el ID del equipo que quieras borrar: ");
        String id = sc.nextLine();

        if (!isId(id)) {
            // Verificamos el ID
            System.err.println("No es valido");

        }

        DeleteResult dr = Equipos.deleteOne(Filters.eq("_id", new ObjectId(id)));
        // se elimina el equipo de la coleccion usando el ID
        if (dr.getDeletedCount() == 1) {
            // Si se ha eliminado el equipo correctamente
            System.out.println("Se ha borrado el equipo con el ID: " + id);

        } else {// si no se ha encontrado ningun equipos
            System.out.println("No se ha encontrado ningun equipo.");
        }
    }

    // Metodo para verificar ID
    private static boolean isId(String id) {
        try {
            new ObjectId(id);
            // Crear un objeto con el id proporcionado

            return true; // Si no se procudce ningun error (Vamos, que si es valido el ID)
        } catch (IllegalArgumentException e) {
            return false; // Si no es valido el ID
        }
    }

    /*
     * -------METODOS PARA VISUALIZAR DATOS-------
     */
    private static void VisualizarDatos(Scanner sc) {
        while (true) {
            System.err.println("¿A que coleccion le quieres visualizar datos? ");
            System.out.println("1- Jugadores");
            System.out.println("2- Equipos");
            System.out.println("3- Salir");

            int num = sc.nextInt();
            switch (num) {
                case 1:
                    JugadoresVis(sc);
                    break;
                case 2:
                    EquiposVIs(sc);
                    break;
                case 3:
                    System.exit(0);

                default:
                    break;
            }
        }
    }

    private static void JugadoresVis(Scanner sc) {
        // Conexion con MongoDB
        MongoClient m = new MongoClient();
        MongoDatabase db = m.getDatabase("IE_Recu");

        MongoCollection<Document> c = db.getCollection("Jugadores"); // !Coleccion

        List<Document> consulta = c.find().into(new ArrayList<Document>());
        // Se hace la consulta para obtener todas las entradas de la coleccion pre
        // seleccionada arriba ⤴️

        // Se recorre las entrads obteniendo las consultas
        for (int i = 0; i < consulta.size(); i++) {
            Document Jugadores = consulta.get(i);
            // Se obtienen los resultados de la coleccion: Jugadores
            System.out.println("===================================");
            System.out.println("Jugador: " + Jugadores.toString());
            System.out.println("===================================");
        }
    }

    private static void EquiposVIs(Scanner sc) {
        // Conexion con MongoDB
        MongoClient m = new MongoClient();
        MongoDatabase db = m.getDatabase("IE_Recu");

        MongoCollection<Document> c = db.getCollection("Equipos"); // !Coleccion

        List<Document> consulta = c.find().into(new ArrayList<Document>());
        // Se hace la consulta para obtener todas las entradas de la coleccion pre
        // seleccionada arriba ⤴️

        // Se recorre las entrads obteniendo las consultas
        for (int i = 0; i < consulta.size(); i++) {
            Document Equipos = consulta.get(i);
            // Se obtienen los resultados de la coleccion: Equipos
            System.out.println("===================================");
            System.out.println("Equipos: " + Equipos.toString());
            System.out.println("===================================");
        }
    }

    /*
     * -------METODOS PARA INSERTAR DATOS (TXT) DE LAS COLECCIONES: JUGADORES Y
     * EQUIPOS-------
     */
    /*
     * 
     * private static void InsertarDatosTXT(Scanner sc) {
     * 
     * // Conexion con MongoDB
     * MongoClient m = new MongoClient();
     * MongoDatabase db = m.getDatabase("IE_Recu");
     * 
     * // Solicitamos la coleccion que queremos hacer las modificaciones
     * while (true) {
     * System.err.
     * println("¿A que coleccion le quieres añadir datos? (POR ARCHIVO TXT)");
     * System.out.println("1- Jugadores");
     * System.out.println("2- Equipos");
     * System.out.println("3- Salir");
     * 
     * String choice = sc.nextLine();
     * 
     * int num;
     * try {
     * num = Integer.parseInt(choice); // Conversion de la opcion a un nº entero
     * if (num < 1 || num > 3) {
     * // Se verifica si el nº esta dentro del rango valido
     * System.err.println("Error: Introduce un número válido (1, 2, o 3).");
     * continue; // Se vuelve al inicio del bucle para solicitar nuevamente la
     * opcion
     * }
     * } catch (NumberFormatException e) {
     * // Si se produce un error en el formato del nº
     * System.err.println("Error: Introduce un número válido (1, 2, o 3).");
     * continue;
     * }
     * 
     * switch (num) {
     * case 1:
     * JugadoresTxt(sc);
     * break;
     * case 2:
     * EquiposTxt(sc);
     * break;
     * case 3:
     * System.exit(0);
     * 
     * default:
     * break;
     * }
     * }
     * 
     * }
     * 
     * private static void JugadoresTxt(Scanner sc) {
     * try {
     * // Conexion con MongoDB
     * MongoClient m = new MongoClient();
     * MongoDatabase db = m.getDatabase("IE_Recu");
     * 
     * MongoCollection<Document> c = db.getCollection("Jugadores"); // Coleccion
     * 
     * System.out.
     * println("Introce la ruta del archivo de txt con los datos de JSON: ");
     * String r = sc.nextLine(); // Se lee la ruta del archivo
     * 
     * // Leer el archivo
     * File f = new File(r);
     * Scanner fileSc = new Scanner(f);
     * 
     * sc.nextLine(); // Limpiamos despues de leer la opcion
     * 
     * // Procesar cada campo/lina
     * while (fileSc.hasNext()) {
     * String l = fileSc.nextLine();
     * 
     * String[] data = l.split(","); // cada elemento separado por una coma
     * if (data.length >= 3) {
     * // Insertamos los datos del documento
     * Document dc = new Document();
     * dc.put("Nombre", data[0]);
     * dc.put("Posicion", data[1]);
     * dc.put("Equipo", data[2]);
     * 
     * c.insertOne(dc);
     * 
     * } else {
     * System.err.println("Error");
     * }
     * }
     * 
     * System.out.println("Datos insertados");
     * m.close();
     * fileSc.close();
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * 
     * }
     * 
     * private static void EquiposTxt(Scanner sc) {
     * // TODO Auto-generated method stub
     * throw new UnsupportedOperationException("Unimplemented method 'EquiposTxt'");
     * }
     */

    // !End Program
}
