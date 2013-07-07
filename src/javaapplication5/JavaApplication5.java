package javaapplication5;

import javaapplication5.HuffmanDS;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class JavaApplication5 {
	/**
	 * Encapsulates a three (possibly different) data elements
	 * @author Ari Trachtenberg
	 *
	 * @param <t1>
	 * @param <t2>
	 * @param <t3>
	 */
	public static class Triple <t1, t2, t3> {
		Triple(t1 aa, t2 bb, t3 cc) { item1=aa; item2=bb; item3=cc;}
		public t1 item1;
		public t2 item2;
		public t3 item3;
	};

	public static void main(String args[]) {
		//try {
			//Triple<Long,Double,Boolean> result = testUrl("http://algorithmics.bu.edu/twiki/bin/view/EC504/WebHome");
			//System.out.println("Encoding time: "+(result.item1/1000.0)+" seconds");
			//System.out.println("Compression ratio:  "+ (result.item2 * 100.0)+"%");
                        System.out.println("\100");
			//if (result.item3)
			//	System.out.println("Decoding test passed!");
			//else
			//	System.out.println("Decoding test FAILED!!!");
			
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	}

	/**
	 * Performs a test of encoding and decoding a given URL
	 * @param inURL The URL to test
	 * @return An encapsulation of:
	 * (i) The amount of time needed to decoded
	 * (ii) The compression ratio
	 * (iii) Whether the decoding test passed (true iff passed)
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Triple<Long, Double, Boolean> testUrl(String inURL) throws IOException, ClassNotFoundException {
		// constants
		final String plainFile = "plain.txt";           // the plain text of the URL input
		final String encodedFile = "encoded.txt";       // the file where the encoding of the URL is put
		final String decodedFile = "decoded.txt";       // the file where the decoding of the encodedFile is put

		// variables
		long initTime, endTime;     // used to time the encoding process
		long origSize, encodedSize; // used to compare the unencoded and encoded input sizes
		File theFile;               // used for File queries

		URL uu = new URL(inURL);

		// 0. Read the URL into a file and record the file length
		// ... based on http://www.exampledepot.com/egs/java.io/CopyFile.html
		InputStream plainStream = uu.openStream();
		OutputStream out = new FileOutputStream(plainFile);
		byte[] buf = new byte[1024];
		int len;
		while ((len = plainStream.read(buf)) > 0) {
			out.write(buf,0,len);
		}
		plainStream.close();
		out.close();
		// ... record the file name
		theFile = new File(plainFile);
		origSize = theFile.length();

		// 1. (Re-)read [from the file] and encode the input URL
		BufferedReader in = new BufferedReader( new InputStreamReader(new FileInputStream(plainFile)));
		initTime = System.currentTimeMillis();
		HuffmanDS coded = new NotSoTrivialDS();
		coded.encode(in);
		endTime = System.currentTimeMillis();

		// 2. Write the coded object to a file
		ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(encodedFile));
		outStream.writeObject(coded);
		outStream.close();
		// ... get the size of the file
		theFile = new File(encodedFile);
		encodedSize = theFile.length();

		// 3. Read the coded object from the file and decode it (should provide the same as the input URL)
		ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(encodedFile));
		HuffmanDS decoded = (HuffmanDS) inStream.readObject();
		PrintStream outDecode = new PrintStream(new FileOutputStream(decodedFile));
		outDecode.println(decoded.decode());

		// 4. Perform check and output results
		/*
		 * System.out.println("Encoding time: "+((endTime - initTime)/1000.0)+" seconds");
		System.out.println("Compression ratio:  "+ ((double) origSize / encodedSize * 100.0)+"%");
		*/
		// ... check that the original and the decoded(encoded(original)) are the same
		if (identical(plainFile, decodedFile))
			return new Triple<Long,Double,Boolean>(endTime-initTime,(double) origSize / encodedSize, true);
		else
			return new Triple<Long,Double,Boolean>(endTime-initTime,(double) origSize / encodedSize, false);
	}

	/**
	 * @param file1 one of the files to compare
	 * @param file2 one of the files to compare
	 * @return true iff the contents of file1 and file2 are identically the same,
	 *          without consideration of termination characters ('\n','\r' are deleted)
	 * @throws FileNotFoundException 
	 */
	private static boolean identical(String file1, String file2) throws FileNotFoundException {
		// ... also based on http://www.exampledepot.com/egs/java.io/CopyFile.html
		String sf1, sf2;
		try {
			sf1 = readFile(file1); sf1 = sf1.replaceAll("[\n\r]","");
			sf2 = readFile(file2); sf2 = sf2.replaceAll("[\n\r]","");
		}
		catch (IOException e) {return false;} // any exceptions are treated as a lack of a match

		return sf1.compareTo(sf2)==0;

	}

	/**
	 * Reads a file into a string
	 * @param file1 the file to read
	 * @return The string representing the file
	 * @throws IOException
	 */
	private static String readFile(String file1) throws IOException {
		StringBuffer data = new StringBuffer(1024);
		BufferedReader rd = new BufferedReader(new FileReader(file1));
		char[] buf = new char[1024];

		int len;
		while ((len = rd.read(buf)) != -1)
			data.append(buf, 0, len);

		rd.close();
		return data.toString();
	}
}
