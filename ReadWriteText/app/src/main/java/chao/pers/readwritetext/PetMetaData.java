package chao.pers.readwritetext;

import android.provider.BaseColumns;

/**
 * Created by 权毅超 on 2020/4/14.
 */
public class PetMetaData {

    private PetMetaData(){}
    //dog表的定义
    public static abstract class DogTable implements BaseColumns{
        public static final String TABLE_NAME="dog";
        public static final String NAME="name";
        public static final String AGE="age";
    }
}
