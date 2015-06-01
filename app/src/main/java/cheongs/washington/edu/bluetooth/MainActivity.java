package cheongs.washington.edu.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class MainActivity extends ActionBarActivity {
    Button bn;
    Set<BluetoothDevice> paired_devices;
    String listDevice [];
    final Button connector = (Button)findViewById(R.id.connect);
    final Button disconnector = (Button)findViewById(R.id.disconnect);
    private BluetoothAdapter bluetooth;
    final TextView info = (TextView)findViewById(R.id.info);
    BroadcastReceiver bluetoothState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String prevStateE = BluetoothAdapter.EXTRA_PREVIOUS_STATE;
            String stateE = BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(stateE, -1);
            int previousState = intent.getIntExtra(prevStateE, -1);
            String notification = "";
            switch(state) {
                case(BluetoothAdapter.STATE_TURNING_ON) : {
                    notification = "Bluetooth turning on, Please Wait";
                    Toast.makeText(MainActivity.this, notification, Toast.LENGTH_SHORT).show();
                    break;
                }
                case(BluetoothAdapter.STATE_ON) : {
                    notification = "Bluetooth has been turned on";
                    Toast.makeText(MainActivity.this, notification, Toast.LENGTH_SHORT).show();
                    break;
                }
                case(BluetoothAdapter.STATE_TURNING_OFF) : {
                    notification = "Bluetooth is turning off";
                    Toast.makeText(MainActivity.this, notification, Toast.LENGTH_SHORT).show();
                    break;
                }
                case(BluetoothAdapter.STATE_OFF) : {
                    notification = "Bluetooth is turned off";
                    Toast.makeText(MainActivity.this, notification, Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }
    };

    // Broadcast receiver for discovering devices
    final BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(bluetooth.isEnabled()) {
            String address = bluetooth.getAddress();
            String name = bluetooth.getName();
            info.setText(name);
        } else {
            info.setText("Bluetooth is off");
        }

        // Discovering portion

        //This broadcast is sent when a device has been found...
        registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        //Start discovery functionality...
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter != null && adapter.isDiscovering()){
            adapter.cancelDiscovery();
        }
        adapter.startDiscovery();


        connector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
                String actionRequestEnabled = BluetoothAdapter.ACTION_REQUEST_ENABLE;
                IntentFilter filter = new IntentFilter(actionStateChanged);
                registerReceiver(bluetoothState,filter);
                startActivityForResult(new Intent(actionRequestEnabled), 0);
                paired_devices = bluetooth.getBondedDevices();
                int count = paired_devices.size();
                listDevice = new String[count];
                int i = 0;
                for(BluetoothDevice device : paired_devices) {
                    listDevice[i] = device.getName();
                    i++;
                }
                Bundle funBundle = new Bundle();
                funBundle.putStringArray("pairs", listDevice);
                Intent in = new Intent("pair_filter");
                in.putExtras(funBundle);
                startActivity(in);
                // Enable Discovery
                Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(discoverable, 0);




            }
        });

        disconnector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.disable();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
