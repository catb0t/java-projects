package org.catb0t;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputManTest {

    private ArrayList<String> twoRooms, threeRooms, twoNamedRooms, fourNamedRooms, emptyNamedRooms, emptyAllowed;

    @BeforeEach
    void setTestData () {
        this.twoRooms        = new ArrayList<>(List.of("1", "2"));
        this.threeRooms      = new ArrayList<>(List.of("2", "4", "6"));
        this.twoNamedRooms   = new ArrayList<>(List.of("Left room", "Right room"));
        this.fourNamedRooms  = new ArrayList<>(List.of("Up", "Down", "Left", "Right"));
        this.emptyNamedRooms = new ArrayList<>(List.of("", "", "", ""));
        this.emptyAllowed    = new ArrayList<>(List.of());
    }

    @Test
    @DisplayName ( "Validate basic Room inputs" )
    void inputOk () {
        assertAll(
                () -> assertTrue(InputMan.isInputOk("2", this.twoRooms)),
                () -> assertFalse(InputMan.isInputOk("3", this.twoRooms)),

                () -> assertTrue(InputMan.isInputOk("6", this.threeRooms)),
                () -> assertFalse(InputMan.isInputOk("7", this.threeRooms)),

                () -> assertTrue(InputMan.isInputOk("Left room", this.twoNamedRooms)),
                () -> assertFalse(InputMan.isInputOk("1", this.twoNamedRooms)),

                () -> assertTrue(InputMan.isInputOk("Right", this.fourNamedRooms)),
                () -> assertFalse(InputMan.isInputOk("Bad", this.fourNamedRooms)),

                () -> assertThrowsExactly(IllegalArgumentException.class,
                        () -> InputMan.isInputOk("Doesn't matter", this.emptyNamedRooms)),
                () -> assertThrowsExactly(IllegalArgumentException.class,
                        () -> InputMan.isInputOk("Doesn't matter either", this.emptyAllowed))

        );
    }
}