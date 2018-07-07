package sample.cmy.cn.androidme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class MeActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener {

    private Button btn_next;

    private int head_id;

    private int body_id;

    private int legs_id;

    private Bundle bundle;

    private Intent intent;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        btn_next = findViewById(R.id.btn_next);
        if (findViewById(R.id.linear_layout) != null) {
           // Toast.makeText(this,"Linear_layout",Toast.LENGTH_LONG).show();
            mTwoPane = true;
            btn_next.setVisibility(View.GONE);
            GridView gridView = findViewById(R.id.grid_view);
            gridView.setNumColumns(2);
        } else {
            mTwoPane = false;
        }
        bundle = new Bundle();
        intent = new Intent(MeActivity.this, MainActivity.class);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }


    @Override
    public void onClick(int position) {

        int bodypartid = position / 12;
        int temp = position - 12 * bodypartid;
        if (mTwoPane) {
            BodyPartFragment bodyPartFragment = new BodyPartFragment();
            BodyPartFragment bodyPartFragment1 = new BodyPartFragment();
            BodyPartFragment bodyPartFragment2 = new BodyPartFragment();
            FragmentManager manager = getSupportFragmentManager();
            switch (bodypartid) {
                case 0:
                    bodyPartFragment.setImageIds(AndroidImageAssets.getHeads());
                    bodyPartFragment.setmImageViewIds(temp);
                    manager.beginTransaction()
                            .replace(R.id.hear_container, bodyPartFragment).commit();
                    break;
                case 1:
                    bodyPartFragment1.setImageIds(AndroidImageAssets.getBodies());
                    bodyPartFragment1.setmImageViewIds(temp);
                    manager.beginTransaction()
                            .replace(R.id.body_container, bodyPartFragment1).commit();
                    break;
                case 2:
                    bodyPartFragment2.setImageIds(AndroidImageAssets.getLegs());
                    bodyPartFragment2.setmImageViewIds(temp);
                    manager.beginTransaction()
                            .replace(R.id.legs_container, bodyPartFragment2).commit();
                    break;
            }


        } else {

            Toast.makeText(MeActivity.this, "bodypartId:" + bodypartid + "temp:" + temp, Toast.LENGTH_LONG).show();
            switch (bodypartid) {
                case 0:
                    head_id = temp;
                    break;
                case 1:
                    body_id = temp;
                    break;
                case 2:
                    legs_id = temp;
                    break;
            }
            bundle.putInt("head_id", head_id);
            bundle.putInt("body_id", body_id);
            bundle.putInt("legs_id", legs_id);
        }
    }
}
