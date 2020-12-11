package swim.pwr.bikeridinggame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import swim.pwr.bikeridinggame.data.Endpoints;
import swim.pwr.bikeridinggame.models.UserRecordModel;

public class RecordsListAdapter extends BaseAdapter {
    private static class RankingView {
        public TextView playerRank;
        public ImageView logo;
        public TextView nick;
        public TextView travelledMeters;
    }

    private Context context;
    private RankingView rankingView;
    private LayoutInflater inflater;
    private RequestQueue mRequestQueue;
    private DataFetchingObserver dataFetchingObserver;
    public static ArrayList<UserRecordModel> userRecords = new ArrayList<>();


    public RecordsListAdapter(Context context, DataFetchingObserver observer) {
        this.context = context;
        this.dataFetchingObserver = observer;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRequestQueue = Volley.newRequestQueue(context);
        userRecords.clear();
        fetchRankingData();
    }

    private void fetchRankingData() {
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, getRecordsEndpoint(), null,
                new RecordsCorrectResponseListener(), new RecordsErrorResponseListener());
        mRequestQueue.add(req);
    }

    private static String getRecordsEndpoint() {
        String endpoint = Endpoints.BACKEND_ENDPOINT + Endpoints.RANKING_ENDPOINT;
        String query = "?sort=desc";
        endpoint += query;
        return endpoint;
    }

    private class RecordsCorrectResponseListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject element = response.getJSONObject(i);
                    String logoUrl = element.getString("logoUrl");
                    String nick = element.getString("nick");
                    int travelledMeters = element.getInt("travelledMeters");
                    UserRecordModel userRecord = new UserRecordModel(logoUrl, nick, travelledMeters);
                    userRecords.add(userRecord);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataFetchingObserver.onDataFetched();
        }
    }

    private class RecordsErrorResponseListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            dataFetchingObserver.onDataFetchingError(error.getMessage());
            VolleyLog.e("Error while reading records data: ", error.getMessage());
        }
    }

    @Override
    public int getCount() {
        return userRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ranking_list_view_row, null);
            initRankingRecordView(convertView);
            convertView.setTag(rankingView);
        } else
            rankingView = (RankingView) convertView.getTag();
        setProperRankingView(position);
        return convertView;
    }

    private void initRankingRecordView(View listView) {
        rankingView = new RankingView();
        rankingView.playerRank = listView.findViewById(R.id.player_rank);
        rankingView.logo = listView.findViewById(R.id.logoImage);
        rankingView.nick = listView.findViewById(R.id.nickText);
        rankingView.travelledMeters = listView.findViewById(R.id.travelledText);
    }

    private void setProperRankingView(int position) {
        UserRecordModel userRecord = userRecords.get(position);
        String rank = String.valueOf(position + 1);
        rankingView.playerRank.setText(rank);
        final int logoResourceId = context.getResources().getIdentifier(userRecord.logoUrl, "drawable", context.getPackageName());
        rankingView.logo.setImageResource(logoResourceId);
        rankingView.nick.setText(userRecord.nick);
        rankingView.travelledMeters.setText(String.format("%s%s", userRecord.travelledMeters, "m"));
    }
}
