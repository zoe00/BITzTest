package zalando.de.bitz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private int REQ_CODE_NEW_ARTICLE = 1;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> values;
    private String wtf = "wtf wtf...";
    private String wtf1 = "wtf another...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
    }

    private void init() {
        initToolbar();
        values = new ArrayList<String>();
        values.add("Article Ruby");
        values.add("Article Gem");
        values.add("Article Diamond");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        ListView list = (ListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CartActivity.this, ArticleActivity.class);
                intent.putExtra("name", values.get(position));
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(CartActivity.this, ArticleActivity.class), REQ_CODE_NEW_ARTICLE);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#FE9A2E"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_NEW_ARTICLE) {
            if (resultCode == RESULT_OK) {
                Bundle res = data.getExtras();
                String result = res.getString("res");
                if (!values.contains(result))
                    values.add(0, result);
                else
                    Toast.makeText(this, "Article already exists", Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
            }
        }
    }
}
