package com.sam.hotspot_build.Tabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam.hotspot_build.R;

import org.w3c.dom.Text;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Tab2 extends Fragment {

    public Tab1 tab1;
    public TextView tabText;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);

        tabText = (TextView) v.findViewById(R.id.tabText);



        return v;


    }


    public Tab2(){

    }

    public void setTab1(Tab1 tab){
        this.tab1 = tab;
    }

    public void changeText(){

    }




}