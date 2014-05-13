package examples.loocv;

import patternClassifier.KNNClassifier;
import patternClassifier.model.distance.CityBlockDistance;
import patternClassifier.model.distance.DistanceMeasure;
import patternClassifier.model.distance.EuclidDistance;
import patternClassifier.model.distance.MaximumNorm;
import patternClassifier.model.main.SampleList;
import patternClassifier.util.SampleFileReader;
import patternClassifier.util.SampleFileWriter;

public class LoocvMain {

	public static void main(String[] args) {

		if (args.length > 0) {
			String sampleFile = args[0];
			String distMeasureString = args[1];
			int kNNParameter = Integer.parseInt(args[2]);
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

				SampleFileReader sampleFileReader = new SampleFileReader(sampleFile);

				SampleList trainSampleList = sampleFileReader.getSampleList();
				SampleList testSampleList = sampleFileReader.getSampleList();
				
				KNNClassifier kNNClassifier = new KNNClassifier(trainSampleList, testSampleList, kNNParameter, distMeasureMethod);
				kNNClassifier.loocvClassify();
				
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
