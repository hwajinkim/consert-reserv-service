package kr.hhplus.be.server.common.exception;

public class LackRemainingTicketExcption extends RuntimeException{
    public LackRemainingTicketExcption(String message) {
        super(message);
    }
}
