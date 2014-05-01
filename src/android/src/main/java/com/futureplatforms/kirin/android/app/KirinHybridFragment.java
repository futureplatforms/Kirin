package com.futureplatforms.kirin.android.app;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.futureplatforms.kirin.HybridModule;
import com.futureplatforms.kirin.HybridModuleNative;

abstract public class KirinHybridFragment<Module extends HybridModule<HybridNativeObject>, HybridNativeObject extends HybridModuleNative>
		extends KirinFragment<Module, HybridNativeObject> implements
		HybridModuleNative {
	private WebView webView;

	public WebView getWebView() {
		return webView;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	public void setWebView(WebView webView, final String url) {
		this.webView = webView;

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setPluginState(PluginState.ON_DEMAND);
		webView.addJavascriptInterface(new InjectedObject(), "JavaProxyObject");

		// remove margin on right side of webView for older Android versions
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String xurl) {
				super.onPageFinished(view, xurl);
				if (url.equals(xurl)) {
					getModule().onEntry();
				}
			}
		});

		webView.loadUrl(url);

	}

	public class InjectedObject {
		@JavascriptInterface
		public void call(final String method, final String params) {
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					getModule().webViewSaid(method, params);
				}
			});
		}
	}

	@Override
	public void tellWebview(final String javascript) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				if (webView != null) {
					webView.loadUrl("javascript:" + javascript);
				}
			}
		});

	}

	@Override
	public void onDestroyView() {
		getModule().onWebviewDestroyed();
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (webView != null) {
			((ViewGroup) webView.getParent()).removeView(webView);
			webView.destroy();
			webView = null;
		}
	}

}
