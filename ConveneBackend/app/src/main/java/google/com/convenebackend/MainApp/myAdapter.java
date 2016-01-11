package google.com.convenebackend.MainApp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import google.com.convenebackend.R;

/**
 * Created by Lydia on 09/01/2016.
 */
public class myAdapter extends ArrayAdapter<Object> {

    private int layoutResourceId;
    private ArrayList<Object> objectList =null;
    private Context mContext;
    private int selectedIndex;

    public myAdapter(Context context, int layoutResourceId, ArrayList<Object> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.objectList = data;
        selectedIndex = -1;
    }

    public void setSelectedIndex(int ind)
    {
        selectedIndex = ind;
        notifyDataSetChanged();
    }

    public void deselectIndex(){
        selectedIndex = -1;
        notifyDataSetChanged();
    }



    private final class ViewHolder {
        ImageView Pic;
        TextView friendName;
        TextView notType;
    }

    private ViewHolder holder = null;

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        int count = objectList.size();
        Log.d("IN getCount", Integer.toString(objectList.size()));
        return objectList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        Log.d("tag myadapter", "inGET view");
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.Pic= (ImageView)row.findViewById(R.id.list_image);
            holder.friendName = (TextView)row.findViewById(R.id.friendName);

            holder.notType = (TextView)row.findViewById(R.id.notType);

            row.setTag(holder);
        }
        else
        {    Log.d("tag myadapter", "row is NULLLLLLL");
            holder = (ViewHolder)row.getTag();
        }

        People people = (People) objectList.get(position);

        Log.d("TAG", people.toString());

        holder.friendName.setText(people.name);
        holder.Pic.setImageResource(people.icon);

        if(selectedIndex!= -1 && position == selectedIndex)
        {
            row.setBackgroundResource(R.drawable.list_selector_bg);
        }
        else
        {
            row.setBackgroundResource(R.drawable.list_bg);
        }

        return row;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }
}
