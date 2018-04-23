package tison.com.outerspacemanagaer.outerspacemanager;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by atison on 23/04/2018.
 */

public class BuildListFragment extends Fragment {
    private ListView lvFragList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_build_list,container);
        lvFragList = v.findViewById(R.id.listViewBuilding);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        lvFragList.setOnItemClickListener((BuildingActivity)getActivity());
    }
}
