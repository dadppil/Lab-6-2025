package functions.threads;

import functions.Functions;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasks(); i++) {

                if (Thread.interrupted()) {
                    System.out.println("Integrator interrupted");
                    return;
                }

                Task.TaskData data;


                semaphore.startRead();
                try {
                    data = task.getTaskData();
                } finally {
                    semaphore.endRead();
                }


                if (data.function == null) {
                    Thread.sleep(1);
                    continue;
                }


                double result = Functions.integrate(
                        data.function,
                        data.leftBorder,
                        data.rightBorder,
                        data.integrationStep
                );


                System.out.printf("Result %.4f %.4f %.4f %.8f%n", data.leftBorder, data.rightBorder, data.integrationStep, result);
                Thread.sleep(15);
            }
        } catch (InterruptedException e) {
            System.out.println("Integrator error: " + e.getMessage());
        }
    }
}
