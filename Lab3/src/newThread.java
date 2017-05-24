import java.util.Optional;
import java.util.concurrent.CountDownLatch;


class newThread extends Thread {
    private Optional<CountDownLatch> preCondition;
    private Optional<CountDownLatch> postSignal;
    private String userLink;
    private String method;

    public newThread(Optional<CountDownLatch> preCondition, Optional<CountDownLatch> postSignal, String userLink, String method) {
        this.postSignal = postSignal;
        this.preCondition = preCondition;
        this.userLink = userLink;
        this.method = method;
    }

    public void run() {
        preCondition.ifPresent(countDownLatch ->
        {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        postSignal.ifPresent(countDownLatch -> {
            try {
                Run obj = new Run();
                if (method.equals("GET")) {

                    obj.sendGet(this.userLink);

                } else if (method.equals("HEAD")) {

                    obj.sendHead(this.userLink);

                } else {
                    System.out.println("\n\t\t POST REQUEST\t\t");
                    obj.sendPost("Post");
                    System.out.println("");

                }
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

}