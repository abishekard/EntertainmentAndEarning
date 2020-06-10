package fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.example.browser.R;
import com.example.browser.info;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.example.browser.info;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link m.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link m#newInstance} factory method to
 * create an instance of this fragment.
 */
public class m extends Fragment {

    private View mholder;
    private Button btn,wel;
    private FirebaseAuth mAuth;
    private String point;
    private TextView uname,p,mrs;
    private int a,dataIndicator;
    private SwipeRefreshLayout swipetorefresh;
    private PublisherAdView adView;
    private LottieAnimationView no;


    //video
    private RewardedVideoAdListener mRewardedVideoAd;
    //video

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public m() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment m.
     */
    // TODO: Rename and change types and number of parameters
    public static m newInstance(String param1, String param2) {
        m fragment = new m();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
        mholder= inflater.inflate(R.layout.fragment_m, container, false);

        info user=new info(getContext());

        dataIndicator=0;
        point = "";
        p=mholder.findViewById(R.id.p);
        mrs =mholder.findViewById(R.id.mrs);
        no = mholder.findViewById(R.id.no);


        uname = mholder.findViewById(R.id.uname);
        uname.setText(user.getUserName());
        p.setText(user.getPoints());
        mrs.setText((Integer.parseInt(user.getPoints())/5)+"");


        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getContext());
        swipetorefresh = mholder.findViewById(R.id.swipetorefresh);

        swipetorefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(connected()) {

                    final ProgressDialog progressDialog =new ProgressDialog(getContext());
                    progressDialog.setMessage("Loading");
                    progressDialog.show();

                    FirebaseDatabase.getInstance().getReference().child("my_users").child(mAuth.getCurrentUser().getUid()).
                        child("points").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        point = dataSnapshot.getValue().toString();


                        if(point != "")
                        {
                            info user = new info(getContext());
                            user.setPoints(point);
                            p.setText(point);
                            a = Integer.parseInt(point) / 5;
                            mrs.setText(a + "");
                            progressDialog.dismiss();
                            dataIndicator=1;

                        }
                        else{
                            Toast.makeText(getContext(),"Please restart your app",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            dataIndicator=1;
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                    try{
                        no.setVisibility(View.GONE);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(dataIndicator==0)
                            {
                                FancyToast.makeText(getContext(),"please restart your app",
                                        FancyToast.INFO,FancyToast.LENGTH_LONG,false).show();

                                progressDialog.dismiss();


                            }
                        }
                    },10000);

                }
                else
                {
                    FancyToast.makeText(getContext(),"check your internet connection",
                            FancyToast.WARNING,FancyToast.LENGTH_SHORT,false).show();

                }
                swipetorefresh.setRefreshing(false);

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
                 //   + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    @Override
    public void onStart() {

        super.onStart();

       /* FirebaseDatabase.getInstance().getReference().child("my_users").child(mAuth.getCurrentUser().getUid()).
                child("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                point = dataSnapshot.getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private boolean connected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null && activeNetworkInfo.isConnected();
    }



}
