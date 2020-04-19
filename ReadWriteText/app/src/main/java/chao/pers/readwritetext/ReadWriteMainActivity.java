package chao.pers.readwritetext;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.util.Locale;

public class ReadWriteMainActivity extends AppCompatActivity {



    private TextView nameTextView;//显示注册的姓名

    private TextView ageTextView;//显示注册的年龄

    private String sname;//用来暂时存储用户输入的姓名

    private String sage;//用来暂时存储用户输入的年龄

    private Button inPutTextButton;//点击进行祖册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_write_main);

        nameTextView=(TextView)findViewById(R.id.name);
        ageTextView=(TextView)findViewById(R.id.age);

        inPutTextButton= (Button) findViewById(R.id.onClick_InputText);
        inPutTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReadWriteMainActivity.this,InputTextActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }


    //接受Intent传回来的数据
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        //测试响应码
        if(requestCode==1&&resultCode==1){
            sname=data.getStringExtra("name");
            nameTextView.setText(sname);

            sage=data.getStringExtra("age");
            ageTextView.setText(sage);
        }
    }

    /**
     * 读取和写入都在onNewIntent方法中完成
     * @param intent
     */
    public void onNewIntent(Intent intent){
        //如果mText中为空，则动作为读操作
        if((sage ==null)&&(sname==null)){
            Intent myIntent=new Intent(this,ShowNFCTagContentActivity.class);
            //把Intent传到显示文本信息的Activity，并进行处理
            myIntent.putExtras(intent);
            //指定动作
            myIntent.setAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            startActivity(myIntent);
        }else{
            //进行写操作
            Tag tag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage=new NdefMessage(new NdefRecord[]{createTextRecord(sname+"\n"+sage)});
            writeTag(ndefMessage, tag);
            //提示用户注册成功
            Toast.makeText(this,"恭喜您，注册成功！",Toast.LENGTH_SHORT).show();
        }
    }

    //将纯文本转换为NdefRecord格式
    public NdefRecord createTextRecord(String text){
        byte[] langBytes= Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding=Charset.forName("UTF-8");
        byte[] textBytes=text.getBytes(utfEncoding);
        int utfBit=0;
        char status=(char)(utfBit+langBytes.length);
        //  状态码：status   语言编码：langBytes.length     文本长度：textBytes.length
        byte []data=new byte[1+langBytes.length+textBytes.length];
        data[0]=(byte)status;
        System.arraycopy(langBytes,0,data,1,langBytes.length);
        System.arraycopy(textBytes,0,data,1+langBytes.length,textBytes.length);
        //创建NdefRecord对象  把一个字节数组封装到NdefRecord当中
        NdefRecord ndefRecord=new NdefRecord(NdefRecord.TNF_WELL_KNOWN,NdefRecord.RTD_TEXT,new byte[0],data);
        return ndefRecord;
    }

    //之前通过intent-filter拦截的是NDEF格式的Tag标签，这里直接进行获取，不再进行判断
    boolean writeTag(NdefMessage ndefMessage,Tag tag){
        try{
            Ndef ndef=Ndef.get(tag);
            ndef.connect();
            ndef.writeNdefMessage(ndefMessage);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
