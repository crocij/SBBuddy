package com.example.peterpenis.sbbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;


import ch.uepaa.p2pkit.P2PKitClient;
import ch.uepaa.p2pkit.P2PKitStatusCallback;
import ch.uepaa.p2pkit.StatusResult;
import ch.uepaa.p2pkit.StatusResultHandling;
import ch.uepaa.p2pkit.discovery.GeoListener;
import ch.uepaa.p2pkit.discovery.InfoTooLongException;
import ch.uepaa.p2pkit.discovery.P2PListener;
import ch.uepaa.p2pkit.discovery.entity.Peer;
import ch.uepaa.p2pkit.internal.messaging.MessageTooLargeException;
import ch.uepaa.p2pkit.messaging.MessageListener;

public class Searching_Buddy extends AppCompatActivity implements View.OnClickListener {

    //Layout P2P Message: 256Byte: 0-n: 0, (n+1) - 254: userid, 255: Flag needs_help (=1) offers help (=0)
    //TODO assure foundBuddy still true when accepting help

    private static final String APP_KEY = "eyJzaWduYXR1cmUiOiJKdDFPbkpCNzNuK0VoQWRMaHRvS0swNlYveU5KQXNoa0x4azliZ1h0VmtHRURBUmpWWWdpbDJXeENIVkJnbzFaak43MUxjOCtGeWs5L1NvM2kvTnQ0ZkxMUmc1bWgzM2Z6QUJQYlVzaVIxWnFnQUFFSHMwK1lLdGUzZnFrZE1PREpKZXNaNkV1V01NWjlpa3djL0tQdzg3TWVGZ1lxZVk0ZkY1dTRUWFlqbWM9IiwiYXBwSWQiOjE0NzgsInZhbGlkVW50aWwiOjE2OTYxLCJhcHBVVVVJRCI6IkYyNjIzNjgxLTkxQjMtNEI5RS1BMDU3LUFDRjg2MEM1MDQxMCJ9";
    private boolean foundBuddy = false;
    private String buddyID = "";
    private String P2PBuddyFUCK = "";

    String buddy_email;
    String buddy_firstname;
    String buddy_karma;
    String buddy_status;
    String buddy_last_login;
    String buddy_registered;
    String buddy_lastname;
    String buddy_password;

    Button buttonLogin;

    // Enabling (1/2) - Enable the P2P Services
    private void enableKit() {

        final StatusResult result = P2PKitClient.isP2PServicesAvailable(this);
        if (result.getStatusCode() == StatusResult.SUCCESS) {
            P2PKitClient client = P2PKitClient.getInstance(this);
            logToView("enabling P2PKit");
            client.enableP2PKit(mStatusCallback, APP_KEY);
            mWantToEnable = false;
        } else {
            mWantToEnable = true;
            logToView("Cannot start P2PKit, status code: " + result.getStatusCode());
            StatusResultHandling.showAlertDialogForStatusError(this, result);
        }
    }

    // Enabling (2/2) - Handle the status callbacks with the P2P Services
    private final P2PKitStatusCallback mStatusCallback = new P2PKitStatusCallback() {

        @Override
        public void onEnabled() {
            logToView("Successfully enabled P2P Services, with node id: " + P2PKitClient.getInstance(Searching_Buddy.this).getNodeId().toString());

            //mP2pSwitch.setEnabled(true);
            //mGeoSwitch.setEnabled(true);

            if (mShouldStartServices) {
                mShouldStartServices = false;

                startP2pDiscovery();
            }
        }

        @Override
        public void onSuspended() {
            logToView("P2P Services suspended");

            mGeoSwitch.setEnabled(false);
            mP2pSwitch.setEnabled(false);
        }

        @Override
        public void onError(StatusResult statusResult) {
            logToView("Error in P2P Services with status: " + statusResult.getStatusCode());
            StatusResultHandling.showAlertDialogForStatusError(Searching_Buddy.this, statusResult);
        }
    };

    private void disableKit() {
        P2PKitClient client = P2PKitClient.getInstance(this);
        client.getDiscoveryServices().removeGeoListener(mGeoDiscoveryListener);
        client.getDiscoveryServices().removeP2pListener(mP2pDiscoveryListener);
        client.getMessageServices().removeMessageListener(mMessageListener);

        client.disableP2PKit();
    }

    private void startP2pDiscovery() {
        try {
            P2PKitClient.getInstance(this).getDiscoveryServices().setP2pDiscoveryInfo(currentID.getBytes());
        } catch (InfoTooLongException e) {
            logToView("P2pListener | The discovery info is too long");
        }
        P2PKitClient.getInstance(this).getDiscoveryServices().addP2pListener(mP2pDiscoveryListener);
    }

