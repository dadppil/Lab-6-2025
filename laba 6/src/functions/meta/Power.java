package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function f;
    private double power;

    public Power(Function f, double power){
        this.f = f;
        this.power = power;
    }
    @Override
    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return f.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new IllegalArgumentException("Значение х не входит в область определения");
        }
        return Math.pow(f.getFunctionValue(x), power);
    }
}
