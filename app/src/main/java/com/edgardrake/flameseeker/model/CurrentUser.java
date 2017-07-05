package com.edgardrake.flameseeker.model;

import android.content.Context;

import com.edgardrake.flameseeker.lib.data.LocalStorage;
import com.edgardrake.flameseeker.lib.data.Serializer;
import com.google.gson.annotations.Expose;

/**
 * Created by Edgar Drake on 12-Jun-17.
 */

public class CurrentUser {

    private static CurrentUser currentUser;

    public static CurrentUser getInstance(Context context) {
        if (currentUser == null) {
            String serialized = LocalStorage.getInstance(context.getApplicationContext())
                .getString(LocalStorage.USER_OBJECT, null);
            currentUser = serialized != null? Serializer.GSON().fromJson(serialized, CurrentUser.class):new CurrentUser();
        }
        currentUser.localEditor = new Editor(context.getApplicationContext());
        return currentUser;
    }

    @Expose
    private String id;
    @Expose
    private String email;
    @Expose
    private String username;
    @Expose
    private String name;

    private Editor localEditor;

    public String getID() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Editor edit() {
        return localEditor;
    }

    private CurrentUser() {}

    /**
     * Copy constructor of user
     * @param source
     */
    private CurrentUser(CurrentUser source) {
        this.id = source.id;
        this.username = source.username;
        this.email = source.email;
        this.name = source.name;
    }

    public static class Editor {

        private Context context;
        private CurrentUser copyUser;

        Editor(Context c) {
            context = c;
            copyUser = new CurrentUser(currentUser);
        }

        public Editor setEmail(String email) {
            copyUser.email = email;
            return this;
        }

        public Editor setUsername(String username) {
            copyUser.username = username;
            return this;
        }

        public Editor setName(String name) {
            copyUser.name = name;
            return this;
        }

        public void commit() {
            String userObject = Serializer.GSON().toJson(copyUser);
            LocalStorage.getInstance(context).edit()
                .putString(LocalStorage.USER_OBJECT, userObject).apply();
            currentUser = copyUser;
        }
    }

    interface Callback {

    }
}
