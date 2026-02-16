import java.util.List;
import java.util.Scanner;

public class Main {
    
    private static final LogManager logManager = new LogManager();
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("   SECURE BANKING LOG MANAGER");
        System.out.println("=".repeat(60));
        
        loadSampleData();
        
        boolean running = true;
        
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            System.out.println();
            
            switch (choice) {
                case 1:
                    addLogMenu();
                    break;
                case 2:
                    getLogsByAccountMenu();
                    break;
                case 3:
                    getRecentLogsMenu();
                    break;
                case 4:
                    detectSuspiciousActivityMenu();
                    break;
                case 5:
                    searchByActionMenu();
                    break;
                case 6:
                    displayStatistics();
                    break;
                case 7:
                    running = false;
                    System.out.println("Thank you for using Banking Log Manager!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MAIN MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. Add Log");
        System.out.println("2. Get Logs by Account");
        System.out.println("3. Get Recent Logs");
        System.out.println("4. Detect Suspicious Activity");
        System.out.println("5. Search by Action Type");
        System.out.println("6. Display Statistics");
        System.out.println("7. Exit");
        System.out.println("=".repeat(60));
    }
    
    private static void addLogMenu() {
        System.out.println("--- ADD NEW LOG ---");
        
        String accountNumber = getStringInput("Enter Account Number: ");
        
        System.out.println("\nAction Types:");
        System.out.println("1. DEPOSIT");
        System.out.println("2. WITHDRAW");
        System.out.println("3. TRANSFER");
        System.out.println("4. LOGIN");
        System.out.println("5. FAILED_LOGIN");
        
        int actionChoice = getIntInput("Select Action Type (1-5): ");
        ActionType actionType = getActionType(actionChoice);
        
        if (actionType == null) {
            System.out.println("Invalid action type!");
            return;
        }
        
        double amount = 0;
        if (actionType == ActionType.DEPOSIT || actionType == ActionType.WITHDRAW || 
            actionType == ActionType.TRANSFER) {
            amount = getDoubleInput("Enter Amount: ");
        }
        
        System.out.println("\nStatus:");
        System.out.println("1. SUCCESS");
        System.out.println("2. FAILED");
        int statusChoice = getIntInput("Select Status (1-2): ");
        LogStatus status = statusChoice == 1 ? LogStatus.SUCCESS : LogStatus.FAILED;
        
        LogEntry log = logManager.addLog(accountNumber, actionType, amount, status);
        System.out.println("\n✓ Log added successfully!");
        System.out.println(log);
    }
    
    private static void getLogsByAccountMenu() {
        System.out.println("--- GET LOGS BY ACCOUNT ---");
        
        String accountNumber = getStringInput("Enter Account Number: ");
        List<LogEntry> logs = logManager.getLogsByAccount(accountNumber);
        
        if (logs.isEmpty()) {
            System.out.println("No logs found for account: " + accountNumber);
        } else {
            System.out.println("\nFound " + logs.size() + " log(s) for account " + accountNumber + ":");
            System.out.println("-".repeat(60));
            for (LogEntry log : logs) {
                System.out.println(log);
            }
        }
    }
    
    private static void getRecentLogsMenu() {
        System.out.println("--- GET RECENT LOGS ---");
        
        int n = getIntInput("Enter number of recent logs to retrieve: ");
        List<LogEntry> logs = logManager.getRecentLogs(n);
        
        if (logs.isEmpty()) {
            System.out.println("No logs available.");
        } else {
            System.out.println("\nMost recent " + logs.size() + " log(s):");
            System.out.println("-".repeat(60));
            for (LogEntry log : logs) {
                System.out.println(log);
            }
        }
    }
    
    private static void detectSuspiciousActivityMenu() {
        System.out.println("--- SUSPICIOUS ACTIVITY DETECTION ---");
        
        List<LogEntry> suspiciousLogs = logManager.detectSuspiciousActivity();
        
        if (suspiciousLogs.isEmpty()) {
            System.out.println("✓ No suspicious activity detected.");
        } else {
            System.out.println("⚠ Found " + suspiciousLogs.size() + " suspicious log(s):");
            System.out.println("-".repeat(60));
            for (LogEntry log : suspiciousLogs) {
                System.out.println(log);
                if (log.getActionType() == ActionType.WITHDRAW && log.getAmount() > 50000) {
                    System.out.println("  → Reason: High-value withdrawal (> 50,000)");
                } else if (log.getActionType() == ActionType.FAILED_LOGIN) {
                    System.out.println("  → Reason: Multiple failed login attempts detected");
                }
                System.out.println();
            }
        }
    }
    
