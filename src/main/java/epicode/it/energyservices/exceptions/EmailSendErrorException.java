package epicode.it.energyservices.exceptions;

public class EmailSendErrorException extends RuntimeException{

    public EmailSendErrorException() {
        super();
    }

    public EmailSendErrorException(String message) {
        super(message);
    }
}
