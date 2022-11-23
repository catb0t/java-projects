package org.catb0t;

public interface RangedPlayable extends Playable {
    RangedPlayable setRangeBegin (long begin);

    RangedPlayable setRangeEnd (long end);

    long rangeBegin ();

    long rangeEnd ();

    boolean isRangeVanilla ();
}
