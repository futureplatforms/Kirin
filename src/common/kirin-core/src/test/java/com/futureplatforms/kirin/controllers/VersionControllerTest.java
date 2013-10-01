package com.futureplatforms.kirin.controllers;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import com.futureplatforms.kirin.controllers.KirinVersionController.KirinVersionDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;

public class VersionControllerTest {
    private Mockery context = new JUnit4Mockery();

    @Test
    public void testFirstRun() {
        //Kirin.kickOff();
        
        final SettingsDelegate settings = new ConsoleSettings();
        final KirinVersionDelegate version = context.mock(KirinVersionDelegate.class);
        KirinVersionController cont = new KirinVersionController(settings);
        
        
        // Put version 1.0.0 in there and ensure firstRun is called
        context.checking(new Expectations() {{
            oneOf(version).firstRun();
        }});
        cont.currentVersion("1.0.0", version);
        context.assertIsSatisfied();

        // Run again with 1.0.0, and ensure firstRun is NOT called
        context.checking(new Expectations() {{
        }});
        cont.currentVersion("1.0.0", version);
        context.assertIsSatisfied();
        
        // Now run with 1.0.1 and ensure upgraded is called with 1.0.0 as the old version
        context.checking(new Expectations() {{
            oneOf(version).upgraded("1.0.0");
        }});
        cont.currentVersion("1.0.1", version);
        context.assertIsSatisfied();
        
        // Finally run again with 1.0.1 and ensure upgraded is NOT called
        context.checking(new Expectations() {{
        }});
        cont.currentVersion("1.0.1", version);
        context.assertIsSatisfied();
    }
}
