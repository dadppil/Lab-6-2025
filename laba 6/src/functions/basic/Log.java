package functions.basic;


import functions.Function;

public class Log implements Function{
    private double base;
    final double EPSILON = 2.220446049250326E-16;
    public Log(double base){
        if(base<=0 || Math.abs(base - 1.0) < EPSILON){
            throw new IllegalArgumentException("Основание логарифма должно быть" +
                    " положительным и не равным 1");
        }
        this.base = base;
    }
    @Override
    public double getLeftDomainBorder() {
        return 0;
    }
    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    @Override
    public double getFunctionValue(double x) {
        if(x<=0){
            throw new IllegalArgumentException("Значение х не входит в область определения");
        }
        return Math.log(x)/Math.log(base);
    }
}
