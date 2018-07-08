package cn.cmy.sample.flowlayoutdemo;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private static final String ADDRESS_URL = "http://live.9158.com/Room/GetHotLive_v2?lon=0.0&province=&lat=0.0&page=2&type=1";

    private List<String> mImageUrls;

    private SwipeRefreshLayout mRefresh;

    private RecyclerView mRecyclerView;

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mRefresh = findViewById(R.id.frag_recommend_3_swipe);
        mRecyclerView = findViewById(R.id.frag_recommend_3);
        mImageUrls = new ArrayList<>();
      //  getData();
        mRefresh.setOnRefreshListener(this);
        mAdapter = new MyAdapter(mImageUrls, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        mRecyclerView.addItemDecoration(decoration);

    }

    @Override
    public void onRefresh() {
        mRefresh.setRefreshing(true);
        getData();
        mRefresh.setRefreshing(false);
    }

    private void getData() {
        HttpUtil.sendRequest(ADDRESS_URL, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONArray jsonArray = new JSONObject(response).
                            getJSONObject("data").
                            getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mImageUrls.add(jsonArray.getJSONObject(i).getString("smallpic"));
                    }
                    Log.i("*************", "getData: urls:" + mImageUrls.get(3));
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {

            }
        });


    }

    /**
     * 实现 每个Item之间的间隔
     */
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }

}
