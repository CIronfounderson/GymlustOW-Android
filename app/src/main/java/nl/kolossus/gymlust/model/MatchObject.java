package nl.kolossus.gymlust.model;

/**
 * Created by Eric de Haan on 21-07-17.
 * <p>
 * MatchObject class to contain a firebase record
 */

public class MatchObject {
    String nivo;
    String status;
    String name;
    String id;
    String startSequence;
    String resultSequence;
    float beamD;
    float beamE;
    float beamN;
    float floorD;
    float floorE;
    float floorN;
    float ponyD;
    float ponyE;
    float ponyN;
    float vault1D;
    float vault1E;
    float vault1N;
    float vault2D;
    float vault2E;
    float vault2N;

    public MatchObject() {
//        this.id = value;
        name = "";
        nivo = "";
        status = "";
        startSequence = "";
        resultSequence = "";
        beamD = 0;
        beamE = 0;
        beamN = 0;
        floorD = 0;
        floorE = 0;
        floorN = 0;
        ponyD = 0;
        ponyE = 0;
        ponyN = 0;
        vault1D = 0;
        vault1E = 0;
        vault1N = 0;
        vault2D = 0;
        vault2E = 0;
        vault2N = 0;
    }

    public Match toMatch() {
        Match ret = new Match();
        ret.setName(nivo);
        ret.setSequence(startSequence);

        Participant participant = new Participant(id, name, nivo, startSequence, resultSequence);
        participant.setBeamScore(new Score(beamE, beamD, beamN));
        participant.setFloorScore(new Score(floorE, floorD, floorN));
        participant.setPonyScore(new Score(ponyE, ponyD, ponyN));
        participant.setVault1Score(new Score(vault1E, vault1D, vault1N));
        participant.setVault2Score(new Score(vault2E, vault2D, vault2N));
        if(status.equals("free")) {
            participant.setStatus(ParticipantStatus.Free);
        } else {
            participant.setStatus(ParticipantStatus.Locked);
        }
        ret.participants.add(participant);

        return ret;
    }

    public void setId(String value) {
        id = value;
    }
}
