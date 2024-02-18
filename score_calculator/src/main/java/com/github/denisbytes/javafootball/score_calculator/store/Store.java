package com.github.denisbytes.javafootball.score_calculator.store;

import com.github.denisbytes.javafootball.score_calculator.proto.MatchProto;

import java.util.ArrayList;

// In memory store
public class Store {
    private static Store instance;
    private ArrayList<MatchProto.Match> matches = new ArrayList<>();
    private Store() {
    }
    public static Store getStore() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }
    public ArrayList<MatchProto.Match> getMatches() {
        return matches;
    }
}