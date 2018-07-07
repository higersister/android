package sample.cmy.cn.androidme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class BodyPartFragment extends Fragment {

    private int mImageViewIds;

    private List<Integer> ImageIds;

    public void setImageIds(List<Integer> imageIds) {
        ImageIds = imageIds;
    }

    public void setmImageViewIds(int mImageViewIds) {
        this.mImageViewIds = mImageViewIds;
    }

    public BodyPartFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState!=null){
            mImageViewIds = savedInstanceState.getInt("index");
            ImageIds = savedInstanceState.getIntegerArrayList("list");
        }

        View rootView = inflater.inflate(R.layout.fragment_body_part, container, false);
        final ImageView imageView = rootView.findViewById(R.id.body_part_image_view);
        if (imageView != null ) {
            imageView.setImageResource(ImageIds.get(mImageViewIds));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mImageViewIds < ImageIds.size() - 1) {
                        mImageViewIds++;
                    } else {
                        mImageViewIds = 0;
                    }
                    imageView.setImageResource(ImageIds.get(mImageViewIds));
                }
            });
        }

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("listIndex",getActivity().getIntent().getExtras().getInt(""));
        outState.putIntegerArrayList("list", (ArrayList<Integer>) ImageIds);
        outState.putInt("index",mImageViewIds);
    }
}


