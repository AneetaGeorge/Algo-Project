import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class efficientSoln {
    private static Map<String, Integer> mismatchCost;
    private static String string1, string2;
    private static int gapPenalty = 30;
    private static Map<Integer, Integer> alignment = new HashMap<>();
    private static int c;
    private static int resCost;

    public efficientSoln(){

        mismatchCost = new HashMap<>();
        String[] genes = {"AA", "CC", "GG", "TT", "AC","AG","AT","CG","CT","GT"};
        Integer[] gap = {0, 0, 0, 0, 110, 48, 94, 118, 48, 110};
        c = 0;
        resCost = 0;
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

    public static void main(String[] args){

        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long start = System.currentTimeMillis();

        efficientSoln ef = new efficientSoln();
        String fileName = "/Users/adityanarsinghpura/Desktop/USC/Algo Mine/assignment/BaseTestcases_CS570FinalProject_Updated/input1.txt";
        fileReader(fileName);
        DC(string1, string2);

        System.out.println(alignment.toString());
        System.out.println("cost = "+resCost);
        long end = System.currentTimeMillis();
        System.out.println("takes " + (end - start)*(Math.pow(10,-3)) + "s");
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Memory used by the program " + (afterUsedMem-beforeUsedMem));

    }

    /*
    private static int[][] stringAlignment(String string1, String string2, int gapPenalty, Map<String, Integer> mismatchCost)
    {
        int OPT[][] = new int[string1.length()][string2.length()];

        for(int i = 0; i < string1.length(); i++)
            OPT[0][i] = i * gapPenalty;

        for(int j = 0; j < string2.length(); j++)
            OPT[j][0] = j * gapPenalty;

        for(int j = 1; j < string2.length();j++)
        {
            for(int i = 1; i < string1.length(); i++)
            {
                String mapKey = (string1.charAt(i) + "" + string2.charAt(j));
                OPT[i][j] = Math.min(OPT[i-1][j-1] + mismatchCost.get(mapKey), OPT[i-1][j] + gapPenalty);
                OPT[i][j] = Math.min(OPT[i][j], OPT[i][j-1] + gapPenalty);
            }
        }

        //System.out.println(OPT[string1.length()-1][string2.length()-1]);
        return OPT;

    }

    private static void findAlignment(int[][] OPT, String string1, String string2, int gapPenalty, Map<String, Integer> mismatchCost)
    {
        int m = string1.length() - 1, n = string2.length() - 1;
        String s1 = "", s2 = "";

        while(m > 0 && n > 0)
        {
            if(OPT[m][n] == OPT[m-1][n-1] + mismatchCost.get(String.valueOf(string1.charAt(m)) + String.valueOf(string2.charAt(n))))
            {
                s1 = string1.charAt(m) + s1;
                s2 = string2.charAt(n) + s2;

                m--;
                n--;
            }
            else if(OPT[m][n] == OPT[m-1][n] + gapPenalty)
            {
                s1 = string1.charAt(m) + s1;
                s2 = "_" + s2;

                m--;
            }
            else
            {
                s1 = "_" + s1;
                s2 = string1.charAt(n) + s2;
                n--;
            }
        }

        while(m >= 0)
        {
            s1 += string1.charAt(m) + s1;
            s2 += "_" + s2;
            m--;
        }

        while(n >= 0)
        {
            s1 += "_" + s1;
            s2 += string1.charAt(n) + s2;
            n--;
        }

//        System.out.println(s1.substring(0,50) + " " + s1.substring(s1.length() - 50));
//        System.out.println(s2.substring(0,50) + " " + s2.substring(s2.length() - 50));

    }


     */

    public static int DC(String x, String y){
        if(x.length() <= 2 || y.length() <= 2)
        {
            int[][] res = DP(x,y);
            int minRes = Integer.MAX_VALUE;
            int i = -1;
            for(int j=1; j<= y.length(); j++){
                int val = res[x.length()][j];
                if(minRes > val){
                    minRes  = val;
                    i = j;
                }
                return minRes;
            }
            /*
            int[][] OPT = stringAlignment(string1, string2, gapPenalty , mismatchCost);
            findAlignment(OPT, string1, string2, gapPenalty, mismatchCost);
            return;
             */
        }
        String xl = x.substring(0,x.length()/2);
        String xr = x.substring((x.length()/2));
        int[][] l = DP(xl,y);
        int[][] r = DP(new StringBuilder(xr).reverse().toString(),new StringBuilder(y).reverse().toString()); //
    /*   int q = 0;
        int cost = Integer.MAX_VALUE;
        for(int i=l.length -1; i>=0; i--)
        {
            if(cost >  l[i][1] + r[l.length-1-i][1])
            {
                cost = l[i][1] + r[l.length-1-i][1];
                q = i;
            }
        }
*/
        int minValue = Integer.MAX_VALUE;
        int index = -1;
        for(int j=1; j<= y.length(); j++){
            int val = l[x.length()/2][j] + r[x.length()/2][y.length() - j];
            if(minValue > val){
                minValue  = val;
                index = j;
            }
        }

        c += minValue/2;
        alignment.put(x.length()/2, index);
        System.out.println(x.substring(0, x.length()/2) +"  "+ x.substring(x.length()/2));
        System.out.println(y.substring(0,index) + "  "+ y.substring(index) +  "   "+ (index));
        resCost += DC(x.substring(0, x.length()/2), y.substring(0,index));
        resCost += DC(x.substring(x.length()/2), y.substring(index));

        return resCost;
    }

    public static int[][] DP(String x, String y) {
        int[][] OPT = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            OPT[i][0] = i * gapPenalty;
        }
        for (int j = 0; j <= y.length(); j++){
            OPT[0][j] = j * gapPenalty;
        }

        for(int j = 1; j <=y.length(); j++)
        {
            for(int i =1; i <= x.length(); i++)
            {
                String mapKey = (string1.charAt(i-1) + "" + string2.charAt(j-1));
                OPT[i][j] = Math.min(OPT[i-1][j-1] + mismatchCost.get(mapKey), OPT[i-1][j] + gapPenalty);
                OPT[i][j] = Math.min(OPT[i][j], OPT[i][j-1] + gapPenalty);
            }
        }

        int minValue = Integer.MAX_VALUE;
        int index = -1;
        for(int j=1; j<= y.length(); j++){
            if(minValue > OPT[x.length()][j]) {
                minValue  = OPT[x.length()][j];
                index = j;
            }
        }

        System.out.println("Minimum cost of alignment is : "+index);
        return OPT;
    }

    /*public int[][] backDp(String x, String y){
        int[][] OPT = new int[x.length()-1][2];

        for(int i=x.length()-1; i >= 0;i--)
        {
            OPT[i][x.length()-1]= i * gapPenalty;

        }
        for(int j = y.length()-1; j >=0 ; j--)
        {
            OPT[0][1] = j * gapPenalty;
            for(int i =x.length()-1; i >= 0 ; i--)
            {
                OPT[i][0] = Math.min(Math.min(mismatchCost.get(x.charAt(i) + "" + y.charAt(j)) + OPT[i+1][1], gapPenalty + OPT[i+1][0]), gapPenalty + OPT[i][1]);
            }
            for(int i = x.length()-1; i >= 0; i--)
            {
                OPT[i][1] = OPT[i][0];
            }
        }
        int minValue = Integer.MAX_VALUE;
        int index = -1;
        for(int i = x.length()-1; i >= 0; i--)
        {
            if(minValue > OPT[i][0])
            {
                minValue  = OPT[i][0];
                index = i;
            }
        }
        return OPT;
    }

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
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
