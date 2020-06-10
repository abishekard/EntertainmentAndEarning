package fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.browser.R;
import com.example.browser.info;
import com.example.browser.web;
import com.example.browser.welcome;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shashank.sony.fancytoastlib.FancyToast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link hom.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link hom#newInstance} factory method to
 * create an instance of this fragment.
 */
public class hom extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mholder;
    private OnFragmentInteractionListener mListener;

    private PublisherAdView ad1,ad2,ad3,ad4,ad5;
    private ImageView yt,tik,zee,hn,en,mx;
    private InterstitialAd mInterstitialAd;
    private int randomAd;


    public hom() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment hom.
     */
    // TODO: Rename and change types and number of parameters
    public static hom newInstance(String param1, String param2) {
        hom fragment = new hom();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mholder = inflater.inflate(R.layout.fragment_hom, container, false);
        ad1 =mholder.findViewById(R.id.ad1);
        ad2 =mholder.findViewById(R.id.ad2);
        ad3 =mholder.findViewById(R.id.ad3);
        ad4 =mholder.findViewById(R.id.ad4);
        ad5 =mholder.findViewById(R.id.ad5);
        yt =mholder.findViewById(R.id.yt);
        tik =mholder.findViewById(R.id.tik);
        hn =mholder.findViewById(R.id.hn);
        en =mholder.findViewById(R.id.en);
        zee =mholder.findViewById(R.id.zee);
        mx=mholder.findViewById(R.id.mx);
        yt.setOnClickListener(this);
        tik.setOnClickListener(this);
        zee.setOnClickListener(this);
        hn.setOnClickListener(this);
        en.setOnClickListener(this);
        mx.setOnClickListener(this);
        randomAd= info.getRandomNumber(1,11);


        PublisherAdRequest adRequest1 = new PublisherAdRequest.Builder().build();
        ad1.loadAd(adRequest1);

        PublisherAdRequest adRequest2 = new PublisherAdRequest.Builder().build();
        ad2.loadAd(adRequest2);

        PublisherAdRequest adRequest3 = new PublisherAdRequest.Builder().build();
        ad3.loadAd(adRequest3);

        PublisherAdRequest adRequest4 = new PublisherAdRequest.Builder().build();
        ad4.loadAd(adRequest4);

        PublisherAdRequest adRequest5 = new PublisherAdRequest.Builder().build();
        ad5.loadAd(adRequest5);

        MobileAds.initialize(getContext(),"ca-app-pub-4499095708288541~5287715383");

        mInterstitialAd = new InterstitialAd(getContext());







        if(randomAd<=5)
        {

            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

        }
        else if(randomAd<9 && randomAd>=5){

            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

        }
        else{

            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

        }


        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                mInterstitialAd.show();
            }
        });


        return mholder;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
           // throw new RuntimeException(context.toString()
              //      + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), web.class);
        if(connected()) {
            switch (v.getId()) {
                case R.id.yt:
                    intent.putExtra("url", "https://www.youtube.com/");
                    startActivity(intent);
                    //loadad();

                    break;
                case R.id.tik:
                    intent.putExtra("url", "https://www.tiktok.com/trending");
                    startActivity(intent);
                    //loadad();
                    break;
                case R.id.zee:
                    intent.putExtra("url", "https://www.zee5.com");
                    startActivity(intent);
                    //loadad();
                    break;
                case R.id.hn:
                    intent.putExtra("url", "https://www.amarujala.com");
                    startActivity(intent);
                    //loadad();
                    break;
                case R.id.en:
                    intent.putExtra("url", "https://m.timesofindia.com");
                    startActivity(intent);
                    //loadad();
                    break;
                case R.id.mx:intent.putExtra("url", "https://www.mxplayer.in");
                    startActivity(intent);
                    //loadad();
                    break;


            }
        }
        else
        {
            FancyToast.makeText(getContext(),"check your internet connection",FancyToast.WARNING,
                    FancyToast.LENGTH_SHORT,false).show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void loadad()
    {

      /*  if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }*/
    }
    private boolean connected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null && activeNetworkInfo.isConnected();
    }


}
