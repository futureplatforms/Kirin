package com.futureplatforms.kirin;

import java.util.LinkedList;
import java.util.Queue;

import com.futureplatforms.kirin.dependencies.StaticDependencies;

/**
 * Deals with a screen with a webview that we need to communicate with.
 */
public abstract class HybridModule<NativeSide extends HybridModuleNative> extends KirinModule<NativeSide>
{
    /**
     * This queue contains the messages that should be sent to the webview for the time when 
     * we receive a message from it indicating it is ready.
     */
    private Queue<String> msgsForWhenWebviewIsReady;

    /**
     * This queue contains any messages that come from the webview before the module's onResume is called.
     */
    private Queue<String[]> msgsForWhenModuleIsReady;
    
    /**
     * This is set to true when the webview tells us it is ready. Once set to true, it remains true until 
     * the module is destroyed.
     * It is set to false in the following circumstances:
     * <li>in the constructor</li>
     * <li>when onWebviewDestroyed() is called</li>
     * <li>when onLoad() is called after onExit() was called and there was no call to onWebviewDestroyed()</li>
     */
    private boolean webviewIsReady;

    /**
     * This is set to true when the module's onEntry() is called. 
     * It is reset to false in the constructor and onExit().
     */
    private boolean moduleIsReady;
    
    public HybridModule(final NativeSide nativeSide)
    {
        super(nativeSide);
        
        this.msgsForWhenWebviewIsReady = new LinkedList<String>();
        this.msgsForWhenModuleIsReady = new LinkedList<String[]>();
        this.webviewIsReady = false;
        this.moduleIsReady = false;
    }
    
    @Override
    protected void _onLoad() {
        super._onLoad();
        if(this.webviewIsReady) {
            // Right. This _onLoad() has been called more than once. 
            // We should assume that the webview has been destroyed since
            this.onWebviewDestroyed();
        }
    }
    
    /**
     * Needs to be called from Fragment.onCreateView() on Android and viewWillAppear on iOS. 
     */
    public final void onEntry()
    {
        for(final String[] msgFromWebview : this.msgsForWhenModuleIsReady) {
            onWebViewSaid(msgFromWebview[0], msgFromWebview[1]);
        }
        
        this.moduleIsReady = true;
        _onEntry();
    }
    
    /**
     * Called whenever onEntry() is called. Override if your module needs to know when the user has started to use it.
     */
    protected void _onEntry() {}
    
    /**
     * Needs to be called from Fragment.onDestroyView() on Android and viewWillDisappear on iOS. 
     */
    public final void onExit()
    {
        this.moduleIsReady = false;
        _onExit();
    }

    /**
     * Called whenever onEntry() is called. Override if your module needs to know when the user has stopped using it.
     */
    protected void _onExit() {}
    
    /**
     * This must be called from Android's Fragment.onDestroyView() or the Activity.onDestroy(), or whenever the webview is destroyed
     */
    public void onWebviewDestroyed() {
        this.webviewIsReady = false;
    }
    
    /**
     * Called from the native side when a webview is trying to tell us something.
     * 
     * One special message is "ready" which indicates that the webview has initialised and is capable of receiving messages from us.
     */
    public final void webViewSaid(final String method, final String params)
    {
        if ("ready".equals(method)) {
            webViewIsReady();
        } else if ("log".equals(method)) {
            StaticDependencies.getInstance().getLogDelegate().log("Hybrid webview LOG: " + params);
        } else {
            if(this.moduleIsReady) {
                onWebViewSaid(method, params);
            } else {
                //PlatformDependencies.getInstance().getLogDelegate().log("HybridScreen.webViewSaid("+method+"/"+params+") module isn't ready yet. Queuing until it is.");
                this.msgsForWhenModuleIsReady.add(new String[]{method, params});
            }
        }
    }
    
    /**
     * Called when a message is sent from the webview to this module.
     * 
     * @param method
     * @param params
     */
    protected abstract void onWebViewSaid(final String method, final String params);

    private void webViewIsReady() {
        //PlatformDependencies.getInstance().getLogDelegate().log("HybridScreen.webViewIsReady() Sending " + this.msgsForWhenWebviewIsReady.size() + " queued messages now.");      
        
        // We may have queued up some messages to send to the webview. Send them now it is ready.
        while(!this.msgsForWhenWebviewIsReady.isEmpty()) {
            final String msg = this.msgsForWhenWebviewIsReady.remove();
            
            getNativeObject().tellWebview(msg);
        }
        
        this.webviewIsReady = true;
    }
    
    protected void tellWebview(final String javascript) {
        final String encodedJavascript = encodeUTF8JsonAsAsciiJson(javascript);
        
        StaticDependencies.getInstance().getLogDelegate().log("trying to tell webview " + encodedJavascript);
        if(this.webviewIsReady) {
            // Send immediately
            getNativeObject().tellWebview(encodedJavascript);
        } else {
            // Queue it for for when we receive the message from the webview stating it's ready.
            //PlatformDependencies.getInstance().getLogDelegate().log("HybridScreen.tellWebview() webview isn't ready yet. Queuing message until it is.");
            this.msgsForWhenWebviewIsReady.add(encodedJavascript);
        }
    }
    
    private static String encodeUTF8JsonAsAsciiJson( final String encodeMe ) {
        final StringBuilder sb = new StringBuilder();
        final int encodeMeLength = encodeMe.length();
        for( int i = 0; i < encodeMeLength; i++ ) {
            final char c = encodeMe.charAt(i);
            
            if( c > 127 ) {
                sb.append("\\u");

                final StringBuffer hexString = new StringBuffer(Integer.toString(c, 16));
                
                while(hexString.length() < 4) {
                    hexString.insert(0, '0');
                }
                
                sb.append(hexString);
            }
            else {
                sb.append(c);
            }
        }
        
        return sb.toString();
    }
}
