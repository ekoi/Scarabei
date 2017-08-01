
package com.jfixby.scarabei.red.flutter.plugins.android;

import com.jfixby.scarabei.android.api.Android;
import com.jfixby.scarabei.api.flutter.plugins.FlutterPluginSpecs;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.SystemInfo;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class ScarabeiAndroidFlutterProxy implements MethodCallHandler {

	@Override
	public void onMethodCall (final MethodCall call, final Result result) {
		L.d("ScarabeiAndroidPlugin.onMethodCall", call.method);
		if (call.method.equals("getSystemInfo")) {
// final String message = "Android " + android.os.Build.VERSION.RELEASE + " " + Android.getSystemInfo();
			final SystemInfo systemInfo = Android.getSystemInfo();
			result.success(systemInfo.listParameters().toJavaMap());
		} else {
			result.notImplemented();
		}
	}

	public static FlutterPluginSpecs filloutRegistration (final FlutterPluginSpecs specs) {
		specs.methodCallHandler = new ScarabeiAndroidFlutterProxy();
		return specs;
	}
}