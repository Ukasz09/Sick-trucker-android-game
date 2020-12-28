package com.github.ukasz09.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ukasz09.DataFetchingObserver;
import com.github.ukasz09.R;
import com.github.ukasz09.RecordsListAdapter;

public class RankingFragment extends Fragment implements DataFetchingObserver {
    private ProgressBar progressBar;
    private TextView dataFetchingError;
    private TextView dataFetchingErrorDetails;

    public RankingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        initProgressBar(view);
        initErrorsInfo(view);
        RecordsListAdapter adapter = new RecordsListAdapter(getActivity(), this);
        ListView listview = view.findViewById(R.id.rankingListView);
        listview.setAdapter(adapter);
        return view;
    }

    private void initProgressBar(View view) {
        progressBar = view.findViewById(R.id.rankingDataLoadingSpinner);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initErrorsInfo(View view) {
        dataFetchingError = view.findViewById(R.id.dataFetchingError);
        dataFetchingError.setVisibility(View.GONE);
        dataFetchingErrorDetails = view.findViewById(R.id.dataFetchingErrorDetails);
        dataFetchingErrorDetails.setVisibility(View.GONE);

    }

    @Override
    public void onDataFetchingError(String errorMsg) {
        progressBar.setVisibility(View.GONE);
        dataFetchingError.setVisibility(View.VISIBLE);
        dataFetchingErrorDetails.setVisibility(View.VISIBLE);
        dataFetchingErrorDetails.setText(errorMsg);
    }

    @Override
    public void onDataFetched() {
        progressBar.setVisibility(View.GONE);
    }
}