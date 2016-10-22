package com.example.tuhv.sdreadmusic;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    String[] item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=  (ListView) findViewById(R.id.lvPlayer);

        final ArrayList<File>nhaccuatoi = timnhac(Environment.getExternalStorageDirectory());

        item = new String[nhaccuatoi.size()];

        for (int i = 0; i < nhaccuatoi.size();i++ ){
            //tost(nhaccuatoi.get(i).getName().toString());
            item[i] = nhaccuatoi.get(i).getName().toString().replace("mp3","");

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.baihat_layout,R.id.textView,item);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("danhsachnhac",nhaccuatoi));

            }
        });
    }
    public ArrayList<File> timnhac(File root){
        ArrayList<File> al = new ArrayList<File>();
        File [] files = root.listFiles();
        for (File baihat:files){
            if (baihat.isDirectory() && !baihat.isHidden()){
                al.addAll(timnhac(baihat));

            }else{
                if (baihat.getName().endsWith(".mp3")){
                    al.add(baihat);
                }
            }
        }
        return al;
    }
    public void tost(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }
}
