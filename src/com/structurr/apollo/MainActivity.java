package com.structurr.apollo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import android.widget.*;
import com.structurr.apollo.common.activities.SampleActivityBase;
import com.structurr.apollo.common.logger.Log;
import com.structurr.apollo.common.logger.LogFragment;
import com.structurr.apollo.common.logger.LogWrapper;
import com.structurr.apollo.common.logger.MessageOnlyLogFilter;
import com.structurr.apollo.bluetoothchat.BluetoothChatFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SampleActivityBase {

    ListView historyListView;
    List messages = new ArrayList<Message>();

    private boolean mLogShown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateMessages();

        setContentView(R.layout.main);
        historyListView = (ListView) findViewById(R.id.messages_history_listview);
        historyListView.setAdapter(new CustomListAdapter(this));

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;
                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) {
                    output.setDisplayedChild(1);
                } else {
                    output.setDisplayedChild(0);
                }
                supportInvalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }

    private void populateMessages() {
        messages.add(new Message("Hi!", false));
        messages.add(new Message("Hello :)", true));
        messages.add(new Message("How are you doing?", true));
        messages.add(new Message("Good, how are you?", false));
        messages.add(new Message("Just doing this Hackathon for Purdue Hackers, you should come!", true));
        messages.add(new Message("That sounds awesome!", false));
        messages.add(new Message("Is there free food?", false));
        messages.add(new Message("Of course, come make friends.", true));
        messages.add(new Message("But what if I already have friends?", false));
        messages.add(new Message("Doesn't matter, Hello World is open to people with friends and those without.", true));
        messages.add(new Message("WOW :O", false));
        messages.add(new Message("I've never been to a hackathon like that before!", false));
        messages.add(new Message("Yeah Computer Science is amazing!", true));
        messages.add(new Message("Yeah but girls develop an intense aversion to you...", false));
        messages.add(new Message("Lolz true fam.", true));
    }

    class CustomListAdapter extends BaseAdapter {

        Context context;
        private LayoutInflater inflater;

        public CustomListAdapter(MainActivity mainActivity) {
            context = mainActivity;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView;
            TextView messageTextView;

            Message message = (Message) getItem(position);

            if (message.isMe()) {
                rowView = inflater.inflate(R.layout.list_item_outgoing, null);
                messageTextView = (TextView) rowView.findViewById(R.id.outgoing_text_view);
                messageTextView.setText(message.getText());
            } else {
                rowView = inflater.inflate(R.layout.list_item_incoming, null);
                messageTextView = (TextView) rowView.findViewById(R.id.incoming_text_view);
                messageTextView.setText(message.getText());
            }

            return rowView;
        }
    }
}
