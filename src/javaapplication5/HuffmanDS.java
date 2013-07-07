package javaapplication5;

import java.io.BufferedReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// a Huffman encoded data structure

public abstract class HuffmanDS implements Serializable {

	// METHODS

	/**
	 * Sets up the HuffmanDS data structure from a given input stream
	 * @param dis The input stream to encode
	 * @requires Must set maxLen appropriately
	 */
	abstract void encode(BufferedReader in);

	/**
	 * Computes the maximum length of a key in the translation hashMap
	 * @param translation The translation hashMap for which to compute the maximum key length
	 * return the maximum key length
	 */
	protected int computeMaxLen(HashMap<String, String> translation) {
		int theMaxLen=0;

		Iterator<String> keys = translation.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			if (theMaxLen < key.length())
				theMaxLen = key.length();
		}
		return theMaxLen;
	}

	/**
	 * @return a human-readable version of the unencoded version of the text stored in the data structure
	 */
	public String print() {
		return print(tr);
	}

	/**
	 * Decodes the coded string stored in the data structure, assuming that it had been encoded
	 * with the translation table "translation"
	 * @param tr The translation table that is assumed to have been used to encode the text
	 * @return The decoded string
	 */
	public String print(HashMap<String, String> translation) {
		// go through the string trying to match text
		String decoded="";

		int theMaxLen = computeMaxLen(translation);
		for (int ii=0; ii<coded.length(); ii++) {                  // check all prefixes of the current string, starting at location ii
			boolean foundMatch=false;
			for (int subii=1; subii<=theMaxLen && ii+subii<=coded.length(); subii++) {
				String to = translation.get(coded.substring(ii, ii+subii));
				foundMatch = false; // true only if a match is found for the sequence
				if (to!=null) {
					// i.e., I found a match
					decoded+=to;
					ii+=subii - 1; // it will be incremented again in the outer for loop
					
					foundMatch = true;
					break;         // out of the inner loop
				}
			}
			if (!foundMatch) {     // if no match is found, simply output the character itself
				decoded+=coded.charAt(ii);
				
			}
		}

		return decoded;
	}

/**
 * 
 * @return A decoded version of the coded input stored in the data structure
 */
	public String decode() {
		HashMap<String, String> decodingTr = switchKeysValues(tr);
		return (print(decodingTr));
		}
	
	/**
	 * @return A hashMap whose keys and values are switched.  In other words, if orig[key]=value, then
	 * for the result, result[value]=key
	 * @requires The original hashMap must be a one-to-one mapping.  Otherwise, the results are undefined.
	 */
	private HashMap<String, String> switchKeysValues(HashMap<String, String> orig) {
		Iterator< Map.Entry<String,String> > entries = orig.entrySet().iterator();
		HashMap< String, String> result = new HashMap< String, String>();

		while (entries.hasNext()) {
			Map.Entry<String,String> entry = entries.next();
			result.put(entry.getValue(), entry.getKey());
		}

		return result;
	}


	// FIELDS
	private static final long serialVersionUID = 1L;
	protected HashMap<String,String> tr; // the translation table for the Huffman encoding
	protected String coded;          // the coded text
}
