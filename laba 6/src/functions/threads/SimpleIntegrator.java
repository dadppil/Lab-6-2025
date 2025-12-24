package functions.threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasks(); i++) {
                Task.TaskData data;

                synchronized (task) {
                    data = task.getTaskData();
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
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println("Integrator error: " + e.getMessage());
        }
    }
}
