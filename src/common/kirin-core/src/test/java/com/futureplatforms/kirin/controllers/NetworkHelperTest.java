package com.futureplatforms.kirin.controllers;

import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegateClient;
import com.futureplatforms.kirin.dependencies.TimerTask.PlatformTimerTask;
import com.futureplatforms.kirin.dependencies.TimerTask.TimerDelegate;
import com.google.common.collect.Maps;

public class NetworkHelperTest {
    private Mockery context = new JUnit4Mockery();

    @Test
    public void testBasicRequest() {
        // Set up the dependencies we want to replace
        final PlatformTimerTask ptt = new PlatformTimerTask() {

            @Override
            public void schedule(int delayMillis) {
            }

            @Override
            public void cancel() {
            }
        };

        TimerDelegate timer = new TimerDelegate() {
            public PlatformTimerTask getPlatformTimerTask() {
                return ptt;
            }
        };
        final NetworkDelegateClient netClient = context
                .mock(NetworkDelegateClient.class);

        StaticDependencies deps = StaticDependencies.getInstance();
        deps.setDependencies(deps.getLogDelegate(), deps.getSettingsDelegate(),
                timer, deps.getLocationDelegate(), netClient,
                deps.getJsonDelegate(), deps.getXmlParser(),
                deps.getFormatter(), deps.getProfile());

        NetworkHelper helper = new NetworkHelper(deps);

        context.checking(new Expectations() {
            {
                oneOf(netClient).doHttp(
                        with(equal(HttpVerb.GET)),
                        with(equal("url")),
                        with(aNull(String.class)),
                        (Map<String, String>) with(equal(Maps
                                .<String, String> newHashMap())),
                        with(any(NetworkResponse.class)));
            }
        });

        // Ensure a basic get succeeds
        helper.execute(HttpVerb.GET, "url", new NetworkResponse() {

            public void onSuccess(int res, String result,
                    Map<String, String> headers) {
            }

            public void onFail(String code) {
            }
        });

        context.assertIsSatisfied();
    }

}
