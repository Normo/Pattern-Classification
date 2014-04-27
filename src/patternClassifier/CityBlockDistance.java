package patternClassifier;


public class CityBlockDistance implements DistanceMeasure {

	@Override
	public double getDistance(Sample sample1, Sample sample2) {
		
		int dim = 0;
		double dist = 0;
		
		if (sample1.getDimension() == sample2.getDimension()) {
			dim = sample1.getDimension();
		} else {
			System.out.println("Merkmalsvektoren haben unterschiedliche Dimensionen: Sample1=" + sample1.getDimension() + ", Sample2=" + sample2.getDimension());
			return 0;
		}
		
		for (int i = 0; i < dim; i++) {
			dist += Math.abs(sample1.values[i] - sample2.values[i]);
		}
		
		return dist;
	}
}
