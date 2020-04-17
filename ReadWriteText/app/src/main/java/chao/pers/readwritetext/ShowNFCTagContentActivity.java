package chao.pers.readwritetext;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import chao.pers.readwritetext.library.TextRecord;

public class ShowNFCTagContentActivity extends AppCompatActivity {

    private TextView mTagContent;

    private Tag mDetectedTag;

    private String mTagText;  //显示Tag中的数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nfctag_content);

        mTagContent=(TextView)findViewById(R.id.textview_tag_content);

        mDetectedTag=getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Ndef ndef=Ndef.get(mDetectedTag);

        mTagText=ndef.getType()+"\nmax size:"+ndef.getMaxSize()+"bytes\n\n";

        readNFCTag();

        //把文本设置到TextView中
        mTagContent.setText(mTagText);

    }

    private void readNFCTag(){
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
            //获取最原始的数据
            Parcelable[] rawMsgs=getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            //进一步解析
            NdefMessage msgs[]=null;
            int contentSize=0;
            if(rawMsgs!=null){
                //获取NDEFMessage对象
                msgs=new NdefMessage[rawMsgs.length];
                for (int i=0;i<rawMsgs.length;i++){
                    //由最原始的数据转换为NDEF数据
                    msgs[i]=(NdefMessage)rawMsgs[i];
                    contentSize+=msgs[i].toByteArray().length;
                }
            }
            try{
                if(msgs!=null){
                    //通常只有一个NdefMessage和一个NdefRecord
                    NdefRecord record=msgs[0].getRecords()[0];
                    TextRecord textRecord=TextRecord.parse(record);
                    mTagText+=textRecord.getText()+"\n\ntext\n"+contentSize+"bytes";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
