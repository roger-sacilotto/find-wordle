package org.example;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

public class WordInjector {
    public static void addWordToGraph(String word, String tag, int i, int wait) {
        String letter1 = word.substring(0,1);
        String letter2 = word.substring(1,2);
        String letter3 = word.substring(2,3);
        String letter4 = word.substring(3,4);
        String letter5 = word.substring(4,5);
        StringBuilder querySB = new StringBuilder();
        querySB.append(" {\"statements\": [ { \"statement\": \"");
        querySB.append(String.format("MATCH (l1:%s {value: \\\"%s\\\"}) ", tag, letter1));
        querySB.append(String.format("MATCH (l2:%s {value: \\\"%s\\\"}) ", tag, letter2));
        querySB.append(String.format("MATCH (l3:%s {value: \\\"%s\\\"}) ", tag, letter3));
        querySB.append(String.format("MATCH (l4:%s {value: \\\"%s\\\"}) ", tag, letter4));
        querySB.append(String.format("MATCH (l5:%s {value: \\\"%s\\\"}) ", tag, letter5));
        querySB.append(String.format("MERGE (l1)-[:HAS_SECOND {word: %1d}]->(l2)", i)).append(String.format("MERGE (l2)-[:HAS_THIRD {word: %1d}]->(l3)", i))
                .append(String.format("MERGE (l3)-[:HAS_FOURTH {word: %1d}]->(l4)", i)).append(String.format("MERGE (l4)-[:HAS_FIFTH {word: %1d}]->(l5)", i));
        querySB.append(String.format("MERGE (:%s_WORD {id: \\\"%s\\\", word: %d}) ", tag, word, i));
        querySB.append("\"}]}");
        if (!tag.equals(Constants.SOLUTION_LETTER_TAG))
            System.out.println("adding " + word);
        //System.out.println(querySB.toString());
        try {
            Neo4j.cypherTx(querySB.toString());
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            System.out.println(String.format("Error injecting word %s/%d: %s", word, i, e.getMessage()));
            e.printStackTrace();
        }
    }

    public static void addLettersToGraph(String tag) {
        String letters = "ABCDEFGHIJKLMNNOPQRSTUVWXYZ";
        StringBuilder querySB = new StringBuilder();
        querySB.append(" {\"statements\": [ { \"statement\": \"");
        for (int i = 0; i < letters.length(); i++) {
            String letter = letters.substring(i, i+1);
            querySB.append(String.format("MERGE (:%s {value: \\\"%s\\\"}) ", tag, letter));
        }
        querySB.append("\"}]}");
        try {
            //System.out.println(querySB.toString());
            JsonNode body = Neo4j.cypherTx(querySB.toString());
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            System.out.println(String.format("Error injecting letters: %s", e.getMessage()));
            e.printStackTrace();
        }
    }
}
