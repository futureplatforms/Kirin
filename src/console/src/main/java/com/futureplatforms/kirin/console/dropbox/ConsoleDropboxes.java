package com.futureplatforms.kirin.console.dropbox;

import java.sql.ResultSet;

/**
 * Created by douglas on 28/09/15.
 */
public class ConsoleDropboxes {
    public final ConsoleDropbox<byte[]> _NetworkDropbox = new ConsoleDropbox<>();
    public final ConsoleDropbox<ResultSet> _DbDropbox = new ConsoleDropbox<>();

    private ConsoleDropboxes() { }

    private static ConsoleDropboxes _Instance;
    public static ConsoleDropboxes getInstance() {
        if (_Instance == null) {
            _Instance = new ConsoleDropboxes();
        }
        return _Instance;
    }
}
