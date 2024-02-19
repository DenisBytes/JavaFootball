package com.github.denisbytes.javafootball.score_calculator.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.denisbytes.javafootball.score_calculator.consumer.KafkaDataConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Util {
    private static final Logger logger = LoggerFactory.getLogger(KafkaDataConsumer.class);

    public static MatchServiceProto.Match findMatch(String matchID, List<MatchServiceProto.Match> matches) {
        for (MatchServiceProto.Match match : matches) {
            if (match.getMatchID().equals(matchID)) {
                return match;
            }
        }
        return null;
    }

    //checking id there's goal in the comments and assigning it to the respective team
    public static void updateGoalsCount(MatchServiceProto.Match.Builder matchBuilder, JsonNode jsonNode) {
        List<String> comments = extractComments(jsonNode);

        for (String comment : comments) {
            if (comment.equals("GOAL")) {
                logger.debug("GOAL");
                String team = jsonNode.get("team").asText();
                if (team.equals(matchBuilder.getTeam1())) {
                    incrementTeam1Goals(matchBuilder);
                } else if (team.equals(matchBuilder.getTeam2())) {
                    incrementTeam2Goals(matchBuilder);
                } else {
                    logger.error("Invalid team name: {} ", team);
                }
            }
            else{
                logger.debug("No Goal");
            }
        }
    }


    private static void incrementTeam1Goals(MatchServiceProto.Match.Builder matchBuilder) {
        logger.debug("Team 1 scored");
        matchBuilder.setTeam1Goals(matchBuilder.getTeam1Goals() + 1);
    }

    private static void incrementTeam2Goals(MatchServiceProto.Match.Builder matchBuilder) {
        logger.debug("Team 2 scored");
        matchBuilder.setTeam2Goals(matchBuilder.getTeam2Goals() + 1);
    }

    public static List<String> extractComments(JsonNode jsonNode) {
        List<String> comments = new ArrayList<>();
        JsonNode commentsNode = jsonNode.get("comments");

        if (commentsNode == null) {
            logger.debug("comments node is null");
            return comments;
        }

        for (JsonNode commentNode : commentsNode) {
            logger.debug("adding comment to comments");
            comments.add(commentNode.asText());
        }
        return comments;
    }

}
