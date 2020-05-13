package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private Graph<Country, DefaultEdge> graph;
	private Map<Integer, Country> countryIdMap;
	private List<Country> countries;
	private BordersDAO dao;
	private List<Border> confini;

	ConnectivityInspector<Country, DefaultEdge> cI;
	//private List<Set<Country>> componentiConnesse;

	public Model() {
		dao = new BordersDAO();

		countryIdMap = new HashMap<Integer, Country>();
		for (Country c : dao.loadAllCountries()) {
			this.countryIdMap.put(c.getId(), c);
		}

	}

	public void creaGrafo(int anno) {
		this.graph = new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);

		// 1) creo i vertici
		countries = dao.getCountry(countryIdMap, anno);
		Graphs.addAllVertices(this.graph, countries);

		confini = new ArrayList<>();
		confini = dao.getCountryPairs(countryIdMap, anno);
		// 2) creo gli archi non orientati e non pesati
		for (Border b : confini) {
			if (!graph.containsEdge(b.getC1(), b.getC2())) {
				Graphs.addEdgeWithVertices(graph, b.getC1(), b.getC2());
			}
		}

	
		cI = new ConnectivityInspector<Country, DefaultEdge>(graph);
	}
	
	
	
	// 1. Componenti connesse con ConnectivityInspector
		public Set<Country> getConnectedComponent(Country stato) {
			cI.connectedSetOf(stato).remove(stato);
			return cI.connectedSetOf(stato);
		}

		
		
	// 2. metodi esposti dalla libreria jGraphT
	// a. BreadthFirstIterator
	public List<Country> getComponenteConnessaVisitaAmpiezza(Country stato) {
		BreadthFirstIterator<Country, DefaultEdge> bfv = new BreadthFirstIterator<>(graph, stato);

		List<Country> visita = new ArrayList<Country>();

		while (bfv.hasNext()) {
			visita.add(bfv.next());
		}
		//tolgo a mano lo stato richiesto
		visita.remove(stato);
		return visita;
	}
	
	// b. DepthFirstIterator
		public List<Country> getComponenteConnessaVisitaProfondita(Country stato) {
			DepthFirstIterator<Country, DefaultEdge> bfv = new DepthFirstIterator<>(graph, stato);
			// Creato l'iteratore si posiziona sul primo elemento
			List<Country> visita = new ArrayList<Country>();

			while (bfv.hasNext()) {
				visita.add(bfv.next());
			}
			//tolgo a mano lo stato richiesto
			visita.remove(stato);
			return visita;
		}
		
		
		//3 implementa manualmente un algoritmo ricorsivo per la visita in profondità
		
		//4 implementa manualmente un algoritmo iterativoutilizzando 2 liste: 
		//nodiVisitati e nodiDaVisitare
		
		
		
		

	public List<Set<Country>> getConnectedComponents() {
		return cI.connectedSets();
	}

	public Map<Integer, Country> getCountryIdMap() {
		return countryIdMap;
	}

	public Graph<Country, DefaultEdge> getGraph() {
		return graph;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public List<Border> getConfini() {
		return confini;
	}
}
