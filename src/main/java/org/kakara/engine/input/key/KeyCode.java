package org.kakara.engine.input.key;

/**
 * An enum representing the KeyCode values.
 *
 * <p>These codes are similar to the KeyCode values in GLFW.</p>
 */
public enum KeyCode {
    UNKNOWN((char) 0, -1),
    SPACE(' ', 32),
    APOSTROPHE('\'', 39),
    COMMA(',', 44),
    MINUS('-', 45),
    PERIOD('.', 46),
    SLASH('/', 47),
    ZERO('0', 48),
    ONE('1', 49),
    TWO('2', 50),
    THREE('3', 51),
    FOUR('4', 52),
    FIVE('5', 53),
    SIX('6', 54),
    SEVEN('7', 55),
    EIGHT('8', 56),
    NINE('9', 57),
    SEMICOLON(';', 59),
    EQUAL('=', 61),
    A('A', 65),
    B('B', 66),
    C('C', 67),
    D('D', 68),
    E('E', 69),
    F('F', 70),
    G('G', 71),
    H('H', 72),
    I('I', 73),
    J('J', 74),
    K('K', 75),
    L('L', 76),
    M('M', 77),
    N('N', 78),
    O('O', 79),
    P('P', 80),
    Q('Q', 81),
    R('R', 82),
    S('S', 83),
    T('T', 84),
    U('U', 85),
    V('V', 86),
    W('W', 87),
    X('X', 88),
    Y('Y', 89),
    Z('Z', 90),
    LEFT_BRACKET('[', 91),
    BACKSLASH('\\', 92),
    RIGHT_BRACKET(']', 93),
    GRAVE_ACCENT('`', 96),
    WORLD_ONE((char) 161, 161),
    WORLD_TWO((char) 162, 162),
    ESCAPE((char) 256, 256),
    ENTER((char) 257, 257),
    TAB((char) 258, 258),
    BACKSPACE((char) 259, 259),
    INSERT((char) 260, 260),
    DELETE((char) 261, 261),
    RIGHT_ARROW((char) 262, 262),
    LEFT_ARROW((char) 263, 263),
    DOWN_ARROW((char) 264, 264),
    UP_ARROW((char) 265, 265),
    PAGE_UP((char) 266, 266),
    PAGE_DOWN((char) 267, 267),
    HOME((char) 268, 268),
    END((char) 269, 269),
    CAPS_LOCK((char) 280, 280),
    SCROLL_LOCK((char) 281, 281),
    NUM_LOCK((char) 282, 282),
    PRINT_SCREEN((char) 283, 283),
    PAUSE((char) 284, 284),
    F1((char) 290, 290),
    F2((char) 291, 291),
    F3((char) 292, 292),
    F4((char) 293, 293),
    F5((char) 294, 294),
    F6((char) 295, 295),
    F7((char) 296, 296),
    F8((char) 297, 297),
    F9((char) 298, 298),
    F10((char) 299, 299),
    F11((char) 300, 300),
    F12((char) 301, 301),
    F13((char) 302, 302),
    F14((char) 303, 303),
    F15((char) 304, 304),
    F16((char) 305, 305),
    F17((char) 306, 306),
    F18((char) 307, 307),
    F19((char) 308, 308),
    F20((char) 309, 309),
    F21((char) 310, 310),
    F22((char) 311, 311),
    F23((char) 312, 312),
    F24((char) 313, 313),
    F25((char) 314, 314),
    NUMPAD_0('0', 320),
    NUMPAD_1('1', 321),
    NUMPAD_2('2', 322),
    NUMPAD_3('3', 323),
    NUMPAD_4('4', 324),
    NUMPAD_5('5', 325),
    NUMPAD_6('6', 326),
    NUMPAD_7('7', 327),
    NUMPAD_8('8', 328),
    NUMPAD_9('9', 329),
    NUMPAD_DECIMAL('.', 330),
    NUMPAD_DIVIDE('/', 331),
    NUMPAD_MULTIPLY('*', 332),
    NUMPAD_SUBTRACT('-', 333),
    NUMPAD_ADD('+', 334),
    NUMPAD_ENTER((char) 335, 335),
    NUMPAD_EQUAL('=', 336),
    LEFT_SHIFT((char) 340, 340),
    LEFT_CONTROL((char) 341, 341),
    LEFT_ALT((char) 342, 342),
    LEFT_SUPER((char) 343, 343),
    RIGHT_SHIFT((char) 344, 344),
    RIGHT_CONTROL((char) 345, 345),
    RIGHT_ALT((char) 346, 346),
    RIGHT_SUPER((char) 347, 347),
    MENU((char) 348, 348);


    private char character;
    private int id;

    KeyCode(char character, int id) {
        this.character = character;
        this.id = id;
    }

    /**
     * Get a key code by its ID.
     *
     * @param id The id of the keycode to get.
     * @return The key code. (Returns KeyCode.UNKNOWN if not found).
     */
    public static KeyCode getKeyCodeById(int id) {
        for (KeyCode keyCode : KeyCode.values()) {
            if (keyCode.id == id)
                return keyCode;
        }

        return KeyCode.UNKNOWN;
    }

    /**
     * Get the ID of the Key.
     *
     * @return The ID of the Key.
     */
    public int getID() {
        return this.id;
    }

    /**
     * Get the character for the key.
     *
     * @return The character for the key.
     */
    public char getCharacter() {
        return this.character;
    }
}
