package oriongladiators.mediscan;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import static java.lang.Math.log10;
import static java.lang.Math.pow;

public class BluetoothActivity extends AppCompatActivity {

    Button retryButton;
    TextView incomingText, safetyText;
    Handler bluetoothIn;

    //data arrays
    String textData[] = new String[12];
    Float floatData[] = new Float[12];
    Float ppmData[] = new Float[12];
    private static final float R0 = 10.0f;
    int loopCounterForText = 0;
    int loopCounterForFloat = 0;
    float averageValue = 0.0f;
    boolean textLoopWillRun = true;
    boolean floatLoopWillRun = true;

    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        for(int i = 0; i < 12; i++) {
            floatData[i] = 0.0f;
            ppmData[i] = 0.0f;
        }

        setGraph();

        //Link the buttons and textViews to respective views
        retryButton = (Button) findViewById(R.id.retry_button);
        //txtString = (TextView) findViewById(R.id.txtString);
//        txtStringLength = (TextView) findViewById(R.id.testView1);
        incomingText = (TextView) findViewById(R.id.incoming_text);
        safetyText = (TextView) findViewById(R.id.safety_text);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread

                    recDataString.append(readMessage);                                      //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("\r\n");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        recDataString.delete(0, recDataString.length());                    //clear
                        if (textLoopWillRun) {
                            safetyText.setText("");
                            textData[loopCounterForText] = "" + dataInPrint;
                            loopCounterForText++;
                            if(loopCounterForText == 12) textLoopWillRun = false;
                            if(floatLoopWillRun) {
                                floatData[loopCounterForFloat] = stringToFloat(dataInPrint);
                                ppmData[loopCounterForFloat] = getPPMValue(floatData[loopCounterForFloat]);
                                incomingText.setText("Data Received : " + ppmData[loopCounterForFloat] + " ppm");
                                loopCounterForFloat++;
                                if(loopCounterForFloat == 12) {
                                    float sum = 0.0f;
                                    for(int i = 0; i < 12; i++) {
                                        sum += ppmData[i];
                                    }
                                    averageValue = getAverageValue(sum);
                                    if(averageValue > 10) {
                                        safetyText.setText("High Concentration | You are not safe");
                                        safetyText.setTextColor(Color.RED);
                                    }
                                    else safetyText.setText("Minimal Concentration | You are safe");
                                    setGraph();
                                    incomingText.setText("Average Value : " + averageValue + " ppm");
                                    floatLoopWillRun = false;
                                }
                                }
                        }
                        //int dataLength = dataInPrint.length();                          //get length of data received
                        //txtStringLength.setText("String Length = " + String.valueOf(dataLength));

//                        if (recDataString.charAt(0) == '#')                             //if it starts with # we know it is what we are looking for
//                        {
//                            String distance = recDataString.substring(1, 4);             //get sensor value from string between indices 1-5
//
//                            distanceView.setText(" Distance = " + distance + "m");    //update the textviews with sensor value
//
//                        }
                        //recDataString.delete(0, recDataString.length());                    //clear all string data
                        // strIncom =" ";
                        // dataInPrint = " ";
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        // Set up onClick listeners for restarting plotting values
        retryButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //mConnectedThread.write("");    // Send "" via Bluetooth
                Toast.makeText(getBaseContext(), "Loading Data...", Toast.LENGTH_SHORT).show();
                loopCounterForText = 0;
                loopCounterForFloat = 0;
                textLoopWillRun = true;
                floatLoopWillRun = true;
            }
        });

        Toast.makeText(getBaseContext(), "Loading Data...", Toast.LENGTH_LONG).show();


    }

    public void setGraph() {
        //Graph part
        CombinedChart combinedChart = (CombinedChart) findViewById(R.id.chart);

        CombinedData data = new CombinedData(getXAxisValues());
        data.setData(barDataOne());
        //data.setData(barDataTwo());
        data.setData(lineData());
        combinedChart.setData(data);
        combinedChart.setDescription("Formalin Test");
        combinedChart.animateXY(4000, 4000);
        combinedChart.invalidate();
    }


    //Graph Essentials
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add("1s");
        labels.add("2s");
        labels.add("3s");
        labels.add("4s");
        labels.add("5s");
        labels.add("6s");
        labels.add("7s");
        labels.add("8s");
        labels.add("9s");
        labels.add("10s");
        labels.add("11s");
        labels.add("12s");
        return labels;
    }

    public LineData lineData() {
        ArrayList<Entry> line = new ArrayList<>();
        line.add(new Entry(averageValue, 0));
        line.add(new Entry(averageValue, 1));
        line.add(new Entry(averageValue, 2));
        line.add(new Entry(averageValue, 3));
        line.add(new Entry(averageValue, 4));
        line.add(new Entry(averageValue, 5));
        line.add(new Entry(averageValue, 6));
        line.add(new Entry(averageValue, 7));
        line.add(new Entry(averageValue, 8));
        line.add(new Entry(averageValue, 9));
        line.add(new Entry(averageValue, 10));
        line.add(new Entry(averageValue, 11));
        LineDataSet lineDataSet = new LineDataSet(line, "Average Value(ppm)");
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        LineData lineData = new LineData(getXAxisValues(), lineDataSet);
        return lineData;
    }

    public BarData barDataOne() {
        ArrayList<BarEntry> group1 = new ArrayList<>();
        group1.add(new BarEntry(ppmData[0], 0));
        group1.add(new BarEntry(ppmData[1], 1));
        group1.add(new BarEntry(ppmData[2], 2));
        group1.add(new BarEntry(ppmData[3], 3));
        group1.add(new BarEntry(ppmData[4], 4));
        group1.add(new BarEntry(ppmData[5], 5));
        group1.add(new BarEntry(ppmData[6], 6));
        group1.add(new BarEntry(ppmData[7], 7));
        group1.add(new BarEntry(ppmData[8], 8));
        group1.add(new BarEntry(ppmData[9], 9));
        group1.add(new BarEntry(ppmData[10], 10));
        group1.add(new BarEntry(ppmData[11], 11));
        BarDataSet barDataSet1 = new BarDataSet(group1, "Parts Per Million");
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barDataOne = new BarData(getXAxisValues(), barDataSet1);
        return barDataOne;
    }

    public float stringToFloat(String text_data) {
        Float number_data;
        number_data = Float.parseFloat(text_data);
        return number_data;
    }

    public float getPPMValue(float Rs) {
        float ppmValue;
        ppmValue = (float) pow(10.0, ((log10(Rs/R0) - 0.0827) / (-0.4807)));
        return ppmValue;
    }

    public float getAverageValue(float sum) {
        float result = 0.0f;
        result = sum / 12;
        return result;
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }


}
