package patternClassifier;

import java.util.ArrayList;
import java.util.HashMap;

public class SampleList {

	ArrayList<Sample> samples;
	@Deprecated
	private int capacity;
	private int size;
	private int dimension;
	private boolean classificated;
	private HashMap<Object, Object> classID_Hash;
	private int countDifferentClassIDs;
	private int[] classIDs;
	
	public SampleList() {
		this.samples = new ArrayList<Sample>();
		this.setSize(this.samples.size());
		this.classID_Hash = new HashMap<>();
	}
	
	public SampleList(int c) {
		this.samples = new ArrayList<Sample>(c);
		this.setSize(this.samples.size());
		this.classID_Hash = new HashMap<>(this.size);
	}
	
	public void addSample(Sample s){
		
		this.samples.add(s);
		this.setSize(this.samples.size());
		
		// count different class ID's	
		int key = s.getClassID();
		int value;
		
		if (!classID_Hash.containsKey(key)) {
			value = 1;
			this.countDifferentClassIDs++;
		} else {
			value = (int)classID_Hash.get(key);
			value++;
		}
		classID_Hash.put(key, value);
		
		this.classIDs = new int[countDifferentClassIDs];
		int i = 0;
		
		for (Object k : classID_Hash.keySet()) {
			this.classIDs[i] = (int) k;
			i++;
		}
	}
	
	public void addSampleList(SampleList slist) {
		for (Sample sw : slist.samples) {
			this.addSample(sw);
		}
	}
	
	/**
	 * Liefert das Sample zu einem gegebenen Index aus der Liste zurück
	 * @param index Index des Sample das zurückgegeben werden soll
	 * @return Das Sample zum angegebenen Index
	 */
	public Sample getSample(int index) {
		return this.samples.get(index);
	}
	
	public void setSample(int index, Sample element) {
		this.samples.set(index, element);
	}
	
	public SampleList getSamplesOfClassID(int id) {
		
		SampleList sList = new SampleList((int) this.classID_Hash.get(id));
		sList.setDimension(this.dimension);
		sList.setClassificated(this.classificated);
		
		for (Sample s : this.samples) {
			if (s.getClassID() == id) {
				sList.addSample(s);
			}
		}
		
		return sList;
	}
	
	/**
	 * Gibt eine Liste mit den Samples zurück, die ein bestimmtes Clusterlabel besitzen.
	 * @param label Das Clusterlabel zu dem Samples zurückgegeben werden sollen
	 * @return Liste mit Samples zum angegebenen Clusterlabel
	 */
	public SampleList getSamplesOfClusterLabel(int label) {
		
		SampleList sList = new SampleList();
		sList.setDimension(this.dimension);
		sList.setClassificated(this.classificated);
		
		for (Sample s : this.samples) {
			if (s.getClusterLabel() == label) {
				sList.addSample(s);
			}
		}
		
		return sList;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public boolean isClassificated() {
		return classificated;
	}

	public void setClassificated(boolean classificated) {
		this.classificated = classificated;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.samples.size(); i++) {
			sb.append(this.samples.get(i) + "\n");
		}
		return sb.toString();
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public HashMap<Object, Object> getClassID_Hash() {
		return classID_Hash;
	}

	public int getCountDifferentClassIDs() {
		return countDifferentClassIDs;
	}

	public int[] getClassIDs() {
		return classIDs;
	}
}
