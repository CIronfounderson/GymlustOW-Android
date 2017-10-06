package nl.kolossus.gymlust;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import nl.kolossus.gymlust.model.Match;
import nl.kolossus.gymlust.model.MatchObject;
import nl.kolossus.gymlust.model.SortOrder;
import nl.kolossus.gymlust.model.Tournament;

public class ResultsActivity extends AppCompatActivity {
    private Tournament tournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        tournament = new Tournament("ow2017");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(tournament.getName());

//        HeaderListView list = new HeaderListView(this);
        HeaderListView list = (HeaderListView) findViewById(R.id.results_list_view);
        final SectionAdapter listAdapter = new SectionAdapter() {
            @Override
            public int numberOfSections() {
                return tournament.matches.size();
            }

            @Override
            public int numberOfRows(int section) {
                return tournament.matches.get(section).participants.size();
            }

            @Override
            public boolean hasSectionHeaderView(int section) {
                return true;
            }

            @Override
            public View getRowView(int section, int row, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(getResources().getLayout(R.layout.member_result_row), null);
                }
                ImageView ivTrophy = (ImageView) convertView.findViewById(R.id.ivTrophy);
                if (tournament.matches.get(section).participants.get(row).isChampion()) {
                    ivTrophy.setVisibility(View.VISIBLE);
                } else {
                    ivTrophy.setVisibility(View.INVISIBLE);
                }
//                TextView tvPlace = (TextView) convertView.findViewById(R.id.member_result_place);
//                tvPlace.setText(String.format(Locale.getDefault(), "%1d", row + 1));

                TextView tvName = (TextView) convertView.findViewById(R.id.member_result_name);
                tvName.setText(String.format(Locale.getDefault(), "%d - %s", row + 1, tournament.matches.get(section).participants.get(row).getName()));

                TextView tvTotal = (TextView) convertView.findViewById(R.id.member_result_total);
                tvTotal.setText(String.format(Locale.getDefault(), "%.3f", tournament.matches.get(section).participants.get(row).getScore()));
                return convertView;
            }

            @Override
            public Object getRowItem(int section, int row) {
                return null;
            }

            public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
                Log.d("SectionHeader", "SectionHeader for section: " + section);
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(getResources().getLayout(R.layout.section_header), null);
                }
                convertView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.holo_blue_light));
                TextView tv = (TextView) convertView.findViewById(R.id.section_header);
                tv.setText(tournament.matches.get(section).getName());
                return convertView;
            }
        };
        list.setAdapter(listAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tournament.matches.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    MatchObject matchObj = child.getValue(MatchObject.class);
                    matchObj.setId(child.getKey());
                    Match match = matchObj.toMatch();
                    boolean isMatchFound = false;
                    for (Match m : tournament.matches) {
                        if (m.getName().equals(match.getName())) {
                            m.participants.add(match.participants.get(0));
                            isMatchFound = true;
                        }
                    }
                    if (!isMatchFound) {
                        tournament.matches.add(match);
                    }
                }
                tournament.rearrange(SortOrder.ResultOrder);
                tournament.checkChampion("1");
                tournament.checkChampion("2");
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
