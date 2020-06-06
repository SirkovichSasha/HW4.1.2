package ru.netology.lists;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {
    private static final String KEY_TITLE="key_title";
    private static final String KEY_COUNT="key_count";
    private static String NOTE_TEXT = "note_text";
    private SharedPreferences myNoteSharedPref;
    private List<Map<String,String>> values=null;
    private BaseAdapter listContentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView list = findViewById(R.id.list);
        values=initList();
        listContentAdapter = createAdapter(values);
        list.setAdapter(listContentAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                values.remove(position);
                listContentAdapter.notifyDataSetChanged();
            }
        });

        initSwipe();

    }

    private List<Map<String,String>> initList() {
        String noteTxt=getStringFromSharedPref();
        return prepareContent(noteTxt);
    }

    private String getStringFromSharedPref() {
        myNoteSharedPref = getSharedPreferences("MyNote", MODE_PRIVATE);
        String noteTxt = myNoteSharedPref.getString(NOTE_TEXT, "");
        if (noteTxt.length() < 1) {
            SharedPreferences.Editor myEditor = myNoteSharedPref.edit();
            noteTxt = getString(R.string.large_text);
            myEditor.putString(NOTE_TEXT, noteTxt);
            myEditor.apply();
        }
        return  noteTxt;
    }
    private void initSwipe() {

        final SwipeRefreshLayout swipeRefreshLayout=findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initList();
                listContentAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String,String>>values) {
        return new SimpleAdapter(this,values,R.layout.item,new String[]{KEY_TITLE,KEY_COUNT},new int[]{R.id.textTv,R.id.simbolCntTv});
    }

    @NonNull
    private String[] oldPrepareContent() {
        return getString(R.string.large_text).split("\n\n");
    }
    private List<Map<String,String>> prepareContent(String noteTxt) {
        List<Map<String,String>> result=new ArrayList<>();
        String[] titles=noteTxt.split("\n\n");
        for (String title:titles)
        {
            Map<String,String>map = new HashMap<>();
            map.put(KEY_TITLE,title);
            map.put(KEY_COUNT,title.length()+"");
            result.add(map);
        }
        return  result;
    }
}
