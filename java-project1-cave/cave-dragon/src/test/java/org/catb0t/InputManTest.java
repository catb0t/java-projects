package org.catb0t;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputManTest {

    private ArrayList<String> twoRooms, threeRooms, twoNamedRooms, fourNamedRooms, emptyNamedRooms, emptyAllowed;

    @BeforeEach
    void setTestData() {
        this.twoRooms = new ArrayList<String>(List.of("1", "2"));
        this.threeRooms = new ArrayList<String>(List.of("2", "4", "6"));
        this.twoNamedRooms = new ArrayList<String>(List.of("Left room", "Right room"));
        this.fourNamedRooms = new ArrayList<String>(List.of("Up", "Down", "Left", "Right"));
        this.emptyNamedRooms = new ArrayList<String>(List.of("", "", "", ""));
        this.emptyAllowed = new ArrayList<String>(List.of());
    }

    @Test
    @DisplayName("Validate basic Room inputs")
    void inputOk() {
        assertAll(
                () -> assertTrue(InputMan.inputOk("2", this.twoRooms)),
                () -> assertFalse(InputMan.inputOk("3", this.twoRooms)),

                () -> assertTrue(InputMan.inputOk("6", this.threeRooms)),
                () -> assertFalse(InputMan.inputOk("7", this.threeRooms)),

                () -> assertTrue(InputMan.inputOk("Left room", this.twoNamedRooms)),
                () -> assertFalse(InputMan.inputOk("1", this.twoNamedRooms)),

                () -> assertTrue(InputMan.inputOk("Right", this.fourNamedRooms)),
                () -> assertFalse(InputMan.inputOk("Bad", this.fourNamedRooms)),

                () -> assertThrowsExactly(IllegalArgumentException.class, () -> InputMan.inputOk("Doesn't matter", this.emptyNamedRooms)),
                () -> assertThrowsExactly(IllegalArgumentException.class, () -> InputMan.inputOk("Doesn't matter either", this.emptyAllowed))

                );
    }
}