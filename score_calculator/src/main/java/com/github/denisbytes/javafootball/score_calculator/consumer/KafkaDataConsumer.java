package com.github.denisbytes.javafootball.score_calculator.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.denisbytes.javafootball.score_calculator.proto.MatchProto;
import com.github.denisbytes.javafootball.score_calculator.store.Store;
import com.github.denisbytes.javafootball.score_calculator.util.Util;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KafkaDataConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaDataConsumer.class);
    private static final String GROUP_ID = "java-football-consumer-group";
    private static final String TOPIC_NAME = "MatchHighlightsData";
    private ObjectMapper objectMapper = new ObjectMapper();
    private Store store = Store.getStore();
    List<MatchProto.Match> matches = store.getMatches();


    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID)
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String jsonString = record.value();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            String matchID = jsonNode.get("matchID").asText();
            String[] teams = matchID.split("-");
            String team1 = new String();
            String team2 = new String();

            if (teams.length == 2) {
                team1 = teams[0];
                team2 = teams[1];
            } else {
                logger.error("Invalid matchID format: {}", matchID);
            }

            MatchProto.Match existingMatch = Util.findMatch(matchID, matches);
            if (existingMatch == null) {
                MatchProto.Match.Builder newMatchBuilder = MatchProto.Match.newBuilder()
                        .setMatchID(matchID)
                        .setTeam1(team1)
                        .setTeam2(team2)
                        .setTeam1Goals(0)
                        .setTeam2Goals(0);
                List<String> comments = Util.extractComments(jsonNode);
                newMatchBuilder.addAllComments(comments);
                System.out.println(comments);
                matches.add(newMatchBuilder.build());
            } else {
                MatchProto.Match.Builder existingMatchBuilder = existingMatch.toBuilder();
                Util.updateGoalsCount(existingMatchBuilder, jsonNode);
                matches.set(matches.indexOf(existingMatch), existingMatchBuilder.build());
            }

            System.out.println("MY MATCH  -----------");
            System.out.println(matches.get(0).getTeam1Goals()+" "+ matches.get(0).getTeam2Goals()+" "+ matches.size());
            System.out.println("---------------------");

            logger.info("Received message for match: {}", matchID);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON message: {}", e.getMessage());
        }
    }
}
