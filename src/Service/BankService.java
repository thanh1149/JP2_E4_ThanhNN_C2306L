package src.Service;

import src.Entity.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BankService {
    public static List<Account> accountList;
    public static List<Customer> customerList;
    public static List<Transaction> transactionList;
    public static AccountService accountService;
    public static CustomerService customerService;

    public BankService(AccountService accountService, List<Transaction> transactionList) {
        BankService.accountService = accountService;
        BankService.transactionList = transactionList;
    }

    public static LocalDateTime convertStringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd H:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
    //1. Transaction withdraw
    public static boolean withdraw(int accID, double amount){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("Enter your account id: ");
            accID = Integer.parseInt(bufferedReader.readLine());
            Optional<Account> getAccID = accountService.findAccByID(accID);
            if(!getAccID.isPresent()){
                System.out.println("No account found.");
                return false;
            }
            Account account = getAccID.get();

            System.out.print("Enter withdraw amount:");
            amount = Double.parseDouble(bufferedReader.readLine());
            if(amount % 10 != 0){
                System.out.println("Withdraw must be in multiples of 10.");
                return false;
            }
            if(accountService.checkBalance(account,amount)){
                account.setBalance(account.getBalance() - amount);
                Transaction transaction = new Transaction(
                        transactionList.size()+1,
                        account,
                        amount,
                        Type.WITHDRAW,
                        LocalDateTime.now(),
                        Status.C
                );
                transactionList.add(transaction);
                System.out.println("Withdraw succesfull, current balance: " + account.getBalance() + account.getCurrency());
                return true;
            }else{
                System.out.println("Not enough balance");
                return false;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }


    //2.Display balance
    public static Optional<Account> getAccBalance(int accID){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.print("Enter account you want to check balance: ");
            accID = Integer.parseInt(bufferedReader.readLine());
            Optional<Account> getAccID = accountService.findAccByID(accID);

            if(getAccID.isPresent()){
                Account account = getAccID.get();
                System.out.println("Account balance: " +account.getBalance());
                return getAccID;
            }else {
                System.out.println("No account found.");
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }


    //3.Get transaction by date
    public static List<Transaction> getTransactionByDate(){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.print("Enter account you want to check transaction: ");
            int accID = Integer.parseInt(bufferedReader.readLine());

            Optional<Account> getAccID = accountService.findAccByID(accID);
            if(!getAccID.isPresent()){
                System.out.println("No account found.");
                return null;
            }
            Account account = getAccID.get();

            System.out.print("Enter start date (yyyy-MM-dd HH:mm:ss): ");
            LocalDateTime startDate = convertStringToLocalDateTime(bufferedReader.readLine());

            System.out.print("Enter end date (yyyy-MM-dd HH:mm:ss): ");
            LocalDateTime endDate = convertStringToLocalDateTime(bufferedReader.readLine());

            List<Transaction> listTransactionByDate = transactionList.stream()
                    .filter(t -> t.getLocalDateTime().isAfter(startDate) && t.getLocalDateTime().isBefore(endDate))
                    .collect(Collectors.toList());

            if (listTransactionByDate.isEmpty()) {
                System.out.println("No transactions found in the given date range.");
            } else {
               listTransactionByDate.forEach(System.out::println);
            }
            return listTransactionByDate;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }
}
