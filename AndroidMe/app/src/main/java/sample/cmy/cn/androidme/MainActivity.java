package sample.cmy.cn.androidme;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            BodyPartFragment bodyPartFragment = new BodyPartFragment();
            BodyPartFragment bodyPartFragment1 = new BodyPartFragment();
            BodyPartFragment bodyPartFragment2 = new BodyPartFragment();
            Log.i("*****************", "onCreate: " + getIntent().getExtras().getInt("head_id"));
            FragmentManager manager = getSupportFragmentManager();
            bodyPartFragment.setImageIds(AndroidImageAssets.getHeads());
            bodyPartFragment1.setImageIds(AndroidImageAssets.getBodies());;
            bodyPartFragment2.setImageIds(AndroidImageAssets.getLegs());
            bodyPartFragment.setmImageViewIds(getIntent().getExtras().getInt("head_id"));
            bodyPartFragment1.setmImageViewIds(getIntent().getExtras().getInt("body_id"));
            bodyPartFragment2.setmImageViewIds(getIntent().getExtras().getInt("legs_id"));
            manager.beginTransaction().
                    add(R.id.hear_container, bodyPartFragment).
                    add(R.id.body_container, bodyPartFragment1).
                    add(R.id.legs_container, bodyPartFragment2).
                    commit();
        }
    }
}
