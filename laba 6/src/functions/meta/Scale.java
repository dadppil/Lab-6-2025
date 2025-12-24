package functions.meta;

import functions.Function;

public class Scale implements Function {

private Function baseFunction;
private double xScale;
private double yScale;
final double EPSILON = 2.220446049250326E-16;

public Scale(Function baseFunction, double xScale, double yScale){
    if (Math.abs(xScale) < EPSILON) {
        throw new IllegalArgumentException("Коэффициент масштабирования по X" +
                " не может быть нулевым");
    }
    this.baseFunction = baseFunction;
    this.xScale = xScale;
    this.yScale = yScale;
}
    @Override
    public double getLeftDomainBorder() {
        if(xScale > 0){
        return baseFunction.getLeftDomainBorder()/xScale;
        }
        else{
        return baseFunction.getRightDomainBorder()/xScale;
        }
    }

    @Override
    public double getRightDomainBorder() {
        if(xScale > 0){
            return baseFunction.getRightDomainBorder()/xScale;
        }
        else{
            return baseFunction.getLeftDomainBorder()/xScale;
        }
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new IllegalArgumentException("Значение х не входит в область определения");
        }
        double scaledX = x * xScale;
        double baseValue = baseFunction.getFunctionValue(scaledX);
        return baseValue * yScale;
    }
}
