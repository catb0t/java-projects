/*
 * Copyright (c) 2022 Cat Stevens. All rights reserved.
 */

package org.catb0t.project2guess;

import java.nio.file.*;

/**
 * Model loading and reloading of configuration from a configured path, as well as changing Game
 * Rules and determining whether the rules have been changed since the configuration was loaded.
 */
public interface Configurable {
    /**
     * @return whether the configuration has been modified in memory since it was originally
     *     loaded from disk.
     */
    boolean isVanilla ();

    /**
     * Load {@link GameRules} from the disk location specified by
     * {@link Configurable#rulesLocation()} and return an instance with those rules applied.
     *
     * @return an instance with updated {@link GameRules}.
     */
    Configurable loadRules ();

    /**
     * Apply new game rules to the configuration, represented in the returned instance.
     * <p>
     * if {@code newRules} is effectively different from the currently applied {@link GameRules},
     * then {@link Configurable#isVanilla()} will no longer return {@code true} on the instance,
     * or any instances created from it, until {@link Configurable#loadRules()} is called again.
     *
     * @param newRules the rules to be applied.
     *
     * @return an instance with the new {@link GameRules} applied.
     */
    Configurable withRules (GameRules newRules);

    /**
     * @return the rules currently applied to this instance.
     */
    GameRules rules ();

    /**
     * Change the location from which {@link Configurable#loadRules()} loads {@link GameRules}
     * data for this configuration. The changed location is represented in the returned instance.
     *
     * @param p the path and basename where the rules can be found.
     *
     * @return an instance reflecting the updated path and basename for the rules.
     */
    Configurable setRulesBasename (Path p);

    /**
     * The absolute path of the disk file from which {@link GameRules} data is read for this
     * {@link Configurable} instance.
     * <p>
     * As currently specified, {@link GameRules} data must be in JSON format in a {@code .json}
     * file. The return value is a combination of {@link Configurable#rulesBasename()} and the
     * path of its containing directory.
     *
     * @return the current fully-qualified path and filename {@link Configurable#loadRules()}
     *     will read from.
     */
    Path rulesLocation ();

    /**
     * Get the base file name used to find the {@link GameRules} data on the disk.
     * <p>
     * The base name is the file name without the file extension, and without any path information.
     * For example, the base name of the file {@code /a/b/c/rules.json} is just {@code rules}
     * without any file extension or dot.
     * <p>
     * Since JSON in a {@code .json} file is the only format allowed for {@link GameRules} data,
     * combining the path and the basename fully identifies the data file location.
     *
     * @return the basename of the rules in this configuration.
     */
    String rulesBasename ();
}