    // Listener of P2P discovery events
    private final P2PListener mP2pDiscoveryListener = new P2PListener() {

        @Override
        public void onP2PStateChanged(final int state) {
            logToView("P2pListener | State changed: " + state);
        }

        @Override
        public void onPeerDiscovered(final Peer peer) {
            byte[] user_message = peer.getDiscoveryInfo();
            if (user_message != null && !foundBuddy && user_message.length == 256 && user_message[255] == 0) {
                foundBuddy = true;
                P2PBuddyFUCK = peer.getNodeId().toString();
                int i = 0;
                for (; user_message[i] == 0; i++) {
                }
                buddyID = "";
                for (; i < 255; i++)
                    buddyID += user_message[i];
                //TODO got buddy ID, now do something with it
            }
        }

        @Override
        public void onPeerLost(final Peer peer) {
            if (P2PBuddyFUCK == peer.getNodeId().toString()) {
                buddyID = "";
                P2PBuddyFUCK = "";
                foundBuddy = false;
            }
            //TODO interrupt confirmation Process
        }

        @Override
        public void onPeerUpdatedDiscoveryInfo(Peer peer) {
            byte[] user_message = peer.getDiscoveryInfo();
            if (peer.getNodeId().toString() == P2PBuddyFUCK) {
                int i = 0;
                for (; user_message[i] == 0; i++) {
                }
                buddyID = "";
                for (; i < 255; i++)
                    buddyID += user_message[i];
            }
            //TODO restart confirmation Process
        }
    };

    private void stopP2pDiscovery() {
        P2PKitClient.getInstance(this).getDiscoveryServices().removeP2pListener(mP2pDiscoveryListener);
        logToView("P2pListener removed");
    }

    private void startGeoDiscovery() {
        P2PKitClient.getInstance(this).getMessageServices().addMessageListener(mMessageListener);

        P2PKitClient.getInstance(this).getDiscoveryServices().addGeoListener(mGeoDiscoveryListener);
    }

    private final GeoListener mGeoDiscoveryListener = new GeoListener() {

        @Override
        public void onGeoStateChanged(final int state) {
            logToView("GeoListener | State changed: " + state);
        }

        @Override
        public void onPeerDiscovered(final UUID nodeId) {
            logToView("GeoListener | Peer discovered: " + nodeId);

            // sending a message to the peer
            try {
                P2PKitClient.getInstance(Searching_Buddy.this).getMessageServices().sendMessage(nodeId, "SimpleChatMessage", "From Android: Hello GEO!".getBytes());
            } catch (MessageTooLargeException e) {
                logToView("GeoListener | " + e.getMessage());
            }
        }

        @Override
        public void onPeerLost(final UUID nodeId) {
            logToView("GeoListener | Peer lost: " + nodeId);
        }
    };

    private final MessageListener mMessageListener = new MessageListener() {

        @Override
        public void onMessageStateChanged(final int state) {
            logToView("MessageListener | State changed: " + state);
        }

        @Override
        public void onMessageReceived(final long timestamp, final UUID origin, final String type, final byte[] message) {
            logToView("MessageListener | Message received: From=" + origin + " type=" + type + " message=" + new String(message));
        }
    };

    private void stopGeoDiscovery() {
        P2PKitClient.getInstance(this).getMessageServices().removeMessageListener(mMessageListener);
        logToView("MessageListener removed");

        P2PKitClient.getInstance(this).getDiscoveryServices().removeGeoListener(mGeoDiscoveryListener);
        logToView("GeoListener removed");
    }

    private boolean mShouldStartServices;
    private boolean mWantToEnable = false;

    private String currentID = "65536";

    private TextView mLogView;
    private Switch mP2pSwitch;
    private Switch mGeoSwitch;

    public void onIDreceived(String id) {
        currentID = id;
        if (P2PKitClient.getInstance(this).isEnabled()) {
            try {
                String bla = currentID;
                byte[] iDB = new byte[256];
                char[] fu = bla.toCharArray();
                iDB[255] = 1; //flag byte 1 = search help, 0 = offer help
                int i = 0;
                for (int i2 = 254; i2 >= 0; i2++) {
                    if (i < fu.length) {
                        iDB[i] = (byte) fu[fu.length - 1 - i];
                    } else
                        iDB[i] = 0;
                }


                P2PKitClient.getInstance(this).getDiscoveryServices().setP2pDiscoveryInfo(iDB);
                //TODO start searching for Buddy
            } catch (InfoTooLongException e) {
                logToView("P2pListener | The discovery info is too long");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching__buddy);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        mShouldStartServices = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        // When to user comes back from playstore after installing p2p services, try to enable p2pkit again
        if (mWantToEnable && !P2PKitClient.getInstance(this).isEnabled()) {
            enableKit();
        }
    }

    public void onClick(View v) {
        if (v == buttonLogin) {
            //TODO Get User stuff
            GetBuddyData getIt = new GetBuddyData();
            getIt.send(this, buddyID, new GetBuddyData.callbackGet() {
                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject a = new JSONObject(s);
                        buddy_email = a.getString("email");
                        buddy_password = a.getString("password");
                        buddy_firstname = a.getString("firstname");
                        buddy_lastname = a.getString("lastname");
                        buddy_karma = a.getString("karma");
                        buddy_status = a.getString("status");
                        buddy_last_login = a.getString("last_login");
                        buddy_registered = a.getString("registered");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            enableKit();
        }
    }

    private void logToView(String message) {
        CharSequence currentTime = DateFormat.format("hh:mm:ss - ", System.currentTimeMillis());
        //mLogView.setText(currentTime + message + "\n" + mLogView.getText());
    }

    private void clearLogs() {
        mLogView.setText("");
    }

    private byte[] getColorBytes(int color) {
        return new byte[]{(byte) Color.red(color), (byte) Color.green(color), (byte) Color.blue(color)};
    }


}

