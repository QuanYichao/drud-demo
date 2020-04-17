package chao.pers.sqlite_prac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private DatabaseAdapter databaseAdaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseAdaper=new DatabaseAdapter(this);
    }

    public void addClick(View view){
        Dog dog=new Dog("white",2);
        databaseAdaper.add(dog);
    }

    public void deleteClick(View view){
        databaseAdaper.delete(1);
    }

    public void updateClick(View view){
        Dog dog=new Dog(1,"black",2);
        databaseAdaper.upgrade(dog);
    }

    public void findByIdClick(View view){
        Dog dog=databaseAdaper.findById(1);
        System.out.println(dog);
    }

    public void findAllClick(View view){
        ArrayList<Dog> dogs=databaseAdaper.findAll();
        int size=dogs.size();
        for (int i = 0; i <size ; i++) {
            System.out.println(dogs.get(i));
        }
    }
}
