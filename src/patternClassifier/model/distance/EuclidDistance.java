package patternClassifier.model.distance;

import patternClassifier.model.main.Sample;

/**
 * Berechnet die Distanz zwischen zwei Mustervektoren auf Basis des euklidischen
 * Abstands.
 * @author normo
 *
 */
public class EuclidDistance implements DistanceMeasure {

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
			dist += Math.pow((sample1.values[i] - sample2.values[i]),2);
		}
		
		dist = Math.sqrt(dist);
		return dist;
	}
}
