import java.util.*;
import java.util.stream.Collectors;

public class LogManager {
    
    private final List<LogEntry> allLogs;
    
    private final Map<String, List<LogEntry>> accountLogsMap;
    
    private final Map<ActionType, List<LogEntry>> actionTypeMap;
    
    private long logIdCounter;
    
    private final SuspiciousActivityDetector suspiciousDetector;
    
    public LogManager() {
        this.allLogs = new ArrayList<>();
        this.accountLogsMap = new HashMap<>();
        this.actionTypeMap = new HashMap<>();
        this.logIdCounter = 1;
        this.suspiciousDetector = new DefaultSuspiciousActivityDetector();
    }
    
    public LogEntry addLog(String accountNumber, ActionType actionType, 
                          double amount, LogStatus status) {
        LogEntry log = new LogEntry(logIdCounter++, accountNumber, 
                                    actionType, amount, status);
        
        allLogs.add(log);
        
        accountLogsMap.computeIfAbsent(accountNumber, k -> new ArrayList<>())
                     .add(log);
        
        actionTypeMap.computeIfAbsent(actionType, k -> new ArrayList<>())
                    .add(log);
        
        return log;
    }
    
    public List<LogEntry> getLogsByAccount(String accountNumber) {
        List<LogEntry> logs = accountLogsMap.get(accountNumber);
        
        if (logs == null) {
            return new ArrayList<>();
        }
        
        return new ArrayList<>(logs);
    }
    
    public List<LogEntry> getRecentLogs(int n) {
        if (n <= 0) {
            return new ArrayList<>();
        }
        
        int size = allLogs.size();
        int startIndex = Math.max(0, size - n);
        
        List<LogEntry> recentLogs = new ArrayList<>();
        for (int i = size - 1; i >= startIndex; i--) {
            recentLogs.add(allLogs.get(i));
        }
        
        return recentLogs;
    }
    
    public List<LogEntry> detectSuspiciousActivity() {
        return suspiciousDetector.detectSuspicious(allLogs);
    }
    
    public List<LogEntry> searchByActionType(ActionType actionType) {
        List<LogEntry> logs = actionTypeMap.get(actionType);
        
        if (logs == null) {
            return new ArrayList<>();
        }
        
        return new ArrayList<>(logs);
    }
    
    public int getTotalLogCount() {
        return allLogs.size();
    }
    
    public List<LogEntry> getAllLogs() {
        return new ArrayList<>(allLogs);
    }
    
    public boolean logExists(long logId) {
        return allLogs.stream()
                     .anyMatch(log -> log.getLogId() == logId);
    }
}
