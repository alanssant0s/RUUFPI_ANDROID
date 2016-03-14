package com.agenciaflex.ruufpi.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.agenciaflex.ruufpi.MainActivity;
import com.agenciaflex.ruufpi.dao.CardapioDao;
import com.agenciaflex.ruufpi.dao.CardapioItemDao;
import com.agenciaflex.ruufpi.dao.ItemDao;
import com.agenciaflex.ruufpi.model.Cardapio;
import com.agenciaflex.ruufpi.model.Item;
import com.agenciaflex.ruufpi.util.RuUfpiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by alanssantos on 5/4/15.
 */
public class GetCardapioAsyncTask extends AsyncTask<String, Integer, JSONObject> {
    private Context context;
    private SharedPreferences sp;
//    private Context main = null;

    public GetCardapioAsyncTask(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(RuUfpiHelper.spString, context.MODE_PRIVATE);

        if(context instanceof MainActivity) {
            ((MainActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) GetCardapioAsyncTask.this.context).getmPager().setVisibility(View.GONE);
                    ((MainActivity) GetCardapioAsyncTask.this.context).getFloatingMenu().setVisibility(View.GONE);
                    ((MainActivity) GetCardapioAsyncTask.this.context).getpBar().setVisibility(View.VISIBLE);
                }
            });
        }
//        this.main = main;

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, - (c.get(Calendar.DAY_OF_WEEK) - 2));
        String url = RuUfpiHelper.DOMAIN + "/services/get_cardapio/"+c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+sp.getInt("unidade_id", 1);
        Log.w("FlexDEBUG", url);

        JSONObject json = null;

        try {
            String response = RuUfpiHelper.makeGETRequest(url);
            if (response != null) {
                json = new JSONObject(response);
            }
        } catch (JSONException e) {
            Log.w("FlexDEBUG", "Erro");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            if (json != null && json.getString("Success").compareTo("true") == 0) {
                Log.w("FlexDEBUG", json.toString());
                //DBs
                CardapioDao cardapioDao = new CardapioDao(context);
                ItemDao itemDao = new ItemDao(context);
                CardapioItemDao cardItemDao = new CardapioItemDao(context);
                cardapioDao.open();
                itemDao.open();
                cardItemDao.open();
                cardItemDao.deleteAll();
                cardapioDao.deleteAll();
//                    itemDao.deleteAll();

                //CDs opened
                Log.w("FlexDEBUG", "Entrou");
                JSONArray data = json.getJSONArray("Data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject card = data.getJSONObject(i).getJSONObject("cardapios");
                    JSONObject ite = data.getJSONObject(i).getJSONObject("items");

                    if (cardapioDao.selectById(card.getInt("id")) == null) {
                        Cardapio cardapio = new Cardapio();
                        cardapio.setId(card.getInt("id"));
                        cardapio.setUnidade_id(card.getInt("unidade_restaurante_id"));
                        cardapio.setType_id(card.getInt("type_id"));
                        cardapio.setDay(card.getString("day"));
                        cardapio.setHorario(card.getString("horario"));

                        cardapioDao.create(cardapio);
                    }
                    if (itemDao.selectById(ite.getInt("id")) == null) {
                        Item item = new Item();
                        item.setId(ite.getInt("id"));
                        item.setItem_category_id(ite.getInt("item_category_id"));
                        item.setName(ite.getString("name"));

                        itemDao.create(item);
                    }

                    cardItemDao.create(card.getInt("id"), ite.getInt("id"));
                }
                cardapioDao.close();
                itemDao.close();
                cardItemDao.close();

                if(context instanceof MainActivity){
                    Toast.makeText(context, "Cardápio atualizado com sucesso.", Toast.LENGTH_LONG).show();
                    ((MainActivity)context).updateAdapter();
                    ((MainActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity)context).getpBar().setVisibility(View.GONE);
                            ((MainActivity)context).getmPager().setVisibility(View.VISIBLE);
                        }
                    });
                }


                Toast.makeText(context, "Cardápio atualizado com sucesso.", Toast.LENGTH_LONG).show();

            } else {
                Log.w("FlexDEBUG", "Não Entrou");
                ((MainActivity)context).getError().setVisibility(View.VISIBLE);
                ((MainActivity)context).getpBar().setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

            e.printStackTrace();
        } finally {
            super.onPostExecute(json);
        }


    }

}
