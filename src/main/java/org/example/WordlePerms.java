package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.example.FindWordle.stringToList;

public class WordlePerms {
    public static void main(String[] args) {
        Set<String> results = new HashSet<>();

        List<String> impossibleList = new ArrayList<>();
        List<String> knownList = stringToList(Parameters.known.toUpperCase());
        System.out.println("prohibited letters: " + Parameters.impossible.toUpperCase() + "\n");
        Permutation.getPermutations(Permutation.getPattern(Parameters.known.toUpperCase(), Parameters.potential.toUpperCase()), Parameters.potentialBadPos, results);
        for (String candidatePermutation: results) {
            //System.out.println(s);
            try {
                String[] queryLetters = Permutation.getQueryLetters(knownList, candidatePermutation, Parameters.potentialBadPos);
                Set<String> queryWords = FindWordle.executeQuery(queryLetters, impossibleList, Parameters.potentialBadPos, true);
                if (!Parameters.permQuery  || queryWords.size() > 0)
                    System.out.println(String.format("%s%s%s%s%s", queryLetters[0],
                            queryLetters[1],
                            queryLetters[2],
                            queryLetters[3],
                            queryLetters[4]));
            } catch (Exception e) {

            }
        }
    }
}
