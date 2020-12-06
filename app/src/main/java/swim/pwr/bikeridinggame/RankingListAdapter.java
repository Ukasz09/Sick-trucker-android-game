package swim.pwr.bikeridinggame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import swim.pwr.bikeridinggame.models.UserRecordModel;

public class RankingListAdapter extends BaseAdapter {
    private class RankingView {
        public ImageView logo;
        public TextView nick;
        public TextView travelledMeters;
    }

    private Context context;
    private RankingView rankingView;
    private LayoutInflater inflater;

    public RankingListAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return RankingFragment.userRecords.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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
        rankingView.logo = listView.findViewById(R.id.logoImage);
        rankingView.nick = listView.findViewById(R.id.nickText);
        rankingView.travelledMeters = listView.findViewById(R.id.travelledText);
    }

    private void setProperRankingView(int position) {
        UserRecordModel userRecord = RankingFragment.userRecords[position];
        final int resourceId = context.getResources().getIdentifier(userRecord.logoUrl, "drawable", context.getPackageName());
        rankingView.logo.setImageResource(resourceId);
        rankingView.nick.setText(rankingView.nick.getText());
        rankingView.travelledMeters.setText(rankingView.travelledMeters.getText());
    }
}
