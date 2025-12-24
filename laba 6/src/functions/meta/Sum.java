package functions.meta;

import functions.Function;

public class Sum implements Function {
    private Function f1;
    private Function f2;
    public Sum(Function f1, Function f2){
        this.f1 = f1;
        this.f2 = f2;
    }
    @Override
    public double getRightDomainBorder() {
        return Math.max(f1.getRightDomainBorder(), f2.getRightDomainBorder());
    }

    @Override
    public double getLeftDomainBorder() {
        return Math.min(f1.getLeftDomainBorder(), f2.getLeftDomainBorder());
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new IllegalArgumentException("Значение х не входит в область определения");
        }
        return f1.getFunctionValue(x) + f2.getFunctionValue(x);
    }
}
