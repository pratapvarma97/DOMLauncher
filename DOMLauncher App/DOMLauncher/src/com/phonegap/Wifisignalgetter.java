package com.phonegap;

import org.json.JSONArray;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


public class Wifisignalgetter extends Plugin { 
    BroadcastReceiver wifireceiver;
	
	public Wifisignalgetter() {
	    this.wifireceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				
				if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
					updateSignalStrength(intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -1));
				}
			}
		};
	}
	 
    private void updateSignalStrength(int strengthDbm) {
            sendJavascript("wifisignalCallback(" + strengthDbm + ")");
    }
	
    @Override
    public void onPause(boolean multitasking)
    {
            stopListen();
    }
   
    @Override
    public void onResume(boolean multitasking)
    {
            startListen();
    }
   
    @Override
    public void onDestroy()
    {
            stopListen();
    }
	
    private void startListen()
    {
		IntentFilter rssiFilter = new IntentFilter();
		rssiFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		this.cordova.getActivity().registerReceiver(wifireceiver, rssiFilter);
    }


	private void stopListen()
    {
		this.cordova.getActivity().unregisterReceiver(wifireceiver);
    }
	
	@Override
    public PluginResult execute(String action, JSONArray args, String callbackId) {
		
            try {
            	String state = args.getJSONObject(0).getString("state");	 
                   
                    if (state.equals("start")) {
                            startListen();Log.d(id, "HAHAHAHAHAHAHAHA" + state);
							return new PluginResult(PluginResult.Status.OK);
                    } else if (state.equals("stop")) {
                            stopListen();Log.d(id, "HAHAHAHAHAHAHAHA" + state);
							return new PluginResult(PluginResult.Status.OK);
                    }
                   
            } catch(Exception e) {
                    return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
            }
		
		
		
			return new PluginResult(PluginResult.Status.OK);
		
			
	}
	
}