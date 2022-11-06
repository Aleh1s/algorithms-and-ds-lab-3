package ua.algorithms.gui;

public enum BasicAction {

    SELECT("SELECT"),
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String actionName;
    BasicAction(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }
}
