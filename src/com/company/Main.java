package com.company;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here

        //File file = new File("/Users/denizyeniceri/Desktop/cs410/NFA-to-DFA-converter/src/com/company/NFA1.txt");
        File file = new File("/Users/denizyeniceri/Desktop/cs410/NFA-to-DFA-converter/src/com/company/NFA2.txt");

        BufferedReader reader = new BufferedReader(new FileReader(file));

        ArrayList<String> state = new ArrayList<>();
        ArrayList<String> alphabets = new ArrayList<>();
        ArrayList<List<String>> transitions = new ArrayList<>();
        String startState = null;
        String finalState = null;
        String line;
        ArrayList<String> txt = new ArrayList<>();
        HashMap<String, HashMap<String, ArrayList<String>>> transitionMap = new HashMap<>();
        HashMap<String, HashMap<String, String>> newTransitionMap = new HashMap<>();



        while ((line = reader.readLine()) != null) {
            txt.add(line);

        }
        for(int i =0; i<txt.size();i++) {
            for (int j = 1; j < txt.indexOf("STATES"); j++) {
                alphabets.add(txt.get(j));
            }
            for (int k = txt.indexOf("STATES") + 1; k < txt.indexOf("START"); k++) {
                state.add(txt.get(k));
            }
            for (int l = txt.indexOf("START") + 1; l < txt.indexOf("FINAL"); l++) {
                startState = txt.get(l);
            }
            for (int m = txt.indexOf("FINAL") + 1; m < txt.indexOf("TRANSITIONS"); m++) {
                finalState = txt.get(m);
            }
            break;
        }
        transitions.add(txt.subList((txt.indexOf("TRANSITIONS")+1), txt.size()-1));
        /*for(int i = 0; i<txt.size();i++){
            if(txt.contains("STATES")){
                for(String alp : txt)
                alphabets.add(txt.get(i+1));
            }
            if(txt.contains("START")){
                state.add(txt.get(i+1));
            }
            if(txt.contains("FINAL")){
                startState = txt.get(i);
            }
            if(txt.contains("TRANSITIONS")){
                finalState = txt.get(i);
            }
            transitions.add(txt.subList(i, txt.size()-1));
        } */
        //alphabets.add(txt.subList(1, 3));
        //state.add(txt.subList(4, 7));
        //startState = txt.get(8);
       // finalState = txt.get(10);
       // transitions.add(txt.subList(12, txt.size() - 1));

        //HashMap<String, HashMap<String, ArrayList<String>>> transitionMap = new HashMap<>();


        for (String transition : transitions.get(0)) {
            String from = transition.substring(0, 1);
            String input = transition.substring(2, 3);
            String to = transition.substring(4, 5);
            // transitionMap created
            if (!transitionMap.containsKey(from))
                transitionMap.put(from, new HashMap<>());
            if (!transitionMap.get(from).containsKey(input))
                transitionMap.get(from).put(input, new ArrayList<>());
            transitionMap.get(from).get(input).add(to);
        }

        System.out.println();




        //HashMap<String, HashMap<String, String>> newTransitionMap = new HashMap<>();

        // add start state and its combined destination state
        newTransitionMap.put(startState, new HashMap<>());
        for (String input : transitionMap.get(startState).keySet()) { //0-1lerin içinde arıyo
            String newState = "";
            for (String dest : transitionMap.get(startState).get(input))
                newState += dest;
            newTransitionMap.get(startState).put(input, newState);
        }

        for (String from : transitionMap.keySet()) {  //A-B içinde arıyo
            for (String input : transitionMap.get(from).keySet()) { //0-1 içinde arıyo
                ArrayList<String> destinations = transitionMap.get(from).get(input); //new possible states

                if (destinations.size() > 1) {
                    String newState = "";
                    for (String destination : destinations)
                        newState += destination;

                    if (!newTransitionMap.containsKey(newState)) {

                        // new state created
                        handleNewState(newState, newTransitionMap, destinations, transitionMap);
                    }
                }
            }
        }

        ArrayList<String> output = new ArrayList<>();
        output.add("ALPHABET");
        /*
        output.add("0");
        output.add("1");
        */
        for(int g = 0; g< alphabets.size();g++){

            output.add(alphabets.get(g));
        }
        output.add("STATES");
        for(int a = 0; a< newTransitionMap.keySet().size();a++) {
            output.add((String) newTransitionMap.keySet().toArray()[a]);
        }
        output.add("START");
        output.add(startState);
        output.add("FINAL");
        for(int b = 0; b<newTransitionMap.keySet().size();b++){
            if(newTransitionMap.keySet().toArray()[b].toString().contains(finalState)){
                output.add((String) newTransitionMap.keySet().toArray()[b]);
            }
        }
        output.add("TRANSITIONS");

        String trans = null;
        for(Map.Entry<String, HashMap<String, String>> s : newTransitionMap.entrySet()){
            String key = s.getKey();
            HashMap<String, String> value = s.getValue();
            for(Map.Entry<String, String> s2 : value.entrySet()){
                String key2 = s2.getKey();
                String val2 = s2.getValue();

                for(int c = 0; c< newTransitionMap.size()*value.keySet().size();c++){
                    trans = key +" "+ key2 +" "+ val2;

                    
                }
                output.add(trans);
            }

        }
        for(int d = 0; d< output.size();d++){
            System.out.println(output.get(d));
        }

    }

    private static void handleNewState(String newState,
                                HashMap<String, HashMap<String, String>> newTransitionMap,
                                ArrayList<String> destinations,
                                HashMap<String, HashMap<String, ArrayList<String>>> transitionMap) {


        newTransitionMap.put(newState, new HashMap<>());
        for (String destination : destinations) {
            if (transitionMap.get(destination) == null)
                continue;

            // combine destinations of parts of new state
            for (String possibleInput : transitionMap.get(destination).keySet()) {
                if (!newTransitionMap.get(newState).containsKey(possibleInput))
                    newTransitionMap.get(newState).put(possibleInput, "");
                String newDestinations = "";
                for (String dest : transitionMap.get(destination).get(possibleInput))
                    newDestinations += dest;


                String finalNewDestination = newTransitionMap.get(newState).get(possibleInput) + newDestinations;
                String result1 = "";
                for (int a = 0; a <= finalNewDestination.length()-1; a++){
                    if (result1.contains("" + finalNewDestination.charAt(a))) {
                        continue;
                    }
                    result1 += finalNewDestination.charAt(a);
                }
                char tempArray[] = result1.toCharArray();
                // Sorting temp array using
                Arrays.sort(tempArray);

                //String result2 = "";
                finalNewDestination = new String(tempArray);
                newTransitionMap.get(newState).put(possibleInput, finalNewDestination);

                // check if new state is created
                if (!newTransitionMap.containsKey(finalNewDestination) && !transitionMap.containsKey(finalNewDestination)) {
                //if (!checkExists(finalNewDestination, newTransitionMap) && !transitionMap.containsKey(finalNewDestination)) {
                    ArrayList<String> newDestinationsList = new ArrayList<>();

                    for (int i = 0; i < finalNewDestination.length(); ++i)
                        newDestinationsList.add(finalNewDestination.substring(i, i + 1));

                    handleNewState(finalNewDestination, newTransitionMap, newDestinationsList, transitionMap);
                }
            }
        }
    }

}