    private static void searchByActionMenu() {
        System.out.println("--- SEARCH BY ACTION TYPE ---");
        
        System.out.println("\nAction Types:");
        System.out.println("1. DEPOSIT");
        System.out.println("2. WITHDRAW");
        System.out.println("3. TRANSFER");
        System.out.println("4. LOGIN");
        System.out.println("5. FAILED_LOGIN");
        
        int choice = getIntInput("Select Action Type (1-5): ");
        ActionType actionType = getActionType(choice);
        
        if (actionType == null) {
            System.out.println("Invalid action type!");
            return;
        }
        
        List<LogEntry> logs = logManager.searchByActionType(actionType);
        
        if (logs.isEmpty()) {
            System.out.println("No logs found for action type: " + actionType);
        } else {
            System.out.println("\nFound " + logs.size() + " log(s) for action type " + actionType + ":");
            System.out.println("-".repeat(60));
            for (LogEntry log : logs) {
                System.out.println(log);
            }
        }
    }
    
    private static void displayStatistics() {
        System.out.println("--- SYSTEM STATISTICS ---");
        System.out.println("Total Logs: " + logManager.getTotalLogCount());
        
        System.out.println("\nLogs by Action Type:");
        for (ActionType type : ActionType.values()) {
            int count = logManager.searchByActionType(type).size();
            System.out.println("  " + type + ": " + count);
        }
    }
    
    private static ActionType getActionType(int choice) {
        switch (choice) {
            case 1: return ActionType.DEPOSIT;
            case 2: return ActionType.WITHDRAW;
            case 3: return ActionType.TRANSFER;
            case 4: return ActionType.LOGIN;
            case 5: return ActionType.FAILED_LOGIN;
            default: return null;
        }
    }
    
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Invalid input. " + prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
    
    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.print("Invalid input. " + prompt);
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }
    
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private static void loadSampleData() {
        System.out.println("\nLoading sample data...");
        
        logManager.addLog("ACC001", ActionType.LOGIN, 0, LogStatus.SUCCESS);
        logManager.addLog("ACC001", ActionType.DEPOSIT, 5000, LogStatus.SUCCESS);
        logManager.addLog("ACC001", ActionType.WITHDRAW, 1000, LogStatus.SUCCESS);
        
        logManager.addLog("ACC002", ActionType.LOGIN, 0, LogStatus.SUCCESS);
        logManager.addLog("ACC002", ActionType.TRANSFER, 2000, LogStatus.SUCCESS);
        logManager.addLog("ACC002", ActionType.WITHDRAW, 500, LogStatus.SUCCESS);
        
        logManager.addLog("ACC003", ActionType.LOGIN, 0, LogStatus.SUCCESS);
        logManager.addLog("ACC003", ActionType.FAILED_LOGIN, 0, LogStatus.FAILED);
        logManager.addLog("ACC003", ActionType.FAILED_LOGIN, 0, LogStatus.FAILED);
        logManager.addLog("ACC003", ActionType.FAILED_LOGIN, 0, LogStatus.FAILED);
        logManager.addLog("ACC003", ActionType.FAILED_LOGIN, 0, LogStatus.FAILED);
        
        logManager.addLog("ACC004", ActionType.LOGIN, 0, LogStatus.SUCCESS);
        logManager.addLog("ACC004", ActionType.WITHDRAW, 75000, LogStatus.SUCCESS);
        
        logManager.addLog("ACC005", ActionType.LOGIN, 0, LogStatus.SUCCESS);
        logManager.addLog("ACC005", ActionType.DEPOSIT, 10000, LogStatus.SUCCESS);
        logManager.addLog("ACC005", ActionType.TRANSFER, 3000, LogStatus.SUCCESS);
        
        System.out.println("✓ Sample data loaded: " + logManager.getTotalLogCount() + " logs created.\n");
    }
}
