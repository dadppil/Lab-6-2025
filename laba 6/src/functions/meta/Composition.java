package functions.meta;

import functions.Function;

public class Composition implements Function {
    private Function outerFunction;
    private Function innerFunction;
    public Composition(Function outerFunction, Function innerFunction){
        this.outerFunction = outerFunction;
        this.innerFunction = innerFunction;
    }
    @Override
    public double getLeftDomainBorder() {
        return innerFunction.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return innerFunction.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new IllegalArgumentException("Значение х не входит в область определения");
        }
        double innerValue = innerFunction.getFunctionValue(x);
        if (innerValue < outerFunction.getLeftDomainBorder() || innerValue > outerFunction.getRightDomainBorder()) {
            throw new IllegalArgumentException("Значение  внутренней функции не входит в область определения внешней");
        }
        return outerFunction.getFunctionValue(innerValue);
    }
}
