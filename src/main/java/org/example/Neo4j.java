package org.example;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Base64;

public class Neo4j {
    public static JsonNode cypherTx(String query) throws UnirestException {
        String auth = Base64.getEncoder().encodeToString(String.format("%s:%s", Constants.NEO4J_USER, Constants.NEO4J_PASSWORD).getBytes());
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:7474/db/neo4j/tx/commit").header("Content-Type", "application/json")
                .header("Accept", "application/json;charset=UTF-8").header("Authorization",  String.format("Basic %s", auth))
                .body(query).asJson();
        JsonNode body = jsonResponse.getBody();
        return body;
   }
}
