package functions;
import java.io.*;
public interface TabulatedFunction extends Function,Serializable,Cloneable {
    public int getPointsCount();
    public void showPoints();
    public FunctionPoint getPoint(int index);
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;
    public double getPointX(int index);
    public void setPointX(int index, double x) throws InappropriateFunctionPointException;
    public double getPointY(int index);
    public void setPointY(int index, double y);
    public void deletePoint(int index);
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
    public Object clone();
}
