package fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.browser.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link gam.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link gam#newInstance} factory method to
 * create an instance of this fragment.
 */
public class gam extends Fragment implements View.OnClickListener {

    View mholder;
    private String string;
    private GridLayout gridLayout;
    private ImageView g1,g2,g3,g4,g5,g6;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public gam() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment gam.
     */
    // TODO: Rename and change types and number of parameters
    public static gam newInstance(String param1, String param2) {
        gam fragment = new gam();
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
        mholder = inflater.inflate(R.layout.fragment_gam, container, false);

        gridLayout = mholder.findViewById(R.id.gridLayout);
        g1=mholder.findViewById(R.id.g1);
        g2=mholder.findViewById(R.id.g2);
        g3=mholder.findViewById(R.id.g3);
        g4=mholder.findViewById(R.id.g4);
        g5=mholder.findViewById(R.id.g5);
        g6=mholder.findViewById(R.id.g6);
        g1.setOnClickListener(this);
        g2.setOnClickListener(this);
        g3.setOnClickListener(this);
        g4.setOnClickListener(this);
        g5.setOnClickListener(this);
        g6.setOnClickListener(this);


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
          //  throw new RuntimeException(context.toString()
                  //  + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId())
        {
            case R.id.g1:
                string="https://www.arkadium.com/games/sweet-shuffle/";
                next(string);
                break;
            case R.id.g2:
                string="https://www.arkadium.com/games/ten-x-ten/";
                next(string);
                break;
            case R.id.g3:
                string="https://www.arkadium.com/games/bubble-dragons/";
                next(string);
                break;
            case R.id.g4:
                string="https://www.arkadium.com/games/knife-smash/\n";
                next(string);
                break;
            case R.id.g5:
                string="https://www.arkadium.com/games/ballistic/";
                next(string);
                break;
            case R.id.g6:
                string="https://www.arkadium.com/games/lumeno/\n";
                next(string);
                break;
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

    private void next(String url)
    {

        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        startActivity(intent);

        /*Intent intent = new Intent(getContext(), onlineGmes.class);
        intent.putExtra("url",url);
        startActivity(intent);*/
    }
}
