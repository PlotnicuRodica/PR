import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class Activity extends Thread {

    private Optional<CountDownLatch> preCondition;

    private Optional<Runnable> action;

    private Optional<CountDownLatch> postSignal;


    public Activity(Optional<Runnable> action,

                    Optional<CountDownLatch> preCondition,


                    Optional<CountDownLatch> postSignal) {

        this.preCondition = preCondition;

        this.action = action;

        this.postSignal = postSignal;

    }

    public void run() {

        preCondition.ifPresent(cdl -> {

            try {

                cdl.await();

            } catch (InterruptedException e) {

                System.out.print(e.getMessage());

            }

        });

        action.ifPresent(Runnable::run);

        postSignal.ifPresent(CountDownLatch::countDown);
    }
}