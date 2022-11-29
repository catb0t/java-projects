/*
 * Copyright (c) 2022 Cat Stevens. All rights reserved.
 */

package org.catb0t;

import java.nio.file.*;

public interface Configurable {
    boolean isVanilla ();

    Configurable loadRules ();

    Configurable withRules (GameRules newRules);

    GameRules rules ();

    Configurable setRulesBasename (Path p);

    Path rulesLocation ();

    String rulesBasename ();
}
