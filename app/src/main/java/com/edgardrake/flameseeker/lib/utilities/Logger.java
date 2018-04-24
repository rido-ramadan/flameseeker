package com.edgardrake.flameseeker.lib.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edgardrake.flameseeker.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Edgar Drake on 20-Sep-17.
 */

public class Logger {

    private List<Pair<String, String>> entries;
    private Context context;

    private static Logger logger;

    private Logger(Context context) {
        this.entries = new ArrayList<>();
        this.context = context;
    }

    public static Logger log(Context context) {
        if (logger == null) {
            logger = new Logger(context);
        }
        logger.entries.clear();
        return logger;
    }

    public Logger addEntry(String key, String value) {
        entries.add(new Pair<>(key, value));
        return this;
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_logger, null, false);
        RecyclerView mLogEntries = ButterKnife.findById(view, R.id.log_entries);
        mLogEntries.setHasFixedSize(true);
        mLogEntries.setAdapter(new EntryAdapter());
        new AlertDialog.Builder(context)
            .setView(view)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .create().show();
    }

    public class EntryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.log_entry_key)
        TextView mKey;
        @BindView(R.id.log_entry_value)
        TextView mValue;

        private EntryHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_logging, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }

    public class EntryAdapter extends RecyclerView.Adapter<EntryHolder> {

        @Override
        public int getItemCount() {
            return entries.size();
        }

        @Override
        public EntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new EntryHolder(parent);
        }

        @Override
        public void onBindViewHolder(EntryHolder holder, int position) {
            String key = entries.get(holder.getAdapterPosition()).first;
            String value = entries.get(holder.getAdapterPosition()).second;
            holder.mKey.setText(key);
            holder.mValue.setText(value);
        }
    }
}
