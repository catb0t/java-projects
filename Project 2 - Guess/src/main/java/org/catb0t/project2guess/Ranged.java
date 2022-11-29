/*
 * Copyright (c) 2022 Cat Stevens. All rights reserved.
 */

package org.catb0t;

import java.util.regex.*;

public interface Ranged {
    Pattern DOLLAR_NAME_REFERENCE = Pattern.compile(Pattern.quote("$") + "[a-zA-Z_0-9]+");

    String replaceDollarNameReferences (CharSequence source);
}
