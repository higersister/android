package cn.cmy.rxjavaretrofit;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class Loading {

    private static AlertDialog alertDialog;

    public static void show(Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        alertDialog = builder.create();
        View view = LayoutInflater.from(context).
                inflate(R.layout.loading_layout,null);
        alertDialog.setView(view);
        alertDialog.show();
    }

    public static void hide(){
        alertDialog.hide();
    }


}
