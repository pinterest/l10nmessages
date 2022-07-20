package com.pinterest.l10nmessages.L10nPropertiesProcessorTest_IO.invalidSourceNoCheck;

import com.pinterest.l10nmessages.L10nProperties;
import com.pinterest.l10nmessages.MessageFormatValidationTarget;

@L10nProperties(
    baseName =
        "com.pinterest.l10nmessages.L10nPropertiesProcessorTest_IO.invalidSourceNoCheck.TestMessages",
    messageFormatValidationTargets = {MessageFormatValidationTarget.LOCALIZED})
public class TestApp {}
