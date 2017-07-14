package com.edgardrake.flameseeker.model;

import android.content.Context;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.data.LocalStorage;
import com.edgardrake.flameseeker.lib.data.Serializer;
import com.google.gson.annotations.Expose;

/**
 * Created by Edgar Drake on 12-Jun-17.
 */

public class AuthUser {

    private static AuthUser currentUser;

    public static AuthUser getInstance(Context context) {
        Context appContext = context.getApplicationContext();
        if (currentUser == null) {
            String serialized = LocalStorage.getInstance(appContext)
                .getString(LocalStorage.USER_OBJECT, null);
            currentUser = serialized != null?
                Serializer.GSON().fromJson(serialized, AuthUser.class) : new AuthUser();
        }
        if (currentUser.editor == null) {
            currentUser.editor = new Editor(appContext);
        }
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

    private Editor editor;

    public Editor edit() {
        return editor;
    }

    private AuthUser() {}

    /**
     * Copy constructor of user
     * @param source Already constructed source user to be copied
     */
    private AuthUser(AuthUser source) {
        this.id = source.id;
        this.username = source.username;
        this.email = source.email;
        this.name = source.name;
    }

    private void commit(Editor editor) {
        if (editor.email != null)       this.email = editor.email;
        if (editor.username != null)    this.username = editor.username;
        if (editor.name != null)        this.name = editor.name;
    }

    public static class Editor {

        @Expose
        private String email;
        @Expose
        private String username;
        @Expose
        private String name;

        private Context context;

        private Editor(Context c) {
            context = c;
        }

        public Editor setEmail(String email) {
            this.email = email;
            return this;
        }

        public Editor setUsername(String username) {
            this.username = username;
            return this;
        }

        public Editor setName(String name) {
            this.name = name;
            return this;
        }

        public void commit() {
            currentUser.commit(this);

            String userObject = Serializer.GSON().toJson(currentUser);
            LocalStorage.getInstance(context).edit()
                .putString(context.getString(R.string.localstorage_auth_user), userObject)
                .apply();
        }
    }

    interface Callback {

    }
}
