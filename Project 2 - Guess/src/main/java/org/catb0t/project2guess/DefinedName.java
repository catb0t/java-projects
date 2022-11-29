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
    final String                          name;

    public DefinedName () {
        this.name         = "NO ARGS CONSTRUCTION";
        this.type         = "string";
        this.intValue     = null;
        this.longValue    = null;
        this.doubleValue  = null;
        this.listmapValue = null;
        this.stringValue  = null;
    }

    public DefinedName (final String name, final String stringValue) {
        this.name         = name;
        this.stringValue  = stringValue;
        this.type         = "string";
        this.intValue     = null;
        this.longValue    = null;
        this.doubleValue  = null;
        this.listmapValue = null;
    }

    public DefinedName (final String name, final Long longValue) {
        this.name         = name;
        this.longValue    = longValue;
        this.type         = "long";
        this.intValue     = null;
        this.stringValue  = null;
        this.doubleValue  = null;
        this.listmapValue = null;
    }
}
