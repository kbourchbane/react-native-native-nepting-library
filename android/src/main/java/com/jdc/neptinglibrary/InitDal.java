package com.jdc.neptinglibrary;

import android.content.Context;
import android.util.Log;

import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;

public class InitDal {

    public static IDAL getDal(Context appContext){
        IDAL dal = null;
        try {
            long start = System.currentTimeMillis();
            dal = NeptuneLiteUser.getInstance().getDal(appContext);
            Log.i("Test","get dal cost:"+(System.currentTimeMillis() - start)+" ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dal;
    }

}
