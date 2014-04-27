package patternClassifier;


public class Main {

	public static void main(String[] args) {
		
		if (args.length > 0) {
			String trainFile = args[0];
			String testFile = args[1];
			String distMeasureString = args[2];
			int kNNParameter = Integer.parseInt(args[3]);
			int kMeansParameter = Integer.parseInt(args[4]);
			DistanceMeasure distMeasureMethod;
			
			if (kNNParameter >= 1) {
				if (distMeasureString.equals("cityblock")) {
					System.out.println("Verwende als Abstandsmaß City-Block-Abstand.");
					distMeasureMethod = new CityBlockDistance();
				} else if (distMeasureString.equals("euklid")) {
					System.out.println("Verwende als Abstandsmaß euklidischen Abstand.");
					distMeasureMethod = new EuclidDistance();
				} else if (distMeasureString.equals("maximumnorm")) {
					System.out.println("Verwende als Abstandsmaß Maximum-Norm.");
					distMeasureMethod = new MaximumNorm();
				} else {
					System.out.println("Ungültige Abstandsmaß! Verwende als Abstandsmaß City-Block-Abstand.");
					distMeasureMethod = new CityBlockDistance();
				}

				SampleFileReader trainFileReader = new SampleFileReader(trainFile);
				SampleFileReader testFileReader = new SampleFileReader(testFile);
				
				SampleList trainSampleList = trainFileReader.getSampleList();
				SampleList testSampleList = testFileReader.getSampleList();
				
				KMeans kMeans = new KMeans(trainSampleList, kMeansParameter, distMeasureMethod);
				SampleList clusteredTrainSample = kMeans.computeCluster();
				
				KNNClassifier kNNClassifier = new KNNClassifier(clusteredTrainSample, testSampleList, kNNParameter, distMeasureMethod);
				kNNClassifier.classify();
				
				SampleFileWriter fileWriter = new SampleFileWriter("test_"+ distMeasureString + "_k" + kNNParameter +".out", kNNClassifier.getTestResult());
				fileWriter.writeSampleFile();

				System.out.println("Beende kNNClassifer.");
			} else {
				System.out.println("Parameter k muss mindestens 1 sein!\nBeende nKKClassifier.");
			}
		} else {
			System.out.println("Es wurden keine Parameter übergeben!\nBeende Classifier.");
		}
		
		
		
		
	}

}
