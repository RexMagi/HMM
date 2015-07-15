package distributions;



import java.util.ArrayList;

public class Observation {
	/* 
	 * the symbols used to describe the emission probabilities stored as doubles to maintain  
	 * generality with continuous pdf's
	 */
	@Override
	public String toString() {
		return "Observation [data=" + data + "]";
	}

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

	ArrayList<Double> data;//an array list  of symbols that describe a single observation 

	public Observation(ArrayList<Double> data) {	
		this.data = data;
	}

	public Observation(char x) {
		if(data == null)
			data = new ArrayList<Double>();
		data.add(new Double((int)x));
	}

	public Observation(int i) {
		if(data == null)
			data = new ArrayList<Double>();
		data.add(new Double(i));
	}

	public Observation(java.util.Date date) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the data
	 */
	public ArrayList<Double> getData() {
		return data;
	}
	/**
	 * @return the symbol at a specific index 
	 * 
	 * */
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
