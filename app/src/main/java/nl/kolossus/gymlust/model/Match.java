package nl.kolossus.gymlust.model;

import java.util.ArrayList;

/**
 * Created by Eric de Haan on 21-07-17.
 * <p>
 * Match class describes a match
 */

public class Match {
    public ArrayList<Participant> participants;
    private String name;
    private String sequence;

    Match() {
        name = "";
        sequence = "";
        participants = new ArrayList<>();
        participants.clear();
    }

    public Match(String title, String sequence) {
        this.name = title;
        this.sequence = sequence;
        participants = new ArrayList<>();
        participants.clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    public String getSequence() { return sequence; }

    public void setSequence(String value) { sequence = value; }
}
