package org.catb0t;

import java.util.List;
import java.util.Map;

sealed interface Rule
        permits Rule.OfBoolean, Rule.OfDouble, Rule.OfList, Rule.OfLong, Rule.OfMap, Rule.OfNull, Rule.OfString {
    String name ();


    record OfList( String name, List<Rule> value ) implements Rule { }


    record OfMap( String name, Map<String, Rule> value ) implements Rule { }


    record OfString( String name, String value ) implements Rule { }


    record OfLong( String name, long value ) implements Rule { }


    record OfBoolean( String name, boolean value ) implements Rule { }


    record OfDouble( String name, double value ) implements Rule { }


    record OfNull( String name, Null value ) implements Rule { }

}


record Null( ) { }
