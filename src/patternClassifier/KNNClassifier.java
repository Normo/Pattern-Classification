package patternClassifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import patternClassifier.model.distance.DistanceMeasure;
import patternClassifier.model.main.*;

public class KNNClassifier {
	
	private SampleList trainingSample;
	private SampleList testSample;
	private SampleList mainSample;
	private SampleList testResult;
	private int kParameter; 
	private DistanceMeasure distanceMeasure;
	int failClassify=0;
	
	public KNNClassifier(SampleList train, SampleList test, int k, DistanceMeasure distanceMethod) {
		this.setTrainingSample(train);
		this.setTestSample(test);
		this.setMainSample(this.trainingSample);
		this.testResult = new SampleList(test.getSize());
		this.testResult.setDimension(testSample.getDimension());
		this.kParameter = k;
		this.distanceMeasure = distanceMethod;
	}
	
	/**
	 * Konstruktor für leave one out cross validation (LOOCV)
	 * @param test Testdatensatz
	 * @param k	Parameter k des nächste-Nachbar-klassifikators
	 * @param distanceMethod Abstandsmaß
	 */
	public KNNClassifier(SampleList test, int k, DistanceMeasure distanceMethod) {
		this.setTrainingSample(test);
		this.setTestSample(test);
		this.setMainSample(this.trainingSample);
//		this.trainingSample = new SampleList();
//		this.trainingSample.addSampleList(test);
//		this.testSample = new SampleList();
//		this.testSample.addSampleList(test);
//		this.setMainSample(this.trainingSample);
		this.testResult = new SampleList(test.getSize());
		this.testResult.setDimension(testSample.getDimension());
		this.kParameter = k;
		this.distanceMeasure = distanceMethod;
	}
	
	public double classify() {
		
		System.out.println("\nKlassifiziere...");
		
		System.out.println("Bereits klassifizierte Objekte (Trainingsstichprobe): " + mainSample.getSize());
		
		int size;
		double distance = 0;
//		double[] dists;
		SampleResult tmpResult;
		List<SampleResult> resultList;
//		DistanceMeasure dM = new DistanceMeasure(); //kann weg!!
		int counter = 0;
		
		for (Sample sample : this.testSample.samples) {
			counter++;
			size = mainSample.getSize();
//			dists = new double[size];
			resultList = new ArrayList<>(kParameter);
			
			for (int i = 0; i < size; i++) {
				tmpResult = new SampleResult();
				distance = this.distanceMeasure.getDistance(sample, mainSample.getSample(i));
				
				// folgender Block kann weg!!
//				if (distMeasure.equals("cityblock")) {
//					distance = dM.cityBlockDistance(sample, mainSample.getSample(i));
////					System.out.println(distance);
//				} else if (distMeasure.equals("euklid")) {
//					distance = dM.euclidDistance(sample, mainSample.getSample(i));
////					System.out.println(distance);
//				} else if (distMeasure.equals("maximumnorm")) {
//					distance = dM.maximumNorm(sample, mainSample.getSample(i));
//				} else {
//					distance = dM.cityBlockDistance(sample, mainSample.getSample(i));
//				}
				
//				dists[i] = distance;
				tmpResult.dist = distance;
				tmpResult.classID = mainSample.getSample(i).getClassID();
				tmpResult.index = i;
				resultList = this.addToResultList(resultList, tmpResult);
			}
			
//			Arrays.sort(dists);
//			System.out.println("\nSample Nr "+counter+": (size="+resultList.size()+")\n");
//			for (int i = 0; i < kParameter; i++) {
//				System.out.println("dists: " + dists[i]);
//				System.out.println("resultList: " + resultList.get(i).dist);
//			}
			
			int newClass = this.evaluateResultList(resultList);
			//System.out.println("Classified to class " + newClass);
			if (sample.getClassID() != newClass) {
				sample.setClassID(newClass);
				failClassify++;
				}
			testResult.addSample(sample);
			//mainSample.addSample(sample);
		}
		
		//System.out.println(testResult);
		double rate = ((double)counter-(double)failClassify)/(double)counter;
		System.out.println("Insgesamt " + counter +" Test-Samples wurden klassifiziert.");
		System.out.println(failClassify+ " von " + counter + " wurden falsch klassifiziert. Klassifikationsrate = " + rate);
		//System.out.println("Klassifizierte Objekte ingesamt: " + (mainSample.getSize()+counter)+"\n");
		
		return rate;
	}

