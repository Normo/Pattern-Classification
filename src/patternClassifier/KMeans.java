package patternClassifier;

import java.util.HashMap;
import java.util.Random;

import patternClassifier.model.distance.DistanceMeasure;
import patternClassifier.model.main.*;
import patternClassifier.util.SampleFileWriter;

public class KMeans {
	
	private SampleList originList;
	private SampleList clusteredList;
	private int clusterCount;
	private DistanceMeasure distanceMeasure;
	
	public KMeans(SampleList samples, int k, DistanceMeasure distMethod) {
		this.originList = samples;
		this.clusterCount = k;
		this.distanceMeasure = distMethod;
	}
	
	public SampleList computeCluster() {
		
		System.out.println("\nStarte kMeans-Algorithmus...");
		System.out.println("Wähle " + this.clusterCount + " zufällige Cluster-Zentren pro Klasse der Trainingsstichprobe...");
		
		this.clusteredList = new SampleList();
		this.clusteredList.setClassificated(this.originList.isClassificated());
		this.clusteredList.setDimension(this.originList.getDimension());
		boolean changed;
		int maxIter = 20;	// Limit of Iterations

		for (int classID : this.originList.getClassIDs()) {
			
			SampleList classList = this.originList.getSamplesOfClassID(classID);
			int classListSize = classList.getSize();
			SampleList centroids = new SampleList(this.clusterCount);
			
			// wähle k zufällige Samples als initiale Cluster-Zentren
			int randIndex;
			HashMap<Object, Object> usedIndeces = new HashMap<>(this.clusterCount);
			

			for (int i = 0; i < this.clusterCount; i++) {
				
				randIndex = this.getRandomIndex(0, classListSize-1);
				
				while (usedIndeces.containsKey(randIndex)) {
					randIndex = this.getRandomIndex(0, classListSize-1);
				}
//				System.out.println("Zufälliger Index '" + randIndex + "' ergibt Cluster-Zentrum " + classList.getSample(randIndex));
				centroids.addSample(classList.getSample(randIndex));
				usedIndeces.put(randIndex, 0);
			}
			
//			System.out.println("Initiale Centroids sind also:\n" + centroids);
			
			// Weise die restlichen Samples den jeweiligen Centroids zu,
			// indem das clusterLabel im Sample gesetzt wird
			double minDistance;
			double currentDistance;
			Sample currentCentroid;
			int clusterLabel = 0;
			
			for (Sample sample : classList.samples) {
				minDistance  = Double.MAX_VALUE;
				clusterLabel = 0;
				for (int i = 0; i < this.clusterCount; i++) {
					currentCentroid = centroids.getSample(i);
					//System.out.println("Berechne Distanz zwischen Sample " + sample + " und dem Centroid " + currentCentroid);
					currentDistance = this.distanceMeasure.getDistance(sample, currentCentroid);
					if (currentDistance < minDistance) {
						minDistance = currentDistance;
						clusterLabel = i;
					}
				}
				//System.out.println("Clusterlabel " + clusterLabel + " zugewiesen!");
				sample.setClusterLabel(clusterLabel);
			}
			
//			for (int i = 0; i < this.clusterCount; i++) {
//				System.out.println("Clusterliste zum Label " + i + ":\n" + classList.getSamplesOfClusterLabel(i));
//			}
			
			// Berechne die Centroids neu
			int iter = 0;
			
			SampleList clusterLabelList;
			Sample updatedCentroid;
			

			
			do {
				changed = false;
				
//				System.out.println("Old Centroids:\n" + centroids);
				for (int i = 0; i < this.clusterCount; i++) {
					clusterLabelList = classList.getSamplesOfClusterLabel(i);
					updatedCentroid = this.getMeanSample(clusterLabelList);
					updatedCentroid.setClassID(classID);
//					System.out.println("Updated Centroid: " + updatedCentroid);
					centroids.setSample(i, updatedCentroid);
				}
//				System.out.println("New Centroids:\n" +centroids);

				for (Sample sample : classList.samples) {
					clusterLabel = sample.getClusterLabel();
					minDistance  = Double.MAX_VALUE;
					for (int i = 0; i < this.clusterCount; i++) {
						currentCentroid = centroids.getSample(i);
//						System.out.println("Berechne Distanz zwischen Sample " + sample + " und dem Centroid " + currentCentroid);
						currentDistance = this.distanceMeasure.getDistance(sample, currentCentroid);
						if (currentDistance < minDistance) {
							minDistance = currentDistance;
							clusterLabel = i;
						}

					}
					if (clusterLabel != sample.getClusterLabel()) {
							changed = true;
							sample.setClusterLabel(clusterLabel);
					}
//					System.out.println("Clusterlabel " + clusterLabel + " zugewiesen!");
				}
				
				
				
				iter++;
//				System.out.println(iter + ". Iterarion");
			} while (changed && iter <= maxIter);
			
			this.clusteredList.addSampleList(centroids);	
		}
		
		System.out.println("Trainingsstichprobe von " + this.originList.getSize() + " auf " + this.clusteredList.getSize() + " Datenpunkte reduziert.");
		SampleFileWriter fileWriter = new SampleFileWriter("trainsample_reduced.out", this.clusteredList);
		fileWriter.writeSampleFile();
		
		return clusteredList;
	}
	
	public int getRandomIndex(int start, int end) {
		Random r = new Random();
		return r.nextInt(( end - start) +1 ) + start;
	}
	
	/**
	 * Berechnet den durchschnittlichen Mekrmalsvektor aus einer Liste
	 * verschiedener Mekrmalsvektoren mit dem arithmetischen Mittel.
	 * @param sList Liste mit Merkmalsvektoren
	 * @return Durchschnittsvektor der Liste
	 */
	public Sample getMeanSample(SampleList sList) {
		//System.out.println("Start getMeanSample()...");
		
		int dim = sList.getDimension();
		int listSize = sList.getSize();
		Sample meanSample = Sample.initializeSample(dim);
		
		//System.out.println("SList:\n" + sList);
		
		for (Sample sample : sList.samples) {
			for (int i = 0; i < dim; i++) {
				meanSample.values[i] += sample.values[i];
			}
		}
		
		for (int i = 0; i < dim; i++) {
			meanSample.values[i] /= listSize;
		}
		//System.out.println("MeanSample:" + meanSample);
		return meanSample;
	}
	
	public void debug() {
		
		SampleList testList = new SampleList();
		testList.setDimension(2);
		testList.setClassificated(false);
		double[] dArray = {5.67, 10.89};
		
		for (int i = 0; i < 10; i++) {
			//testList.addSample(Sample.initializeSample(2));
			testList.addSample(new Sample(dArray));
		}
		
		System.out.println("MeanSample: " + this.getMeanSample(testList) + "\n");
		
		this.computeCluster();
		
	}

}
