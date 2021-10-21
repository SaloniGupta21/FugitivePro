package migees.migrants.saloni.fugitivepro;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import migees.migrants.saloni.fugitivepro.R;

public class individualType extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    List<Places> findPlaces;
    TextView placeName, placeAddress, placeOpen, placeRating;
    Button directions;
    FloatingActionButton fabMAp;

    //recycler view
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Places> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    private PublisherAdView mPublisherAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences("locationPref", Context.MODE_PRIVATE);
        String lang = sharedpreferences.getString("language", "en");

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_individual_type);

        mPublisherAdView = findViewById(R.id.publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().addTestDevice("8FBF3A81751A4BCF63A01844D0657D03").build();

        mPublisherAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mPublisherAdView.loadAd(adRequest);

        Intent receiver = getIntent();
        String types = receiver.getStringExtra("type");
        types = types.trim();
        if (types.contains(" "))
            types = types.replaceAll(" ", "+").toLowerCase();

    //    myOnClickListener = new MyOnClickListener(this);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        removedItems = new ArrayList<Integer>();

        Toast.makeText(this, types, Toast.LENGTH_LONG).show();

        new GetPlaces(this, recyclerView, types).execute();

        fabMAp = findViewById(R.id.dispPlaces);
        fabMAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(individualType.this, MapsActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) findPlaces);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);

                //startActivity(new Intent(individualType.this, MapsActivity.class).putExtra("listOfPlaces", (List<Places>)findPlaces));
            }
        });

    }

    class GetPlaces extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private Context context;
        private String[] placeName;
        private String[] imageUrl;
        private RecyclerView recyclerView;
        private String category;

        public GetPlaces(Context context, RecyclerView recyclerView, String category) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.recyclerView = recyclerView;
            this.category = category;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();

            if(findPlaces.isEmpty())
            {
                Toast.makeText(context, "Could not find any results", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(individualType.this, toVisit.class));
                finish();
            }

            adapter = new CustomAdapter(findPlaces, context);
            recyclerView.setAdapter(adapter);

        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(true);
            dialog.setMessage(context.getResources().getString(R.string.dialogLoading));
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            PlacesService service = new PlacesService("AIzaSyAK9UwHVySsk3ikuM7yLupGai4eLeevSKQ");

            // "AIzaSyAIMWuVkqexqQPIuxHO93RznuwIfUHyMpY"
            Double latitude = Double.valueOf(sharedpreferences.getString("latitude", "0.00"));
            Double longitude = Double.valueOf(sharedpreferences.getString("longitude", "0.00"));
            findPlaces = service.findPlaces(latitude, longitude, category, sharedpreferences.getString("language", "en"));
            // hospiral for hospital
            // atm for ATM


                placeName = new String[findPlaces.size()];
                imageUrl = new String[findPlaces.size()];

                for (int i = 0; i < findPlaces.size(); i++) {

                    Places placesDetail = findPlaces.get(i);
                    placesDetail.getIcon();

                    //  System.out.println(  placesDetail.getName());
                    placeName[i] = placesDetail.getName();

                    imageUrl[i] = placesDetail.getIcon();

                }


            return null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPublisherAdView != null) {
            mPublisherAdView .pause();
        }
    }

    @Override
    public void onPause() {
        if (mPublisherAdView != null) {
            mPublisherAdView .pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mPublisherAdView  != null) {
            mPublisherAdView .destroy();
        }
        super.onDestroy();
    }


}
