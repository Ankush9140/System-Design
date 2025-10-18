import java.text.SimpleDateFormat;
import java.util.*;

enum Transaction_Type {
    Withdraw, Deposit, Transfer, Balance_Check;
}

class Transaction {
    private Transaction_Type type;
    private String details;
    private Date date;
    private double amount;

    public Transaction(Transaction_Type type, String details, double amount) {
        this.amount = amount;
        this.type = type;
        this.date = new Date();
        this.details = details;
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date) + " | " + type + " | ₹" + amount + " | " + details;
    }
}

class Account {
    private String holderName;
    private String accountNumber;
    private double balance;
    private double dailyLimit;
    private double dailyWithdrawn;
    private List<Transaction> transactions;

    public Account(String holderName, String accountNumber, double balance) {
        this.holderName = holderName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.dailyLimit = 10000;
        this.dailyWithdrawn = 0;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public double getDailyWithdrawn() { return dailyWithdrawn; }
    public double getDailyLimit() { return dailyLimit; }

    public void resetDailyLimit() {
        dailyWithdrawn = 0;
    }

    public void getMiniStatement() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions made yet.");
            return;
        }
        System.out.println("Last 5 Transactions:");
        for (int i = transactions.size() - 1, cnt = 0; i >= 0 && cnt < 5; i--, cnt++) {
            System.out.println(transactions.get(i));
        }
    }
    public boolean canRemove(double amount) {
        if (amount > balance) {
            System.out.println("❌ Insufficient funds.");
            return false;
        }
        if (dailyWithdrawn + amount > dailyLimit) {
            System.out.println("❌ Daily withdrawal limit reached.");
            return false;
        }
        return true;
    }
    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void removeBalance(double amount) {
        this.balance -= amount;
        this.dailyWithdrawn += amount;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }
}

class Card {
    private String cardNumber;
    private String pin;

    public Card(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public boolean validatePin(String typedPin) {
        return pin.equals(typedPin);
    }

    public String getCardNumber() { return cardNumber; }
}

class Bank {
    private Map<String, Account> accounts = new HashMap<>();
    private Map<String, Card> cards = new HashMap<>();
    private Map<Card, Account> cardToAccount = new HashMap<>();

    public void openAccount(Card card, Account account) {
        if (cards.containsKey(card.getCardNumber())) {
            System.out.println("❌ Card already linked.");
            return;
        }
        accounts.put(account.getAccountNumber(), account);
        cards.put(card.getCardNumber(), card);
        cardToAccount.put(card, account);
    }

    private Card getCard(String cardNumber) {
        return cards.get(cardNumber);
    }

    public boolean validateCard(String cardNumber, String pin) {
        Card card = getCard(cardNumber);
        return card != null && card.validatePin(pin);
    }

    public void withdraw(Card card, double amount) {
        Account acc = cardToAccount.get(card);
        if (!acc.canRemove(amount)) {
            return;
        }
        acc.removeBalance(amount);
        acc.addTransaction(new Transaction(Transaction_Type.Withdraw, "ATM Withdraw", amount));
    }

    public void deposit(Card card, double amount) {
        Account acc = cardToAccount.get(card);
        acc.addBalance(amount);
        acc.addTransaction(new Transaction(Transaction_Type.Deposit, "ATM Deposit", amount));
    }

    public void transfer(String cardNumber, double amount, String targetAccountNumber) {
        Card card = getCard(cardNumber);
        if (card != null && accounts.containsKey(targetAccountNumber)) {
            Account source = cardToAccount.get(card);
            Account target = accounts.get(targetAccountNumber);

            if (!source.canRemove(amount)) {
                return;
            }

            source.removeBalance(amount);
            target.addBalance(amount);

            source.addTransaction(new Transaction(Transaction_Type.Transfer, "To: " + targetAccountNumber, amount));
            target.addTransaction(new Transaction(Transaction_Type.Transfer, "From: " + source.getAccountNumber(), amount));

            System.out.println("✅ ₹" + amount + " transferred from " + source.getAccountNumber() + " to " + targetAccountNumber);
        } else {
            System.out.println("❌ Invalid target account.");
        }
    }

    public void printMiniStatement(String cardNumber) {
        Card card = getCard(cardNumber);
        if (card != null) {
            cardToAccount.get(card).getMiniStatement();
        }
    }

    public Account getAccount(Card card) {
        return cardToAccount.get(card);
    }
}

class ATM {
    private double cashAvailable;
    private static ATM instance = null;
    private boolean verified = false;
    private Card insertedCard = null;
    private Bank bank;

    private ATM(double cash, Bank bank) {
        this.cashAvailable = cash;
        this.bank = bank;
    }

    public static ATM getInstance(double cash, Bank bank) {
        if (instance == null) {
            instance = new ATM(cash, bank);
        }
        return instance;
    }

    public void enterCard(Card card, String pin) {
        verified = bank.validateCard(card.getCardNumber(), pin);
        if (verified) {
            insertedCard = card;
            System.out.println("✅ Card verified. Welcome!");
        } else {
            System.out.println("❌ Invalid PIN.");
        }
    }

    public void removeCard() {
        verified = false;
        insertedCard = null;
        System.out.println("Card removed. Thank you!");
    }

    public void withdraw(double amount) {
        if (!verified) {
            System.out.println("❌ Authenticate first.");
            return;
        }
        if (amount > cashAvailable) {
            System.out.println("❌ ATM out of cash.");
        } else {
            Account acc = bank.getAccount(insertedCard);
            if (acc.getBalance() < amount) {
                System.out.println("❌ Insufficient funds.");
            } else if (acc.getDailyWithdrawn() + amount > acc.getDailyLimit()) {
                System.out.println("❌ Daily withdrawal limit reached.");
            } else {
                bank.withdraw(insertedCard, amount);
                cashAvailable -= amount;
                System.out.println("✅ ₹" + amount + " withdrawn. Current Balance: ₹" + acc.getBalance());
            }
        }
    }

    public void deposit(double amount) {
        if (!verified) {
            System.out.println("❌ Authenticate first.");
            return;
        }
        bank.deposit(insertedCard, amount);
        cashAvailable += amount;
        System.out.println("✅ ₹" + amount + " deposited.");
    }

    public void transfer(double amount, String toAccountNumber) {
        if (!verified) {
            System.out.println("❌ Authenticate first.");
            return;
        }
        bank.transfer(insertedCard.getCardNumber(), amount, toAccountNumber);
    }

    public void printMiniStatement() {
        if (!verified) {
            System.out.println("❌ Authenticate first.");
            return;
        }
        bank.printMiniStatement(insertedCard.getCardNumber());
    }

    public void refillCash(double amount) {
        cashAvailable += amount;
        System.out.println("✅ ATM refilled. Cash available: ₹" + cashAvailable);
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();

        Account acc1 = new Account("Arjun", "ACC1001", 5000);
        Account acc2 = new Account("Meera", "ACC1002", 12000);
        Card card1 = new Card("CARD001", "1234");
        Card card2 = new Card("CARD002", "4321");

        bank.openAccount(card1, acc1);
        bank.openAccount(card2, acc2);

        ATM atm = ATM.getInstance(20000, bank);

        atm.enterCard(card1, "1234");
        atm.withdraw(3000);
        atm.deposit(500);
        atm.printMiniStatement();
        atm.transfer(1000, "ACC1002");
        atm.printMiniStatement();
        atm.removeCard();

        atm.refillCash(10000);
    }
}
