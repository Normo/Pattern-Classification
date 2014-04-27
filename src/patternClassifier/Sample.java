package patternClassifier;

public class Sample {

	private int dimension;
	private int classID;
	double[] values;
	private int valCount;
	private int clusterLabel;
	private boolean clusterLabelFlag = false;

	public Sample(int dim) {
		this.dimension = dim;
		this.values = new double[this.dimension];
		this.valCount = 0;
	}
	
	public Sample(double[] dArray) {
		this.values = dArray;
		this.dimension = dArray.length;
		this.valCount = this.dimension;
	}
	
	public void addValue(double val) {
		if (this.valCount < this.dimension) {
			this.values[this.valCount] = val;
			this.valCount++;
		} else {
			System.out.println("Dimension erreicht!");
		}
	}
	
	/**
	 * Erzeugt einen neuen Merkmalsvektor bestimmter Dimension und initialisiert
	 * die Merkmalswerte mit Null.
	 * @param dim Dimension des neuen Vektors
	 * @return Null-Merkmalsvektor
	 */
	public static Sample initializeSample(int dim) {
		Sample newSample = new Sample(dim);
		newSample.setClassID(0);
		
		for (int i = 0; i < dim; i++) {
			newSample.addValue(0.0);
		}
		
		return newSample;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (double value : values) {
			sb.append(value + "\t");
		}
		
		sb.append(this.classID);
		
		return sb.toString();
	}
	
	public int getClassID() {
		return classID;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public int getValCount() {
		return valCount;
	}

	public void setValCount(int valCount) {
		this.valCount = valCount;
	}

	public int getClusterLabel() {
		return clusterLabel;
	}

	public void setClusterLabel(int clusterLabel) {
		this.clusterLabel = clusterLabel;
		this.clusterLabelFlag = this.clusterLabelFlag? false : true;
		
	}
	
}
