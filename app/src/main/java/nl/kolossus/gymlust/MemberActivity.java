package nl.kolossus.gymlust;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import nl.kolossus.gymlust.model.Participant;
import nl.kolossus.gymlust.model.ParticipantObject;
import nl.kolossus.gymlust.model.Score;

public class MemberActivity extends AppCompatActivity {
    Participant participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        Intent intent = getIntent();
        String memberId = intent.getStringExtra("MemberId");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ow2017/" + memberId);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ParticipantObject participantObj = dataSnapshot.getValue(ParticipantObject.class);
                participantObj.setId(dataSnapshot.getKey());
                participant = participantObj.toParticipant();
                showData(participant);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showData(Participant participant) {
        TextView tvName = (TextView) findViewById(R.id.member_name);
        tvName.setText(participant.getName());

        TextView tvVaultScore = (TextView) findViewById(R.id.score_vault);
        tvVaultScore.setText(String.format(Locale.getDefault(), "%.3f", participant.getVaultScore()));

        Score score = participant.getVault1Score();
        LinearLayout llVault1 = (LinearLayout) findViewById(R.id.vault1_score_layout);
        showScore(llVault1, score);

        score = participant.getVault2Score();
        LinearLayout llVault2 = (LinearLayout) findViewById(R.id.vault2_score_layout);
        showScore(llVault2, score);

        score = participant.getPonyScore();
        TextView tvPonyScore = (TextView) findViewById(R.id.score_pony);
        tvPonyScore.setText(String.format(Locale.getDefault(), "%.3f", score.getTotal()));
        LinearLayout llPony = (LinearLayout) findViewById(R.id.pony_score_layout);
        showScore(llPony, score);

        score = participant.getBeamScore();
        TextView tvBeamScore = (TextView) findViewById(R.id.score_beam);
        tvBeamScore.setText(String.format(Locale.getDefault(), "%.3f", score.getTotal()));
        LinearLayout llBeam = (LinearLayout) findViewById(R.id.beam_score_layout);
        showScore(llBeam, score);

        score = participant.getFloorScore();
        TextView tvFloorScore = (TextView) findViewById(R.id.score_floor);
        tvFloorScore.setText(String.format(Locale.getDefault(), "%.3f", score.getTotal()));

        LinearLayout llFloor = (LinearLayout) findViewById(R.id.floor_score_layout);
        showScore(llFloor, score);
    }

    private void showScore(LinearLayout scoreLayout, Score score) {
        TextView tvD = (TextView) scoreLayout.findViewById(R.id.txt_d);
        tvD.setText(String.format(Locale.getDefault(), "%.3f", score.getDScore()));
        TextView tvE = (TextView) scoreLayout.findViewById(R.id.txt_e);
        tvE.setText(String.format(Locale.getDefault(), "%.3f", score.getEScore()));
        TextView tvN = (TextView) scoreLayout.findViewById(R.id.txt_n);
        if (score.getNScore() == 0f) {
            TextView tvNLabel = (TextView) scoreLayout.findViewById(R.id.lbl_n);
            tvNLabel.setVisibility(View.INVISIBLE);
            tvN.setVisibility(View.INVISIBLE);
        } else {
            tvN.setText(String.format(Locale.getDefault(), "%.3f", score.getNScore()));
        }
    }
}
