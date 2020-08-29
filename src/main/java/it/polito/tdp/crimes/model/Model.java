package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.Adiacenza;
import it.polito.tdp.crimes.db.EventsDao;

public class Model {

	private EventsDao dao ;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> vertici ;
	private List<Adiacenza> adiacenze;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<String> getReati(){
		return this.dao.getReati();
	}
	
	public List<Integer> getAnni(){
		return this.dao.getAnni();
	}
	
	public void creaGrafo(String reato, Integer anno) {
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.vertici = this.dao.getVertici(reato, anno);
		Graphs.addAllVertices(this.grafo, vertici) ;
		this.adiacenze = this.dao.getArchi(reato, anno);
		for(Adiacenza a : this.adiacenze) {
			if(this.grafo.vertexSet().contains(a.getS1()) && this.grafo.vertexSet().contains(a.getS2()) && a.getPeso() >0) {
				Graphs.addEdgeWithVertices(this.grafo, a.getS1(), a.getS2(), a.getPeso());
			}
		}
	}
	
	public int numVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getResult(){
		// Ordino le adiacenze e metto in prima posizione quella col meso massimo
		Collections.sort(adiacenze);
		// Mi salvo il peso massimo
		Integer pesoMax = adiacenze.get(0).getPeso();
		// Inserisco l'arco corrispondente nella lista di risultato
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		result.add(adiacenze.get(0));
		for(Adiacenza a : this.adiacenze) {
			if(a != adiacenze.get(0)) {
				if(a.getPeso() == pesoMax) {
					result.add(a);
				}
			}
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
