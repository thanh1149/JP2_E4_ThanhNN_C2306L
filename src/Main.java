package src;

import src.Entity.*;
import src.Service.AccountService;
import src.Service.BankService;
import src.Service.CustomerService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        int choice = 0;

        List<Customer> customerList = new ArrayList<>();
        List<Account> accountList = new ArrayList<>();
        List<Transaction> transactionList = new ArrayList<>();

        String rootPath = System.getProperty("user.dir");
        String customersPath = rootPath.replace("\\","/") + "/data/Customer.txt";
        String accountsPath = rootPath.replace("\\","/") + "/data/Account.txt";
        String transactionsPath = rootPath.replace("\\","/") + "/data/Transaction.txt";


        CustomerService customerService = new CustomerService(customersPath);
        AccountService accountService = new AccountService(customerService,accountsPath);
        BankService bankService = new BankService(accountService,transactionList);

        BankService.accountList = accountList;
        BankService.transactionList = transactionList;
        BankService.customerList = customerList;

        // read customer file
        try{
            fileReader = new FileReader(customersPath);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine())!=null) {
                if (!line.isEmpty()) {
                    String[] data = line.split(";");
                    customerList.add(new Customer(Integer.parseInt(data[0]), data[1], data[2]));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        //read account file
        try{
            fileReader = new FileReader(accountsPath);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine())!=null) {
                if (!line.isEmpty()) {
                    String data[] = line.split(";");
                    Optional<Customer> customer = customerService.findByID(Integer.parseInt(data[1]));
                    if (customer.isPresent()) {
                        Customer customerInsert = customer.get();
                        Currency currency = Currency.valueOf(data[3]);
                        accountList.add(new Account(Integer.parseInt(data[0]), customerInsert, Double.parseDouble(data[2]), currency));
                    }else {
                        System.out.println("Customer with ID " + data[1] + " not found.");
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        //reade transaction file
        try{
            fileReader = new FileReader(transactionsPath);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine())!=null) {
                if (!line.isEmpty()) {
                    String data[] = line.split(";");
                    Optional<Account> account = accountService.findAccByID(Integer.parseInt(data[1]));
                    if(account.isPresent()){
                        Account account1 =account.get();
                        Type type = Type.valueOf(data[3]);
                        Status status = Status.valueOf(data[5]);
                        transactionList.add(new Transaction(
                                Integer.parseInt(data[0])
                                ,account1
                                ,Float.parseFloat(data[2])
                                ,type
                                ,AccountService.convertStringToLocalDateTime(data[4])
                                ,status));
//                        System.out.println("Transaction data: " + line);
                    }else {
                        // Xử lý trường hợp không tìm thấy Account
                        System.out.println("Account with ID " + data[1] + " not found.");
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        do{
            System.out.println("1.Withdraw money.");
            System.out.println("2.Display account balance.");
            System.out.println("3.Display transaction in specific time.");
            System.out.println("4.Exit.");
            System.out.print("Your choice: ");
            try{
                String choiceStr  = bufferedReader.readLine();
                choice = Integer.parseInt(choiceStr);
                switch (choice){
                    case 1:
                        bankService.withdraw(0,0);
                        break;
                    case 2:
                        bankService.getAccBalance(0);
                        break;
                    case 3:
                        bankService.getTransactionByDate();
                        break;

                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }while(choice!=4);

    }
}