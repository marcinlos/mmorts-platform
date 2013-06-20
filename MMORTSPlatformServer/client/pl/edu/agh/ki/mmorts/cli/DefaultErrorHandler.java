package pl.edu.agh.ki.mmorts.cli;

public class DefaultErrorHandler implements ErrorHandler {

    @Override
    public boolean onError(CommandException e) throws CommandException {
        if (e instanceof NoSuchCommandException) {
            NoSuchCommandException ee = (NoSuchCommandException) e;
            System.err.println("Command `" + ee.getCommand() + "' not found");
        } else {
            e.printStackTrace(System.err);
        }
        return true;
    }

}
