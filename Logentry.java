import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry {
    private final long logId;
    private final String accountNumber;
    private final ActionType actionType;
    private final double amount;
    private final LocalDateTime timestamp;
    private final LogStatus status;
    
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public LogEntry(long logId, String accountNumber, ActionType actionType, 
                    double amount, LogStatus status) {
        this.logId = logId;
        this.accountNumber = accountNumber;
        this.actionType = actionType;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }
    
    public long getLogId() {
        return logId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public ActionType getActionType() {
        return actionType;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public LogStatus getStatus() {
        return status;
    }
    
    @Override
    public String toString() {
        return String.format(
            "LogID: %d | Account: %s | Action: %s | Amount: %.2f | Time: %s | Status: %s",
            logId, accountNumber, actionType, amount,
            timestamp.format(FORMATTER), status
        );
    }
}
