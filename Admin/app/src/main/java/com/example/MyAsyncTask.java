package com.example;

import android.os.AsyncTask;
import android.widget.Toast;

public class MyAsyncTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        // Perform long-running task here
        return "Task completed";
    }

    @Override
    protected void onPostExecute(String result) {
        // Update UI with the result

    }
}
