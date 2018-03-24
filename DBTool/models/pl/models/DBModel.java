package pl.models;

import java.util.Map;

public class DBModel {

	private String connection;
	private String table;
	private Map<String, Object> fields;
	
	public String getConnection() {
		return connection;
	}
	public void setConnection(String connection) {
		this.connection = connection;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public Map<String, Object> getFields() {
		return fields;
	}
	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}
}
