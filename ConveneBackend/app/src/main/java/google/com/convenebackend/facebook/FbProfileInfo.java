package google.com.convenebackend.facebook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import google.com.convenebackend.MainApp.UserUtils;
import google.com.convenebackend.R;
import google.com.convenebackend.fragments.OneFragment;

public class FbProfileInfo {
    UserUtils utils;
    public Context context;

    private String name = utils.getUserName();
    private String userId = utils.getUserID();

    private ArrayList friendListArray = utils.getFriendList();
    private ArrayAdapter adapter;

    private Map<String, Object> friendMap = new HashMap<String, Object>();

    protected FragmentActivity activityContext;
    //add a constructor with the Context of your activity
    public FbProfileInfo(FragmentActivity _context){
        activityContext = _context;
    }

    public void clearFriendListArray(){
        friendListArray.clear();
    }


    public void getProfileInformation(final Activity activity) {
        GraphRequest meRequest = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            utils.setUserName(response.getJSONObject().getString("name").toString());
                            utils.setUserID(response.getJSONObject().getString("id").toString());

                            TextView userNameDisplay = (TextView) activityContext.findViewById(R.id.userName);
                            name = utils.getUserName();
                            userNameDisplay.setText(name);
                            userNameDisplay.setVisibility(View.VISIBLE);

                            JSONObject data = response.getJSONObject().getJSONObject("picture").getJSONObject("data");
                            String url = data.getString("url");
                            URL imageURL = new URL(url);

                            Log.d("IC URL", imageURL.toString());

                            //If Async class is a non-static inner class inside of your Activity, then
                            // you need an instance of the enclosing class in order to instantiate the inner class.
                            //FbProfileInfo outerClass = new FbProfileInfo(); //Outer class
                            DownloadImageTask task;
                            task = new DownloadImageTask();
                            task.execute(imageURL.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, picture.type(normal)");
        meRequest.setParameters(parameters);
        meRequest.executeAsync();

        //get friends list
        GraphRequest friendRequest = GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray jarray,
                                            GraphResponse response) {
                        try {
                            JSONArray jsonArray = response.getJSONObject().getJSONArray("data");
                            OneFragment.friendListArray.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject friendObject = jsonArray.getJSONObject(i);

                                String friendName = friendObject.getString("name");
                                String friendID = friendObject.getString("id");
                                Log.i("FRIENDDDDdata ", friendID);
                                friendMap.put(friendName, friendID);
                                OneFragment.friendListArray.add(friendName);
                            }

                            //populate listview adpater with friends list
                            OneFragment.adapter.notifyDataSetChanged();
                            ListView lv = (ListView) activityContext.findViewById(R.id.lvFriend);
                            lv.setVisibility(View.VISIBLE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle params = new Bundle();
        params.putString("fields", "id,name,friends");
        friendRequest.setParameters(params);
        friendRequest.executeAsync();
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected void onPreExecute() {
        }

        protected Bitmap doInBackground(String... urls) {

            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", "image download error");
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            //set image of users profile pic
            utils.setProfilePic(new RoundImage(result));
            if(AccessToken.getCurrentAccessToken()!=null){
                ImageView pImage = (ImageView) activityContext.findViewById(R.id.profileimage);
                pImage.setBackground(utils.getProfilePic());
                pImage.setVisibility(View.VISIBLE);
            }

        }
    }
}