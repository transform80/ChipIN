package com.dcnproject.yashdani.chipin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yash Dani on 29-03-2018.
 */

public class TransactionGroupAdapter extends RecyclerView.Adapter<TransactionGroupAdapter.MyViewHolder> {

    private List<TransactionGroup> transactionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView payee, amount, desc;

        public MyViewHolder(View view) {
            super(view);
            payee = (TextView) view.findViewById(R.id.payee);
            desc = (TextView) view.findViewById(R.id.description);
            amount = (TextView) view.findViewById(R.id.amount);
        }
    }


    public TransactionGroupAdapter(List<TransactionGroup> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TransactionGroup transactionGroup = transactionList.get(position);
        holder.payee.setText(transactionGroup.getPayee());
        holder.desc.setText(transactionGroup.getDesc());
        holder.amount.setText(transactionGroup.getAmount());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}
