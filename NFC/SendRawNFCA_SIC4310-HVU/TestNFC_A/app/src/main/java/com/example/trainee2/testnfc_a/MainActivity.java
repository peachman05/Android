package com.example.trainee2.testnfc_a;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        // TODO: handle Intent
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
             tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(tag == null){

                Log.d("Erorrrrrrrr","tag is null");
            }else{
                Log.d("Successs","tag is alive");

            }
            new NdefReaderTask().execute(tag);

        }else{
            Log.d("Fail Action is:",action);
        }
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            NfcA nfcA =  NfcA.get(tag);
            if(nfcA != null){
                Log.d("Successs","nfcA is alive");

            }else{

                Log.d("Erorrrrrrrr","nfcA is null");
            }
            try{

                nfcA.connect();
                byte[] l_cmd_selProf1 = "Select \"Comford\" profile\n".getBytes();
                sendDataViaUART(l_cmd_selProf1, nfcA);

            }catch(Exception e){
                Log.d("Erorrrrrrrr","connect failllll");
            }


            return null;
        }
    }
    public byte[] sendDataViaUART(byte[] data,NfcA nfcA) {
        byte[] receive = null ;
       try{
           receive = nfcA.transceive(getBuffer(data));
           Log.d("Success","send success");
       }catch(Exception e){
           Log.d("Erorrrrrrrr","send failllll");
       }
        return receive;
    }

    public byte[] getBuffer(byte[] data) {
        int datlen = data.length;
        byte[] buffer = new byte[datlen + 1];
        buffer[0] = (byte) 0xB1; // set 0xB1 in first byte
        System.arraycopy(data, 0, buffer, 1, datlen);
        return buffer;
    }
}
