package nl.kolossus.gymlust.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;

import java.util.Locale;

/**
 * Created by Eric de Haan on 21-07-17.
 */

public class Participant implements Comparable<Participant> {
    private String id = "";
    private String name = "";
    private String level = "";
    private String startSequence = "";
    private String resultSequence = "";
    private Score beamScore = new Score(0, 0, 0);
    private Score floorScore = new Score(0, 0, 0);
    private Score vault1Score = new Score(0, 0, 0);
    private Score vault2Score = new Score(0, 0, 0);
    private Score ponyScore = new Score(0, 0, 0);
    private float score = 0.0f;
    private boolean champion = false;
    private ParticipantStatus status = ParticipantStatus.Free;

    public Participant() {
    }

    public Participant(String id, String name, String lvl, String startSeq, String resultSeq) {
        this.id = id;
        this.name = name;
        this.level = lvl;
        this.startSequence = startSeq;
        this.resultSequence = resultSeq;
        this.beamScore = new Score();
        this.floorScore = new Score();
        this.ponyScore = new Score();
        this.vault1Score = new Score();
        this.vault2Score = new Score();
        this.score = 0;
        this.champion = false;
        this.status = ParticipantStatus.Free;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getStartSequence() {
        return startSequence;
    }

    public String getResultSequence() {
        return resultSequence;
    }

    public Score getBeamScore() {
        return beamScore;
    }

    public Score getFloorScore() {
        return floorScore;
    }

    public Score getPonyScore() {
        return ponyScore;
    }

    public Score getVault1Score() {
        return vault1Score;
    }

    public Score getVault2Score() {
        return vault2Score;
    }

    public float getScore() {
        return score;
    }

    public Boolean isChampion() {
        return champion;
    }

    public ParticipantStatus getStatus() {
        return status;
    }

    public void setId(String value) {
        id = value;
    }

    public void setName(String value) {
        name = value;
    }

    public void setLevel(String value) {
        level = value;
    }

    public void setStartSequence(String value) {
        startSequence = value;
    }

    public void setResultSequence(String value) {
        resultSequence = value;
    }

    public void setChampion(Boolean value) {
        champion = value;
    }

    public void setBeamScore(Score value) {
        beamScore = value;
        setScore();
    }

    public void setFloorScore(Score value) {
        floorScore = value;
        setScore();
    }

    public void setPonyScore(Score value) {
        ponyScore = value;
        setScore();
    }

    public void setVault1Score(Score value) {
        vault1Score = value;
        setScore();
    }

    public void setVault2Score(Score value) {
        vault2Score = value;
        setScore();
    }

    public float getVaultScore() {
        return (vault1Score.total + vault2Score.total) / 2;
    }

    public void setStatus(ParticipantStatus value) {
        status = value;
    }

    private void setScore() {
        score = beamScore.total + floorScore.total + ponyScore.total + ((vault1Score.total + vault2Score.total) / 2);
        String result = startSequence.substring(0, 3) + String.format(Locale.getDefault(), "%05d", 99999 - (int) (score * 1000));
        setResultSequence(result);
    }

    public void save(DatabaseReference dbRef) {
        ParticipantObject participantObject = new ParticipantObject(this);
        dbRef.setValue(participantObject);
    }

    public int compareTo(@NonNull Participant p) {
        return resultSequence.compareTo(p.getResultSequence());
    }
}

