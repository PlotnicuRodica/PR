import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class App {

    public static void main(String[] args) {

        CountDownLatch dependency1 = new CountDownLatch(1);
        CountDownLatch dependency2 = new CountDownLatch(1);
        CountDownLatch dependency3 = new CountDownLatch(1);
        CountDownLatch dependency4 = new CountDownLatch(1);
        CountDownLatch dependency5 = new CountDownLatch(2);




        new Activity(Optional.of(() -> System.out.println("Thread 1")),


                Optional.empty(),

                Optional.of(dependency1)).start();



        new Activity(Optional.of(() -> System.out.println("Thread 2")),

                Optional.of(dependency1),

                Optional.of(dependency2)).start();


        new Activity(Optional.of(() -> System.out.println("Thread 5")),

                Optional.of(dependency2),

                Optional.of(dependency5)).start();


        new Activity(Optional.of(() -> System.out.println("Thread 6")),

                Optional.empty(),

                Optional.of(dependency3)).start();
        new Activity(Optional.of(() -> System.out.println("Thread 4")),

                Optional.of(dependency3),

                Optional.of(dependency4)).start();
        new Activity(Optional.of(() -> System.out.println("Thread 7")),

                Optional.of(dependency4),

                Optional.of(dependency5)).start();


        new Activity(Optional.of(() -> System.out.println("Thread 3")),

                Optional.of(dependency5),

                Optional.empty()).start();
    }

}