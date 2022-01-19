package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class InsertSolutionWords
{
    public static void main( String[] args )
    {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode rootNode = (ObjectNode) mapper.readTree(new File("./resources/wordle-words.json"));
            ArrayNode words = (ArrayNode) rootNode.findValue("words");
            System.out.println("num words: " + words.size());
            WordInjector.addLettersToGraph(Constants.SOLUTION_LETTER_TAG);
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i).asText().toUpperCase();
                WordInjector.addWordToGraph(word, Constants.SOLUTION_LETTER_TAG, i, 500);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println( "Hello World!" );
    }

}
