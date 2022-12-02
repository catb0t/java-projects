/*
 * Copyright (c) 2022 Cat Stevens. All rights reserved.
 */

package org.catb0t;

import java.util.*;

public record GameRules(
    List<String> stringsFile,
    Long maxTries,
    Boolean canPlayAgain,
    String validInputPattern,
    Boolean isInputPatternFullMatch,
    List<DefinedName> declaredNames
) {
}
