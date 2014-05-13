package patternClassifier.model.distance;

import patternClassifier.model.main.Sample;

/**
 * Schnittstelle für die verschiedenen Abstandsmaße.
 * @author normo
 *
 */
public interface DistanceMeasure {

	/**
	 * Berechnet das Abstandsmaß zweier Mustervektoren
	 * @param sample1 Mustervektor
	 * @param sample2 Mustervektor
	 * @return Abstand als double-Wert
	 */
	public double getDistance(Sample sample1, Sample sample2);
	
}
