package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class InsertHerringWords {
    public static void main( String[] args ) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode rootNode = (ObjectNode) mapper.readTree(new File("./resources/wordle-herrings.json"));
            ArrayNode words = (ArrayNode) rootNode.findValue("words");
            System.out.println("num words: " + words.size());
            WordInjector.addLettersToGraph(Constants.HERRING_LETTER_TAG);
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i).asText().toUpperCase();
                WordInjector.addWordToGraph(word, Constants.HERRING_LETTER_TAG, 5000 + i, 100);
                if (i > 0 && i % 20 == 0)
                    System.out.println(String.format("added %1d of %1d herring words", i+1, words.size()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Hello World!");
    }
}
