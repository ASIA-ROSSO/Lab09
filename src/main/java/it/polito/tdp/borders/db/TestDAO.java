package it.polito.tdp.borders.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Border;

public class TestDAO {

	public static void main(String[] args) {

		BordersDAO dao = new BordersDAO();

		System.out.println("Lista di tutte le nazioni:");
		List<Country> countries = dao.loadAllCountries();
		
		Map<Integer, Country> countryIdMap = new HashMap<Integer, Country>();
		for(Country c : countries) {
			countryIdMap.put(c.getId(), c);
		}
		
		System.out.println("\nLista di tutte le nazioni prima del 2000:");
		List<Country> countries2 = dao.getCountry(countryIdMap, 2000);
		for(Country c : countries2) {
			System.out.format("%d %s %s\n", c.getId(), c.getStateAbb(), c.getStateNme());
		}
		
		
	}
}
