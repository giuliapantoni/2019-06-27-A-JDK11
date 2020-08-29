package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getReati(){
		String sql = "SELECT DISTINCT e.offense_category_id AS reato " + 
				"FROM EVENTS e " +
				"ORDER BY reato ASC " ;
		List<String> reati = new ArrayList<String>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				reati.add(res.getString("reato"));
			}
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return reati;
	}
	
	
	public List<Integer> getAnni(){
		String sql = "SELECT DISTINCT year(e.reported_date) AS anno " + 
				"FROM EVENTS e " + 
				"ORDER BY anno ASC " ;
		List<Integer> anni = new ArrayList<Integer>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				anni.add(res.getInt("anno"));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return anni;
	}
	
	public List<String> getVertici(String reato, Integer anno){
		String sql = "SELECT distinct e.offense_type_id as otid " + 
				"FROM EVENTS e " + 
				"WHERE e.offense_category_id = ? " + 
				"AND YEAR(e.reported_date) = ? " ;
		List<String> vertici = new ArrayList<String>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, reato);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				vertici.add(res.getString("otid"));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return vertici;
	}
	
	public List<Adiacenza> getArchi(String reato, Integer anno){
		String sql = "SELECT e1.offense_type_id AS id1, e2.offense_type_id AS id2, COUNT(DISTINCT e1.district_id) AS peso " + 
				"FROM EVENTS e1, EVENTS e2 " + 
				"WHERE e1.offense_type_id != e2.offense_type_id " + 
				"AND e1.district_id = e2.district_id " + 
				"AND e1.offense_category_id = ? " + 
				"AND e2.offense_category_id = ? " + 
				"AND YEAR(e1.reported_date) = ? " + 
				"AND YEAR(e2.reported_date) = ? " + 
				"GROUP BY id1, id2 " ;
		List<Adiacenza> adiacenze = new ArrayList<Adiacenza>() ;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, reato);
			st.setString(2, reato);
			st.setInt(3, anno);
			st.setInt(4, anno);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				adiacenze.add(new Adiacenza(res.getString("id1"), res.getString("id2"), res.getInt("peso")));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return adiacenze;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
