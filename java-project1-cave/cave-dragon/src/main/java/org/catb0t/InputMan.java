package org.catb0t;

import java.util.ArrayList;
import java.util.HashSet;

public class InputMan {
    public static boolean inputOk (String str, final ArrayList<String> allowed_inputs) throws IllegalArgumentException {
        if ( str == null || allowed_inputs == null ) {
            throw new IllegalArgumentException("Null argument: " + (str == null? "str" : "allowed_inputs" ));
        }

        final HashSet<String> allowed_set = new HashSet<String>(allowed_inputs);

        if ( str.isBlank() || allowed_set.isEmpty() ) {
            throw new IllegalArgumentException("Empty argument: " + (str.isBlank()? "str" : "allowed_inputs" ));
        } else if ( allowed_set.stream().anyMatch(String::isBlank) ) {
            throw new IllegalArgumentException("Empty room named in allowed_inputs");
        }

        return allowed_set.contains(str);
    }
}
