package org.catb0t;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {

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
    @DisplayName ( value = "Validate basic Room inputs" )
    void inputOk () {
        assertAll(
                () -> assertTrue(InputHandler.isInputOk("2", this.twoRooms)),
                () -> assertFalse(InputHandler.isInputOk("3", this.twoRooms)),

                () -> assertTrue(InputHandler.isInputOk("6", this.threeRooms)),
                () -> assertFalse(InputHandler.isInputOk("7", this.threeRooms)),

                () -> assertTrue(InputHandler.isInputOk("Left room", this.twoNamedRooms)),
                () -> assertFalse(InputHandler.isInputOk("1", this.twoNamedRooms)),

                () -> assertTrue(InputHandler.isInputOk("Right", this.fourNamedRooms)),
                () -> assertFalse(InputHandler.isInputOk("Bad", this.fourNamedRooms)),

                () -> assertThrowsExactly(IllegalArgumentException.class,
                        () -> InputHandler.isInputOk("Doesn't matter", this.emptyNamedRooms)),
                () -> assertThrowsExactly(IllegalArgumentException.class,
                        () -> InputHandler.isInputOk("Doesn't matter either", this.emptyAllowed))

        );
    }
}