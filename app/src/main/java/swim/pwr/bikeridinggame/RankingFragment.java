package swim.pwr.bikeridinggame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import java.util.List;
import java.util.Objects;

import swim.pwr.bikeridinggame.data.Endpoints;
import swim.pwr.bikeridinggame.models.UserRecordModel;

public class RankingFragment extends Fragment {
    private RequestQueue mRequestQueue;

    public static ArrayList<UserRecordModel> userRecords = new ArrayList<>(List.of(new UserRecordModel("author_logo", "Janik", 123),new UserRecordModel("author_logo", "Wojtek", 99)));

    public RankingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        fetchRankingData();
    }

    private void fetchRankingData() {
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, getRecordsEndpoint(), null,
                new RankingFragment.RecordsCorrectResponseListener(), new RecordsErrorResponseListener());
        mRequestQueue.add(req);
    }

    private static String getRecordsEndpoint() {
        String endpoint = Endpoints.BACKEND_ENDPOINT + Endpoints.RANKING_ENDPOINT;
        String query = "?sort=desc";
        endpoint += query;
        return endpoint;
    }

    private static class RecordsCorrectResponseListener implements Response.Listener<JSONArray> {
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
        }
    }

    private static class RecordsErrorResponseListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            System.err.println(error);
            VolleyLog.e("Error while reading records data: ", error.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        ListView listview = view.findViewById(R.id.rankingListView);
        RecordsListAdapter adapter = new RecordsListAdapter(getActivity());
        listview.setAdapter(adapter);
        return view;
    }
}