package swim.pwr.bikeridinggame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

    public RecordsListAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return RankingFragment.userRecords.size();
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
        UserRecordModel userRecord = RankingFragment.userRecords.get(position);
        //TODO: change to getting image from url
        String rank = String.valueOf(position + 1);
        rankingView.playerRank.setText(rank);
        final int logoResourceId = context.getResources().getIdentifier(userRecord.logoUrl, "drawable", context.getPackageName());
        rankingView.logo.setImageResource(logoResourceId);
        rankingView.nick.setText(userRecord.nick);
        rankingView.travelledMeters.setText(String.format("%s%s", userRecord.travelledMeters, "m"));
    }
}
