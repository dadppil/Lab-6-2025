package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable,Cloneable {
    private double x;
    private double y;
    private static final double EPSILON = Math.ulp(1.0);;

    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    public FunctionPoint(FunctionPoint point){
        x = point.x;
        y = point.y;
    }
    FunctionPoint(){
        x = 0;
        y = 0;
    }
    public double getY(){
        return y;
    }
    public double getX(){
        return x;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public void showPoint(){
        System.out.println("[" + x + "," + y + "]") ;
    }
    @Override
    public String toString(){
        return ("[" + x + "," + y + "]") ;
    }
    @Override
    public boolean equals(Object o){
        if (o == null || getClass() != o.getClass())
            return false;

        FunctionPoint that = (FunctionPoint) o;

        return Double.compare(this.x, that.x) == 0 &&
                Double.compare(this.y, that.y) == 0;
    }
    @Override
    public int hashCode(){
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);

        // XOR для старших и младших частей обоих double
        int hash = (int)(xBits ^ (xBits >>> 32));
        hash = 31 * hash + (int)(yBits ^ (yBits >>> 32));

        return hash;
    }
    @Override
    public Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}
