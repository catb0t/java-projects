/*
 * Copyright (c) 2022 Cat Stevens. All rights reserved.
 */

package org.catb0t.project2guess;

import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

public class GuessGame {
    static final Pattern     DOLLAR_NAME_REFERENCE = Pattern.compile(
        Pattern.quote("$") + "[a-zA-Z_0-9]+"
    );
    static final DefinedName UNKNOWN_NAME          = new DefinedName(
        "++UNKNOWN VARIABLE++",
        "++UNKNOWN VALUE++"
    );

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

    public GuessGame playGame () {

        final long maxTries = this.config().rules().maxTries();
        for (int i = 0; i < maxTries; i++) {
            System.out.println("<playGame> " + (new Random().nextInt(20) + 1));
            System.out.println("introMessage: "
                               + this.localiser.getRandomString("introMessage"));
            System.out.println("preludeMessage: "
                               + this.localiser.getRandomString("preludeMessage"));
            System.out.println("tryPromptMessage: "
                               + this.localiser.getRandomString("tryPromptMessage"));
            System.out.println("guessHigherMessage: "
                               + this.localiser.getRandomString("guessHigherMessage"));
            System.out.println("guessLowerMessage: "
                               + this.localiser.getRandomString("guessLowerMessage"));
            System.out.println("invalidInputMessage: "
                               + this.localiser.getRandomString("invalidInputMessage"));
            System.out.println("failMessage: "
                               + this.localiser.getRandomString("failMessage"));
            System.out.println("winMessage: "
                               + this.localiser.getRandomString("winMessage"));
        }

        return this;
    }

    void evaluateListExpression (final List<String> expr) {
        final var baseFuncCall = expr.get(0);
        switch ( baseFuncCall ) {
            case "if":
                break;
            case "<":
            case ">":
                this.evaluateBoolExpression(expr);
            default:
                throw new IllegalArgumentException(
                    "unknown function for afterEachIteration: " + baseFuncCall
                );
        }
    }

    private DefinedName evaluateBoolExpression (final List<String> expr) {
        @NonNls final var baseFuncCall = expr.get(0);

        final var esize = expr.size();
        if (("<".equals(baseFuncCall) || ">".equals(baseFuncCall)) && (esize != 3)) {
            throw new IllegalArgumentException(
                "wrong number of arguments to expression '"
                + baseFuncCall + "': expected 2, got " + esize
            );
        }

        return new DefinedName();
    }


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


    public String replaceDollarNameReferences (final CharSequence source) {
        final Matcher matcher = GuessGame.DOLLAR_NAME_REFERENCE.matcher(source);

        return matcher.replaceAll(mr -> this.dollarNameLookup(mr.group()));
    }

    private String dollarNameLookup (final String s) {
        return this.getDollarNameValue(s).toString();
    }

    private DefinedName getDollarNameValue (final String param) {
        final var val = this.nameTable.getOrDefault(
            param.substring(1), GuessGame.UNKNOWN_NAME
        );
        if (val == GuessGame.UNKNOWN_NAME) {
            throw new IllegalArgumentException(
                "unknown name: "
                + param + "(is it defined in " +
                "the declaredNames key?)"
            );
        }
        return val;
    }
}
