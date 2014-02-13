package com.futureplatforms.kirin.console.json;

import org.junit.Before;

import com.futureplatforms.kirin.console.Kirin;

public class JSONTest {
	@Before
	public void before() throws Exception {
		Kirin.kickOff();
	}
	/*
	@Test
	public void testConstructorSuccess() {
		JSONDelegate json = StaticDependencies.getInstance().getJsonDelegate();
		try {
			json.createJSONObject("{}");
			// pass
		} catch (JSONException e) {
			fail();
		}
	}
	
	@Test
	public void testConstructorErr() {
		JSONDelegate json = StaticDependencies.getInstance().getJsonDelegate();
		try {
			json.createJSONObject("");
			fail();
		} catch (JSONException e) {
			// pass
		}
	}
	
	@Test
	public void testPropertyNotPresent() {
		JSONDelegate json = StaticDependencies.getInstance().getJsonDelegate();
		try {
			JSONObject obj = json.createJSONObject("{}");
			obj.get("foo");
			fail();
		} catch (JSONException e) {
			// pass
		}
	}
	
	@Test
	public void testPropertyPresent() {
		JSONDelegate json = StaticDependencies.getInstance().getJsonDelegate();
		try {
			JSONObject obj = json.createJSONObject("{\"foo\":\"bar\"}");
			assertSame("bar", obj.get("foo"));
			obj = json.createJSONObject("{\"foo\":true}");
			assertSame(true, obj.get("foo"));
		} catch (JSONException e) {
			fail();
		}
	}*/
}
