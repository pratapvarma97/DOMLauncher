package com.phonegap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;


public class Applist extends CordovaPlugin {

	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {		
		
		if(action.equals("generateIcons")){			
			PackageManager pm = this.cordova.getActivity().getPackageManager();

			List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
			
			for (ApplicationInfo packageInfo : packages) {
					
				Intent appActivity = pm.getLaunchIntentForPackage(packageInfo.packageName);
				
				if(appActivity != null){
					String pkgName = packageInfo.packageName;		
					Drawable appIcon = packageInfo.loadIcon(this.cordova.getActivity().getPackageManager());
					File sdcard = Environment.getExternalStorageDirectory();
					File file = new File(sdcard, "/DOMLauncher/settings/icons/"+pkgName+".png");
					FileOutputStream foStream = null;
					
					Bitmap bitmap = ((BitmapDrawable)appIcon).getBitmap();
					
					ByteArrayOutputStream oStream = new ByteArrayOutputStream();  
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream); //bm is the bitmap object   
					
					 byte[]	b = oStream.toByteArray();				
						try {
							foStream = new FileOutputStream(file);
							oStream.write(b);
							oStream.writeTo(foStream);
							oStream.close();
							foStream.close();					
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					pkgName = null;	
					appIcon = null;
					file = null;
					b = null;
					foStream = null;
					oStream = null;
					bitmap = null;
				}				
			}
			packages = null;
			pm = null;
			System.gc();
		}
		
		if(action.equals("appList")){			
			final PackageManager pm = this.cordova.getActivity().getPackageManager();
			File sdcard = Environment.getExternalStorageDirectory();
			
			JSONArray jArray = new JSONArray();
			
			List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
			
			for (ApplicationInfo packageInfo : packages) {
							
				String pkgName = packageInfo.packageName;
				 			
				Intent appActivity = pm.getLaunchIntentForPackage(packageInfo.packageName);
				    
				String appName = packageInfo.loadLabel(this.cordova.getActivity().getPackageManager()).toString();
			
				//Code to send package information to Eclipse Log.
				//Log.d(id, "Name:" + appName);
				//Log.d(id, "Package:" + pkgName);
				//Log.d(id, "Activity:" + appActivity);
				try {
					
					if(appActivity != null){
						
						String[] appIntent = appActivity.toString().split("/");
						
						String appIFormated = appIntent[1].substring(0, appIntent[1].length() - 2);

						JSONObject json = new JSONObject();
						json.put("name", appName).put("activity", appIFormated).put("package", pkgName).put("path", "file:///" + sdcard.getAbsolutePath()+"/DOMLauncher/settings/icons/"+ pkgName+".png");				
						jArray.put(json);
					}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}// Turns the JSON array into a string and returns the value. 
			
			String results = jArray.toString();				
			
			callbackContext.success(new JSONObject().put("returnVal", results));			
		}

		return true;
	}

}