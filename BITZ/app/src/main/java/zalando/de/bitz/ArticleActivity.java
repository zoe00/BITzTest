package zalando.de.bitz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.expandablelayout.library.ExpandableLayout;

import java.util.ArrayList;

/**
 * Created by zfarooq on 22/10/15.
 */
public class ArticleActivity extends AppCompatActivity implements View.OnClickListener {

    private ExpandableLayout l1, l2, l3, l4, l5;
    ArrayList mSelectedItemsSize = new ArrayList();
    ArrayList mSelectedItemsSupColorCode = new ArrayList();
    ArrayList mSelectedItemsZalColorName = new ArrayList();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText etProductName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        init();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void init() {
        initToolbar();
        l1 = (ExpandableLayout) findViewById(R.id.expandableLayoutColor);
        l2 = (ExpandableLayout) findViewById(R.id.expandableLayoutPrice);
        l3 = (ExpandableLayout) findViewById(R.id.expandableLayoutOrder);
        l4 = (ExpandableLayout) findViewById(R.id.expandableLayoutSizing);
        l5 = (ExpandableLayout) findViewById(R.id.expandableLayoutComments);
        etProductName = (EditText) findViewById(R.id.etProductName);
        if (getIntent().getExtras() != null) {
            etProductName.setText(getIntent().getExtras().getString("name"));
        }
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

    private void showMultipleChoiceDialog(String title, final String[] choices, final ArrayList<String> mSelectedItems, final TextView result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMultiChoiceItems(choices, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked && !mSelectedItems.contains(choices[which]))
                                    mSelectedItems.add(choices[which]);
                                else if (!isChecked && mSelectedItems.contains(choices[which]))
                                    mSelectedItems.remove(choices[which]);
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String res = "";
                        for (int i = 0; i < mSelectedItems.size(); i++) {
                            res = res + mSelectedItems.get(i);
                            if (i != mSelectedItems.size() - 1)
                                res = res + ", ";
                        }
                        if (res.length() != 0)
                            result.setText(res);
                    }
                })
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView img = (ImageView) findViewById(R.id.imageView1);
            img.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvSize) {
            showMultipleChoiceDialog("Choose size(s)", new String[]{"S", "M", "L", "XL"}, mSelectedItemsSize, (TextView) v);
        } else if (v.getId() == R.id.tvSupColor) {
            showMultipleChoiceDialog("Choose supplier color code(s)", new String[]{"Red", "Green", "Blue", "Cyan"}, mSelectedItemsSupColorCode, (TextView) v);
        } else if (v.getId() == R.id.tvZalColorName) {
            showMultipleChoiceDialog("Choose zalando color name(s)", new String[]{"ZRed", "ZGreen", "ZBlue", "ZCyan"}, mSelectedItemsZalColorName, (TextView) v);
        } else if (v.getId() == R.id.btnCancel)
            finish();
        else if (v.getId() == R.id.btnSave) {
            if (etProductName.getText().length() == 0)
                Toast.makeText(this, "Please enter product name", Toast.LENGTH_LONG).show();
            else {
                Bundle conData = new Bundle();
                conData.putString("res", etProductName.getText().toString());
                Intent intent = new Intent();
                intent.putExtras(conData);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (v.getId() == R.id.imageView1)
            dispatchTakePictureIntent();
        else {
            int pos = Integer.parseInt(v.getTag().toString());
            ExpandableLayout layout = null;
            if (pos == 0)
                layout = l1;
            else if (pos == 1)
                layout = l2;
            else if (pos == 2)
                layout = l3;
            else if (pos == 3)
                layout = l4;
            else if (pos == 4)
                layout = l5;
            if (layout.isOpened()) {
                layout.hide();
                ImageView icon = (ImageView) ((ViewGroup) v).getChildAt(1);
                icon.setImageResource(R.drawable.ic_expand_close);
            } else {
                layout.show();
                ImageView icon = (ImageView) ((ViewGroup) v).getChildAt(1);
                icon.setImageResource(R.drawable.ic_expand_open);
            }
        }
    }
}