package com.agenciaflex.ruufpi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agenciaflex.ruufpi.R;
import com.agenciaflex.ruufpi.util.RuUfpiHelper;

/**
 * Created by hp1 on 28-12-2014.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEMx= 1;
    private static final int TYPE_FOCU = 2;

    private String mNavTitles[] = RuUfpiHelper.TITLES;
    private int mIcons[] = RuUfpiHelper.ICONS;
    private int index;
    private int iconIndex;

    public MyAdapter( int Icon, int index) {
        this.index = index;
        iconIndex = Icon;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private int Holderid;
        private TextView textView;
        private ImageView imageView;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM || ViewType == TYPE_FOCU) {

                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                Holderid = 1;
            } else {
                Holderid = 0;
            }
        }

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            if (viewType != TYPE_ITEM) {
                v.findViewById(R.id.layout).setBackgroundColor(v.getResources().getColor(R.color.backgroudColor));
                vhItem.textView.setTextColor(v.getResources().getColor(R.color.fontFocus));
            }

            return vhItem; // Returning the created object
        }

    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        if (holder.Holderid == 1) {
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            if(position != index)
                holder.imageView.setImageResource(mIcons[position - 1]);// Settimg the image with array of our icons
            else
                holder.imageView.setImageResource(iconIndex);// Settimg the image with array of our icons
        } else {

        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length + 1; // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        if(position == index)
            return TYPE_FOCU;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}