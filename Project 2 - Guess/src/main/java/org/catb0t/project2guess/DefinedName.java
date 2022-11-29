/*
 * Copyright (c) 2022 Cat Stevens. All rights reserved.
 */

package org.catb0t;

import java.io.*;
import java.util.*;

class DefinedName implements Serializable {
    final String                          type;
    final List<Map<String, List<String>>> listmapValue;
    final Integer                         intValue;
    final Long                            longValue;
    final Double                          doubleValue;
    final String                          stringValue;
    String name;

    public DefinedName () {
                              this.name = "NO ARGS CONSTRUCTION";
                              this.type = "string";
                          }

    public DefinedName (final String name, final String stringValue) {
        this.name        = name;
        this.stringValue = stringValue;
        this.type        = "string";
    }

    public DefinedName (final String name, final Long longValue) {
        this.name      = name;
        this.longValue = longValue;
        this.type      = "long";
    }
}
