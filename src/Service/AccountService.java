package src.Service;

import src.Entity.Account;
import src.Entity.Currency;
import src.Entity.Customer;
import src.Generic.IBankService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountService implements IBankService {
    public static List<Account> accounts = new ArrayList<>();
    public static CustomerService cs;

    public AccountService(){}
    public AccountService(CustomerService customerService,String accountsPath) {
        cs= customerService;
        try {
            FileReader fileReader = new FileReader(accountsPath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    String data[] = line.split(";");
                    Optional<Customer> customer = cs.findByID(Integer.parseInt(data[1]));
                    if (customer.isPresent()) {
                        Customer customerInsert = customer.get();
                        Currency currency = Currency.valueOf(data[3]);
                        accounts.add(new Account(Integer.parseInt(data[0]), customerInsert, Double.parseDouble(data[2]), currency));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<Account> findAccByID(int id){
        return accounts.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
    }

    public static LocalDateTime convertStringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd H:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    public boolean checkBalance(Account account, double amount){
        return account.getBalance() >= amount;
    }

    @Override
    public Optional findByID(int id) {
        return Optional.empty();
    }
}
