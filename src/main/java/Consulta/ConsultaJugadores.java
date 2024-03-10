package Consulta;

import java.util.ArrayList;

import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ConsultaJugadores {
	public static void main(String[] args) throws Exception {
		try {
				
			if (args.length != 5) {
				System.err.println("\nError: Se necesitan 5 argumentos");
			}
			
			MongoClient mClient = new MongoClient();
			MongoDatabase database = mClient.getDatabase("IE");
			MongoCollection<Document> jugadoresC = database.getCollection("Jugadores");
		
		
		String nombre = args[0];
		String nacionalidad = args[1];
		String posicion = args[2];
		String elemento = args[3];
		String equipo = args[4];
		
		Document f = new Document();
		
		if (!nombre.isEmpty()) {
			f.put("nombre", nombre);
		}
		if (!nacionalidad.isEmpty()) {
			f.put("nacionalidad", nacionalidad);
		}
		if (!equipo.isEmpty()) {
			f.put("equipo", equipo);
		}
		if (!elemento.isEmpty()) {
			f.put("elemento", elemento);
		}
		if (!posicion.isEmpty()) {
			f.put("posicion", posicion);
		}
		List<Document> jugandores = jugadoresC.find(f).into(new ArrayList<>());
		
		if (jugandores.isEmpty()) {
			System.err.println("\nNo hay jugador con esos criterios");
			
		}else {
			System.err.println("\nRESULTADOS DE LA CONSULTA:");
			for (Document j : jugandores) {
				System.err.println("------------------");
				System.out.println("Nombre: " + j.getString("nombre"));
				System.out.println("Nacionalidad: " + j.getString("nacionalidad"));
				System.out.println("Posicion: " + j.getString("posicion"));
				System.out.println("Elemento: " + j.getString("elemento"));
				System.out.println("Equipo: " + j.getString("equipo"));
			}
		}
		mClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
