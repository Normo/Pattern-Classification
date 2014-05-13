package patternClassifier.model.main;

/**
 * Basisklasse für einen Stichproben- bzw Mustervektor.
 * @author normo
 *
 */
public class Sample {
	/**
	 * Dimension des Vektors bzw Anzahl der Merkmale des Stichprobenwertes.
	 */
	private int dimension;
	/**
	 * Klassenzugehörigkeit
	 */
	private int classID;
	/**
	 * Die Merkmalswerte des Mustervektors.
	 */
	public double[] values;
	/**
	 * Zähler für Menge der Merkmalswerte des Vektors.
	 */
	private int valCount;
	/**
	 * Label, das angibt, zu welchem Cluster dieser Merkmalsvektor gehört.
	 */
	private int clusterLabel;
	/**
	 * Label, das angibt, ob das clusterLabel bereits gesetzt wurde.
	 */
	private boolean clusterLabelFlag = false;

	/**
	 * Standard-Konstruktor
	 * @param dim Dimension des Merkmalsvektors
	 */
	public Sample(int dim) {
		this.dimension = dim;
		this.values = new double[this.dimension];
		this.valCount = 0;
	}
	/**
	 * Double-Array-Konstruktor
	 * @param dArray Werte des neuen Merkmalsvektors
	 */
	public Sample(double[] dArray) {
		this.values = dArray.clone();
		this.dimension = dArray.length;
		this.valCount = this.dimension;
	}
	
	/**
	 * Fügt einen neuen Merkmalswert zum Vektor hinzu.
	 * @param val neuer Merkmalswert
	 */
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
	
	/**
	 * Gibt den Merkmalsvektor als String-Repräsentation zurück.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (double value : values) {
			sb.append(value + "\t");
		}
		
		sb.append(this.classID);
		
		return sb.toString();
	}
	
	/**
	 * Liefert Klassenzugehörigkeit zurück.
	 * @return classID
	 */
	public int getClassID() {
		return classID;
	}

	/**
	 * Setzt Klasse des Merkmalsvektors.
	 * @param classID neue Klasse des Vektors
	 */
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
