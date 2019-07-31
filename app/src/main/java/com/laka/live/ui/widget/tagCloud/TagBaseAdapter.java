package com.laka.live.ui.widget.tagCloud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.laka.live.R;

import java.util.List;

/**
 * @author fyales
 * @since 8/26/15.
 */
public class TagBaseAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    private int selectedPosition = -1;
    public TagBaseAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tag_view, null);
            holder = new ViewHolder();
            holder.tagBtn = (Button) convertView.findViewById(R.id.tag_btn);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final String text = getItem(position);
        holder.tagBtn.setText(text);
        if(position==selectedPosition){
            holder.tagBtn.setSelected(true);
        }else{
            holder.tagBtn.setSelected(false);
        }
        return convertView;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
    public int getSelectedPosition() {
      return selectedPosition;
    }
    static class ViewHolder {
        Button tagBtn;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        selectedPosition = -1;
    }
}
