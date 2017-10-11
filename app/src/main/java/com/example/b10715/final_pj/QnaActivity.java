package com.example.b10715.final_pj;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class QnaActivity extends AppCompatActivity {
    private Button btnSend;
    private EditText user_email, content, subject;
    private Spinner sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);

        user_email = (EditText) findViewById(R.id.user_email);
        subject = (EditText) findViewById(R.id.subject);
        content = (EditText) findViewById(R.id.content);
        sort = (Spinner) findViewById(R.id.sort);
        btnSend = (Button) findViewById(R.id.btnSend);

        user_email.setText(LoginActivity.call_email);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ArrayAdapter sortAdapter = ArrayAdapter.createFromResource(this, R.array.sort, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort.setAdapter(sortAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strSubject = "[KEEPET] From : " + user_email.getText().toString() + " - [ " + sort.getSelectedItem().toString() + " ]" + subject.getText().toString();
                Log.i("ERROR", strSubject);
                // Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:mina693@hanmail.net"));
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, "mina693@hanmail.net");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, strSubject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, content.getText().toString());
                emailIntent.setType("message/rfc822");
                //  startActivity(Intent.createChooser(emailIntent, "이메일 클라이언트 선택하기 :"));

                startActivity(emailIntent);
                Intent doneIntent = new Intent(QnaActivity.this, HomeActivity.class);
                startActivity(doneIntent);
            }
        });
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    ;

}
