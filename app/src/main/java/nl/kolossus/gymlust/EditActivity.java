package nl.kolossus.gymlust;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import nl.kolossus.gymlust.model.Participant;
import nl.kolossus.gymlust.model.ParticipantObject;
import nl.kolossus.gymlust.model.ParticipantStatus;
import nl.kolossus.gymlust.model.Score;

public class EditActivity extends AppCompatActivity {
    private AppUser mAppUser;
    Participant participant;
    FirebaseDatabase database;
    DatabaseReference myRef;
    int vaultNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        String memberId = intent.getStringExtra("MemberId");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ow2017/" + memberId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ParticipantObject participantObj = dataSnapshot.getValue(ParticipantObject.class);
                participantObj.setId(dataSnapshot.getKey());
                participant = participantObj.toParticipant();
                showData(participant);
                participant.setStatus(ParticipantStatus.Locked);
                participant.save(myRef);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(Participant participant) {
        LinearLayout vaultSwitch = (LinearLayout) findViewById(R.id.vault_switch_layout);
        vaultSwitch.setVisibility(View.INVISIBLE);

        TextView tvTitle = (TextView) findViewById(R.id.edit_title);
        mAppUser = AppUser.getInstance();
        if (mAppUser.isBeamReferee) {
            tvTitle.setText(R.string.beam_name);
            showScoreData(participant.getBeamScore());
        }
        if (mAppUser.isVaultReferee) {
            vaultSwitch.setVisibility(View.VISIBLE);
            tvTitle.setText(R.string.vault_name);
            if (vaultNumber == 1) {
                showScoreData(participant.getVault1Score());
            } else {
                showScoreData(participant.getVault2Score());
            }
        }
        if (mAppUser.isFloorReferee) {
            tvTitle.setText(R.string.floor_name);
            showScoreData(participant.getFloorScore());
        }
        if (mAppUser.isPonyReferee) {
            tvTitle.setText(R.string.uneven_bars_name);
            showScoreData(participant.getPonyScore());
        }

        TextView tvName = (TextView) findViewById(R.id.edit_name);
        tvName.setText(participant.getName());
    }

    private void showScoreData(Score score) {
        showScorePartData((LinearLayout) findViewById(R.id.edit_d_score), score.getDScore(), getString(R.string.d_score_label));
        showScorePartData((LinearLayout) findViewById(R.id.edit_e_score), score.getEScore(), getString(R.string.e_score_label));
        showScorePartData((LinearLayout) findViewById(R.id.edit_n_score), score.getNScore(), getString(R.string.n_score_label));
    }

    private void showScorePartData(LinearLayout ll, float value, String text) {
        TextView tvLabel = (TextView) ll.findViewById(R.id.score_label);
        tvLabel.setText(text);
        ScrollableNumberPicker vwPoints = (ScrollableNumberPicker) ll.findViewById(R.id.score_points);
        vwPoints.setValue(floatGetInt(value));
        ScrollableNumberPicker vwDecimals = (ScrollableNumberPicker) ll.findViewById(R.id.score_decimals);
        vwDecimals.setValue(floatGetDecimals(value, 2));
    }

    private void getFormData() {
        int precision = 2;
        LinearLayout ll = (LinearLayout) findViewById(R.id.edit_d_score);
        float dScore = getScoreData(ll, precision);
        ll = (LinearLayout) findViewById(R.id.edit_e_score);
        float eScore = getScoreData(ll, precision);
        ll = (LinearLayout) findViewById(R.id.edit_n_score);
        float nScore = getScoreData(ll, precision);

        if (mAppUser.isBeamReferee) {
            participant.setBeamScore(new Score(eScore, dScore, nScore));
        }
        if (mAppUser.isVaultReferee) {
            if (vaultNumber == 1) {
                participant.setVault1Score(new Score(eScore, dScore, nScore));
            } else {
                participant.setVault2Score(new Score(eScore, dScore, nScore));
            }
        }
        if (mAppUser.isFloorReferee) {
            participant.setFloorScore(new Score(eScore, dScore, nScore));
        }
        if (mAppUser.isPonyReferee) {
            participant.setPonyScore(new Score(eScore, dScore, nScore));
        }
    }

    private float getScoreData(LinearLayout layout, int precision) {
        ScrollableNumberPicker np = (ScrollableNumberPicker) layout.findViewById(R.id.score_points);
        int points = np.getValue();
        np = (ScrollableNumberPicker) layout.findViewById(R.id.score_decimals);
        return points + (float) (np.getValue() / Math.pow(10, precision));
    }

    private int floatGetInt(float value) {
        return (int) value;
    }

    private int floatGetDecimals(float value, int precision) {
        float tmp = value - floatGetInt(value);
        tmp *= Math.pow(10, precision);
        return Math.round(tmp);
    }

    public void buttonPressed(View vw) {
        participant.setStatus(ParticipantStatus.Free);
        if (vw.getId() == R.id.edit_button_ok) {
            getFormData();
        }
        participant.save(myRef);
        super.onBackPressed();
    }

    public void vaultSwitchedChanged(View vw) {
        getFormData();
        TextView tvLabel1 = (TextView) findViewById(R.id.vault_1_label);
        TextView tvLabel2 = (TextView) findViewById(R.id.vault_2_label);
        if (((Switch) vw).isChecked()) {
            vaultNumber = 2;
            showScoreData(participant.getVault2Score());
            tvLabel2.setTextColor(ContextCompat.getColor(this, R.color.gymlustBackground));
            tvLabel1.setTextColor(ContextCompat.getColor(this, R.color.disabledColor));
        } else {
            vaultNumber = 1;
            showScoreData(participant.getVault1Score());
            tvLabel2.setTextColor(ContextCompat.getColor(this, R.color.disabledColor));
            tvLabel1.setTextColor(ContextCompat.getColor(this, R.color.gymlustBackground));
        }
    }

}
