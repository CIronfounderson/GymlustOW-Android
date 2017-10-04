package nl.kolossus.gymlust.model;

/**
 * Created by Eric de Haan on 21-07-17.
 * Score is the class used to hold the score data for one event.
 */

public class Score {
    private float eScore;
    private float dScore;
    private float nScore;
    float total;

    Score() {
        eScore = 0;
        dScore = 0;
        nScore = 0;
        total = 0;
    }

    public Score(float eScore, float dScore, float nScore) {
        this.eScore = eScore;
        this.dScore = dScore;
        this.nScore = nScore;
        this.total = eScore + dScore - nScore;
    }

    public void setEScore(float value) {
        eScore = value;
        setTotal();
    }

    public void setDScore(float value) {
        dScore = value;
        setTotal();
    }

    public void setNScore(float value) {
        nScore = value;
        setTotal();
    }

    private void setTotal(){
        total = eScore + dScore - nScore;
    }

    public float getEScore() {
        return eScore;
    }

    public float getDScore() {
        return dScore;
    }

    public float getNScore() {
        return nScore;
    }

    public float getTotal() {
        return total;
    }
}
