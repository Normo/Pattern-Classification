package patternClassifier.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import patternClassifier.model.main.Sample;
import patternClassifier.model.main.SampleList;

public class SampleFileReader {
	
	private String fileName;
	private SampleList sampleList;
	
	public SampleFileReader(String strFile) {
		this.fileName = strFile;
		this.sampleList = new SampleList();
		if (this.readFile(this.fileName)) {
			//System.out.println("Sample File " + this.fileName + " eingelesen!\n");
		}
	}

	private boolean readFile(String fileName) {
		
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("Datei " + fileName + " existiert nicht!");
			return false;
		}
		
		//System.out.println("\nLese Sample-Datei " + fileName + " ein..");
		
		BufferedReader fileReader = null;
		String line = null;
		char firstChar;
		
		try {
			fileReader = new BufferedReader(new FileReader(file));
			
			do {
				
				line = this.readNewLine(fileReader);
				
				if (line != null) {
					
					if (line.isEmpty()) {
						continue;
					}
					
					firstChar = line.charAt(0);
					if(firstChar == '#') {
						if (line.equals("# header")) {
							this.evaluateHeader(fileReader);
						} else if (line.equals("# data points")) {
							this.evaluateBody(fileReader);
						}	
					}
					
				} else {
					continue;
				}
				
			} while (line != null);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}	

	// Liest neue Zeile ein
	private String readNewLine(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		if (line != null) {
			line.trim();
		} 		
		return line;
	}
	
	// Wertet den Header eines Samples aus
	private void evaluateHeader(BufferedReader reader) throws IOException {
		
		String line = new String();
		char firstChar;
		
		int sampleCount = 0;					// Anzahl der enthaltenen Mustervektoren in der Datei
		int sampleDimension = 0;				// Dimension der Mustervektoren
		boolean sampleClassifaction = false;	// Angabe, ob Mustervektoren bereits klassifiziert sind
		
		//System.out.println("Header wird eingelesen...");
		
		int headerInfoCount = 0;
		
		while (!line.equals("# data points") && line != null && headerInfoCount != 3) {
			line = this.readNewLine(reader);
			firstChar = line.charAt(0);
			if(firstChar == '#' || line.isEmpty()) {
				continue;
			} else if (headerInfoCount == 0) {
				sampleCount = Integer.parseInt(line) > 0? Integer.parseInt(line) : 0;
				headerInfoCount++;									
			} else if (headerInfoCount == 1) {
				sampleDimension = Integer.parseInt(line) > 0? Integer.parseInt(line) : 0;
				headerInfoCount++;
			} else if (headerInfoCount == 2) {
				sampleClassifaction = Integer.parseInt(line) > 0? true : false;
				headerInfoCount++;
			} 
		}
//		System.out.println("Auswertung Header:\nAnzahl Mustervektoren: " + sampleCount +"\nDimension Mustervektoren: " + sampleDimension + "\nStichprobe klassifiziert? " + sampleClassifaction);
		
		this.sampleList.setSize(sampleCount);
		this.sampleList.setDimension(sampleDimension);
		this.sampleList.setClassificated(sampleClassifaction);
	}
	
	private void evaluateBody(BufferedReader reader) throws IOException {
		String line = new String();
		char firstChar;
		String[] columns;
		Sample sample;
		
		//System.out.println("Mustervektoren werden eingelesen...");
		
		do {
			line = this.readNewLine(reader);
			
			if (line == null || line.isEmpty()) {
				continue;
			} else {
				//System.out.println("\tMustervektor: " + line);
				
				columns = line.split("\\s+");
				
				sample = new Sample(sampleList.getDimension());
				
				if (sampleList.isClassificated()) {
//					System.out.println("ClassID: "+ columns[sampleList.getDimension()]);
					sample.setClassID(Integer.parseInt(columns[sampleList.getDimension()]));
				}
				
				for (int i = 0; i < sampleList.getDimension(); i++) {
					firstChar = columns[i].charAt(0);
					
					if (firstChar >= '0' && firstChar <= '9' || firstChar == '-') {
						sample.addValue(Double.parseDouble(columns[i]));
					}
				}
//				System.out.println(sample);
				this.sampleList.addSample(sample);
			}
		} while (line != null);
	}
	
	public SampleList getSampleList() {
		return sampleList;
	}

	public void setSampleList(SampleList sampleList) {
		this.sampleList = sampleList;
	}
}
