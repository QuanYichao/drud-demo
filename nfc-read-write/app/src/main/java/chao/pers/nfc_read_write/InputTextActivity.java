package chao.pers.nfc_read_write;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputTextActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText ageEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_text);
        nameEditText=(EditText)findViewById(R.id.edit_text_name);
        ageEditText=(EditText)findViewById(R.id.edit_text_age);

    }

    public void onClick_OK(View view){
        Intent intent=new Intent();
        intent.putExtra("name",nameEditText.getText().toString());
        intent.putExtra("age",ageEditText.getText().toString());
        setResult(1, intent);
        finish();
    }
}
