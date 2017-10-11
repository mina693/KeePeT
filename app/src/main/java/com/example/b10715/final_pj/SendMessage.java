package com.example.b10715.final_pj;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendMessage extends AsyncTask<String, Void, Void> {
    private Exception exception;

    @Override
    protected Void doInBackground(String... params) {
        try {
            try {
                Log.i("sendMessage","camtestxxxxxxxxxxx");
                Socket socket = new Socket("192.168.0.12", 8080);
                PrintWriter outTOServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                outTOServer.print(params[0]);
                outTOServer.flush();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            this.exception = e;
            return null;
        }
        return null;
    }


}