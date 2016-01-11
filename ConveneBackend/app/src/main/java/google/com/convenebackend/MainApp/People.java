package google.com.convenebackend.MainApp;

import google.com.convenebackend.R;

/**
 * Created by Lydia on 09/01/2016.
 */
public class People {

    public int icon=R.mipmap.default_profile;
    public String name;

    public People(){
        super();
    }

    public People(int icon, String title) {
        super();
        this.name = title;
    }
}
