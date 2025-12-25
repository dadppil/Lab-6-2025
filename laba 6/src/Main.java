import functions.*;

import functions.basic.*;
import functions.threads.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.Semaphore;


import static java.nio.file.Files.newBufferedWriter;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ ИНТЕГРИРОВАНИЯ ФУНКЦИЙ ===\n");

        try {
            // Часть 1: Базовое интегрирование
            testBasicIntegration();

            // Часть 2: Поиск оптимального шага
            testOptimalStepFinding();
            System.out.println("\n\n2. ПРОСТАЯ МНОГОПОТОЧНАЯ ВЕРСИЯ\n");
            simpleThreads();

            System.out.println("\n\n3. УЛУЧШЕННАЯ ВЕРСИЯ С СЕМАФОРОМ\n");
            complicatedThreads();

            Thread.sleep(50);
            Thread.currentThread().interrupt();

        }catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void testBasicIntegration() {
        System.out.println("1. БАЗОВОЕ ИНТЕГРИРОВАНИЕ\n");

        // Тест 1: Экспонента на отрезке [0, 1]
        System.out.println("1.1 Интеграл экспоненты на [0, 1]:");
        Function exp = new Exp();
        double left = 0.0;
        double right = 1.0;

        // Теоретическое значение: ∫e^x dx от 0 до 1 = e - 1 ≈ 1.718281828459045
        double theoretical = Math.E - 1;
        System.out.printf("   Теоретическое значение: %.15f%n", theoretical);

        // Интегрирование с разными шагами
        double[] steps = {0.5, 0.1, 0.01, 0.001, 0.0001};

        System.out.println("\n   Результаты интегрирования с разными шагами:");
        System.out.println("   Шаг\t\tИнтеграл\t\tПогрешность\t\tОтносит. погр.");
        System.out.println("   ----------------------------------------------------------------------------");

        for (double step : steps) {
            try {
                double integral = Functions.integrate(exp, left, right, step);
                double error = Math.abs(integral - theoretical);
                double relativeError = error / theoretical;

                System.out.printf("   %.4f\t%.15f\t%.15f\t%.2e%n",
                        step, integral, error, relativeError);
            } catch (Exception e) {
                System.out.printf("   %.4f\tОшибка: %s%n", step, e.getMessage());
            }
        }

        // Тест 2: Линейная функция (точное решение известно)
        System.out.println("\n1.2 Интеграл линейной функции f(x) = 2x на [0, 3]:");
        Function linear = new Function() {
            @Override
            public double getLeftDomainBorder() { return Double.NEGATIVE_INFINITY; }
            @Override
            public double getRightDomainBorder() { return Double.POSITIVE_INFINITY; }
            @Override
            public double getFunctionValue(double x) { return 2 * x; }
        };

        double linearTheoretical = 9.0; // ∫2x dx от 0 до 3 = x² от 0 до 3 = 9
        double linearIntegral = Functions.integrate(linear, 0, 3, 0.001);
        double linearError = Math.abs(linearIntegral - linearTheoretical);

        System.out.printf("   Теоретическое значение: %.6f%n", linearTheoretical);
        System.out.printf("   Численное значение: %.10f%n", linearIntegral);
        System.out.printf("   Погрешность: %.10f%n", linearError);
        System.out.printf("   Относительная погрешность: %.2e%n", linearError / linearTheoretical);
    }

    private static void testOptimalStepFinding() {
        System.out.println("\n\n2. ПОИСК ОПТИМАЛЬНОГО ШАГА\n");

        // Тест с экспонентой
        Function exp = new Exp();
        double left = 0.0;
        double right = 1.0;
        double theoretical = Math.E - 1;

        System.out.println("2.1 Поиск шага для точности 1e-7 (7 знаков после запятой):");

        double targetAccuracy = 1e-7;
        double initialStep = 0.1;

        try {
            double optimalStep = Functions.findOptimalStep(exp, left, right, targetAccuracy, initialStep);
            double integral = Functions.integrate(exp, left, right, optimalStep);
            double error = Math.abs(integral - theoretical);

            System.out.printf("   Найденный шаг: %.10f%n", optimalStep);
            System.out.printf("   Значение интеграла: %.15f%n", integral);
            System.out.printf("   Фактическая погрешность: %.15f%n", error);
            System.out.printf("   Требуемая погрешность: %.15f%n", targetAccuracy);
            System.out.printf("   Условие выполнено: %s%n", error < targetAccuracy ? "ДА" : "НЕТ");

            // Проверяем с разными начальными шагами
            System.out.println("\n2.2 Проверка с разными начальными шагами:");
            System.out.println("   Нач. шаг\tОптимальный шаг\t\tПогрешность");
            System.out.println("   --------------------------------------------------------");

            double[] initialSteps = {1.0, 0.5, 0.1, 0.05};
            for (double initStep : initialSteps) {
                try {
                    double optStep = Functions.findOptimalStep(exp, left, right, targetAccuracy, initStep);
                    double intValue = Functions.integrate(exp, left, right, optStep);
                    double err = Math.abs(intValue - theoretical);

                    System.out.printf("   %.3f\t\t%.10f\t%.15f%n", initStep, optStep, err);
                } catch (Exception e) {
                    System.out.printf("   %.3f\t\tОшибка: %s%n", initStep, e.getMessage());
                }
            }

            // Поиск шага для разных точностей
            System.out.println("\n2.3 Зависимость шага от требуемой точности:");
            System.out.println("   Точность\tОптимальный шаг\t\tКоличество отрезков");
            System.out.println("   --------------------------------------------------------");

            double[] accuracies = {1e-3, 1e-4, 1e-5, 1e-6, 1e-7, 1e-8};
            for (double accuracy : accuracies) {
                double step = Functions.findOptimalStep(exp, left, right, accuracy, 0.1);
                int segments = (int) Math.ceil((right - left) / step);

                System.out.printf("   %.0e\t\t%.10f\t\t%d%n", accuracy, step, segments);
            }

        } catch (Exception e) {
            System.out.println("   Ошибка при поиске оптимального шага: " + e.getMessage());
        }
    }
    private static void nonThread(){
        System.out.println("Метод, реализующий последовательную версию программы");
        Task task = new Task(100);
        for(int i = 0; i<task.getTasks();i++) {
            double base = Math.random() * 100;
            double left = Math.random()*100;
            double right = (Math.random()*(200-100+1))+100;
            double step = Math.random();
            Function f = new Log(base);
            task.setF(f);
            task.setLeft(left);
            task.setRight(right);
            task.setStep(step);
            System.out.println("Source: "+ task.getLeft() + " " + task.getRight() + " " + task.getStep());
            double integrated = Functions.integrate(task.getF(),task.getLeft(),task.getRight(),task.getStep());
            System.out.println("Result: " + task.getLeft() + " " + task.getRight() + " " + task.getStep() + " " + integrated);
            System.out.println();
        }
    }
    private static void simpleThreads(){
        System.out.println("Метод реализующий потоковую версию программы");
        Task task = new Task(100);
        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));
        generatorThread.start();
        integratorThread.start();
        try {
            generatorThread.join();
            integratorThread.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted while waiting");
        }
    }
    public static void complicatedThreads() {
        System.out.println("Запуск улучшенной версии с семафором...");

        Task task = new Task(100);
        Semaphore semaphore = new Semaphore(1);

        // Создание потоков
        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);

        // Установка приоритетов
        generator.setPriority(Thread.NORM_PRIORITY);
        integrator.setPriority(Thread.NORM_PRIORITY);

        // Запуск потоков
        generator.start();
        integrator.start();

        // Ожидание завершения потоков
        try {
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted while waiting");
        }

        System.out.println("Улучшенная версия завершена");
    }
}


