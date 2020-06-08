package oriongladiators.mediscan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReportToAuthoritiesActivity extends AppCompatActivity {

    EditText receiverEmail, senderEmail, messageBody, messageSubject;
    Button sendButton;

    String mailAddress, senderAddress, mailSubject, mailText, mailBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_to_authorities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNew2);
        setSupportActionBar(toolbar);

        receiverEmail = (EditText) findViewById(R.id.receiver_email);
        senderEmail = (EditText) findViewById(R.id.sender_email);
        messageBody = (EditText) findViewById(R.id.message_body);
        messageSubject = (EditText) findViewById(R.id.message_subject);
        sendButton = (Button) findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailAddress = String.valueOf(receiverEmail.getText());
                mailSubject = String.valueOf(messageSubject.getText());
                senderAddress = String.valueOf(senderEmail.getText());
                mailText = String.valueOf(messageBody.getText());
                mailBody = "From: " + senderAddress + "\n" + mailText;
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mailAddress});
                i.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
                i.putExtra(Intent.EXTRA_TEXT   , mailBody);
                if(mailAddress.equals("") || mailSubject.equals("") || senderAddress.equals("") || mailText.equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill out all the fields.", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        startActivity(Intent.createChooser(i, "Send mail using ..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getBaseContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
