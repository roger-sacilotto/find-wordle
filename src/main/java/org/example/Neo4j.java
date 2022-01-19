package org.example;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Neo4j {
    public static JsonNode cypherTx(String query) throws UnirestException {

        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:7474/db/neo4j/tx/commit").header("Content-Type", "application/json")
                .header("Accept", "application/json;charset=UTF-8").header("Authorization", "Basic bmVvNGo6QXZpZDEyMw==")
                .body(query).asJson();
        JsonNode body = jsonResponse.getBody();
        return body;
   }
}
