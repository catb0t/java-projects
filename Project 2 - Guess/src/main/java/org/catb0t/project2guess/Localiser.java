/*
 * Copyright (c) 2022 Cat Stevens. All rights reserved.
 */

package org.catb0t.project2guess;

import com.google.gson.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class Localiser {
    private final Random                    rand;
    private final Map<String, List<String>> bundleKeys;
    private final String                    keysBasename;

    private final Locale locale;

    private final ResourceBundle stringsBundle;

    Localiser (
        final String keysFileBasename,
        final Locale locale,
        final String pathSep,
        final String fileExt
    ) throws IOException, URISyntaxException {
        this.rand         = new Random();
        this.locale       = locale;
        this.keysBasename = keysFileBasename;

        final Gson gson = new Gson();

        this.bundleKeys = gson.fromJson(
            Configuration.stringFromResource(keysFileBasename, pathSep, fileExt), HashMap.class
        );

        this.stringsBundle = ResourceBundle.getBundle(this.keysBasename, this.locale);
    }

    String getRandomString (final String field) {
        return this.stringsBundle.getString(this.getRandomPropertyKeyForField(field));
    }

    String getRandomPropertyKeyForField (final String key) {
        final List<String> content = this.bundleKeys.getOrDefault(
            key,
            new ArrayList<>(0)
        );

        return content.isEmpty() ? ""
                                 : content.get(this.rand.nextInt(content.size()));
    }

    Map<String, List<String>> bundleKeys () {
        return Collections.unmodifiableMap(this.bundleKeys);
    }

    @Override
    public String toString () {
        return "Localiser{" +
               "rand=" + this.rand +
               ", bundleKeys=" + this.bundleKeys +
               ", keysBasename='" + this.keysBasename + '\'' +
               ", locale=" + this.locale +
               ", stringsBundle=" + this.stringsBundle +
               '}';
    }
}
