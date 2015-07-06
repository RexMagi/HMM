package distributions;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Observation {
/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Observation other = (Observation) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		return true;
	}

ArrayList<Double> data;

public Observation(ArrayList<Double> data) {	
	this.data = data;
}

public Observation(String x) {
	data = new ArrayList<Double>();
	String temp = "";
	while(x.indexOf('.')!=-1){
	temp+=x.substring(0,x.indexOf('.'));	
	x=x.substring(x.indexOf('.')+1);	
		
	}
	temp+=x;
	data.add(new Double(Double.valueOf("."+temp)));
}

public Observation(int i) {
	data = new ArrayList<Double>();
	data.add(new Double(i));
}

/**
 * @return the data
 */
public ArrayList<Double> getData() {
	return data;
}
public Double getData(int x) {
	return data.get(x);
}

/**
 * @param data the data to set
 */
public void setData(ArrayList<Double> data) {
	this.data = data;
}

}
