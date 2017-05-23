package com.marbit.hobbytrophies.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.model.MessageMeeting;
import com.marbit.hobbytrophies.overwrite.CircleTransform;
import com.marbit.hobbytrophies.utilities.DateUtils;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcelo on 18/11/16.
 */

public class MessageMeetingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessageMeeting> messageMeetingList;
    private Context context;
    private RequestQueue requestQueue;

    public MessageMeetingAdapter(Context context) {
        this.messageMeetingList = new ArrayList<>();
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.list_item_meeting_messages, parent, false);
        viewHolder = new MessageViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageViewHolder gameViewHolder = (MessageViewHolder) holder;
        MessageMeeting messageMeeting = messageMeetingList.get(position);
        try {
            gameViewHolder.bindMessage(messageMeeting, position);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return messageMeetingList.size();
    }

    public void setList(List<MessageMeeting> list) {
        this.messageMeetingList.addAll(list);
    }

    public void clearAll() {
        this.messageMeetingList.clear();
    }

    private class MessageViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView userName;
        private ImageView imageUser;
        private TextView messageText;
        private TextView messageDate;
        private ImageView imageMoreOptions;

        public MessageViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.userName = (TextView) itemView.findViewById(R.id.user_name);
            this.imageUser = (ImageView) itemView.findViewById(R.id.user_image);
            this.messageText = (TextView) itemView.findViewById(R.id.message_text);
            this.messageDate = (TextView) itemView.findViewById(R.id.text_view_date_message);
            this.imageMoreOptions = (ImageView) itemView.findViewById(R.id.ic_more_options);

        }

        public void bindMessage(final MessageMeeting messageMeeting, final int position) throws ParseException {
            this.userName.setText(messageMeeting.getUser().getPsnId());
            this.messageText.setText(messageMeeting.getText());

            DateUtils dateUtils = new DateUtils();
            this.messageDate.setText(dateUtils.convertToLocalTimeString(messageMeeting.getDate()));
            Picasso.with(context)
                    .load(messageMeeting.getUser().getAvatarUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .transform(new CircleTransform())
                    .into(this.imageUser);
            this.imageMoreOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v, messageMeeting.getId(), position);
                }
            });
            if(messageMeeting.getUser().getPsnId().equals(Preferences.getUserName(context))){
                this.imageMoreOptions.setVisibility(View.VISIBLE);
            }else {
                this.imageMoreOptions.setVisibility(View.INVISIBLE);
            }
        }


        public void showPopup(View v, final int messageId, final int position) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.actions, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.option_delete_message:
                            sendRequestDeleteMessage(messageId, position);
                            break;
                    }
                    return true;
                }
            });
            popup.show();
        }


        private void sendRequestDeleteMessage(int messageId, int position) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("message-id", messageId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            requestQueue.add(getStringPostDeleteMessage(jsonObject, position)).setShouldCache(false);
        }

        private JsonObjectRequest getStringPostDeleteMessage(JSONObject jsonParams, final int position){
            return (JsonObjectRequest) new JsonObjectRequest(Request.Method.POST,"http://www.hobbytrophies.com/foros/ps3/delete-message-meeting.php", jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int responseCode = response.getInt("response-code");
                                if(responseCode == 1){
                                    messageMeetingList.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Mensaje borrado", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error al borrar el mensaje", Toast.LENGTH_SHORT).show();
                }
            }).setShouldCache(false);

        }

    }
}
