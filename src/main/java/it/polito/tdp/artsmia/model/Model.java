package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private Map<Integer, ArtObject> idMap;
	
	
	public Model() {
		idMap = new HashMap<Integer, ArtObject>();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<ArtObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		ArtsmiaDAO dao = new ArtsmiaDAO();
		
		dao.listObjects(idMap);
		
		// Aggiungere i vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		// Aggiungere gli archi
		
		// Approccio 1: doppio ciclo for sui vertici (dati 2 vertici vedo se sono collegati)
		/*
		for(ArtObject a1 : this.grafo.vertexSet()) {
			for(ArtObject a2 : this.grafo.vertexSet()) {
				int peso = dao.getPeso(a1, a2);
				if(peso>0) {
					// controllo se esiste gia' l'arco
					if(!this.grafo.containsEdge(a1, a2));{
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		}
		*/
		
		// Approccio 2: mi faccio dare dal DB direttamente tutte le adiacenze
		for(Adiacenza a : dao.getAdiacenze()) {
			if(a.getPeso()>0) {
				Graphs.addEdge(this.grafo, idMap.get(a.getObj1()), idMap.get(a.getObj2()), a.getPeso());
			}
		}
		
		System.out.println(String.format("Grafo creato! # Vertici %d, # Archi %d", this.grafo.vertexSet().size(), this.grafo.edgeSet().size()));
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
}
