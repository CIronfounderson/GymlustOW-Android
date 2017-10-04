package nl.kolossus.gymlust;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import nl.kolossus.gymlust.model.Match;
import nl.kolossus.gymlust.model.MatchObject;
import nl.kolossus.gymlust.model.ParticipantStatus;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Match> mArraylistMatches;
    private AppUser mAppUser;

    static final int REFEREE_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView ivReferee = (ImageView)findViewById(R.id.referee_image);
        ivReferee.setVisibility(View.INVISIBLE);

        mAppUser = AppUser.getInstance();

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mArraylistMatches = new ArrayList<>();
        mArraylistMatches.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ow2017");

//        HeaderListView list = new HeaderListView(this);
        HeaderListView list = (HeaderListView) findViewById(R.id.main_list_view);
        final SectionAdapter listAdapter = new SectionAdapter() {
            @Override
            public int numberOfSections() {
                return mArraylistMatches.size();
            }

            @Override
            public int numberOfRows(int section) {
                return mArraylistMatches.get(section).participants.size();
            }

            @Override
            public boolean hasSectionHeaderView(int section) {
                return true;
            }

            @Override
            public View getRowView(int section, int row, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(getResources().getLayout(R.layout.member_row), null);
                }
                ImageView ivLight = (ImageView) convertView.findViewById(R.id.ivLight);
                if(mArraylistMatches.get(section).participants.get(row).getStatus()==ParticipantStatus.Free){
                    ivLight.setVisibility(View.INVISIBLE);
                } else {
                    ivLight.setVisibility(View.VISIBLE);
                }
                TextView tvName = (TextView) convertView.findViewById(R.id.member_name);
                tvName.setText(mArraylistMatches.get(section).participants.get(row).getName());

                TextView tvTotal = (TextView) convertView.findViewById(R.id.member_total);
                tvTotal.setText(String.format(Locale.getDefault(), "%.3f", mArraylistMatches.get(section).participants.get(row).getScore()));
                return convertView;
            }

            @Override
            public Object getRowItem(int section, int row) {
                return null;
            }

            public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
                if(mAppUser.isReferee() && (mArraylistMatches.get(section).participants.get(row).getStatus()== ParticipantStatus.Free)){
                    Intent intent = new Intent(getBaseContext(), EditActivity.class);
                    intent.putExtra("MemberId", mArraylistMatches.get(section).participants.get(row).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getBaseContext(), MemberActivity.class);
                    intent.putExtra("MemberId", mArraylistMatches.get(section).participants.get(row).getId());
                    startActivity(intent);
                }
            }

            public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
                Log.d("SectionHeader", "SectionHeader for section: " + section);
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(getResources().getLayout(R.layout.section_header), null);
                }
                convertView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.holo_blue_light));
                TextView tv = (TextView) convertView.findViewById(R.id.section_header);
                tv.setText(mArraylistMatches.get(section).getName());
                return convertView;
            }
        };
        list.setAdapter(listAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArraylistMatches.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    MatchObject matchObj = child.getValue(MatchObject.class);
                    matchObj.setId(child.getKey());
                    Match match = matchObj.toMatch();
                    boolean isMatchFound = false;
                    for (Match m : mArraylistMatches) {
                        if (m.getName().equals(match.getName())) {
                            m.participants.add(match.participants.get(0));
                            isMatchFound = true;
                        }
                    }
                    if (!isMatchFound) {
                        mArraylistMatches.add(match);
                    }
                }
//                mArraylistMatches.sort();
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void refereeLogin(View v) {
        Intent intent = new Intent(this, RefereeLoginActivity.class);
        startActivityForResult(intent, REFEREE_LOGIN);
    }

    public void gotoResults(View v) {
        Intent intent = new Intent(this, ResultsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REFEREE_LOGIN) {
            mAppUser.logOffReferee();
            if (resultCode == Activity.RESULT_OK) {
                ImageView ivReferee = (ImageView)findViewById(R.id.referee_image);
                String result = data.getStringExtra("Referee");
                switch (result) {
                    case "balk":
                        ivReferee.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.beam));
                        ivReferee.setVisibility(View.VISIBLE);
                        mAppUser.isBeamReferee = true;
                        break;
                    case "brug":
                        ivReferee.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unevenbars));
                        ivReferee.setVisibility(View.VISIBLE);
                        mAppUser.isPonyReferee = true;
                        break;
                    case "vloer":
                        ivReferee.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.floor));
                        ivReferee.setVisibility(View.VISIBLE);
                        mAppUser.isFloorReferee = true;
                        break;
                    case "sprong":
                        ivReferee.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vault));
                        ivReferee.setVisibility(View.VISIBLE);
                        mAppUser.isVaultReferee = true;
                        break;
                    default:
                        ivReferee.setVisibility(View.INVISIBLE);

                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.d("INTENT", "Referee login canceled");
            }
        }
    }

}
