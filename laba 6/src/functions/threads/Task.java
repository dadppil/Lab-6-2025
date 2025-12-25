package functions.threads;

import functions.Function;


public class Task {
    private Function f;
    private double left;
    private double right;
    private double step;
    private int tasks;

    public Task(int tasksCount) {
        this.tasks = tasksCount;
    }

    // Синхронизированные геттеры и сеттеры
    public synchronized void setF(Function function) {
        this.f = function;
    }

    public synchronized Function getF() {
        return f;
    }

    public synchronized void setLeft(double leftBorder) {
        this.left = leftBorder;
    }

    public synchronized double getLeft() {
        return left;
    }

    public synchronized void setRight(double rightBorder) {
        this.right = rightBorder;
    }

    public synchronized double getRight() {
        return right;
    }

    public synchronized void setStep(double integrationStep) {
        this.step = integrationStep;
    }

    public synchronized double getStep() {
        return step;
    }

    public synchronized int getTasks() {
        return tasks;
    }
}