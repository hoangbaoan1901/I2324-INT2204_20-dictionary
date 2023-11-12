package src.Application;
import src.Dictionary.*;
import java.util.Scanner;
import static src.Dictionary.DictionaryCommandline.printMenu;

public class Start {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Dictionary eng = new Dictionary();
        DictionaryCommandline app = new DictionaryCommandline(eng);

        System.out.println("Welcome to My Application!");
        while (true) {
            printMenu();
            System.out.print("Your action: ");

            if (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();

                if (userInput.matches("[0-9]")) {
                    int action = Integer.parseInt(userInput);

                    switch (action) {
                        case 0:
                            System.out.println("Exiting application. Goodbye!");
                            System.exit(0);
                            break;
                        case 1:
                            app.dictionaryBasic();
                            break;
                        case 2:
                            app.deleteWordFromCommandLine();
                            break;
                        case 3:
                            app.updateWordFromCommandLine();
                            break;
                        case 4:
                            app.showAllWords();
                            break;
                        case 5:
                            app.dictionaryLookup();
                            break;
                        case 6:
                            System.out.print("Enter a prefix to search: ");
                            String prefix = scanner.nextLine();
                            app.dictionarySearcher(prefix);
                            break;
                        case 7:
                            System.out.println("Accessing the Game section (not implemented).");
                            break;
                        case 8:
                            System.out.print("Enter the file path to import from: ");
                            String importFilePath = scanner.nextLine();
                            app.insertFromFile(importFilePath);
                            break;
                        case 9:
                            System.out.print("Enter the file path to export to: ");
                            String exportFilePath = scanner.nextLine();
                            app.dictionaryExportToFile(exportFilePath);
                            break;
                        default:
                            System.out.println("Action not supported");
                            break;
                    }
                } else {
                    System.out.println("Action not supported");
                }
            } else {
                System.out.println("No input found.");
                break;
            }
        }
        scanner.close();
    }
}
