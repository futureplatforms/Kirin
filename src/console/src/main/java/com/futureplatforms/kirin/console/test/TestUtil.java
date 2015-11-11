package com.futureplatforms.kirin.console.test;

import org.junit.Assert;

import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by douglashoskins on 22/09/2015.
 */
public class TestUtil {

    public static void setup() throws InterruptedException {
        TestUtil.deleteDB();
    }

    public static void printoutDB(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount() + 1;
        while (rs.next()) {
            System.out.println("=== ROW START ===");
            for (int i=1; i<cols; i++) {
                System.out.println(meta.getColumnName(i) + ": " + rs.getString(i));
            }
            System.out.println("=== ROW END ===");
        }
    }

    public static void deleteDB() {
        String currentDir = System.getProperty("user.dir");
        File[] files = new File(currentDir).listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith("db")) {
                file.delete();
            }
        }
    }

    public static Thread doAsync(final Runnable r) throws InterruptedException {
        return doAsync(r, false);
    }

    public static Thread doAsync(final Runnable r, boolean waitTerminated) throws InterruptedException {
        final boolean[] didTimeOut = new boolean[1];
        Thread testThread = new Thread(r);

        synchronized (r) {
            testThread.start();
            final Thread curr = Thread.currentThread();
            Thread timeoutThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // The point of this is to make sure tests fail gracefully if the callbacks
                        // we're waiting for don't happen
                        Thread.sleep(3000);
                        synchronized (r) {
                            // If the original thread is still waiting after 3000ms, call it a fail.
                            if (curr.getState() == Thread.State.WAITING) {
                                didTimeOut[0] = true;
                                r.notify();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            timeoutThread.start();

            r.wait();
        }
        if (didTimeOut[0]) {
            Assert.fail("Timed out!");
        }

        if (waitTerminated) {
            waitTerminated(testThread);
        }
        return testThread;
    }

    public static void waitTerminated(Thread t) {
        while (t.getState() != Thread.State.TERMINATED) {
            Thread.yield();
        }
        return;
    }
}
