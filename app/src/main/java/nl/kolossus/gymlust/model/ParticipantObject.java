package nl.kolossus.gymlust.model;

/**
 * Created by ericdehaan on 24-07-17.
 */

public class ParticipantObject {
    String id;
    float beamD;
    float beamE;
    float beamN;
    float floorD;
    float floorE;
    float floorN;
    String name;
    String nivo;
    String startSequence;
    String resultSequence;
    float ponyD;
    float ponyE;
    float ponyN;
    String status;
    float vault1D;
    float vault1E;
    float vault1N;
    float vault2D;
    float vault2E;
    float vault2N;

    public ParticipantObject() {
        name = "";
        nivo = "";
        status = "free";
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

    ParticipantObject(Participant participant) {
        id=participant.getId();
        name=participant.getName();
        nivo=participant.getLevel();
        startSequence=participant.getStartSequence();
        resultSequence=participant.getResultSequence();
        if(participant.getStatus()==ParticipantStatus.Free){
            status="free";
        }else{
            status="locked";
        }
        Score score=participant.getBeamScore();
        beamD = score.getDScore();
        beamE=score.getEScore();
        beamN = score.getNScore();
        score=participant.getFloorScore();
        floorD=score.getDScore();
        floorE=score.getEScore();
        floorN = score.getNScore();
        score = participant.getPonyScore();
        ponyD=score.getDScore();
        ponyE= score.getEScore();
        ponyN=score.getNScore();
        score=participant.getVault1Score();
        vault1D=score.getDScore();
        vault1E=score.getEScore();
        vault1N=score.getNScore();
        score=participant.getVault2Score();
        vault2D=score.getDScore();
        vault2E=score.getEScore();
        vault2N=score.getNScore();
    }

    public Participant toParticipant() {
        Participant participant = new Participant();
        participant.setName(name);
        participant.setLevel(nivo);
        participant.setStartSequence(startSequence);
        participant.setResultSequence(resultSequence);
        if(status.equals("free")){
            participant.setStatus(ParticipantStatus.Free);
        } else {
            participant.setStatus(ParticipantStatus.Locked);
        }
        participant.setBeamScore(new Score(beamE, beamD, beamN));
        participant.setFloorScore(new Score(floorE, floorD, floorN));
        participant.setPonyScore(new Score(ponyE, ponyD, ponyN));
        participant.setVault1Score(new Score(vault1E, vault1D, vault1N));
        participant.setVault2Score(new Score(vault2E, vault2D, vault2N));

        return participant;
    }

    public void setId(String value) {
        id = value;
    }

}
