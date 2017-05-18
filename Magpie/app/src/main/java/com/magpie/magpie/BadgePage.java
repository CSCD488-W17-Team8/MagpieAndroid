package com.magpie.magpie;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.magpie.magpie.CollectionUtils.Collection;
import com.magpie.magpie.CollectionUtils.Element;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BadgePage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BadgePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BadgePage extends Fragment implements AdapterView.OnItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Collection selectedColl;
    ArrayList<Element> displayElements;
    private ListView badgeList;
    private GridView badgeGrid;
    private TextView collTitle;
    private TabLayout viewTabs;
    private TabLayout.Tab listTab, gridTab;
    private CustomBadgeListAdapter cbla;
    private CustomBadgeListAdapter cbga;
    private Spinner filter;

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

    /*
     * Method onCreate: Handles the obtaining of the Collection to be used in the fragment.
     * It is assumed that there will always be a Collection to use.
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b = getArguments();
            selectedColl = (Collection) b.getSerializable("TheCollection");
        }
    }

    /*
     * Method onCreateView: Creates the visual that the user interacts with.
     * Note that there are two different lists "active" when the fragment is launched.
     * As a result, both are instantiated on launch, and never again until relaunch.
     *
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_badge_page, container, false);
        collTitle = (TextView) v.findViewById(R.id.CollectionTitle);
        badgeGrid = (GridView) v.findViewById(R.id.BadgeGridView);
        badgeList = (ListView) v.findViewById(R.id.BadgeListView);
        viewTabs = (TabLayout) v.findViewById(R.id.ViewTabs);
        listTab = viewTabs.getTabAt(0);
        gridTab = viewTabs.getTabAt(1);
        viewTabs.setOnTabSelectedListener(tlSelected);
        collTitle.setText(selectedColl.getName());
        filter = (Spinner) v.findViewById(R.id.BadgeFilter);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.BadgeFilterArray, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter.setAdapter(spinnerAdapter);
        filter.setOnItemSelectedListener(this);
        //ArrayList<String> elementNames = elementToString(selectedColl);
        displayElements = new ArrayList<>();
        displayElements.addAll(selectedColl.getCollectionElements());
        cbla = new CustomBadgeListAdapter(this, displayElements, "List");
        cbga = new CustomBadgeListAdapter(this, displayElements, "Grid");
        //ArrayAdapter<String> badgeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, elementNames);
        badgeList.setAdapter(cbla);
        badgeGrid.setAdapter(cbga);
        badgeList.setOnItemClickListener(onLVClick);
        badgeGrid.setOnItemClickListener(onGVClick);
        cbla.notifyDataSetChanged();
        cbga.notifyDataSetChanged();
        return v;
    }

    /*
     * TabLayout.onTabSelectedListener tlSelected: Handles the switching between the list and the grid visual. Self-explanatory code.
     *
     */

    TabLayout.OnTabSelectedListener tlSelected = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if(tab.getPosition() == 0)
                badgeList.setVisibility(View.VISIBLE);
            else
                badgeGrid.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if(tab.getPosition() == 0)
                badgeList.setVisibility(View.INVISIBLE);
            else
                badgeGrid.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            if(tab.getPosition() == 0)
                badgeList.setVisibility(View.VISIBLE);
            else
                badgeGrid.setVisibility(View.VISIBLE);
        }
    };

    /*
     * AdapterView.OnItemClickListener onLVClick: handles the selecting of any ListView Item and performs and action on it.
     * It is expected that the action will be to go the the Information Page of the associated Badge/Element.
     *
     */

    AdapterView.OnItemClickListener onLVClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Element e = selectedColl.getCollectionElements().get(i);
            selectedColl.setCollected(e);
            Toast.makeText(getContext(), e.getCollectionID() + " - " + e.getName() + ": " + e.getLatitude() + ", " + e.getLongitude(), Toast.LENGTH_SHORT).show();
            //Ultimately, will be sending the whole collection
        }
    };

    /*
     * AdapterView.OnItemClickListener onGVClick: handles the selecting of any GridView Item and performs and action on it.
     * It is expected that the action will be to go the the Information Page of the associated Badge/Element.
     *
     */

    AdapterView.OnItemClickListener onGVClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Element e = selectedColl.getCollectionElements().get(i);
            selectedColl.setCollected(e);
            Toast.makeText(getContext(), e.getCollectionID() + " - " + e.getName() + ": " + e.getLatitude() + ", " + e.getLongitude(), Toast.LENGTH_SHORT).show();
            //Ultimately, will be sending the whole collection
        }
    };



    /*private ArrayList<String> elementToString(Collection selectedCollPassed) {
        ArrayList<String> temp = new ArrayList<>();
        for(Element e : selectedCollPassed.getCollectionElements()){
            temp.add(e.getName());
        }
        return temp;
    }*/

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){
            filterUnachieved();
        }
        else if(position == 1){
            filterAchieved();
        }
        else if(position == 2){
            filterFive();
        }
        else if(position == 3){
            filterTen();
        }
        else if(position == 4){
            filterFifteen();
        }
        else if(position == 5){
            filterMoreThanFifteen();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void filterUnachieved(){
        displayElements.clear();
        for(Element e : selectedColl.getCollectionElements()){
            if(!e.isCollected()){
                displayElements.add(e);
            }
        }
        cbla.notifyDataSetChanged();
        cbga.notifyDataSetChanged();
    }

    private void filterAchieved(){
        displayElements.clear();
        for(Element e : selectedColl.getCollectionElements()){
            if(e.isCollected()){
                displayElements.add(e);
            }
        }
        cbla.notifyDataSetChanged();
        cbga.notifyDataSetChanged();
    }

    private void filterFive(){
        displayElements.clear();
        for(Element e : selectedColl.getCollectionElements()){
            if(Double.compare(e.getTime(), 5.0) < 0){
                displayElements.add(e);
            }
        }
        cbla.notifyDataSetChanged();
        cbga.notifyDataSetChanged();
    }

    private void filterTen(){
        displayElements.clear();
        for(Element e : selectedColl.getCollectionElements()){
            if(Double.compare(e.getTime(), 10.0) < 0){
                displayElements.add(e);
            }
        }
        cbla.notifyDataSetChanged();
        cbga.notifyDataSetChanged();
    }

    private void filterFifteen(){
        displayElements.clear();
        for(Element e : selectedColl.getCollectionElements()){
            if(Double.compare(e.getTime(), 15.0) < 0){
                displayElements.add(e);
            }
        }
        cbla.notifyDataSetChanged();
        cbga.notifyDataSetChanged();
    }
    private void filterMoreThanFifteen(){
        displayElements.clear();
        for(Element e : selectedColl.getCollectionElements()){
            if(Double.compare(e.getTime(), 15.0) >= 0){
                displayElements.add(e);
            }
        }
        cbla.notifyDataSetChanged();
        cbga.notifyDataSetChanged();
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
