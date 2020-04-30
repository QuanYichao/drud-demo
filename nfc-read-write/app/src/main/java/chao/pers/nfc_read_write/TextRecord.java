package chao.pers.nfc_read_write;

import android.nfc.NdefRecord;

import java.util.Arrays;

/**
 * Created by 权毅超 on 2020/3/31.
 * TextRecord类的作用就是将NdefRecord中的NEDF数据提取出来，变为纯文本
 */
public class TextRecord {
    private final String mText;

    private TextRecord(String text){
        mText=text;
    }

    public String getText(){
        return mText;
    }

    public static TextRecord parse(NdefRecord ndefRecord){

        if(ndefRecord.getTnf()!=NdefRecord.TNF_WELL_KNOWN){
            return null;
        }
        if(!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)){
            return null;
        }
        try{
            byte[] payload=ndefRecord.getPayload();

            String textEncoding;
            if((payload[0]&0x80)==0){
                textEncoding="UTF-8";
            }else{
                textEncoding="UTF-16";
            }
            int languageCodeLength=payload[0]&0x3f;

            String languageCode=new String(payload,1,languageCodeLength,"US-ASCII");

            String text=new String(payload,languageCodeLength+1,payload.length-languageCodeLength-1,textEncoding);

            return new TextRecord(text);
        }catch(Exception e){
            throw new IllegalArgumentException();
        }

    }

}
