package com.agenciaflex.ruufpi.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.agenciaflex.ruufpi.R;
import com.agenciaflex.ruufpi.adapters.MyAdapter;
import com.agenciaflex.ruufpi.listener.RecyclerViewOnItemTouchListener;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;

public class RuUfpiHelper {
    public static String DOMAIN = "http://ruufpi.educacao.ws/service";

    public static String spString = "SharedPreferencesRuUfpi";

    public static String[] unidades = {"Teresina", "Parnaiba"};
    public static String[] unidades_campi = {"M.P.P.", "M.R.V."};

    public static String TITLES[] = {"Cardápio", "Unidades", "Sobre"};
    public static int ICONS[] = {R.drawable.ic_silverware_variant, R.drawable.ic_map_marker_radius, R.drawable.ic_alert_circle};

    public static int CARDAPIO_INDEX = 1;
    public static int UNIDADE_INDEX_ = 2;
    public static int SOBRE_INDEX = 3;

    private static String SENDER_ID = "707525293378";

    public static final String TAG = "Script";
    private static GoogleCloudMessaging gcm;
    public static String regId;


    public static String getFormatedData(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());

        if (date != null) {
            return sdf.format(date);
        } else {
            return "";
        }
    }

    public static Date convertStringDateToDate(String date) {
        try {
            if (date.length() == 0) {
                return null;
            } else if (date.length() > 11) {
                return (Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()).parse(date);

            } else {
                return (Date) new SimpleDateFormat("yyyy-MM-dd",
                        Locale.getDefault()).parse(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getTimeCurrent() {
        Calendar cal = Calendar.getInstance();

        Date timeNow = new Date();

        cal.setTime(timeNow);

        // Um numero negativo vai decrementar a data
        cal.add(Calendar.MILLISECOND,
                TimeZone.getDefault().getOffset(timeNow.getTime()) * -1);

        return cal.getTime();
    }

    public static String getHumanData(Date date) {
        if (date != null) {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(date);
        } else {
            return "NULL";
        }
    }

    public static int diffInMinutes(Date d1, Date d2) {
        int MILLIS_IN_MINUTE = 60000;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        // c1.set(Calendar.MILLISECOND, 0);
        // c1.set(Calendar.SECOND, 0);
        // c1.set(Calendar.MINUTE, 0);
        // c1.set(Calendar.HOUR_OF_DAY, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        // c2.set(Calendar.MILLISECOND, 0);
        // c2.set(Calendar.SECOND, 0);
        // c2.set(Calendar.MINUTE, 0);
        // c2.set(Calendar.HOUR_OF_DAY, 0);
        return (int) ((c1.getTimeInMillis() - c2.getTimeInMillis()) / MILLIS_IN_MINUTE);
    }

    public static String makeGETRequest(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);

        // Fazendo a requisicao ao servidor
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            return httpclient.execute(get, responseHandler);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // public static String makePUTRequest(String url, MultipartEntityBuilder
    // meb) {
    // // Atualizar na WEB
    // HttpClient httpclient = new DefaultHttpClient();
    // HttpPut put = new HttpPut(url);
    //
    // try {
    // put.setEntity(meb.build());
    //
    // // setup the request headers
    // // post.setHeader("Accept", "application/json");
    // // post.setHeader("Content-Type", "application/json");
    // // post.setHeader("Content-Type", "image/jpeg");
    //
    // ResponseHandler<String> responseHandler = new BasicResponseHandler();
    // return httpclient.execute(put, responseHandler);
    // } catch (HttpResponseException e) {
    // e.printStackTrace();
    // return null;
    // } catch (IOException e) {
    // e.printStackTrace();
    // return null;
    // }
    // }
    //
    public static String makePOSTRequest(String url, MultipartEntityBuilder
            meb) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        try {
            post.setEntity(meb.build());

            // setup the request headers
            // post.setHeader("Accept", "application/json);
            // post.setHeader("Content-Type", "application/json");
            // post.setHeader("Content-Type", "image/jpeg");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            return httpclient.execute(post, responseHandler);
        } catch (HttpResponseException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromWeb(String imageURL) {
        Bitmap bitmap = null;
        if (imageURL != null && imageURL.compareTo("null") != 0) {
            try {
                InputStream in = new URL(DOMAIN + imageURL).openStream();

                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        } else {
            return null;
        }
    }

    public static Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        if (byteArray != null) {
            try {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(
                        byteArray);
                return BitmapFactory.decodeStream(imageStream);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void makeDrawer(RecyclerView mRecyclerView, RecyclerView.Adapter mAdapter,
                                  RecyclerView.LayoutManager mLayoutManager, DrawerLayout Drawer,
                                  ActionBarDrawerToggle mDrawerToggle, Context c, View v, int ICON,
                                  int index, Toolbar toolbar, Activity activity) {

        mRecyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(ICON, index);
        mRecyclerView.setAdapter(mAdapter);
        Drawer = (DrawerLayout) v.findViewById(R.id.DrawerLayout);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewOnItemTouchListener(c, Drawer, index));
        mLayoutManager = new LinearLayoutManager(c);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDrawerToggle = new ActionBarDrawerToggle(activity, Drawer, toolbar, R.string.open, R.string.close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }


        };

        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();
    }

    public static void registerIdInBackground(final Context ctx) {
        new AsyncTask<Object,Object,Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(ctx);
                    }

                    regId = gcm.register(SENDER_ID);

                    msg = "Register Id: " + regId;

                    String url = RuUfpiHelper.DOMAIN + "/services/registration_id";
                    MultipartEntityBuilder meb = MultipartEntityBuilder.create();
                    meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    meb.addTextBody("data[RegistrationsId][registration_id]", regId+"");
                    meb.addTextBody("data[RegistrationsId][unidade_id]", ctx.getSharedPreferences(spString, ctx.MODE_PRIVATE).getInt("unidade_id",1) + "");


                    String feedback = RuUfpiHelper.makePOSTRequest(url, meb);

                    AndroidSystemUtil.storeRegistrationId(ctx, regId);
                } catch (IOException e) {
                    Log.i(TAG, e.getMessage());
                }

                return msg;
            }

            @Override
            public void onPostExecute(Object msg) {
//                tvRegistrationId.setText((String) msg);
            }

        }.execute(null, null, null);
    }
}
