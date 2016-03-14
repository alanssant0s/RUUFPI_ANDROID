package com.agenciaflex.ruufpi.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.agenciaflex.ruufpi.R;

/**
 * Created by alanssantos on 3/16/15.
 */
public class RuDialogFragment extends DialogFragment {

    String title;
    String event;
    Context context;

    /**
     * Create a new instance of MyDialogFragment, providing "num" as an
     * argument.
     */
    public static RuDialogFragment newInstance(String title,String event, Context context) {
        RuDialogFragment f = new RuDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        args.putSerializable("title", title);
        f.context = context;
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getArguments().getString("event");
        title = getArguments().getString("title");

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NO_TITLE;
        setStyle(style, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_dialog, container, false);
        Holder holder = new Holder();
        holder.textView1 = (TextView) view.findViewById(R.id.textView1);
        holder.textView2 = (TextView) view.findViewById(R.id.textView2);
        holder.button1 = (Button) view.findViewById(R.id.button1);

        holder.textView1.setText(title);
        holder.textView1.setTypeface(null, Typeface.BOLD);

        holder.textView2.setText(event);

//        ((LinearLayout) view.findViewById(R.id.dialog_container))
//                .setBackground(getResources().getDrawable(
//                        FeiraHelper.backGround[event.categoria.id - 1]));
//        holder.button1.setBackground(getResources().getDrawable(
//                FeiraHelper.buttonBackGround[event.categoria.id - 1]));
//
//        holder.button2.setBackground(getResources().getDrawable(
//                FeiraHelper.buttonBackGround[event.categoria.id - 1]));

        holder.button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });

        return view;
    }

    class Holder {
        Button button1;
        TextView textView1, textView2;
    }
}