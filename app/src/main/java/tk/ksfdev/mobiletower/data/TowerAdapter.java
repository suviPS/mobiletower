package tk.ksfdev.mobiletower.data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tk.ksfdev.mobiletower.MapsActivity;
import tk.ksfdev.mobiletower.R;


public class TowerAdapter extends RecyclerView.Adapter<TowerAdapter.TowerViewHolder>{

    private ArrayList<MobileTower> mList;
    private Context mContext;


    public TowerAdapter(Context context){
        mContext = context;
    }

    @Override
    public TowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_tower_layout, parent, false);
        return new TowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TowerViewHolder holder, int position) {
        final MobileTower mTower = mList.get(position);
        String txt = "[Signal] type: " + mTower.getConnectionType() + " | str:  " + mTower.getSignalDbm() + "dBm" +
                "\n[Tower] mcc: " + mTower.getMcc() + " | mnc: " + mTower.getMnc() + " | lac: " + mTower.getLac() + " | cid: " + mTower.getCid();

        holder.mTextView.setText(txt);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                intent.putExtra(DataUtils.TOWER_MCC, mTower.getMcc());
                intent.putExtra(DataUtils.TOWER_MNC, mTower.getMnc());
                intent.putExtra(DataUtils.TOWER_LAC, mTower.getLac());
                intent.putExtra(DataUtils.TOWER_CID, mTower.getCid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList == null)
            return 0;
        return mList.size();
    }

    public void swapData(ArrayList<MobileTower> list){
        mList = list;
        this.notifyDataSetChanged();
    }


    //view holder
    class TowerViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;

        TowerViewHolder(View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.tower_textview01);

        }
    }
}
