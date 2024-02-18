package com.github.denisbytes.javafootball.score_calculator.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.denisbytes.javafootball.score_calculator.proto.MatchProto;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static MatchProto.Match findMatch(String matchID, List<MatchProto.Match> matches) {
        for (MatchProto.Match match : matches) {
            if (match.getMatchID().equals(matchID)) {
                return match;
            }
        }
        return null;
    }

    public static void updateGoalsCount(MatchProto.Match.Builder matchBuilder, JsonNode jsonNode) {
        List<String> comments = extractComments(jsonNode);
        System.out.println("TEST 1 --------");

        // Iterate over each comment in the list
        for (String comment : comments) {
            System.out.println("TEST 2 --------");
            if (comment.equals("GOAL")) {
                System.out.println("TEST 3 --------");
                String team = jsonNode.get("team").asText();
                if (team.equals(matchBuilder.getTeam1())) {
                    System.out.println("TEST 4 --------");
                    incrementTeam1Goals(matchBuilder);
                } else if (team.equals(matchBuilder.getTeam2())) {
                    System.out.println("TEST 5 --------");
                    incrementTeam2Goals(matchBuilder);
                } else {
                    System.out.println("Invalid team name: " + team);
                }
            }
        }
    }



    private static void incrementTeam1Goals(MatchProto.Match.Builder matchBuilder) {
        System.out.println("TEST 6-----------------");
        matchBuilder.setTeam1Goals(matchBuilder.getTeam1Goals() + 1);
    }

    private static void incrementTeam2Goals(MatchProto.Match.Builder matchBuilder) {
        System.out.println("TEST 7-----------------");
        matchBuilder.setTeam2Goals(matchBuilder.getTeam2Goals() + 1);
    }

    public static List<String> extractComments(JsonNode jsonNode) {
        List<String> comments = new ArrayList<>();
        JsonNode commentsNode = jsonNode.get("comments");

        // Check if commentsNode is null
        if (commentsNode == null) {
            System.out.println("Comments node is null");
            return comments;
        }

        for (JsonNode commentNode : commentsNode) {
            System.out.println("comments are not null");
            System.out.println(comments);
            comments.add(commentNode.asText());
        }
        return comments;
    }

}
