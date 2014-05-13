package patternClassifier.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import patternClassifier.model.main.Sample;
import patternClassifier.model.main.SampleList;

public class SampleFileWriter {

	String fileName;
	SampleList testResultSample;
	
	public SampleFileWriter(String path, SampleList list) {
		
		this.fileName = path;
		this.testResultSample = list;
		
	}
	
	public void writeSampleFile() {
		
		File outputFile = new File(fileName);
		
		BufferedWriter out = null;
		
		try {
			out = new BufferedWriter(new FileWriter(outputFile));
			
			out.write("# header\n");
			out.write(this.testResultSample.getSize()+"\n");
			out.write(this.testResultSample.getDimension()+"\n");
			out.write(1+"\n");
			out.write("# data points\n");
			
			for (Sample sample : testResultSample.samples) {
				out.write(sample.toString()+"\n");
			}
			
			System.out.println("\nErgebnis in Datei " +fileName+ " geschrieben!\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	
}
