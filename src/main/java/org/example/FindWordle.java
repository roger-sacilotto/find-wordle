package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.*;

public class FindWordle {

    private static boolean dumpQuery = false;
            ;

    public static void main(String[] args) {
        Set<String> results = new HashSet<>();

        List<String> impossibleList = stringToList(Parameters.impossible.toUpperCase());
        List<String> knownList = stringToList(Parameters.known.toUpperCase());

        Permutation.getPermutations(Permutation.getPattern(Parameters.known.toUpperCase(), Parameters.potential.toUpperCase()), Parameters.potentialBadPos, results);
        Set<String> words = new HashSet<>();
        for (String candidatePermutation: results) {
            //System.out.println(s);
            try {
                String[] queryLetters = Permutation.getQueryLetters(knownList, candidatePermutation, Parameters.potentialBadPos);
                Set<String> queryWords = executeQuery(queryLetters, impossibleList, Parameters.potentialBadPos, Parameters.querySolutionSet);
                words.addAll(queryWords);
            } catch (Exception e) {

            }
        }
        for (String s: words) {
            System.out.println(s);
        }
    }

    protected static List<String> stringToList(String input) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            result.add(input.substring(i, i+1));
        }
        return result;
    }

    private static Set<String> stringArrayToListAtPosition(String[] input, int position) {
        Set<String> result = new HashSet<>();
        for (String s: input) {
            String sp = s.substring(position, position + 1);
            if (! sp.equals("*"))
                result.add(sp.toUpperCase());
        }
        return result;
    }


    protected static Set<String> executeQuery(String[] queryLetters, List<String> impossibleList, String[] potentialBadPos, boolean useSolutions) {
        Set<String> results = new HashSet<>();
        StringBuilder checkSB = new StringBuilder();

//        for (String l : queryLetters)
//            checkSB.append(l);
//        if (checkSB.toString().equals("*IZZ*"))
//            System.out.println(checkSB.toString());

        String tag = useSolutions ? Constants.SOLUTION_LETTER_TAG : Constants.HERRING_LETTER_TAG;
        StringBuilder querySB = new StringBuilder();
        querySB.append(" {\"statements\": [ { \"statement\": \"");
        querySB.append("with duration.inDays(date(\\\"2021-06-19\\\"), date()).days as offset ");
        for (int position = 0; position < 5; position++) {
            Set<String> ngLetters = stringArrayToListAtPosition(potentialBadPos, position);
            ngLetters.addAll(impossibleList);
            addNgToQuery(ngLetters, position, querySB);
        }
         querySB.append(String.format("match p=((l1:%s", tag));
        if (!queryLetters[0].equals("*"))
            querySB.append(String.format(" {value: \\\"%s\\\"}", queryLetters[0]));
        querySB.append(String.format(")-[r2:HAS_SECOND]->(l2:%s", tag));
        if (!queryLetters[1].equals("*"))
            querySB.append(String.format(" {value: \\\"%s\\\"}", queryLetters[1]));
        querySB.append(String.format(")-[r3:HAS_THIRD]->(l3:%s", tag));
        if (!queryLetters[2].equals("*"))
            querySB.append(String.format(" {value: \\\"%s\\\"}", queryLetters[2]));
        querySB.append(String.format(")-[r4:HAS_FOURTH]->(l4:%s", tag));
        if (!queryLetters[3].equals("*"))
            querySB.append(String.format(" {value: \\\"%s\\\"}", queryLetters[3]));
        querySB.append(String.format(")-[r5:HAS_FIFTH]->(l5:%s", tag));
        if (!queryLetters[4].equals("*"))
            querySB.append(String.format(" {value: \\\"%s\\\"}", queryLetters[4]));
        querySB.append(")) where r2.word = r3.word AND r3.word = r4.word AND r4.word = r5.word ");
        querySB.append("AND NOT apoc.coll.contains(ng1, l1.value) ");
        querySB.append("AND NOT apoc.coll.contains(ng2, l2.value) ");
        querySB.append("AND NOT apoc.coll.contains(ng3, l3.value) ");
        querySB.append("AND NOT apoc.coll.contains(ng4, l4.value) ");
        querySB.append("AND NOT apoc.coll.contains(ng5, l5.value) ");
        if (useSolutions && ! Parameters.ignoreDate)
            querySB.append("AND r2.word >= offset ");
        querySB.append("return apoc.text.join([l1.value, l2.value, l3.value, l4.value, l5.value], \\\"\\\"), r2.word");
        querySB.append("\"}]}");
        if (dumpQuery)
            System.out.println(querySB.toString());
        try {
            JsonNode body = Neo4j.cypherTx(querySB.toString());
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode resultRoot = null;
                resultRoot = (ObjectNode) mapper.readTree(body.toString());
            ArrayNode response = (ArrayNode) resultRoot.findValue("results");
            for (int i = 0; i < response.size(); i++) {
                ObjectNode result = (ObjectNode) response.get(i);
                ArrayNode data = (ArrayNode) result.findValue("data");
                for (int j = 0; j < data.size(); j++) {
                    ObjectNode datum = (ObjectNode)data.get(j);
                    ArrayNode values = (ArrayNode) datum.findValue("row");
                    if (values.size() > 0)
                        //System.out.println(values.get(0).asText());// + " " + values.get(1).asInt());
                        results.add(values.get(0).asText());
                }

            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return results;

    }

    private static void addNgToQuery(Set<String> ngLetters, int position, StringBuilder querySB) {
            querySB.append(", [");
            boolean first = true;
            for (String ng : ngLetters) {
                if (! first)
                    querySB.append(", ");
                else
                    first = false;
                querySB.append("\\\"").append(ng).append("\\\"");
            }
            querySB.append(String.format("] as ng%1d ", position+1));

    }


}
