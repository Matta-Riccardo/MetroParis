package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

//	Implemento qui il metodo per creare il grafo.
	
	private List<Fermata> fermate;
	private Graph <Fermata, DefaultEdge> grafo;
	private Map<Integer,Fermata> fermateIdMap;
	
	public List<Fermata> getFermate(){
		
		if(this.fermate== null) {
			MetroDAO dao = new MetroDAO();
			this.fermate = dao.getAllFermate();
			
			this.fermateIdMap = new HashMap<Integer,Fermata>();
			for(Fermata f : fermate) {
				this.fermateIdMap.put(f.getIdFermata(), f);
			}
		}
		return this.fermate;
	}
	
	
	public List<Fermata> calcolaPercorso(Fermata partenza, Fermata arrivo){
		
		creaGrafo();
		Map<Fermata, Fermata> alberoInverso = visitaGrafo(partenza);
		
		Fermata corrente = arrivo;
		List<Fermata> percorso = new ArrayList<Fermata>();
		
		while(corrente != null) { //dato che il vertice radice ovviamente avra null come sorgente, quindi essendo che leggo la visita al contrario quando arrivo alla radice corrente sarà uguale a null
			percorso.add(0, corrente); //faccio l'inserimento in testa ogni volta per non avere la stampa finale del percorso dall'arrivo alla partenza
			corrente = alberoInverso.get(corrente);
		}
		
		return percorso;
		
	}
	
	
	
	public void creaGrafo() {  //Questa funzione potrebbe benissimo essere privata, tanto basterà che sarà visibile al chiamante
		
//		Tale funzione leggerà i dati dal db per creare il grafo.
//		Devo istanziare il grafo scegliendo il tipo di grafo che a me serve.
		
		this.grafo = new SimpleDirectedGraph <Fermata, DefaultEdge>(DefaultEdge.class);
//		il grafo qui è ancora vuoto. --> Devo fare una query al db per estrarre tutti gli oggetti fermata e utilizzarli (convertirli) nel grafo come vertici.
		
		MetroDAO dao = new MetroDAO();
		
//		List<Fermata> fermate = dao.getAllFermate();
//		Map<Integer,Fermata> fermateIdMap = new HashMap<Integer,Fermata>();     SPOSTO TALI METODI DENTRO GET FERMATE, ESSENDO IL PRIMO METODO CHE VERRA' CHIAMATO.
//		for(Fermata f : fermate) {
//			fermateIdMap.put(f.getIdFermata(), f);
//		}
		
		Graphs.addAllVertices(this.grafo, getFermate());
		
//		System.out.println(this.grafo);
//		System.out.println("Vertici = " + this.grafo.vertexSet().size());
		
//		Finora ho creato un grafo di 619 vertici (Fermate) che sono isolate tra loro, non ho ancora definito nessun arco che le colleghi.

//		-----------Primo metodo di creazione archi: itero su ogni coppia di vertici-----------
//		(Posso iterare su 'fermate' oppure su 'this.grafo.vertexSet()').
		
//		for(Fermata partenza : fermate) {
//			for(Fermata arrivo : fermate) {
//				if(dao.isFermateConnesse(partenza, arrivo)) { // if(esiste almeno una connessione tra partenza e arrivo){...}  --> creo allora questa query!
//					this.grafo.addEdge(partenza, arrivo);
//				}
//			}
//		}
		
//		System.out.println("Archi = " + this.grafo.edgeSet().size());
		
//		Questo metodo di creazione degli archi è il più semplice ma risulta essere estramamente lento.
//		--> Anzichè fare un doppio ciclo per tutte le coppie di vertici (fermate) posso concentrarmi su un vertice per volta:
//		    Prendo una fermata e mi chiedo: quali sono tutti gli archi uscenti (o entranti) da tale fermata, mi serve una query allora che data una fermata di partenza mi restituisca tutte le fermate di arrivo.
	
//		-----------Secondo metodo di creazione archi: dato ciascun vertice trovo i vertici ad esso adiacenti-----------
//		(Posso iterare su 'fermate' oppure su 'this.grafo.vertexSet()').
		
//		for(Fermata partenza : fermate) {
//			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza); // Query che data una stazione di partenza mi restituisce una lista di interi che rappresentano ognuno un'id della stazione che risulta essere collegata a quella di partenza
//			for(Integer id : idConnesse) {
////				Devo qui ora convertire l'oggetto Integer in un oggetto fermata corrispondente
//				Fermata arrivo = null;
//				for(Fermata f : fermate) {
//					if(f.getIdFermata()==id) {
//						arrivo = f;
//						break;
//					}
//				}
//				this.grafo.addEdge(partenza, arrivo);
//			}
//		}
//		
//		System.out.println("Archi = " + this.grafo.edgeSet().size());
//		

//	-----------Secondo metodo di creazione archi (VARIANTE B): dato ciascun vertice trovo i vertici ad esso adiacenti-----------
//				Farò una query che, al posto di restituire una lista di interi che poi dovrò riconvertire in stazioni, mi restituisca direttamente una lista di stazioni collegate (stazioni di arrivo) a partire dalla stazione di partenza
//		
//		for(Fermata partenza : fermate) {
//			List<Fermata> fermateConnesse = dao.getFermateConnesse(partenza);
//			for(Fermata arrivo : fermateConnesse) {
//				this.grafo.addEdge(partenza, arrivo);
//			}
//		}
//		
//		System.out.println("Archi = " + this.grafo.edgeSet().size());
		
//	-----------Secondo metodo di creazione archi (VARIANTE C): dato ciascun vertice trovo i vertici ad esso adiacenti-----------
//		Farò una query che restituisce una lista di interi che poi dovrò riconvertire in stazioni, tramite una mappa Map<Integer,Fermata> --> 'Identy Map'.
		
		
//		for(Fermata partenza : fermate) {
//			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza); // Query che data una stazione di partenza mi restituisce una lista di interi che rappresentano ognuno un'id della stazione che risulta essere collegata a quella di partenza
//			for(int id : idConnesse) {
//				Fermata arrivo = fermateIdMap.get(id);
//				this.grafo.addEdge(partenza, arrivo);
//			}
//		}
//		
//		System.out.println("Archi = " + this.grafo.edgeSet().size());
		
		
//	-----------Terzo metodo di creazione archi: faccio una sola query che mi restitusca le coppie di fermate da collegare-----------
//		Delego tutto il lavoro da fare al db.
		
		List<CoppiaId> fermateDaCollegare = dao.getAllFermateConnesse();
		for(CoppiaId coppia : fermateDaCollegare) {
			this.grafo.addEdge(fermateIdMap.get(coppia.getIdPartenza()), fermateIdMap.get(coppia.getIdArrivo()));

			
		}
		
//		System.out.println(this.grafo);
//		System.out.println("Vertici = " + this.grafo.vertexSet().size());
//		System.out.println("Archi = " + this.grafo.edgeSet().size());
		
//		visitaGrafo(fermate.get(0));
		
	}
		
