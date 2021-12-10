package testFiles;

import java.io.*;
import java.util.*;

public class efficientSoln{
    private static Map<String, Integer> mismatchCost;
    private static String string1, string2;
    private static int gapPenalty;

    private static void setMismatchCost(){
        mismatchCost = new HashMap<>();
        String[] genes = {"AA", "CC", "GG", "TT", "AC","AG","AT","CG","CT","GT"};
        Integer[] gap = {0, 0, 0, 0, 110, 48, 94, 118, 48, 110};
        gapPenalty = 30;
        for(int i =0; i < genes.length; i++)
        {
            mismatchCost.put(genes[i], gap[i]);
            mismatchCost.put(new StringBuilder(genes[i]).reverse().toString(), gap[i]);
        }
    }

    public static void main(String[] args){

        long memUsedBefore = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();
        setMismatchCost();
        String fileName = "/Users/adityanarsinghpura/Desktop/USC/Algo Mine/assignment/BaseTestcases_CS570FinalProject_Updated/input2.txt";
        fileReader(fileName);
        List<String> alignmentList = divideAndConquer(string1, string2);
        String alignment1 = alignmentList.get(0);
        String alignment2 = alignmentList.get(1);

        System.out.println(alignment1.substring(0,50) + " " + alignment1.substring(alignment1.length() - 50));
        System.out.println(alignment2.substring(0,50) + " " + alignment2.substring(alignment2.length() - 50));

        int minCost = 0;
        for(int i = 0; i < alignment1.length(); i++)
        {
            if(alignment1.charAt(i) == '_' || alignment2.charAt(i) == '_')
                minCost += 30;
            else
                minCost += mismatchCost.get(alignment1.charAt(i) + "" + alignment2.charAt(i));
        }

        System.out.println(minCost);
        long endTime = System.currentTimeMillis();
        System.out.println("takes " + (endTime - startTime)*(Math.pow(10,-3)) + "s");
        long memUsedAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory used by the program " + (memUsedAfter-memUsedBefore));
    }

    /**
     I dont know what to write!!
     @param index
     @param input
     @return The merged list of the soln?
     */
    private static List<String> findMinCostAlignment(String string1, String string2, int gapPenalty, Map<String, Integer> mismatchCost)
    {
        int[][] OPT = new int[string1.length() + 1][string2.length() + 1];
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
        return findAlignment(OPT, string1, string2, gapPenalty, mismatchCost);
    }

    /**
     I dont know what to write!!
     @param index
     @param input
     @return The merged list of the soln?
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
        List<String> finalAlignment = new ArrayList<>();
        finalAlignment.add(alignmentString1);
        finalAlignment.add(alignmentString2);
        return finalAlignment;
    }

    /**
     I dont know what to write!!
     @param index
     @param input
     @return The merged list of the soln?
     */
    private static String inputGenerator(String input, int index)
    {
        String firstHalf = input.substring(0, index+1);
        String secondHalf = input.substring(index+1);
        return firstHalf + input + secondHalf;
    }

    /**
     I dont know what to write!!
     @param list1
     @param list2
     @return The merged list of the soln?
     */
    public static List<String> mergeList(List<String> list1, List<String> list2)
    {
        List<String> mergedList = new ArrayList<>();
        mergedList.add(list1.get((0)) + list2.get(0));
        mergedList.add(list1.get((1)) + list2.get(1));
        return mergedList;
    }

    /**
     This function finds an efficient position for the second sequence comparing it with the first
     sequence and returns the last row of the computation.
     This is done efficiently to reduce the space complexity by just using 2 rows and re-initializing them
     with the calculated value.
     @param str1 DNA sequence - 1
     @param str2 DNA sequence - 2
     @return the last row of the OPT
     */
    public static Integer[] efficientDP(String str1, String str2)
    {
        Integer[][] OPT = new Integer[2][str2.length()+1];
        for(int i = 0; i < str2.length() + 1; i++)
        {
            OPT[0][i] = i * gapPenalty;
        }
        OPT[1][0] = 30;
        for(int i = 1; i <= str1.length(); i++)
        {
            for(int j = 1; j <= str2.length(); j++)
            {
                if(str1.charAt(i-1) != str2.charAt(j-1))
                {
                    OPT[1][j] = Math.min(OPT[0][j-1] + mismatchCost.get(str1.charAt(i-1) + "" + str2.charAt(j-1)),
                            Math.min(OPT[0][j] + gapPenalty, OPT[1][j-1] + gapPenalty));
                }
                else
                    OPT[1][j] = OPT[0][j-1];
            }
            for(int k = 0; k <= str2.length(); k++)
            {
                OPT[0][k] = OPT[1][k];
            }
            if(i != str1.length())
                OPT[1][0] += gapPenalty;
        }
        return OPT[1];
    }

    /**
     * Recursively dividing the X and Y at optimal points -
     * where the X string is divided in the middle and the Y string is divided at an optimal point
     * which is found using the DP_Efficient() function.
     * The Xl and Xr are calculated by finding the minimum cost using the DP_Efficient function and
     * then running this divideAndConquer()
     * @param str1 DNA sequence - 1
     * @param str2 DNA sequence - 2
     * both these params are used to find the best matching
     * @return 2 list of strings
     */
    public static List<String> divideAndConquer(String str1, String str2)
    {
        if(str1.length() <= 2 || str2.length() <= 2)
        {
            return findMinCostAlignment(str1, str2, gapPenalty, mismatchCost);
        }
        else
        {
            int xMidPoint = str1.length()/2 % 2 == 0? str1.length()/2 : str1.length()/2 + 1;
            String xRightReverse = new StringBuilder(str1.substring(xMidPoint)).reverse().toString();
            String yReverse = new StringBuilder(str2).reverse().toString();
            Integer[] Xl = efficientDP(str1.substring(0, xMidPoint), str2);
            Integer[] Xr = efficientDP(xRightReverse, yReverse);
            int ySplitPoint = -1, currMinCost = Integer.MAX_VALUE;
            List<Integer> list = new ArrayList<>();
            for(int j = Xr.length-1; j >= 0; j-- )
            {
                list.add(Xr[j]);
            }
            Xr = list.toArray(new Integer[list.size()]);
            int cost1, cost2;
            for(int k = 0; k < Xl.length; k++)
            {
                cost1 = Xl[k];
                cost2 = Xr[k];
                if(cost1 + cost2 < currMinCost)
                {
                    ySplitPoint = k;
                    currMinCost = cost1 + cost2;
                }
            }
            List<String> list1 = divideAndConquer(str1.substring(0, xMidPoint), str2.substring(0,ySplitPoint));
            List<String> list2 = divideAndConquer(str1.substring(xMidPoint), str2.substring(ySplitPoint));
            return mergeList(list1, list2);
        }
    }

    /**
     Create two different strings appended again and again at the location specified after the
     string in the file.
     @param fileName  the name of the file to read from
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
                if(Character.isAlphabetic(data.charAt((0)))) {
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
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}