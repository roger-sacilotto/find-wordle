package org.example;

import java.util.List;
import java.util.Set;

public class Permutation {
    public static void getPermutations(String str, String[] potentialBadPos, Set<String> results )
    {
        if (str.isEmpty()) {
            results.add("*****");
            return;
        }

        String ans = "";
        getPermutations(str, potentialBadPos, ans, results);
    }

    // Function to print all the permutations of str
    protected static void getPermutations(String str, String[] potentialBadPos, String ans, Set<String> results )
    {
       // If string is empty, permutation has been computed
        if (str.length() == 0) {
            results.add(ans);
            return;
        }

        for (int i = 0; i < str.length(); i++) {

            // ith character of str
            char ch = str.charAt(i);

            // Rest of the string after excluding
            // the ith character
            String ros = str.substring(0, i) +
                    str.substring(i + 1);

            // Recursive call
            getPermutations(ros, potentialBadPos,ans + ch, results);
        }
    }

    private static boolean allowPermutation(String candidate, String[] badPositions) {

        for (int i = 0; i < candidate.length(); i++) {
            String c = candidate.substring(i, i+1);
            if (! c.equals("*")) {
                for (String bp : badPositions) {
                    String bpc = bp.toUpperCase().substring(i, i+1);
                    if (c.equals(bpc))
                        return false;
                }
            }
        }
        return true;
    }

    protected static String getPattern(String known, String potential) {
        String result = potential;
        int numUnknown = 5;
        for (int i = 0; i< known.length(); i++) {
            if (! known.substring(i, i+1).equals("*"))
                numUnknown--;
        }
        int neededStars = numUnknown - potential.length();
        for (int i = 0; i < neededStars; i++) {
            result = result + "*";
        }
        return result;
    }

    protected static String[] getQueryLetters(List<String> knownList, String potential, String[] potentialBadPos) throws Exception {
        String queryLetters[] = knownList.toArray(new String[0]);
        int nextUnknown = 0;
        for (int i = 0; i < 5; i++) {
            if (queryLetters[i].equals("*")) {
                queryLetters[i] = potential.substring(nextUnknown, nextUnknown + 1);
                nextUnknown++;
            }
        }
        if (! allowPermutation(stringArrayToString(queryLetters), potentialBadPos))
            throw new Exception("permutation not allowed");

        return queryLetters;

    }

    private static String stringArrayToString(String[] input) {
        StringBuilder resultSB = new StringBuilder();
        for (String s : input)
            resultSB.append(s);
        return resultSB.toString();
    }
}
