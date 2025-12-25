package functions.threads;
import functions.Function;
import static functions.Functions.integrate;

public class SimpleIntegrator implements Runnable {
    private Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        int processed = 0;

        while (processed < task.getTasks()) {
            Function function = null;
            double left = 0;
            double right = 0;
            double step = 0;

            synchronized (task) {
                function = task.getF();
                left = task.getLeft();
                right = task.getRight();
                step = task.getStep();
                task.setF(null);
            }

            if (function != null) {
                processed++;
                try {
                    double result = integrate(function, left, right, step);
                    System.out.printf("Result %.4f %.4f %.4f %.8f%n", left, right, step, result);
                } catch (Exception e) {
                    System.out.println("Integrator error: " + e.getMessage());
                }
            }
            else {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        System.out.println("Integrator завершен");
    }
}
