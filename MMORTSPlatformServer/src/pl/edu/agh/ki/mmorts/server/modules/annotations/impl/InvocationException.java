package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

public class InvocationException extends MappingException {

    private Invoker invoker;

    public InvocationException(Invoker invoker) {
        this.invoker = invoker;
    }

    public InvocationException(Invoker invoker, String message) {
        super(message);
        this.invoker = invoker;
    }

    public InvocationException(Invoker invoker, Throwable cause) {
        super(cause);
        this.invoker = invoker;
    }

    public InvocationException(Invoker invoker, String message, Throwable cause) {
        super(message, cause);
        this.invoker = invoker;
    }
    
    public Invoker getInvoker() {
        return invoker;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trying to execute ").append(invoker.method);
        sb.append('\n');
        sb.append(super.toString());
        return sb.toString();
    }

}
