import Service.DescriptorService;
import java.util.Scanner;
import java.util.stream.Stream;

public class MainDescriptorApp {

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        DescriptorService descriptorService = new DescriptorService();

        Stream.iterate(1, i -> i + 1)
                .limit(3)
                .map(i -> {
                    System.out.print("Ruta de archivo: ");
                    return console.nextLine();
                })
                .map(path -> getDescription(descriptorService, path))
                .peek(System.out::println)
                .filter(description -> !description.contains("not found"))
                .findFirst()
                .orElse("Has alcanzado el # maximo de intentos.");
    }

    private static String getDescription(DescriptorService descriptorService, String path) {
        return descriptorService.GetDescriptionFromClass(path);
    }
}
