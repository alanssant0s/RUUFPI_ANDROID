package com.agenciaflex.ruufpi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by alanssantos on 2/9/15.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    private String[] values;

    public SpinnerAdapter(Context ctx, int txtViewResourceId, String[] objects) {
        super(ctx, txtViewResourceId, objects);
        inflater = (LayoutInflater)ctx.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        this.values = objects;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, prnt);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, prnt);
    }

    public View getCustomView(int position, ViewGroup parent) {
        View convertView = inflater.inflate(R.layout.spinner, parent, false);
        TextView main_text = (TextView) convertView.findViewById(R.id.textView);
        main_text.setText(values[position]);
        return convertView;
    }
}