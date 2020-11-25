package com.gzuliani.btremote;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ControlDeviceActivity extends AppCompatActivity {

    private BluetoothConnection connection;

    private TextView outputPane;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_device);

        Intent intent = getIntent();
        BluetoothDevice device = intent.getExtras().getParcelable("device");

        TextView address = findViewById(R.id.device_address);
        address.setText(String.format("Device: %s (%s)", device.getName(), device.getAddress()));

        outputPane = findViewById(R.id.output_pane);
        button1 = findViewById(R.id.button_1);
        button1.setEnabled(false);
        button2 = findViewById(R.id.button_2);
        button2.setEnabled(false);
        button3 = findViewById(R.id.button_3);
        button3.setEnabled(false);
        connectButton = findViewById(R.id.button_connect);
        connectButton.setText(R.string.button_connect_connect);

        try {
            connection = new BluetoothConnection(device.getAddress(), 3000);
        } catch (Exception e) {
            outputPane.setText(e.toString());
        }
    }

    public void button1(View v) {
        send("1");
    }

    public void button2(View v) {
        send("2");
    }

    public void button3(View v) {
        send("3");
    }

    public void connect(View v) {
        String caption = (String)connectButton.getText();
        String response;
        try {
            if (connection.isOpen()) {
                connection.close();
            }
            else {
                connection.open();
            }
            response = "OK";
        } catch (Exception e) {
            response = String.format("ERROR: %s", e.toString());
        }
        outputPane.setText(response);
        connectButton.setText(connection.isOpen()
                ? R.string.button_connect_disconnect
                : R.string.button_connect_connect);
        button1.setEnabled(connection.isOpen());
        button2.setEnabled(connection.isOpen());
        button3.setEnabled(connection.isOpen());
    }

    private void send(String command) {
        String response;
        try{
            connection.send(command.getBytes());
            response = String.format("Command \"%s\" sent successfully", command);
        } catch (Exception e) {
            response = String.format("ERROR: %s", e.toString());
        }
        outputPane.setText(response);
    }
}
