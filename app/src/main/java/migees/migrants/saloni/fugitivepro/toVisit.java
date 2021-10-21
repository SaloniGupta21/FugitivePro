package migees.migrants.saloni.fugitivepro;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import migees.migrants.saloni.fugitivepro.R;

public class toVisit extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ListView lv;
    private ArrayAdapter<String> mAdapter;
    private Typeface mTypeface;
    List<String> container;
    SharedPreferences sharedpreferences;
    String lang;
    private PublisherAdView mPublisherAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_visit);

        sharedpreferences = getSharedPreferences("locationPref", Context.MODE_PRIVATE);
        lang = sharedpreferences.getString("language", "en");

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_to_visit);


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

        final String[] defaultContainer = { "Amusement Park", "Bank", "Beauty salon", "Bar", "Car repair",
                "florist", "Pharmacy", "Post office", "Gas station", "Hospital", "Movie theater", "Restaurant",
                "School", "Shoe store", "Shopping mall", "Spa", " "
        };

        container = new ArrayList<>();
        container.add(  getApplicationContext().getResources().getString(R.string.amusementPark));
        container.add(getResources().getString(R.string.bank));
        container.add(getResources().getString(R.string.beautysalon));
        container.add(getResources().getString(R.string.bar));
        container.add(getResources().getString(R.string.carRepair));
        container.add(getResources().getString(R.string.florist));
        container.add(getResources().getString(R.string.pharmacy));
        container.add(getResources().getString(R.string.postoffice));
        container.add(getResources().getString(R.string.gasstation));
        container.add(getResources().getString(R.string.hospital));
        container.add(getResources().getString(R.string.movietheater));
        container.add(getResources().getString(R.string.restaurant));
        container.add(getResources().getString(R.string.school));
        container.add(getResources().getString(R.string.shoestore));
        container.add(getResources().getString(R.string.shoppingmall));
        container.add(getResources().getString(R.string.spa));
        container.add(" ");

        lv = findViewById(R.id.listOfPlaces);

        mTypeface = Typeface.createFromAsset(getAssets(),"fonts/weathericons-regular-webfont.ttf");

        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,container){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                // Set the typeface/font for the current item
                item.setTypeface(mTypeface);

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#f2edee"));

                // Set the item text style to bold
                item.setTypeface(item.getTypeface(), Typeface.BOLD);

                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);

                // return the view
                return item;
            }
        };

        // Data bind the list view with array adapter items
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = defaultContainer[position];

                Intent intent = new Intent(toVisit.this, individualType.class);
                intent.putExtra("type", category);
                startActivity(intent);
                 }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPublisherAdView  != null) {
            mPublisherAdView.resume();
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent(toVisit.this, individualType.class);
        intent.putExtra("type", query.toLowerCase());
        startActivity(intent);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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


