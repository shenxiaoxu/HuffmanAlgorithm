package javaapplication5;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class NotSoTrivialDS extends HuffmanDS {
	/**
	 * Implements a "trivial" encoding
	 */

	private static final long serialVersionUID = 1L;

	// constructor
	public NotSoTrivialDS() {
		tr = new HashMap<String,String>();
	}

	@Override
	void encode(BufferedReader in) {
		String inputString="";

		// 1. read the data stream into a String, keeping track of character frequencies
		HashMap<String,Integer> freq = new HashMap<String,Integer>(); // maps characters seen to their frequencies

		String str;
		try {
			while ((str = in.readLine()) != null) {
				inputString += str + "\n";     // record the string

				// go through all pairs of characters in the String
				for (int ii=0; ii<str.length(); ii+=2) {
					String substr;
					try {
						substr = str.substring(ii, ii+2);
					} catch (IndexOutOfBoundsException e)	// i.e. the string only had one character left
						{substr = "" + str.charAt(ii);}
					if (freq.get(substr)==null) // i.e. first occurrence of this character?
						freq.put(substr, 1);
					else                    // otherwise, increment frequency
						freq.put(substr, freq.get(substr)+1 );

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 3. The table will map the most common character-pairs to '\1', next most common to "\0\1", then "\0\0\1", ..., thereby making a prefix-free encoding list
		// ... Loosely based on reference http://www.coderanch.com/t/382750/Java-General/java/Sorting-HashMap-by-values
		ArrayList<Map.Entry<String, Integer>> sorted = new ArrayList<Map.Entry<String, Integer>> ( freq.entrySet());
		Collections.sort( sorted , new Comparator<Map.Entry<String,Integer>>() {
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				return -o1.getValue().compareTo(o2.getValue()); // reverse order of frequency
			}
		});

		Iterator<Map.Entry<String, Integer>> substrSeen = sorted.iterator();
		int count=0; // counts how many characters we have seen thusfar
		while (substrSeen.hasNext()) {
			Map.Entry<String, Integer> theEntry = substrSeen.next();

			// set up the output character
			String result = "";
			for (int ii=0; ii<count; ii++)
				result+='\0';
			result+='\1';
			count++; // increment for the next char
			tr.put(theEntry.getKey(), result); // record the translation
		}
		
		// 4. Encode the string with the translation table
		// ... this is done with a trick - put the input into the coded string and then "decode"
		coded = inputString;
		coded = print();
		}
}
