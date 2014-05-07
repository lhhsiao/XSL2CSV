
/**
 * ReplaceNonASCIICharacters.<span id="IL_AD9" class="IL_AD">java</span>
 * @author Kushal Paudyal
 * www.sanjaal.com/java
 *
 * This class reads a file with non ASCII Characters in it.
 * Replaces the non ASCII Characters using <span id="IL_AD12" class="IL_AD">regular</span> expression.
 * Saves the content with non-ASCII Characters removed to a new file.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class ReplaceNonAsciiCharacters {

	public static void main(String args[]) {

		/**
		 * This is the input file name with some non-ASCII characters in the
		 * <span id="IL_AD8" class="IL_AD">content of</span> the file.
		 */
		String fileName = "C:/Temp/WithNonASCIICharacters.txt";
		/**
		 * This is the location of the <span id="IL_AD7"
		 * class="IL_AD">output</span> file - the content of this file will be
		 * the input file content minus the non-ASCII characters.
		 */
		String outputFileName = "C:/Temp/WithNonASCIICharactersRemoved.txt";

		try {

			/**
			 * Create a reader to read the input file
			 */
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line = "";

			String formattedStr = "";
			int count = 0;
			/**
			 * Iterate through each line of content remove any non-ASCII
			 * characters with blank using regular expression.
			 * 
			 * Append the new line character properly.
			 */
			while ((line = in.readLine()) != null) {
				if (count == 0)
					formattedStr += line.replaceAll("[^\\p{ASCII}]", "");
				else
					formattedStr += "\n" + line.replaceAll("[^\\p{ASCII}]", "");

				count++;
			}

			/**
			 * Write the content to the output file using BufferedWriter object.
			 */
			BufferedWriter out = new BufferedWriter(new FileWriter(
					outputFileName));
			out.write(formattedStr);

			/**
			 * Once done, flush the writer and close it.
			 */
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * SANJAAL CORPS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
	 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT
	 * NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
	 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SANJAAL CORPS SHALL NOT BE
	 * LIABLE FOR ANY <span id="IL_AD5" class="IL_AD">DAMAGES</span> SUFFERED BY
	 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR
	 * ITS DERIVATIVES.
	 * 
	 * THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
	 * CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE <span
	 * id="IL_AD6" class="IL_AD">PERFORMANCE</span>, SUCH AS IN THE OPERATION OF
	 * NUCLEAR FACILITIES, AIRCRAFT NAVIGATION OR COMMUNICATION SYSTEMS, AIR
	 * TRAFFIC CONTROL, DIRECT LIFE SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN
	 * WHICH THE FAILURE OF THE SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL
	 * INJURY, OR SEVERE PHYSICAL OR ENVIRONMENTAL <span id="IL_AD4"
	 * class="IL_AD">DAMAGE</span> ("HIGH RISK ACTIVITIES"). SANJAAL CORPS
	 * SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
	 * HIGH RISK ACTIVITIES.
	 */
}