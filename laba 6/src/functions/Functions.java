package functions;
import functions.meta.*;

public final class Functions {
    private Functions(){}
    public static Function shift(Function f, double shiftX, double shiftY){
        Shift shift = new Shift(f,shiftX,shiftY);
        return shift;
    }
    public static Function scale(Function f, double scaleX, double scaleY){
        Scale scale = new Scale(f,scaleX,scaleY);
        return scale;
    }
    public static Function power(Function f, double power){
        Power powerF = new Power(f,power);
        return powerF;
    }
    public static Function sum(Function f1, Function f2){
        Sum sum = new Sum(f1,f2);
        return sum;
    }
    public static Function mult(Function f1, Function f2){
        Mult mult = new Mult(f1,f2);
        return mult;
    }
    public static Function composition(Function f1, Function f2){
        Composition composition = new Composition(f1,f2);
        return composition;
    }
    public static double integrate(Function f, double lefftX, double rightX, double discret){
        double sum = 0;
        if (discret <= 0) {
            throw new IllegalArgumentException("Шаг интегрирования должен быть положительным");
        }

        if (lefftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if(f.getLeftDomainBorder()>lefftX||f.getRightDomainBorder()<rightX)
            throw new IllegalArgumentException("Интервал интегрирования выходит за границы оюласти определения функции");

        if(lefftX+discret>rightX)
            discret = rightX-lefftX;

        for(;lefftX<rightX;lefftX+=discret){
            if(lefftX+discret>=rightX)
                discret = rightX-lefftX;

            sum += (f.getFunctionValue(lefftX)+f.getFunctionValue(lefftX+discret))*discret/2;
        }
        return sum;
    }
    public static double findOptimalStep(Function function,
                                         double leftBorder,
                                         double rightBorder,
                                         double targetAccuracy,
                                         double initialStep) {

        double step = initialStep;
        double previousIntegral = integrate(function, leftBorder, rightBorder, step);
        double currentIntegral;

        int maxIterations = 100;
        int iteration = 0;

        while (iteration < maxIterations) {
            step /= 2.0;
            currentIntegral = integrate(function, leftBorder, rightBorder, step);

            double error = Math.abs(currentIntegral - previousIntegral);

            if (error < targetAccuracy) {
                return step;
            }

            previousIntegral = currentIntegral;
            iteration++;
        }

        throw new IllegalStateException("Не удалось достичь требуемой точности за " + maxIterations + " итераций");
    }
}
