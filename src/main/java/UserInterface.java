import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class UserInterface {
    private static UserInterface userInterface;
    private DataBaseControl dataBaseControl = DataBaseControl.getInstance();
    private Scanner scanner;
    private int option;
    private boolean flag = false;

    static {
        userInterface = new UserInterface();
    }

    private UserInterface() {

    }

    public static UserInterface getInstance() {
        return userInterface;
    }

    public void getInterface(Scanner scanner) {
        this.scanner = scanner;
        while (!flag) {
            printMenu();
            scanResult();
            switchMenu();
        }
    }

    private void printMenu() {
        System.out.println("+-----------------------------------------------------------+");
        System.out.println("| Choose option from 0 to 6:                                |");
        System.out.println("+-----------------------------------------------------------+");
        System.out.println("| 1.Add product.                                            |");
        System.out.println("| 2.Print all products info.                                |");
        System.out.println("+-----------------------------------------------------------+");
        System.out.println("| 3.Add client.                                             |");
        System.out.println("| 4.Print all clients info.                                 |");
        System.out.println("+-----------------------------------------------------------+");
        System.out.println("| 5.Create new order.                                       |");
        System.out.println("| 6.Print all orders info.                                  |");
        System.out.println("+-----------------------------------------------------------+");
        System.out.println("| 0.Exit.                                                   |");
        System.out.println("+-----------------------------------------------------------+");
    }

    private void scanResult() {
        while (true) {
            String temp = scanner.nextLine();
            if ((temp.equals("0") || temp.equals("1") || temp.equals("2") || temp.equals("3") ||
                    temp.equals("4") || temp.equals("5") || temp.equals("6"))) {
                option = Integer.parseInt(temp);
                return;
            } else {
                System.out.println("Choose from 0 to 6.");
            }
        }
    }

    private void switchMenu() {
        switch (option) {
            case 0: {
                flag = true;
                break;
            }
            case 1: {
                addProduct();
                break;
            }
            case 2: {
                printProducts();
                break;
            }
            case 3: {
                addClient();
                break;
            }
            case 4: {
                printClients();
                break;
            }
            case 5: {
                addOrder();
                break;
            }
            case 6: {
                printOrders();
                break;
            }
        }
    }

    private void addProduct() {
        System.out.println("+-----------------------------------------------------------+");
        System.out.println("| Enter the title:                                          |");
        String title = scanner.nextLine();
        System.out.println("| Enter weight:                                             |");
        int weight = Integer.parseInt(scanner.nextLine());
        System.out.println("| Enter price:                                              |");
        int price = Integer.parseInt(scanner.nextLine());
        dataBaseControl.addProduct(title, weight, price);
    }

    private void printProducts() {
        dataBaseControl.printProducts();
    }

    private void addClient() {
        System.out.println("+-----------------------------------------------------------+");
        System.out.println("| Enter customer's name:                                    |");
        String name = scanner.nextLine();
        System.out.println("| Enter customer's phone:                                   |");
        String phone = scanner.nextLine();
        System.out.println("| Enter customer's address:                                 |");
        String address = scanner.nextLine();
        dataBaseControl.addClient(name, phone, address);
    }

    private void printClients() {
        dataBaseControl.printClients();
    }

    private void addOrder() {
        ArrayList<Integer> array = new ArrayList<Integer>();
        String temp;
        System.out.println("+-----------------------------------------------------------+");
        System.out.println("| Enter customer's ID:                                      |");
        int customerId = Integer.parseInt(scanner.nextLine());
        System.out.println("| Enter IDs of items that you want to buy:                  |");
        array.add(Integer.parseInt(scanner.nextLine()));
        while (true) {
            System.out.println("| Input another ID? (Y)                                     |");
            temp = scanner.nextLine();
            if (temp.equals("Y")) {
                System.out.println("| Enter IDs of items that you want to buy:                  |");
                array.add(Integer.parseInt(scanner.nextLine()));
            } else {
                break;
            }
        }
        dataBaseControl.addOrder(customerId, array);
    }

    private void printOrders() {
        dataBaseControl.printOrders();
    }
}
