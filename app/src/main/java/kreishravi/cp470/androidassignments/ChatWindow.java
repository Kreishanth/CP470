package kreishravi.cp470.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "Chat Window Activity";
    Button send_button = null;
    EditText chat_text = null;
    ListView list_view = null;
    ChatAdapter messageAdapter = null;
    ArrayList<String> messages = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        send_button = findViewById(R.id.send_button);
        chat_text = findViewById(R.id.chat_edit_text);
        list_view = findViewById(R.id.list_view);

        messageAdapter = new ChatAdapter(this);
        list_view.setAdapter(messageAdapter);
    }

    public void addMessage(View view) {
        String chat_message = chat_text.getText().toString();
        messages.add(chat_message);
        messageAdapter.notifyDataSetChanged();
        chat_text.setText("");
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return messages.size();
        }

        public String getItem(int position) {
            return messages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;

            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }
}