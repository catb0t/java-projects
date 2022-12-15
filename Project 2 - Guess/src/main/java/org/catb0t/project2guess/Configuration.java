/*
 * Copyright (c) 2022 Cat Stevens. All rights reserved.
 */

package org.catb0t.project2guess;

import com.google.gson.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class Configuration implements Configurable {
    private final String rulesBasename;

    private final GameRules rules;

    private final String pathSep;
    private final String fileExt;

    Configuration (
        final String otherRulesBasename,
        final GameRules otherRules,
        final String otherPathSep,
        final String otherFileExt
    ) {
        this.rulesBasename = otherRulesBasename;
        this.rules         = otherRules;
        this.pathSep       = otherPathSep;
        this.fileExt       = otherFileExt;
    }

    Configuration (
        final String rulesFileBasename,
        final String sep,
        final String ext
    ) throws IOException, URISyntaxException {
        this.rulesBasename = rulesFileBasename;

        this.pathSep = sep;
        this.fileExt = ext;

        final Gson gson = new Gson();

        this.rules = gson.fromJson(
            Configuration.stringFromResource(this.rulesBasename, this.pathSep, this.fileExt),
            GameRules.class
        );
    }

    protected static String stringFromResource (
        final String resourceBasename,
        final String pathSep,
        final String fileExt
    ) throws IOException, URISyntaxException {
        final var resource = Main.class.getResource(pathSep + resourceBasename + fileExt);
        if (resource == null) {
            System.err.println("game data resource not found");
            throw new IOException(pathSep + resourceBasename + fileExt);
        }

        final URI gameDataURI;
        try {
            gameDataURI = resource.toURI();
        } catch (final URISyntaxException e) {
            throw e;
        }

        return Files.readString(Path.of(gameDataURI));
    }

    @Override
    public boolean isVanilla () {
        return false;
    }

    @Override
    public Configurable loadRules () {
        throw new IllegalArgumentException("unimplemented");
    }

    @Override
    public Configurable withRules (final GameRules newRules) {
        throw new IllegalArgumentException("unimplemented");

    }

    @Override
    public GameRules rules () {
        return this.rules;
    }

    @Override
    public Configurable setRulesBasename (final Path p) {
        throw new IllegalArgumentException("unimplemented");

    }

    @Override
    public Path rulesLocation () {
        return Path.of(this.pathSep + this.rulesBasename + this.fileExt);
    }

    @Override
    public String rulesBasename () {
        return this.rulesBasename;
    }

    public String pathSep () {
        return this.pathSep;
    }

    public String fileExt () {
        return this.fileExt;
    }

    public Configurable setRulesBasename (final String newName) {
        return new Configuration(newName, this.rules, this.pathSep, this.fileExt);
    }
}
