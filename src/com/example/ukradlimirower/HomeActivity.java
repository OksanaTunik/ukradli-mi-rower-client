package com.example.ukradlimirower;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class HomeActivity extends BaseActivity {
    
    ListView lvMain = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        lvMain = (ListView) findViewById(R.id.lvMain);
    
        lvMain.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String alertId = (String) view.getTag();
                showListItem(alertId);
            }
        });
    }
}
