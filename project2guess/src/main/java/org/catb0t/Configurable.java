package org.catb0t;

import java.nio.file.Path;

/**
 * An object deriving its specific rules from data stored elsewhere. The rule format is itself externally specified.
 */
public interface Configurable {
    boolean isVanilla ();

    Configurable loadRules ();

    Configurable saveRules (Path p);

    Configurable setRule (String ruleName, Rule ruleValue);

    Rule rule (String ruleName);

    Configurable setRulesLocation (Path p);

    Path rulesLocation ();

    Configurable setGrammarLocation (Path p);

    Path grammarLocation ();

}
