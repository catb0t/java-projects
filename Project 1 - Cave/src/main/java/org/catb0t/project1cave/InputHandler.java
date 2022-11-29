package org.catb0t;

import java.util.*;

enum InputHandler {
    ;

    static boolean isInputOk (final String str, final List<String> allowedInputs) {
        return InputHandler.isInputOk(str, allowedInputs, () -> { });
    }

    static boolean isInputOk (
        final String str, final List<String> allowedInputs,
        final Runnable notOkAction
    )
        throws IllegalArgumentException {

        if ((str == null) || (allowedInputs == null)) {
            throw new IllegalArgumentException(
                "Null argument: " + (str == null ? str : "allowedInputs"));
        }

        final Collection<String> allowedSet = new HashSet<>(allowedInputs);

        if (allowedSet.isEmpty()) {
            throw new IllegalArgumentException("Empty argument: allowedInputs");
        }
        if (allowedSet.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Empty room named in allowedInputs");
        }

        if (!allowedSet.contains(str)) {
            notOkAction.run();
            return false;
        }
        return true;
    }
}
