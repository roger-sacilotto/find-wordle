package org.example;

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordlePerms {
    public static void main(String[] args) {
        Set<String> results = new HashSet<>();

        List<String> knownList = FindWordle.stringToList(Parameters.known.toUpperCase());
        System.out.println("prohibited letters: " + Parameters.impossible.toUpperCase() + "\n");
        Permutation.getPermutations(Permutation.getPattern(Parameters.known.toUpperCase(), Parameters.potential.toUpperCase()), Parameters.potentialBadPos, results);
        for (String candidatePermutation: results) {
            //System.out.println(s);
            try {
                String[] queryLetters = Permutation.getQueryLetters(knownList, candidatePermutation, Parameters.potentialBadPos);
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
