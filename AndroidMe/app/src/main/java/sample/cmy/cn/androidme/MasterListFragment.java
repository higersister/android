package sample.cmy.cn.androidme;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class MasterListFragment extends Fragment {

    private MasterListAdapter adapter;

    public OnImageClickListener mCallBack;

    public interface OnImageClickListener {
        void onClick(int position);
    }

    public MasterListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBack = (OnImageClickListener) context;
        } catch (Exception e) {
            throw new ClassCastException("must implement OnImageClickListener");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new MasterListAdapter(getContext(), AndroidImageAssets.getAll());

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        GridView gridView = rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCallBack.onClick(i);
            }
        });

        return rootView;
    }
}
