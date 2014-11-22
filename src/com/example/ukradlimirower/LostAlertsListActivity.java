package com.example.ukradlimirower;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class LostAlertsListActivity extends BaseActivity {
    ListView lvMain = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_alerts_list);

        lvMain = (ListView) findViewById(R.id.lvMain);



        lvMain.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String alertId = (String) view.getTag();
                showLostAlert(alertId);
            }
        });

        Button btnAlert = (Button) findViewById(R.id.btnAlert);

        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewLostAlert();
            }
        });
    }
}
