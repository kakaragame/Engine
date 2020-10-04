package org.kakara.engine.input;

/**
 * Mouse buttons.
 */
public enum MouseClickType {
    RIGHT_CLICK(1),
    LEFT_CLICK(0),
    MIDDLE_CLICK(2),
    OTHER(3);

    int num;
    MouseClickType(int num){
        this.num = num;
    }

    public static MouseClickType valueOf(int num){
        for(MouseClickType type : MouseClickType.values()){
            if(num == type.num)
                return type;
        }
        return OTHER;
    }
}
