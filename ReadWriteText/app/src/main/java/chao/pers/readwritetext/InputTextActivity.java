package chao.pers.readwritetext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputTextActivity extends AppCompatActivity {

    private Button button;
    private EditText mTextTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_text);
        mTextTag=(EditText)findViewById(R.id.edittext_text_tag);
        //mTextTag.setOnClickListener(new WriteListener());
        //button=(Button)findViewById(R.id.onClick_InputText);
        //button.setOnClickListener(new WriteListener());
    }

    /*class sWriteListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            intent.putExtra("text",mTextTag.getText().toString());
            setResult(1, intent);
            finish();
        }
    }*/


    public void onClick_OK(View view){
        Intent intent=new Intent();
        intent.putExtra("text",mTextTag.getText().toString());
        setResult(1, intent);
        finish();
    }
}
