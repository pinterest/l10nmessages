package com.pinterest.l10nmessages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
public @interface L10nPropertiesList {

  L10nProperties[] value();
}
