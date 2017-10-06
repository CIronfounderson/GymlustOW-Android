package nl.kolossus.gymlust.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Eric de Haan on 21-07-17.
 * <p>
 * Match class describes a match
 */

public class Match implements Comparable<Match> {
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

    public int compareTo(Match m) {
        return sequence.compareTo(m.getSequence());
    }

    public void rearrange(SortOrder order) {
        Comparator<Participant> comparator;
        if(order == SortOrder.StartOrder) {
            comparator = new Comparator<Participant>() {
                @Override
                public int compare(Participant o1, Participant o2) {
                    return o1.getStartSequence().compareTo(o2.getStartSequence());
                }
            };
        } else {
            comparator = new Comparator<Participant>() {
                @Override
                public int compare(Participant o1, Participant o2) {
                    return o1.getResultSequence().compareTo(o2.getResultSequence());
                }
            };
        }
        Collections.sort(participants, comparator);
    }
}
