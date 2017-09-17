package jp.manavista.lessonmanager.constants;

/**
 *
 * Date Label
 * <p>
 * Overview:<br>
 * </p>
 */
public enum DateLabel {

    TODAY(0),
    YESTERDAY(-1),
    TOMORROW(1);

    private int code;

    DateLabel(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}