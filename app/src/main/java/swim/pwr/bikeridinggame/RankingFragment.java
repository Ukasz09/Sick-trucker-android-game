package swim.pwr.bikeridinggame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import swim.pwr.bikeridinggame.models.UserRecordModel;

public class RankingFragment extends Fragment {
    //TODO: tmp mocked
    public static UserRecordModel[] userRecords = {
            new UserRecordModel("author_logo", "John", 324),
            new UserRecordModel("author_logo", "Stasi", 23),
            new UserRecordModel("author_logo", "Bolko", 122),
            new UserRecordModel("author_logo", "Vole", 2),
            new UserRecordModel("author_logo", "Michu", 2),
            new UserRecordModel("author_logo", "Kennedi", 2),
    };

    public RankingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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