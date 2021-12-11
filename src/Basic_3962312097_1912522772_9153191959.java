import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.*;


public class Basic_3962312097_1912522772_9153191959 {

    private static Map<String, Integer> mismatchCost = new HashMap<>();
    private static String inputString1, inputString2;
    private static int gapPenalty = 30;
    private static final String OUTPUT_FILENAME = "output.txt";
    private static double minAlignCost;

    public static void main(String[] args){

        long memUsedBefore = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();

        setMismatchCost();

        String fileName = args[0];
        fileReader(fileName);

        List<String> alignment = findMinCostAlignment(inputString1, inputString2, gapPenalty , mismatchCost);

        String alignmentString1 = alignment.get(0);
        String alignmentString2 = alignment.get(1);

        long endTime = System.currentTimeMillis();
        long memUsedAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        writeAlignmentToFile(alignmentString1, alignmentString2, minAlignCost, (memUsedAfter-memUsedBefore) / 1024,
                (endTime - startTime)*(Math.pow(10,-3)), OUTPUT_FILENAME );

    }

    /**
     This function initializes the hashmap 'mismatchCost' consisting of penalties for replacing a character 'i' with character 'j'
     */
    private static void setMismatchCost(){
        String[] genes = {"AA", "CC", "GG", "TT", "AC","AG","AT","CG","CT","GT"};
        Integer[] gap = {0, 0, 0, 0, 110, 48, 94, 118, 48, 110};
            for(int i =0; i < genes.length; i++)
            {
                mismatchCost.put(genes[i], gap[i]);
                mismatchCost.put(new StringBuilder(genes[i]).reverse().toString(), gap[i]);
            }

    }

    /**
     This function generates the input string from the base string and index provided in input.txt
     @param input : The base string
     @param index : The index where the string needs to be added again
     @return The input string to be used for alignment
     */
    private static String inputGenerator(String input, int index)
    {
        String firstHalf = input.substring(0, index+1);
        String secondHalf = input.substring(index+1, input.length());

        return firstHalf + input + secondHalf;
    }

    /**
     Create two different strings appended again and again at the location specified after the
     string in the file.
     @param fileName : the name of the file to read from
     @throws FileNotFoundException if the file is not found
     */
    private static void fileReader(String fileName){
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            String tempString = "";

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data = data.trim();

                if(Character.isAlphabetic(data.charAt((0))))
                {
                    if(!tempString.equals(""))
                        inputString1 = tempString;

                    tempString = data;
                }
                else
                {
                    tempString = inputGenerator(tempString, Integer.parseInt(data));
                }
            }

            inputString2 = tempString;
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    /**
     This function fills the OPT array using the recurrence to find the minimum cost alignment and invokes findAlignment
     to return the actual alignment
     @param string1 : 1st input string
     @param string2 : 2nd input string
     @param gapPenalty : the penalty for adding a gap ("_")
     @param mismatchCost : a hashmap consisting of penalties for replacing a character 'i' with character 'j'
     @return The alignment is returned as a list of 2 strings
     */
    private static List<String> findMinCostAlignment(String string1, String string2, int gapPenalty, Map<String, Integer> mismatchCost)
    {
        int OPT[][] = new int[string1.length() + 1][string2.length() + 1];

        for(int i = 0; i <= string1.length(); i++)
            OPT[i][0] = i * gapPenalty;

        for(int j = 0; j <= string2.length(); j++)
            OPT[0][j] = j * gapPenalty;

        for(int j = 1; j <= string2.length();j++)
        {
            for(int i = 1; i <= string1.length(); i++)
            {
                String mapKey = (string1.charAt(i-1) + "" + string2.charAt(j-1));
                OPT[i][j] = Math.min(OPT[i-1][j-1] + mismatchCost.get(mapKey), OPT[i-1][j] + gapPenalty);
                OPT[i][j] = Math.min(OPT[i][j], OPT[i][j-1] + gapPenalty);
            }
        }

        minAlignCost = OPT[string1.length()][string2.length()];
        return findAlignment(OPT, string1, string2, gapPenalty, mismatchCost);

    }

    /**
     This function finds the actual alignment of the strings by backtracking the OPT array values
     @param OPT : the OPT array consisting of cost of alignments
     @param string1 : 1st input string
     @param string2 : 2nd input string
     @param gapPenalty : the penalty for adding a gap ("_")
     @param mismatchCost : a hashmap consisting of penalties for replacing a character 'i' with character 'j'
     @return The alignment is returned as a list of 2 strings
     */
    private static List<String> findAlignment(int[][] OPT, String string1, String string2, int gapPenalty, Map<String, Integer> mismatchCost)
    {
        int m = string1.length(), n = string2.length();
        String alignmentString1 = "", alignmentString2 = "";

        while(m > 0 && n > 0)
        {
            if(OPT[m][n] == OPT[m-1][n-1] + mismatchCost.get(String.valueOf(string1.charAt(m-1)) + String.valueOf(string2.charAt(n-1))))
            {
                alignmentString1 = string1.charAt(m-1) + alignmentString1;
                alignmentString2 = string2.charAt(n-1) + alignmentString2;

                m--;
                n--;
            }
            else if(OPT[m][n] == OPT[m-1][n] + gapPenalty)
            {
                alignmentString1 = string1.charAt(m-1) + alignmentString1;
                alignmentString2 = "_" + alignmentString2;

                m--;
            }
            else
            {
                alignmentString1 = "_" + alignmentString1;
                alignmentString2 = string2.charAt(n-1) + alignmentString2;
                n--;
            }
        }

        while(m > 0)
        {
            alignmentString1 = string1.charAt(m-1) + alignmentString1;
            alignmentString2 = "_" + alignmentString2;
            m--;
        }

        while(n > 0)
        {
            alignmentString1 = "_" + alignmentString1;
            alignmentString2 = string2.charAt(n-1) + alignmentString2;
            n--;
        }

        List<String> alignment = new ArrayList<>();
        alignment.add(alignmentString1);
        alignment.add(alignmentString2);

        return alignment;
    }

    /**
     This function writes the alignment (first 50 & last 50 characters), the alignment cost, the time & memory taken by
     the program to "Output.txt" file
     @param alignStr1 : alignment of the 1st string
     @param alignStr2 : alignment of the 2nd string
     @param alignCost : minimum cost of alignment between the strings
     @param memory : total Memory used by the program
     @param execTime : total time taken by the program
     @param filename : output filename
     @throws FileNotFoundException if the file is not found
     */
    private static void writeAlignmentToFile(String alignStr1, String alignStr2, double alignCost, double memory, double execTime, String filename)
    {
        String line1 = alignStr1.substring(0,50) + " " + alignStr2.substring(0,50);
        String line2 = alignStr1.substring(alignStr1.length() - 50) + " " + alignStr2.substring(alignStr2.length() - 50);

        List<String> lines = Arrays.asList(line1, line2, String.valueOf(alignCost), String.valueOf(execTime), String.valueOf(memory));
        Path file = Paths.get(filename);
        try
        {
            Files.write(file, lines, StandardCharsets.UTF_8);
        }
        catch(Exception e)
        {
            System.out.println("Exception occurred while writing to File : " + e.toString());
        }

    }

}