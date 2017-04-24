package com.magpie.magpie;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.magpie.magpie.CollectionUtils.Collection;
import com.magpie.magpie.CollectionUtils.Element;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BadgePage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BadgePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BadgePage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Collection c;
    private ListView lv;
    private GridView gv;
    private TextView tv;
    private TabLayout viewTabs;
    private TabLayout.Tab listTab, gridTab;
    private ProgressBar pb;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BadgePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BadgePage.
     */
    // TODO: Rename and change types and number of parameters
    public static BadgePage newInstance(String param1, String param2) {
        BadgePage fragment = new BadgePage();
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
            Bundle b = getArguments();
            c = (Collection) b.getSerializable("TheCollection");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_badge_page, container, false);
        tv = (TextView) v.findViewById(R.id.CollectionTitle);
        gv = (GridView) v.findViewById(R.id.BadgeGridView);
        lv = (ListView) v.findViewById(R.id.BadgeListView);
        viewTabs = (TabLayout) v.findViewById(R.id.ViewTabs);
        listTab = viewTabs.getTabAt(0);
        gridTab = viewTabs.getTabAt(1);
        viewTabs.setOnTabSelectedListener(tlSelected);
        pb = (ProgressBar) v.findViewById(R.id.CollectionProgress);
        pb.setMax(c.getCollectionSize());
        pb.setProgress(c.getCollected());
        tv.setText(c.getName());
        ArrayList<String> elementNames = elementToString(c);
        ArrayAdapter<String> badgeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, elementNames);
        lv.setAdapter(badgeAdapter);
        gv.setAdapter(badgeAdapter);
        lv.setOnItemClickListener(onLVClick);
        gv.setOnItemClickListener(onGVClick);
        badgeAdapter.notifyDataSetChanged();
        return v;
    }

    TabLayout.OnTabSelectedListener tlSelected = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if(tab.getPosition() == 0)
                lv.setVisibility(View.VISIBLE);
            else
                gv.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if(tab.getPosition() == 0)
                lv.setVisibility(View.INVISIBLE);
            else
                gv.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            if(tab.getPosition() == 0)
                lv.setVisibility(View.VISIBLE);
            else
                gv.setVisibility(View.VISIBLE);
        }
    };

    AdapterView.OnItemClickListener onLVClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Element e = c.getCollectionElements().get(i);
            c.setCollected(e);
            pb.setProgress(c.getCollected());
            Toast.makeText(getContext(), e.getCollectionID() + " - " + e.getName() + ": " + e.getLatitude() + ", " + e.getLongitude(), Toast.LENGTH_SHORT).show();
            //Ultimately, will be sending the whole collection
        }
    };

    AdapterView.OnItemClickListener onGVClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Element e = c.getCollectionElements().get(i);
            c.setCollected(e);
            pb.setProgress(c.getCollected());
            Toast.makeText(getContext(), e.getCollectionID() + " - " + e.getName() + ": " + e.getLatitude() + ", " + e.getLongitude(), Toast.LENGTH_SHORT).show();
            //Ultimately, will be sending the whole collection
        }
    };

    private ArrayList<String> elementToString(Collection c) {
        ArrayList<String> temp = new ArrayList<>();
        for(Element e : c.getCollectionElements()){
            temp.add(e.getName());
        }
        return temp;
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
}
