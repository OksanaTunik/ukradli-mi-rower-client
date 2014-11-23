package com.example.ukradlimirower;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

/**
 * Created by shybovycha on 22.11.14.
 */
public class ShowLostActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);

        Bundle bundle = getIntent().getExtras();
        String alertId = bundle.getString("alertId");

        new LoadLostAlertTask(alertId).execute();
    }

    public class LoadLostAlertTask extends AsyncTask<Void, Void, LostAlert> {
        protected String alertId;

        public LoadLostAlertTask(String alertId) {
            this.alertId = alertId;
        }

        @Override
        protected LostAlert doInBackground(Void... params) {
            return AlertsApiClient.getLostAlert(alertId);
        }

        @Override
        protected void onPostExecute(LostAlert result) {
            if (result == null) {
                showLostAlertsList();
                return;
            }

            setContentView(R.layout.view_lost_alert);

            ListView lvMain = (ListView) findViewById(R.id.found_alerts);
            List<FoundAlert> children = result.foundAlerts;

            FoundAlertAdapter adapter = new FoundAlertAdapter(ShowLostActivity.this, R.layout.found_alerts_list_item, children);

            lvMain.setAdapter(adapter);

            TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
            TextView tvDescr = (TextView) findViewById(R.id.tvDescription);
            LinearLayout imageList = (LinearLayout) findViewById(R.id.images);

            for (int i = 0; i < result.images.size(); i++) {
                ImageView image = new ImageView(ShowLostActivity.this);
                image.setImageBitmap(getBitmapFromUrl(result.images.get(i)));
                image.setAdjustViewBounds(true);
                image.setLayoutParams(new AbsListView.LayoutParams(200, 200));

                imageList.addView(image);
            }

            tvTitle.setText(result.getTitle());
            tvDescr.setText(result.getDescription());

            /*lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String alertId = (String) view.getTag();
                    showLostAlert(alertId);
                }
            });*/

            Button btnFound = (Button) findViewById(R.id.btnFound);

            btnFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCreateFoundAlert(alertId);
                }
            });
        }
    }

    public class FoundAlertAdapter extends ArrayAdapter<FoundAlert> {
        public FoundAlertAdapter(Context context, int resource) {
            super(context, resource);
        }

        public FoundAlertAdapter(Context context, int resource, List<FoundAlert> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItem = convertView;

            if (listItem == null) {
                listItem = getLayoutInflater().inflate(R.layout.found_alerts_list_item, null);
            }

            FoundAlert alert = getItem(position);

            if (alert == null) {
                return listItem;
            }

            // ImageView image = (ImageView) listItem.findViewById(R.id.image);
            // image.setImageResource(...);

            TextView title = (TextView) listItem.findViewById(R.id.title);
            TextView description = (TextView) listItem.findViewById(R.id.description);

            title.setText(alert.getTitle());
            description.setText(alert.getDescription());

            listItem.setTag(String.format("%d", alert.getId()));

            return listItem;
        }
    }
}