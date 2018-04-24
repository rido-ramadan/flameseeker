package com.edgardrake.flameseeker.lib.widget.viewcomponent;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;

/**
 * Created by Edgar Drake on 04-Jan-18.
 */

public abstract class ViewComponentDialog {

    private Context context;
    private View view;
    private AlertDialog dialog;

    /**
     * Super constructor that should be called when creating a custom view dialog
     * @param context Context
     * @param layoutRes Layout XML to be inflated as dialog
     */
    public ViewComponentDialog(Context context, @LayoutRes int layoutRes) {
        this.view = LayoutInflater.from(context).inflate(layoutRes, null, false);
        this.context = context;
        bind(this, view);
    }

    /**
     * @return Inflated view of the custom dialog
     */
    public View getView() {
        return view;
    }

    private AlertDialog create() {
        return new AlertDialog.Builder(context).setView(getView()).create();
    }

    private AlertDialog getDialog() {
        if (dialog == null) {
            dialog = create();
        }
        return dialog;
    }

    /**
     * Show the finished built dialog
     */
    public void show() {
        getDialog().show();
    }

    /**
     * Set the button action for the specified button ID
     * @param buttonID Resource ID of the dialog button. Must be defined in the view XML.
     * @param listener Action to be executed when the button is clicked
     */
    public ViewComponentDialog setOnButtonClicked(@IdRes int buttonID,
                                                  final OnDialogButtonClicked listener) {
        findById(view, buttonID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(getDialog(), view);
            }
        });
        return this;
    }

    /**
     * Set the action to be executed when the dialog is closed as cancelled.
     * @param listener Action to be executed.
     */
    public ViewComponentDialog setOnCancelled(DialogInterface.OnCancelListener listener) {
        getDialog().setOnCancelListener(listener);
        return this;
    }

    public interface OnDialogButtonClicked {
        void onClick(DialogInterface dialog, View view);
    }
}
