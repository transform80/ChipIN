package com.dcnproject.yashdani.chipin;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yash Dani on 29-03-2018.
 */

public class ContactCardsAdapter extends RecyclerView.Adapter<ContactCardsAdapter.MyViewHolder> {

    private List<ContactCards> contactList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email;
        public ImageView display;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            email = (TextView) view.findViewById(R.id.email);
            display = (ImageView) view.findViewById(R.id.contactImage);
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
        /*holder.display.setImageResource(contactCards.getDisplay());*/
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}

