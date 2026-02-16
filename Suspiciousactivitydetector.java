import java.util.List;

public interface SuspiciousActivityDetector {
    List<LogEntry> detectSuspicious(List<LogEntry> logs);
}
