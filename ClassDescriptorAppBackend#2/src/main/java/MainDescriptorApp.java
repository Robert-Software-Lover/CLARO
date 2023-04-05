import Service.DescriptorService;

import java.util.Scanner;

public class MainDescriptorApp {


    public static void main(String []args) {

	    int attemps = 1;

        while (attemps <= 3){

            System.out.print("Ruta de archivo: ");

            Scanner console = new Scanner(System.in);

            String path = console.nextLine();

            DescriptorService descriptorService = new DescriptorService();

            System.out.println(descriptorService.GetDescriptionFromClass(path));

            System.out.println("");

            if(descriptorService.GetDescriptionFromClass(path).contains("not found")){

                if(attemps==3) {

                    System.out.println("Has llegado al limite de intentos.");

                }

                attemps++;

            }
            else{
                break;
            }


        }

    }
}
