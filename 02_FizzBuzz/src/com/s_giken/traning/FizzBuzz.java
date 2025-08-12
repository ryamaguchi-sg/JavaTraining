package com.s_giken.traning;

import java.util.stream.IntStream;

public class FizzBuzz {
    public static void main(String... args) {
        var o = new FizzBuzz();
        o.normalFizzBuzz();
        System.out.println("");
        o.streamFizzBuzz();
    }

    private void normalFizzBuzz() {
        // 通常版
        for(var i = 1; i <= 100; i++) {
            if(i % 3 == 0 && i % 5 == 0) {
                System.out.print("FizzBuzz");
            }
            else if(i % 3 == 0) {
                System.out.print("Fizz");
            }
            else if(i % 5 == 0) {
                System.out.print("Buzz");
            }
            else {
                System.out.print(i);
            }
            System.out.print(" ");
        }
    }
    
    private void printFizzBuzz(int x) {
        System.out.printf("%s ", (x % 3 == 0 && x % 5 == 0) ? "FizzBuzz" : 
                                 (x % 3 == 0) ? "Fizz" : 
                                 (x % 5 == 0) ? "Buzz" : 
                                 x);
    }

    private void streamFizzBuzz() {
        // Stream API版
        IntStream.rangeClosed(1, 100)
                 .forEach((x) -> printFizzBuzz(x));
    }
}