//	-----------Fatto il grafo è ora utile trovare i percorsi: mi chiedo allora da un grafo e un vertice di partenza come trovare gli altri vertici raggiungibili attravero un percorso più o meno lungo,
//		       non mi interessano sono gli archi
		
//		---> Dati due vertici esistono più percorsi che li possono collegare

// 		DEVO UTILIZZARE DEGLI ALGORITMI DI VISITA IN AMPIEZZA E IN PROFONDITA DEL GRAFO:

//		- IN AMPIEZZA: algoritmo che parte da un vertice sorgente (punto di partenza) e cerca come prima cosa tutti i vertici adiacenti al vertice di partenza (vertici di livello 1)..
//		               dai vertici di livello 1 va a vedere i loro adiacenti (i vertici di livello 2), e così via ....
//		               Parto dal vertice di livello zero, poi passo a calcolare tutti i vertici adiacenti del livello zero, e successivamente esploro i livelli successi a patto che i vertici esplorati siano adiacenti ad almeno un vertice del livello precedente e che non siano già stati visitati
//		               e continuo fino a che non avrò più vertici da esplorare.
//		               VANTAGGI: riesco a trovare i vertici raggiungibili nel minor numero di passaggi.
//		-------> A valle della visita posso ricostruire l'albero di visita: ovvero l'albero che contiene gli archi che sono stati utilizzati per scoprire nuovi vertici. 

//		- IN PROFONDITA: mentre la visita in ampiezza non si allontana dalla sorgente fino a che non ha esplorato tutti i vertici posti a una certa distanza dalla sorgente stessa, la visita in profondità 'parte e va fino in fondo fino a che può' e se trova vicoli chiusi torna indietro. 
//		  				 Parto da un vertice V e mi trovo i vertici a lui adiacenti, se sono già visitati non fa nulla, se non sono stati visitati li aggiungo e proseguo in profondità a cercare gli altri.
//						 SVANTAGGI: raggiunge sempre lo stesso di numeru di vertici raggiungibili trovato con l'altro metodo ma il numero di percorsi di visita è sicuramente diverso (alcuni percorsi non saranno presenti) infatti avrò solo i primi trovati e non tutti.
	
	
	
	public Map<Fermata, Fermata> visitaGrafo(Fermata partenza) {
//		provo a visitare il grafo usando l'iteratore in ampiezza.
		GraphIterator<Fermata, DefaultEdge>  visita = new BreadthFirstIterator<Fermata, DefaultEdge>(this.grafo, partenza);
		
		Map<Fermata, Fermata> alberoInverso = new HashMap<Fermata, Fermata>(); //la chiave è la stazione figlis, il valore sarà la stazione padre
		alberoInverso.put(partenza, null); // imposto la radice dell'albero
		
		visita.addTraversalListener(new RegistraAlberoDiVisita(alberoInverso, this.grafo));
		
		while (visita.hasNext()) {
			Fermata f = visita.next();
//			System.out.println(f);
			
//			tali vertici me li darà in ordine di visita ma nulla mi dice dove finisce un livello e dove ne comincia un altro.
						
			}
		
		return alberoInverso;
		
		}

//		---------------------------------------------------------
		
//		Ora l'evoluzione del programma prevede di agganciare questa esplorazione a un interfaccia utende dove è quindi possibile decidere una stazione di partenza e una di arrivo
//		e da ciò calcolare il/i percorsi possibili
		
		
		
}
	
	
	
