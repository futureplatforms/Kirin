#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.core.modules.natives;

import com.futureplatforms.kirin.gwt.client.modules.IKirinNativeObject;

public interface TestModuleNative extends IKirinNativeObject {
    void testyNativeMethod(String str); 
}
