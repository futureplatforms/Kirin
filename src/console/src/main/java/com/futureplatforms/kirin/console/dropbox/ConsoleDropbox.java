package com.futureplatforms.kirin.console.dropbox;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by douglas on 28/09/15.
 */
public class ConsoleDropbox<T> {

    protected ConsoleDropbox() {}

    private final Map<Integer, T> _Map = new HashMap<>();

    private int _NextToken = Integer.MIN_VALUE;

    public String putItem(T c) {
        int next = _NextToken;
        _NextToken++;
        _Map.put(next, c);
        return ""+next;
    }

    public T getItem(String token) {
        return _Map.remove(Integer.parseInt(token, 10));
    }
}
