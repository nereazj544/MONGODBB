package Consulta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB {

    private static final String dbStr = "IE";
    private static final String colStr = "Jugadores";
    private static final String json = "src\\main\\java\\RECURSOS\\JUGADORES-DEFINITIVOS.json";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.err.println("\n Selecciona una opcion: ");
            System.out.println("1. Crear archivo JSON.");
            System.out.println("2. Leer el archivo JSON y insertar la coleccion dentro del MongoDB.");
            System.out.println("3. Insertar");
            System.out.println("4. Borrar");
            System.out.println("5. Actualizar");
            System.out.println("6. EXIT");

            int e = sc.nextInt();

            switch (e) {
                case 1:
                    CrearFicheroJson();
                    break;
                case 2:
                    leerFicheroGuardardatos();
                    break;
                case 3:
                    insertar(sc);
                    break;
                case 4:
                    borrar(sc);
                    break;
                case 5:
                    actualizar(sc);
                    break;
                case 6:
                    System.err.println("¡¡ADIOSSSSS!!");
                    System.exit(0);

                default:
                    System.err.println("ERROR");
            }
        }
    }

    // Crear JSON

    private static void CrearFicheroJson() {
        // MongoDB
        MongoClient mClient = new MongoClient();
        MongoDatabase database = mClient.getDatabase("IE");
        MongoCollection<Document> jugadoresCollection = database.getCollection("Jugadores");

        File file = new File("src\\main\\java\\RECURSOS\\J.json");
        FileWriter fic;

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

        mClient.close();
    }

    // leer fichero

    private static void leerFicheroGuardardatos() {
        try (MongoClient mClient = new MongoClient();
                FileReader fr = new FileReader("src\\main\\java\\RECURSOS\\J.json");
                BufferedReader bf = new BufferedReader(fr)) {
            MongoDatabase database = mClient.getDatabase("IE");
            MongoCollection<Document> c = database.getCollection("Jugadores");

            String json;

            while ((json = bf.readLine()) != null) {
                System.out.println(json);
                Document dc = new Document(Document.parse(json));
                c.insertOne(dc);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Insertar datos
    private static void insertar(Scanner sc) {
        try(MongoClient mClient = (MongoClient) MongoClients.create()) {
            MongoDatabase database = mClient.getDatabase(dbStr);
            MongoCollection<Document> c = database.getCollection(colStr);

            System.out.println("Inserte: Nombre, Nacionalidad, Posiciones, Elemento, Equipo. Por separado");
            String d = sc.nextLine();
            Document document = Document.parse(d);

            c.insertOne(document);
            System.out.println("Insertado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void borrar(Scanner sc) {

    }

    private static void actualizar(Scanner sc) {

    }
}
