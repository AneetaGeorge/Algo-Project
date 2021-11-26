import java.io.*;
import java.util.*;


public class Basic_3962312097 {
    private static Map<String, Integer> mismatchCost;
    private static String string1, string2;

    public Basic_3962312097(){
        mismatchCost = new HashMap<>();
        String[] genes = {"AA", "CC", "GG", "TT", "AC","AG","AT","CG","CT","GT"};
        Integer[] gap = {0, 0, 0, 0, 110, 48, 94, 118, 48, 110};
            for(int i =0; i < genes.length; i++)
            {
                mismatchCost.put(genes[i], gap[i]);
                mismatchCost.put(new StringBuilder(genes[i]).reverse().toString(), gap[i]);
            }

    }
    public static void main(String[] args){

        int gapPenalty = 30;
        Basic_3962312097 nI = new Basic_3962312097();
        String fileName = args[0];
        fileReader(fileName);

        int[][] OPT = findMinCostAlignment(string1, string2, gapPenalty , mismatchCost);
        findAlignment(OPT, string1, string2, gapPenalty, mismatchCost);
    }

    private static String inputGenerator(String input, int index)
    {
        String firstHalf = input.substring(0, index+1);
        String secondHalf = input.substring(index+1, input.length());

        return firstHalf + input + secondHalf;
    }

    private static int[][] findMinCostAlignment(String string1, String string2, int gapPenalty, Map<String, Integer> mismatchCost)
    {
        int OPT[][] = new int[string1.length() + 1][string2.length() + 1];

        for(int i = 0; i <= string1.length(); i++)
            OPT[0][i] = i * gapPenalty;

        for(int j = 0; j <= string2.length(); j++)
            OPT[j][0] = j * gapPenalty;

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
        return OPT;

    }

    private static void findAlignment(int[][] OPT, String string1, String string2, int gapPenalty, Map<String, Integer> mismatchCost)
    {
        int m = string1.length(), n = string2.length();
        String s1 = "", s2 = "";

        while(m > 0 && n > 0)
        {
            if(OPT[m][n] == OPT[m-1][n-1] + mismatchCost.get(String.valueOf(string1.charAt(m-1)) + String.valueOf(string2.charAt(n-1))))
            {
                s1 = string1.charAt(m-1) + s1;
                s2 = string2.charAt(n-1) + s2;

                m--;
                n--;
            }
            else if(OPT[m][n] == OPT[m-1][n] + gapPenalty)
            {
                s1 = string1.charAt(m-1) + s1;
                s2 = "_" + s2;

                m--;
            }
            else
            {
                s1 = "_" + s1;
                s2 = string2.charAt(n-1) + s2;
                n--;
            }
        }

        while(m > 0)
        {
            s1 = string1.charAt(m-1) + s1;
            s2 = "_" + s2;
            m--;
        }

        while(n > 0)
        {
            s1 = "_" + s1;
            s2 = string2.charAt(n-1) + s2;
            n--;
        }

        System.out.println(s1.substring(0,50) + " " + s2.substring(0,50));
        System.out.println(s1.substring(s1.length() - 50) + " " + s2.substring(s2.length() - 50));

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
                        string1 = tempString;

                    tempString = data;
                }
                else
                {
                    tempString = inputGenerator(tempString, Integer.parseInt(data));
                }
            }

            string2 = tempString;
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}