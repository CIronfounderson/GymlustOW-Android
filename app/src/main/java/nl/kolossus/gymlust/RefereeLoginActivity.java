package nl.kolossus.gymlust;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RefereeLoginActivity extends AppCompatActivity {
    private Intent returnIntent = new Intent();

    private String referee = "";

    public String getReferee() {
        return referee;
    }

    public void setReferee(String value) {
        referee = value;
        returnIntent.putExtra("Referee", referee);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referee_login);
    }

    public void buttonPressed(View v) {
        Intent returnIntent = new Intent();
        switch (v.getId()) {
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED, returnIntent);
                finish();
                break;
            case R.id.btn_ok:
                checkCode();
                break;
        }
    }

    void checkCode() {
        RadioGroup event = (RadioGroup) findViewById(R.id.event_type);
        final RadioButton radio = (RadioButton) findViewById(event.getCheckedRadioButtonId());
        EditText etCode = (EditText) findViewById(R.id.txt_code);
        final String enteredCode = etCode.getText().toString();
        final String radioTag = (String)radio.getTag();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("referees");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals(radioTag)) {
                        String childValue = child.getValue().toString();
                        if (child.getValue().toString().equals(String.format("{code=%s}",enteredCode))) {
                            setReferee(radioTag);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
