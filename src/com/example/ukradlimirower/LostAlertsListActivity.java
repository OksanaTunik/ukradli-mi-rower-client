package com.example.ukradlimirower;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class LostAlertsListActivity extends BaseActivity {
    ListView lvMain = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);

        new LoadLostAlertsTask().execute();
    }

    public class LoadLostAlertsTask extends AsyncTask<Void, Void, List<LostAlert>> {
        @Override
        protected List<LostAlert> doInBackground(Void... voids) {
            return AlertsApiClient.getAllLostAlerts();
        }

        @Override
        protected void onPostExecute(List<LostAlert> result) {
            setContentView(R.layout.lost_alerts_list);

            ListView lvMain = (ListView) findViewById(R.id.lvMain);

            LostAlertAdapter adapter = new LostAlertAdapter(LostAlertsListActivity.this, R.layout.lost_alerts_list_item, result);

            lvMain.setAdapter(adapter);

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

    public class LostAlertAdapter extends ArrayAdapter<LostAlert> {
        public LostAlertAdapter(Context context, int resource) {
            super(context, resource);
        }

        public LostAlertAdapter(Context context, int resource, List<LostAlert> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItem = convertView;

            if (listItem == null) {
                listItem = getLayoutInflater().inflate(R.layout.lost_alerts_list_item, parent, false);
            }

            LostAlert alert = getItem(position);

            if (alert == null) {
                return listItem;
            }

            // ImageView image = (ImageView) listItem.findViewById(R.id.image);
            // image.setImageResource(...);

            TextView title = (TextView) listItem.findViewById(R.id.title);
            TextView description = (TextView) listItem.findViewById(R.id.description);
            ImageView image = (ImageView) listItem.findViewById(R.id.image);

            title.setText(alert.getTitle());
            description.setText(alert.getDescription());

            if (alert.images.size() > 0)
                image.setImageBitmap(alert.bitmaps.get(0));

            listItem.setTag(String.format("%d", alert.getId()));

            return listItem;
        }
    }
}
