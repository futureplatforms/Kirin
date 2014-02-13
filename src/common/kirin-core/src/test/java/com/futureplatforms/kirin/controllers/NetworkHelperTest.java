package com.futureplatforms.kirin.controllers;

import org.junit.Test;

public class NetworkHelperTest {

    @Test
    public void testBasicRequest() {
        // Set up the dependencies we want to replace
       /* final PlatformTimerTask ptt = new PlatformTimerTask() {

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
        		deps.getLocationDelegate(), netClient,
                deps.getJsonDelegate(), deps.getXmlParser(),
                deps.getFormatter(), deps.getProfile());

        InternalDependencies intDeps = InternalDependencies.getInstance();
        intDeps.setDependencies(timer, null);
        
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

        context.assertIsSatisfied();*/
    }

}
