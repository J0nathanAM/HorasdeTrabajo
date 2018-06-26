package jonathaenalvarezm.horasdetrabajo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by IDS Comercial on 03/10/2017.
 */

public class MyPreference {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Context context;

    private static final int PRIVATE_MODE=0;
    public static final String PREFER_NAME="HorasdeTrabajo";
    public static final String IN_FIRST_TIME="InFirtsTime";

    public MyPreference(Context context){
        this.context=context;
        preferences=context.getSharedPreferences(PREFER_NAME,PRIVATE_MODE);
        editor=preferences.edit();
    }

    public boolean isFirstTime(){
        return preferences.getBoolean(IN_FIRST_TIME,true);
    }

    public void setOld( boolean b){
        if(b){
            editor.putBoolean(IN_FIRST_TIME,false);
            editor.commit();
        }
    }

}
