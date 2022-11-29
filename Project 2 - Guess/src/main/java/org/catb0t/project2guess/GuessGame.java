/*
 * Copyright (c) 2022 Cat Stevens. All rights reserved.
 */

package org.catb0t;

import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

public class GuessGame implements Ranged, Interactive, LoopableGame {
    static final  String                   DEFINE_NAMES_KEY = "define_names";
    final         Map<String, DefinedName> nameTable;
    private final Configurable             config;
    private final Localiser                localiser;

    GuessGame (final Configurable configurable, final Localiser local) {
        this.config    = configurable;
        this.localiser = local;

        this.nameTable = this.config()
                             .rules()
                             .declaredNames()
                             .stream().collect(Collectors.toMap(v -> v.name, Function.identity()));

    }

    public final Configurable config () {
        return this.config;
    }

    @Override
    public Interactive playGame () {
        System.out.println("<playGame> " + new Random().nextInt(20) + 1);
        return this;
    }

    @Override
    public String toString () {
        return "GuessGame{" +
               "config=" + this.config +
               '}';
    }

    String localiseFieldName (final String fieldName) {
        return this.replaceDollarNameReferences(
            this.localiser.getRandomString(fieldName)
        );
    }

    @Override
    public String replaceDollarNameReferences (final CharSequence source) {
        final Matcher matcher = Ranged.DOLLAR_NAME_REFERENCE.matcher(source);

        return matcher.replaceAll(this::dollarNameLookup);
    }

    private String dollarNameLookup (final MatchResult matchResult) {
        return this.nameTable.getOrDefault(
            matchResult.group(),
            new DefinedName("++UNKNOWN VARIABLE++", "++UNKNOWN VALUE++")
        ).stringValue;
    }
}
