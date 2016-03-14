package com.agenciaflex.ruufpi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agenciaflex.ruufpi.R;
import com.agenciaflex.ruufpi.model.Item;

import java.util.List;

/**
 * Created by alanssantos on 4/13/15.
 */
public class MainAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Item> list;

    public MainAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = list.get(position);

        convertView = inflater.inflate(R.layout.main_fragment_adapter,null);

        MainHolder h = new MainHolder();

        h.textView = (TextView)convertView.findViewById(R.id.textView);
        h.textView.setText(item.getName());

        return convertView;
    }

    private class MainHolder {
        TextView textView;
    }
}
