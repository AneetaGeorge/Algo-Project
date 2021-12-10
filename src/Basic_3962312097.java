import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.*;


public class Basic_3962312097 {

    private static Map<String, Integer> mismatchCost = new HashMap<>();
    private static String inputString1, inputString2;
    private static int gapPenalty = 30;
    private static final String OUTPUT_FILENAME = "Output.txt";

    public static void main(String[] args){
        setMismatchCost();

        String fileName = args[0];
        fileReader(fileName);

        List<String> alignment = findMinCostAlignment(inputString1, inputString2, gapPenalty , mismatchCost);

        String alignmentString1 = alignment.get(0);
        String alignmentString2 = alignment.get(1);

        System.out.println(alignmentString1.substring(0,50) + " " + alignmentString1.substring(alignmentString1.length() - 50));
        System.out.println(alignmentString2.substring(0,50) + " " + alignmentString2.substring(alignmentString2.length() - 50));


    }

    private static void setMismatchCost(){
        String[] genes = {"AA", "CC", "GG", "TT", "AC","AG","AT","CG","CT","GT"};
        Integer[] gap = {0, 0, 0, 0, 110, 48, 94, 118, 48, 110};
            for(int i =0; i < genes.length; i++)
            {
                mismatchCost.put(genes[i], gap[i]);
                mismatchCost.put(new StringBuilder(genes[i]).reverse().toString(), gap[i]);
            }

    }

    private static String inputGenerator(String input, int index)
    {
        String firstHalf = input.substring(0, index+1);
        String secondHalf = input.substring(index+1, input.length());

        return firstHalf + input + secondHalf;
    }

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

        System.out.println("Minimum cost of alignment is : " + OPT[string1.length()][string2.length()]);
        return findAlignment(OPT, string1, string2, gapPenalty, mismatchCost);

    }

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

        writeAlignmentToFile(alignmentString1, alignmentString2, OUTPUT_FILENAME);

        return alignment;
    }

    private static void writeAlignmentToFile(String alignStr1, String alignStr2, String filename)
    {
        String line1 = alignStr1.substring(0,50) + " " + alignStr2.substring(0,50);
        String line2 = alignStr1.substring(alignStr1.length() - 50) + " " + alignStr2.substring(alignStr2.length() - 50);

        List<String> lines = Arrays.asList(line1, line2);
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