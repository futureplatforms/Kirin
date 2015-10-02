package com.futureplatforms.kirin.android.dropbox;

import android.database.Cursor;

/**
 * Created by douglas on 28/09/15.
 */
public class AndroidDropboxes {
    public final AndroidDropbox<byte[]> _NetworkDropbox = new AndroidDropbox<>();
    public final AndroidDropbox<Cursor> _DbDropbox = new AndroidDropbox<>();

    private AndroidDropboxes() { }

    private static AndroidDropboxes _Instance;
    public static AndroidDropboxes getInstance() {
        if (_Instance == null) {
            _Instance = new AndroidDropboxes();
        }
        return _Instance;
    }
}
