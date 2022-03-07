package uz.jl.test;

import java.util.Optional;


/**
 * @author Axmadjonov Eliboy, Mon 11:33 AM,12/13/2021
 */
public class Main {
    public static void main(String[] args) {
        String a= null;
        Optional<String> myOptional = Optional.ofNullable(a);
//        System.out.println(myOptional.isPresent());
//        System.out.println(myOptional.isEmpty());
//        System.out.println(myOptional);
        //System.out.println(myOptional.get());
        //myOptional = myOptional.filter(i->i.equals("Whooo"));
        System.out.println(myOptional.orElse("hello"));


        myOptional.ifPresent(s -> {
            System.out.println("All B4 student must work High level companies");
        });
//        String data = myOptional.orElseThrow(() -> new RuntimeException("MuhammadJafar Kaltey yiysiz"));
//        System.out.println(data);

        myOptional.ifPresentOrElse((d) -> {
                    System.out.println("Here is the body of optional : " + d);
                },
                () -> {
                    System.out.println("Sorry Bro but it is empty");
                });
//        String[] arr = new String[10];
//        arr[5] = "hello";
////        Optional<String> optionalS = Optional.ofNullable(arr[5]);
////        System.out.println(optionalS.isPresent());
//        Optional<User> op = Optional.empty();



//        System.out.println(op);
    }
}
