package com.agenciaflex.ruufpi.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.agenciaflex.ruufpi.R;
import com.agenciaflex.ruufpi.adapters.MainAdapter;
import com.agenciaflex.ruufpi.dao.CardapioDao;
import com.agenciaflex.ruufpi.dao.ItemDao;
import com.agenciaflex.ruufpi.model.Cardapio;
import com.agenciaflex.ruufpi.model.Item;
import com.agenciaflex.ruufpi.util.RuUfpiHelper;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by alanssantos on 3/31/15.
 */
public class MainFragment extends Fragment {

    private Toolbar toolbar_janta, toolbar_almoco;
    private Calendar day = new GregorianCalendar();
    private CardapioDao cardapioDao;
    private ItemDao itemDao;

    private ListView almocoListView, jantaListView;
    private MainAdapter almocoAdapter, jantaAdapter;

    private SharedPreferences sp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sp = getActivity().getSharedPreferences(RuUfpiHelper.spString, getActivity().MODE_PRIVATE);
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        day = (Calendar) getArguments().getSerializable("day");
        int d = getArguments().getInt("position");
        day.add(Calendar.DAY_OF_MONTH, d);

        cardapioDao = new CardapioDao(getActivity());
        cardapioDao.open();
        itemDao = new ItemDao(getActivity());
        itemDao.open();

        String ru;

        if (sp.getInt("restaurante_id", 1) == 2)
            ru = "II";
        else if (sp.getInt("restaurante_id", 1) == 3)
            ru = "III";
        else
            ru = "I";


        final float scale = getActivity().getResources().getDisplayMetrics().density;
        int pixels = (int) (29.5 * scale + 0.5f);
        ViewGroup.LayoutParams params = null;

        toolbar_almoco = (Toolbar) view.findViewById(R.id.toolbar_almoco);
        toolbar_almoco.setTitle("Almoço");
        toolbar_janta = (Toolbar) view.findViewById(R.id.toolbar_janta);
        toolbar_janta.setTitle("Jantar");

        //Almoço
        Cardapio cardapioAl = cardapioDao.selectByDayRestaurant(new SimpleDateFormat("yyyy-MM-dd").format(day.getTime()), sp.getInt("restaurante_id", 1), 1);
        List<Item> itemsAl = new ArrayList<>();
        if (cardapioAl != null) {
            itemsAl = itemDao.selectAllItemsByCardapio(cardapioAl.getId());
            almocoAdapter = new MainAdapter(getActivity(), itemsAl);
            almocoListView = (ListView) view.findViewById(R.id.listView_almoco);
            almocoListView.setAdapter(almocoAdapter);

            ((TextView) view.findViewById(R.id.almoco_footer)).setText(((cardapioAl) == null ? "" : cardapioAl.getHorario()) + " RU " + ru);

            params = almocoListView.getLayoutParams();
            params.height = itemsAl.size() * pixels;
            almocoListView.setLayoutParams(params);
        } else {
            view.findViewById(R.id.adefinir_almoco).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.almoco_footer)).setText("RU " + ru);
        }
        //Janta
        Cardapio cardapioJan = null;
        List<Item> itemsJan = new ArrayList<>();
        if (d != 5) {
            cardapioJan = cardapioDao.selectByDayRestaurant(new SimpleDateFormat("yyyy-MM-dd").format(day.getTime()), sp.getInt("restaurante_id", 1), 2);
            if (cardapioJan != null) {
                itemsJan = itemDao.selectAllItemsByCardapio(cardapioJan.getId());
                jantaAdapter = new MainAdapter(getActivity(), itemsJan);
                jantaListView = (ListView) view.findViewById(R.id.listView_janta);
                jantaListView.setAdapter(jantaAdapter);

                ((TextView) view.findViewById(R.id.janta_footer)).setText(((cardapioJan) == null ? "" : cardapioJan.getHorario()) + " RU " + ru);

                params = jantaListView.getLayoutParams();
                params.height = itemsJan.size() * pixels;
                jantaListView.setLayoutParams(params);
            } else {
                view.findViewById(R.id.adefinir_jantar).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.janta_footer)).setText("RU " + ru);
            }
        } else {
            view.findViewById(R.id.card_janta).setVisibility(View.GONE);
        }

        itemDao.close();
        cardapioDao.close();

        return view;
    }
}