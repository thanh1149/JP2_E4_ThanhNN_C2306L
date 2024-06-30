package src.Service;

import src.Entity.Customer;
import src.Generic.IBankService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerService implements IBankService {
    public static List<Customer> customers = new ArrayList<>();
    public CustomerService(){}

    public CustomerService(String customersPath) {
        try{
            FileReader fileReader = new FileReader(customersPath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine())!=null) {
                if (!line.isEmpty()) {
                    String[] data = line.split(";");
                    customers.add(new Customer(Integer.parseInt(data[0]), data[1], data[2]));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public  Optional findByID(int id) {
        return customers.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }
}
