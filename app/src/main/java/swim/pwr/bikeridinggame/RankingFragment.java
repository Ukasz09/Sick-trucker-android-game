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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import swim.pwr.bikeridinggame.data.Endpoints;
import swim.pwr.bikeridinggame.models.UserRecordModel;

public class RankingFragment extends Fragment {
    private RequestQueue mRequestQueue;

    //TODO: tmp mocked
//    public static UserRecordModel[] userRecords = {
//            new UserRecordModel("author_logo", "John", 324),
//            new UserRecordModel("author_logo", "Stasi", 23),
//            new UserRecordModel("author_logo", "Bolko", 122),
//            new UserRecordModel("author_logo", "Vole", 2),
//            new UserRecordModel("author_logo", "Michu", 2),
//            new UserRecordModel("author_logo", "Kennedi", 2),
//    };

    public static ArrayList<UserRecordModel> userRecords = new ArrayList<>();


    public RankingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        fetchRankingData();
    }

    private void fetchRankingData() {
        String endpoint = Endpoints.BACKEND_ENDPOINT + Endpoints.RANKING_ENDPOINT;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, endpoint, null,
                new RankingFragment.RecordsCorrectResponseListener(), new RecordsErrorResponseListener());
        mRequestQueue.add(req);
    }

    private static class RecordsCorrectResponseListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try {
                String logoUrl = response.getString("logoUrl");
                String nick = response.getString("nick");
                int travelledMeters = response.getInt("travelledMeters");
                UserRecordModel userRecord = new UserRecordModel(logoUrl, nick, travelledMeters);
                userRecords.add(userRecord);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RecordsErrorResponseListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            VolleyLog.e("Error while reading records data: ", error.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        ListView listview = view.findViewById(R.id.rankingListView);
        RankingListAdapter adapter = new RankingListAdapter(getActivity());
        listview.setAdapter(adapter);
        return view;
    }
}