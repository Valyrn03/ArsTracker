package application;

public interface Command {
    public boolean execute();

    public String name();
}
