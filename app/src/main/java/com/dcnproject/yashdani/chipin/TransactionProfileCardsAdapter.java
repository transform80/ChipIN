package com.dcnproject.yashdani.chipin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yash Dani on 30-03-2018.
 */

public class TransactionProfileCardsAdapter extends RecyclerView.Adapter<TransactionProfileCardsAdapter.MyViewHolder> {

    private List<TransactionProfileCards> transProfileList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView profilePayee, profileAmount, profileDesc;

        public MyViewHolder(View view) {
            super(view);
            profilePayee = (TextView) view.findViewById(R.id.profilePayee);
            profileDesc = (TextView) view.findViewById(R.id.profileDesc);
            profileAmount = (TextView) view.findViewById(R.id.profileAmount);
        }
    }


    public TransactionProfileCardsAdapter(List<TransactionProfileCards> transProfileList) {
        this.transProfileList = transProfileList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_row_profile, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TransactionProfileCards transactionProfileCards = transProfileList.get(position);
        holder.profilePayee.setText(transactionProfileCards.getPayee());
        holder.profileDesc.setText(transactionProfileCards.getDesc());
        holder.profileAmount.setText(transactionProfileCards.getAmount());
    }

    @Override
    public int getItemCount() {
        return transProfileList.size();
    }
}
