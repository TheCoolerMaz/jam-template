package com.jamtemplate.gwt;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Strudel")
public class Strudel {
    public static native void evaluate(String code);
    public static native void hush();
    public static native void setParam(String name, float value);
}
