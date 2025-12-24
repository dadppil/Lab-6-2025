package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function baseFunction;
    private double xShift;
    private double yShift;
    public Shift(Function baseFunction, double xShift, double yShift){
        this.baseFunction =baseFunction;
        this.xShift = xShift;
        this.yShift = yShift;
    }
    @Override
    public double getLeftDomainBorder() {
        return baseFunction.getLeftDomainBorder()+xShift;
    }

    @Override
    public double getRightDomainBorder() {
        return baseFunction.getRightDomainBorder()+xShift;
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new IllegalArgumentException("Значение х не входит в область определения");
        }
        double shiftedX = x+xShift;
        return baseFunction.getFunctionValue(shiftedX)+yShift;
    }
}
