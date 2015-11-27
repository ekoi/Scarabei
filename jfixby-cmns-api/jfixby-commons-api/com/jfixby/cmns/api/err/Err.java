package com.jfixby.cmns.api.err;

import com.jfixby.cmns.api.components.ComponentInstaller;

public class Err {

	static private ComponentInstaller<ErrorComponent> componentInstaller = new ComponentInstaller<ErrorComponent>("Error");

	public static final void installComponent(ErrorComponent component_to_install) {
		componentInstaller.installComponent(component_to_install);
	}

	public static final ErrorComponent invoke() {
		return componentInstaller.invokeComponent();
	}

	public static final ErrorComponent component() {
		return componentInstaller.getComponent();
	}

	public static void reportWarning(String message) {
		invoke().reportWarning(message);
	}

	public static void reportError(String message) {
		invoke().reportError(message);
	}

	public static void reportError(Throwable e) {
		invoke().reportError(e);
	}

}
