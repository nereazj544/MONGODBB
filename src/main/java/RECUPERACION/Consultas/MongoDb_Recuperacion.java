package RECUPERACION.Consultas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import org.bson.Document;
import org.bson.types.ObjectId;

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
            System.out.println("2- Insertar datos por teclado.");
            System.out.println("3- Borrar");
            System.out.println("5- Salir");

            int e = sc.nextInt(); // Cuando el usuario ponga el numero saltara el caso en el switch

            switch (e) {
                case 1:
                    CrearJSON(sc);
                    break;
                case 2:
                    InsertarDatosScanner(sc);
                    break;
                case 3:
                    Borrar(sc);
                    break;

                case 5:
                    System.err.println("Adios :)");
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
                    JugadoresJSON();
                    break;
                case 2:
                    EquiposJSON();
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

        File file = new File("src\\main\\java\\RECUPERACION_MAYO\\Consultas\\Doc\\Jugadores-MONGODB.json");
        FileWriter fic;
        System.out.println("Ruta donde se creara el archivo: " + file); // !Muestor la ruta donde se guardara el doc.

        try {
            fic = new FileWriter(file);
            BufferedWriter f = new BufferedWriter(fic);

            System.err.println("------------");
            List<Document> c = jugadoresCollection.find().into(new ArrayList<Document>());

            for (int i = 0; i < c.size(); i++) {
                System.out.println("Elemnto: " + i + ", " + c.get(i).toString());
                f.write(c.get(i).toJson());
                f.newLine();
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        m.close();
    }

    private static void EquiposJSON() {

        MongoClient m = new MongoClient();
        MongoDatabase database = m.getDatabase("IE_Recu");
        MongoCollection<Document> EquiposCollection = database.getCollection("Equipos");

        File file = new File("src\\main\\java\\RECUPERACION_MAYO\\Consultas\\Doc\\Equipos-MONGODB.json");
        FileWriter fic;
        System.out.println("Ruta donde se creara el archivo: " + file);
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
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        m.close();
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

            System.out.println("Datos a insertar: Nombre, Posicion, Equipo");
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
            c.insertOne(d);
            System.out.println("Insertado Correctamente.");

            m.close();
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

            System.out.println("Datos a insertar: Nombre, Liga, Entrenador, Capitan, Jugadores");
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
            c.insertOne(d);
            System.out.println("Insertado Correctamente.");

            m.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void Borrar(Scanner sc) {

        MongoClient m = new MongoClient(); 

        MongoDatabase db = m.getDatabase("IE_Recu");

        while (true) {
            System.out.println("¿A qué colección le quieres borrar datos? (POR TECLADO)");
            System.out.println("1- Jugadores");
            System.out.println("2- Equipos");
            System.out.println("3- Salir");

            String choice = sc.nextLine();

            int num;
            try {
                num = Integer.parseInt(choice); 
                if (num < 1 || num > 3) {
                    System.err.println("Error: Introduce un número válido (1, 2, o 3).");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Introduce un número válido (1, 2, o 3).");
                continue;
            }

            switch (num) {
                case 1:
                    JugadoresBorrar(sc, db);
                    break;
                case 2:
                    EquiposBorrar(sc, db);
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
    // // try{
    // MongoClient m = new MongoClient();
    // MongoDatabase db = m.getDatabase("IE_Recu");

    // while (true) {
    // System.err.println("¿A que coleccion le quieres añadir datos? (POR
    // TECLADO)");
    // System.out.println("1- Jugadores");
    // System.out.println("2- Equipos");
    // System.out.println("3- Salir");

    // int num = sc.nextInt();
    // switch (num) {
    // case 1:
    // JugadoresBorrar(sc, db);
    // break;
    // case 2:
    // EquiposBorrar(sc, db);
    // break;
    // case 3:
    // System.exit(0);

    // default:
    // break;
    // }
    // }
    // // }catch (NumberFormatException e){
    // // e.printStackTrace();
    // // }

    private static void JugadoresBorrar(Scanner sc, MongoDatabase db) {
        System.out.println("Introduce el ID del jugador que quieras borrar: ");
        MongoCollection<Document> jugadores = db.getCollection("Jugadores");

        String id = sc.nextLine();

        if (!isId(id)) {
            System.err.println("No es valido");

        }

        DeleteResult dr = jugadores.deleteOne(Filters.eq("_id", new ObjectId(id)));

        if (dr.getDeletedCount() == 1) {
            System.out.println("Se ha borrado el jugador con el ID: " + id);

        } else {
            System.out.println("No se ha encontrado ningun jugador");
        }

    }

    private static void EquiposBorrar(Scanner sc, MongoDatabase db) {
        MongoCollection<Document> Equipos = db.getCollection("Equipos");

        System.out.println("Introduce el ID del jugador que quieras borrar: ");
        String id = sc.nextLine();

        if (!isId(id)) {
            System.err.println("No es valido");

        }

        DeleteResult dr = Equipos.deleteOne(Filters.eq("_id", new ObjectId(id)));

        if (dr.getDeletedCount() == 1) {
            System.out.println("Se ha borrado el jugador con el ID: " + id);

        } else {
            System.out.println("No se ha encontrado ningun jugador");
        }
    }

    private static boolean isId(String id) {
        try {
            new ObjectId(id);

            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    // !End Program
}
