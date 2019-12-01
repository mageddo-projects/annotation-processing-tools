package com.acme.firstlevel;

import nativeimage.Reflection;

@Reflection(scanPackage = "com.acme.firstlevel", declaredFields = true)
public class Config {
}
