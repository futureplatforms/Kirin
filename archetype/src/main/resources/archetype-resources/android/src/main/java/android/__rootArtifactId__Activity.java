#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.futureplatforms.kirin.android.Kirin;
import ${package}.core.modules.TestModule;
import ${package}.core.modules.natives.TestModuleNative;

public class ${rootArtifactId}Activity extends Activity implements TestModuleNative {

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Kirin.kickOff(this);
        TestModule tm = new TestModule();
        tm.onPrototypeLoad(this);
        tm.testyMethod("here's a string from android native", 1337);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.main, menu);
    	return true;
    }

	@Override
    public void testyNativeMethod(String str) {
        Log.d("HelloAndroidActivity", "testyNativeMethod(" + str + ")");
    }

}