	public double loocvClassify() {
		System.out.println("\nKlassifiziere...");

//		System.out.println("Bereits klassifizierte Objekte (Trainingsstichprobe): " + mainSample.getSize());

		int size;
		double distance = 0;
		//		double[] dists;
		SampleResult tmpResult;
		List<SampleResult> resultList;
		
		int counter = 0;
		int loocvIndex = 0;
		// falls sich Klasse ändert, wird neues Sampleobjekt erstellt und der Ergebnisliste hinzugefügt
		Sample resultSample;
		
		for (Sample sample : this.testSample.samples) {
		
			counter++;
			size = mainSample.getSize();
			//dists = new double[size];
			resultList = new ArrayList<>(kParameter);

			for (int i = 0; i < size; i++) {
				if(i == loocvIndex) {
					continue;
				}
				tmpResult = new SampleResult();
				
				distance = this.distanceMeasure.getDistance(sample, mainSample.getSample(i));
				
				tmpResult.dist = distance;
				tmpResult.classID = mainSample.getSample(i).getClassID();
				tmpResult.index = i;
				
				resultList = this.addToResultList(resultList, tmpResult);
			}

			//			Arrays.sort(dists);
			//			System.out.println("\nSample Nr "+counter+": (size="+resultList.size()+")\n");
			//			for (int i = 0; i < kParameter; i++) {
			//				System.out.println("dists: " + dists[i]);
			//				System.out.println("resultList: " + resultList.get(i).dist);
			//			}

			int newClass = this.evaluateResultList(resultList);
			//System.out.println("Classified to class " + newClass);
			if (sample.getClassID() != newClass) {
				resultSample = new Sample(sample.values);
//				System.out.println("#################################################");
//				System.out.println("Class " + sample.getClassID() + " to " + newClass);
				resultSample.setClassID(newClass);
				resultSample.setClusterLabel(sample.getClusterLabel());
				failClassify++;
			} else {
				resultSample = sample;
			}
			testResult.addSample(resultSample);
			//mainSample.addSample(sample);
			loocvIndex++;
		}

		//System.out.println(testResult);
		double rate = ((double)counter-(double)failClassify)/(double)counter;
		System.out.println("Insgesamt " + counter +" Test-Samples wurden klassifiziert.");
		System.out.println(failClassify+ " von " + counter + " wurden falsch klassifiziert. Klassifikationsrate = " + rate);
//		System.out.println("Klassifizierte Objekte ingesamt: " + (mainSample.getSize()+counter)+"\n");

		return rate;	
	}
	
	private int evaluateResultList(List<SampleResult> resultList) {
//		System.out.println("Best of " + this.kParameter + ":");
//		for (int i = 0; i < this.kParameter; i++) {
//			System.out.print(" "+resultList.get(i).classID);
//		}
//		
//		System.out.println();
//		
		HashMap<Object, Object> hash = new HashMap<>(kParameter);
		int key;
		int value = 0;
		int max = 0;
		int leadingClassID = -1;
		
//		for (SampleResult sampleResult : resultList) {
		for (int i = 0; i < this.kParameter; i++) {
			SampleResult sampleResult = resultList.get(i);
			key = sampleResult.classID;
			
			if (!hash.containsKey(key)) {
				value = 1;
			} else {
				value = (int)hash.get(key);
				value++;
			}
			hash.put(key, value);
			if (value > max) {
				max = value;
				leadingClassID = key;
			}
		}
		
//		System.out.println("Hash: " + hash);
		
		if (max < (kParameter/2)) {
			leadingClassID = -1;
		}

		return leadingClassID;
	}

	private List<SampleResult> addToResultList(List<SampleResult> resultList,	SampleResult tmpResult) {

		int index = 0;
		int size = resultList.size();

		if(size == 0) {
			resultList.add(tmpResult);
		} else {
			while (index < size && (index+1) <= this.kParameter && tmpResult.dist > resultList.get(index).dist) {
				index++;
			}

			if (index < this.kParameter) {
				resultList.add(index, tmpResult);
			}
		}
		
		
		// kann gelöscht, wenn keine Fehler mehr bei der Klassifizierung auftreten!!!
//		if (resultList.size() == 0) {
//			resultList.add(tmpResult);
//		} else {
//			int listSize = resultList.size();
//			
//			for (int i = 0; i < Math.min(listSize, kParameter); i++) {
//				if (listSize < kParameter) {
//					if (tmpResult.dist <= resultList.get(i).dist) {
//						resultList.add(i, tmpResult);
//						break;
//					} else if (i+1 == listSize) {
//						resultList.add(tmpResult);
//						break;
//					}
//				} else if (tmpResult.dist <= resultList.get(i).dist) {
//					resultList.add(i, tmpResult);
//					resultList.remove(kParameter);
//					break;
//				} else {
//					continue;
//				}
//			}
//		}
		
		
		return resultList;
	}

	public SampleList getTrainingSample() {
		return trainingSample;
	}

	public void setTrainingSample(SampleList trainingSample) {
		this.trainingSample = trainingSample;
	}

	public SampleList getTestSample() {
		return testSample;
	}

	public void setTestSample(SampleList testSample) {
		this.testSample = testSample;
	}

	public int getkParameter() {
		return kParameter;
	}

	public void setkParameter(int kParameter) {
		this.kParameter = kParameter;
	}

	public SampleList getMainSample() {
		return mainSample;
	}

	public void setMainSample(SampleList mainSample) {
		this.mainSample = mainSample;
	}	
	
	public SampleList getTestResult() {
		return testResult;
	}

}
