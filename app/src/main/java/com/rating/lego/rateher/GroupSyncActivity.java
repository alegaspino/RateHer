package com.rating.lego.rateher;

import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupSyncActivity extends AppCompatActivity {

    IntentFilter intentFilter = new IntentFilter();

    WifiP2pManager.Channel channel;
    WifiP2pManager manager;
    WifiManager wifiManager;
    BroadcastReceiver receiver;

    Button btnWifi, btnDiscover;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNames;
    WifiP2pDevice[] devices;

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            List<WifiP2pDevice> refreshedPeers = new ArrayList<WifiP2pDevice>(wifiP2pDeviceList.getDeviceList());

            if (!refreshedPeers.equals(peers)) {
                peers.clear();
                peers.addAll(refreshedPeers);

                deviceNames = new String[wifiP2pDeviceList.getDeviceList().size()];
                devices = new WifiP2pDevice[wifiP2pDeviceList.getDeviceList().size()];
                int index = 0;

                for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {
                    deviceNames[index] = device.deviceName;
                    devices[index] = device;
                    index++;
                }
            }

            Log.d("GROUPSYNC", refreshedPeers.toString());
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;

            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {

            } else if (wifiP2pInfo.groupFormed) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_sync);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

       /* manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {

            }
        });

        WifiP2pDevice device = peers.get(0);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {

            }
        });

        manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
                String groupPassword = wifiP2pGroup.getPassphrase();
            }
        }); */

       btnWifi = (Button) findViewById(R.id.button_wifiToggle);
       btnWifi.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (wifiManager.isWifiEnabled()) {
                   wifiManager.setWifiEnabled(false);
                   btnWifi.setText("TURN WIFI ON");
               } else {
                   wifiManager.setWifiEnabled(true);
                   btnWifi.setText("TURN WIFI OFF");
               }
           }
       });

       btnDiscover = (Button) findViewById(R.id.button_discoverToggle);
       btnDiscover.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                   @Override
                   public void onSuccess() {
                       btnDiscover.setText("DISCOVER ON");
                   }

                   @Override
                   public void onFailure(int i) {

                   }
               });
           }
       });
    }

    @Override
    public void onResume() {
        super.onResume();
        receiver = new WifiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}