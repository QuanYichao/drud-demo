package chao.pers.readwritetext;
import android.text.format.Time;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

import chao.pers.readwritetext.library.TextRecord;

public class ShowNFCTagContentActivity extends AppCompatActivity {

    private TextView signTextView;//显示是否打卡
    //设置定时时间 5秒，
    private static final long DELAY=5000;

    private TextView mTagContent;

    private Tag mDetectedTag;

    private String mTagText;  //显示Tag中的数据

    //数据库封装类
    private DatabaseAdapter databaseAdaper;

    private Dog dog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nfctag_content);

        mTagContent=(TextView)findViewById(R.id.textview_tag_content);
        signTextView=(TextView)findViewById(R.id.signflag);
        mDetectedTag=getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

        //Ndef ndef=Ndef.get(mDetectedTag);

        //mTagText=ndef.getType()+"\nmax size:"+ndef.getMaxSize()+"bytes\n\n";

        readNFCTag();

        //把文本设置到TextView中
        mTagContent.setText(mTagText.substring(4,mTagText.length()));


        /**
         * 进行数据库添加打卡操作，若数据库中没有数据，那么打卡添加，若有数据，则已经打过卡
         */
        databaseAdaper=new DatabaseAdapter(this);
        if(databaseAdaper.findById(sax(mTagText).getName())==null){

            System.out.println("根据"+sax(mTagText).getName()+"来查询有没有这条狗");
            databaseAdaper.add(dog);
            System.out.println("数据库里面没有这个dog，那么我添加了");
            System.out.println(dog);
            signTextView.setText(R.string.noSign);
        }else {
            System.out.println("数据库里面有这条dog，无需添加");
            System.out.println(dog);
            signTextView.setText(R.string.yesSign);
        }

        /**
         * 5秒后自动退回到主界面
         */
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        };
        timer.schedule(task, DELAY);

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
                    //mTagText+=textRecord.getText()+"\n\ntext\n"+contentSize+"bytes";
                    mTagText+=textRecord.getText();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析String格式为Dog实体对象
     */
    public Dog sax(String text){
        /**
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line1=null;
        String line2=null;
        try{
            dog=new Dog();
            line1=bufferedReader.readLine();
            System.out.println("第一行的数据"+line1);
            dog.setName(line1);
            
            line2= bufferedReader.readLine();
            int age=Integer.parseInt(line2);
            System.out.println("第二行的数据"+line2);
            dog.setAge(age);

            System.out.println("解析时狗的名字：" + dog.getName() + ",狗的年龄：" + dog.getAge());
        }catch (Exception e) {
            e.printStackTrace();
        }
         **/
        String []dogArr;
        dogArr=text.split("\\.");
        dog=new Dog();
        dog.setName(dogArr[0]);
        dog.setAge(Integer.parseInt(dogArr[1]));
        return dog;
    }


    /**
     * 判断当前系统时间是否在指定时间的范围内
     *
     * beginHour 开始小时,例如22
     * beginMin  开始小时的分钟数,例如30
     * endHour   结束小时,例如 8
     * endMin    结束小时的分钟数,例如0
     * true表示在范围内, 否则false
     */
    public boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();
        Time now = new Time();
        now.set(currentTimeMillis);
        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;
        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;
        // 跨天的特殊情况(比如22:00-8:00)
        if (!startTime.before(endTime)) {
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            //普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }


}
