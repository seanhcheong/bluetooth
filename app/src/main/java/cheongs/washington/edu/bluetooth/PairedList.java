package cheongs.washington.edu.bluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class PairedList extends Activity {
    ListView viewOfList;
    String[] pairs;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_list);
        viewOfList = (ListView) findViewById(R.id.listView);
        Bundle funBundle = getIntent().getExtras();
        pairs = funBundle.getStringArray("pairs");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, pairs);
        viewOfList.setAdapter(adapter);
    }
}
