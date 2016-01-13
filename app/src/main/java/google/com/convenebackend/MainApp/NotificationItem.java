package google.com.convenebackend.MainApp;

import google.com.convenebackend.R;

/**
 * Created by Lydia on 11/01/2016.
 */
public class NotificationItem {


    public int icon= R.mipmap.default_profile;
    public String name;
    public String notType;

    public NotificationItem(){
        super();
    }

    public NotificationItem(int icon, String title, String nType) {
        super();
        this.name = title;
        this.notType = nType;
    }

}

