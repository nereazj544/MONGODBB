package ANTERIORES_CARPETAS.Consulta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class FicheroTexto {
    public static void main(String[] args) {

        CrearFicheroJson();
        leerFicheroGuardardatos();
    }

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

    private static void leerFicheroGuardardatos() {
        try ( MongoClient mClient = new MongoClient();
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

        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
