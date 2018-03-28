package pl.dbtool.models;

public class DBJoinedColumn {

	private Object joinedColumnValue;
	private Object element;
	
	public Object getJoinedColumnValue() {
		return joinedColumnValue;
	}
	public void setJoinedColumnValue(Object joinedColumnValue) {
		this.joinedColumnValue = joinedColumnValue;
	}
	public Object getElement() {
		return element;
	}
	public void setElement(Object element) {
		this.element = element;
	}
}
