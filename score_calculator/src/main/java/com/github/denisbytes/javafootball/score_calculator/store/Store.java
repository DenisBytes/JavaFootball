package com.github.denisbytes.javafootball.score_calculator.store;

import java.util.ArrayList;

// In memory store
public class Store {
    private static Store instance;
    private ArrayList<MatchServiceProto.Match> matches = new ArrayList<>();
    private Store() {
    }
    public static Store getStore() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }
    public ArrayList<MatchServiceProto.Match> getMatches() {
        return matches;
    }
}