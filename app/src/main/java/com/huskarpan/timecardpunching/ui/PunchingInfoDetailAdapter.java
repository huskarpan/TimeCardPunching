package com.huskarpan.timecardpunching.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huskarpan.timecardpunching.R;
import com.huskarpan.timecardpunching.entities.PunchingInfoDetail;
import com.huskarpan.timecardpunching.entities.PunchingInfoRecord;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

/**
 * Created by Huskar on 2015/9/19.
 *
 * @description 打卡明细数据适配器
 */
public class PunchingInfoDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final Context mContext;
    private final List<PunchingInfoDetail> mDetails;
    private final LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public PunchingInfoDetailAdapter(Context context, List<PunchingInfoDetail> details) {
        mContext=context;
        mDetails =details;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_item_punching, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        PunchingInfoDetail detail = mDetails.get(position);
        holder.getTvPunchingTime().setText(detail.getFormatTime(mContext));
        boolean isOnDuty = detail.getRecordType() == PunchingInfoDetail.RECORD_TYPE_ON_DUTY;
        TextView tvPunchingAction = holder.getTvPunchingAction();
        tvPunchingAction.setText(mContext.getString(
                isOnDuty ? R.string.punching_type_on_duty : R.string.punching_type_off_duty));
        tvPunchingAction.setTextColor(mContext.getResources().getColor(isOnDuty?R.color.color_accent2:R.color.color_accent));
        if (mOnItemClickListener != null) {
            holder.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int layoutPosition = holder.getLayoutPosition();
                    LogUtils.d("onBindViewHolder position:" + position + ", layoutPosition: " + layoutPosition);
                    mOnItemClickListener.OnClick(holder.getContainer(), layoutPosition);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }

    /**
     * 增加一项打卡信息
     * @param punchingInfoDetail 打卡信息明细
     */
    public void Add(PunchingInfoDetail punchingInfoDetail) {
        int position = getItemCount();
        mDetails.add(position, punchingInfoDetail);
        notifyItemInserted(position);
    }

    /**
     * 删除一项打卡信息
     * @param position 删除位置
     */
    public void Delete(int position) {
        LogUtils.d("delete, position: " + position);
        mDetails.remove(position);
        notifyItemRemoved(position);

    }

    /**
     * 设置列表项点击监听器
     * @param listener 列表项点击监听器
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener=listener;
    }

    /**
     * 点击监听器
     */
    public interface OnItemClickListener {

        /**
         * 点击列表项
         * @param itemView 列表项View
         * @param position 位置
         */
        void OnClick(View itemView, int position);
    }
}


class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView tvPunchingAction;
    private TextView tvPunchingTime;
    private View container;
    public MyViewHolder(View itemView) {
        super(itemView);
        container=itemView;
        tvPunchingTime = (TextView) itemView.findViewById(R.id.tv_punchingTime);
        tvPunchingAction = (TextView) itemView.findViewById(R.id.tv_punching_action);
    }

    public TextView getTvPunchingAction() {
        return tvPunchingAction;
    }

    public void setTvPunchingAction(TextView tvPunchingAction) {
        this.tvPunchingAction = tvPunchingAction;
    }

    public TextView getTvPunchingTime() {
        return tvPunchingTime;
    }

    public void setTvPunchingTime(TextView tvPunchingTime) {
        this.tvPunchingTime = tvPunchingTime;
    }

    public View getContainer() {
        return container;
    }
}
