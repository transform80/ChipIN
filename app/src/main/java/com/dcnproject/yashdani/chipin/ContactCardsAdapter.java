package com.dcnproject.yashdani.chipin;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Yash Dani on 29-03-2018.
 */

public class ContactCardsAdapter extends RecyclerView.Adapter<ContactCardsAdapter.MyViewHolder> {

    private List<ContactCards> contactList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email;
        public CircleImageView display;
        View itemView;
        public MyViewHolder(View view) {
            super(view);
            itemView = view;
            name = (TextView) view.findViewById(R.id.name);
            email = (TextView) view.findViewById(R.id.email);
            display = (CircleImageView) view.findViewById(R.id.contactImage);
        }
        public void setImageUrl(final Context ctx, final String image){
            final CircleImageView display = (CircleImageView) itemView.findViewById(R.id.contactImage);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(display, new Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(display);
                }
            });
        }
    }
    public ContactCardsAdapter(List<ContactCards> contactList) {
        this.contactList = contactList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_row, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ContactCards contactCards = contactList.get(position);
        holder.name.setText(contactCards.getName());
        holder.email.setText(contactCards.getEmail());
//        holder.display.setText(contactCards.getDisplay());
        holder.setImageUrl(holder.display.getContext(),contactCards.getDisplay());
    }
    @Override
    public int getItemCount() {
        return contactList.size();
    }
}

