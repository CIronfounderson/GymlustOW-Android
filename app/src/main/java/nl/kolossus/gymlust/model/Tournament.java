package nl.kolossus.gymlust.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ericdehaan on 06-10-17.
 * <p>
 * model for Tournament
 */

public class Tournament {
    private String name;
    public ArrayList<Match> matches;


    public Tournament(String name) {
        this.name = name;
        this.matches = new ArrayList<Match>();
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    public void rearrange(SortOrder order) {
        Collections.sort(matches);
        for (Match m : matches) {
            m.rearrange(order);
        }
    }

    public void checkChampion(String prefix) {
        String championId = "";
        float championScore = 0;

        for (Match m : matches) {
            if (m.getSequence().startsWith(prefix)) {
                for (Participant p : m.participants) {
                    if (p.getScore() > championScore) {
                        championId = p.getId();
                        championScore = p.getScore();
                    }
                }
            }
        }
        if (!championId.equals("")) {
            setChampion(championId, prefix);
        }
    }

    private void setChampion(String id, String prefix) {
        for (Match m : matches) {
            if (m.getSequence().startsWith(prefix)) {
                for (Participant p : m.participants) {
                    p.setChampion(p.getId().equals(id));
                }
            }
        }
    }
}
