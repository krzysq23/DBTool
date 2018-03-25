package pl.dbtool.models;

public class DBParametr {

	/**
	 * Metoda przechowuje parametry do zapytania w klauzurze WHERE
	 * @param String fieldName
	 * @param String operator
	 * @param Object value
	 */
	public DBParametr(String fieldName, String operator, Object value) {
		super();
		this.fieldName = fieldName;
		this.operator = operator;
		this.value = value;
	}
	
	/**
	 * Nazwa parametru : String
	 */
	private String fieldName;
	/**
	 * Operator : String
	 */
	private String operator;
	/**
	 * Wartoœæ : Object
	 */
	private Object value;
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}
