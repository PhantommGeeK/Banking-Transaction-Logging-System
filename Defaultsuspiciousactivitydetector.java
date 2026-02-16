import java.util.*;

public class DefaultSuspiciousActivityDetector implements SuspiciousActivityDetector {
    
    private static final double SUSPICIOUS_WITHDRAWAL_THRESHOLD = 50000.0;
    private static final int FAILED_LOGIN_THRESHOLD = 3;
    private static final int RECENT_LOG_WINDOW = 5;
    
    @Override
    public List<LogEntry> detectSuspicious(List<LogEntry> logs) {
        Set<LogEntry> suspiciousLogs = new HashSet<>();
        
        for (LogEntry log : logs) {
            if (log.getActionType() == ActionType.WITHDRAW && 
                log.getAmount() > SUSPICIOUS_WITHDRAWAL_THRESHOLD) {
                suspiciousLogs.add(log);
            }
        }
        
        Map<String, List<LogEntry>> accountLogs = groupByAccount(logs);
        
        for (Map.Entry<String, List<LogEntry>> entry : accountLogs.entrySet()) {
            List<LogEntry> accountLogList = entry.getValue();
            suspiciousLogs.addAll(detectFailedLoginPattern(accountLogList));
        }
        
        return new ArrayList<>(suspiciousLogs);
    }
    
    private Map<String, List<LogEntry>> groupByAccount(List<LogEntry> logs) {
        Map<String, List<LogEntry>> accountMap = new HashMap<>();
        
        for (LogEntry log : logs) {
            accountMap.computeIfAbsent(log.getAccountNumber(), k -> new ArrayList<>())
                      .add(log);
        }
        
        return accountMap;
    }
    
    private List<LogEntry> detectFailedLoginPattern(List<LogEntry> accountLogs) {
        List<LogEntry> suspicious = new ArrayList<>();
        
        int size = accountLogs.size();
        int startIndex = Math.max(0, size - RECENT_LOG_WINDOW);
        
        int failedLoginCount = 0;
        for (int i = startIndex; i < size; i++) {
            LogEntry log = accountLogs.get(i);
            if (log.getActionType() == ActionType.FAILED_LOGIN) {
                failedLoginCount++;
            }
        }
        
        if (failedLoginCount > FAILED_LOGIN_THRESHOLD) {
            for (int i = startIndex; i < size; i++) {
                LogEntry log = accountLogs.get(i);
                if (log.getActionType() == ActionType.FAILED_LOGIN) {
                    suspicious.add(log);
                }
            }
        }
        
        return suspicious;
    }
}
