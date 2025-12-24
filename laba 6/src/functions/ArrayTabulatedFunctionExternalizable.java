package functions;

import java.io.*;

public class ArrayTabulatedFunctionExternalizable implements Externalizable,TabulatedFunction {
    private FunctionPoint[] points;
    private static final long serialVersionUID = 2L;
    final double EPSILON = Math.ulp(1.0);;
    private int pointsCount;
    public ArrayTabulatedFunctionExternalizable(double leftX, double rightX, int pointsCount){
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i<pointsCount;i++){
            double x = leftX + i*step;
            points[i] = new FunctionPoint(x, 0);
        }
        this.pointsCount = pointsCount;
    }
    public ArrayTabulatedFunctionExternalizable(){}
    public ArrayTabulatedFunctionExternalizable(double leftX, double rightX, double[] values){
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        pointsCount = values.length;
        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i< pointsCount;i++){
            double x = leftX + i*step;
            points[i] = new FunctionPoint(x, values[i]);
        }

    }
    public ArrayTabulatedFunctionExternalizable(FunctionPoint[] points){
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        for(int i = 1; i<points.length;i++){
            if(points[i-1].getX()>=points[i].getX()){
                throw new IllegalArgumentException("Точки в массиве не упорядочены" +
                        " по значению абсциссы");
            }
        }
        pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount];
        for (int i = 0; i< pointsCount;i++){
            this.points[i] = new FunctionPoint(points[i]);
        }
    }
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pointsCount = in.readInt();
        if (pointsCount < 2) {
            throw new InvalidObjectException("pointsCount должно быть >= 2");
        }

        points = new FunctionPoint[pointsCount + 5];
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }
    public double getLeftDomainBorder(){
        return points[0].getX();
    }
    public double getRightDomainBorder(){
        return points[pointsCount-1].getX();
    }
    public int getPointsCount() {
        return pointsCount;
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }


        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(points[i].getX() - x) <= EPSILON) {
                return points[i].getY();
            }
        }


        for (int i = 0; i < pointsCount - 1; i++) {
            if (x >= points[i].getX() && x <= points[i + 1].getX()) {
                double leftX = points[i].getX();
                double rightX = points[i + 1].getX();
                double leftY = points[i].getY();
                double rightY = points[i + 1].getY();


                return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
            }
        }


        return Double.NaN;
    }
    public void showPoints(){
        for (int i = 0; i<pointsCount;i++){
            System.out.print("Значение точки "+ (i+1) + ": ");
            points[i].showPoint();
            System.out.println();
        }
    }
    public FunctionPoint getPoint(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
        }
        return new FunctionPoint(points[index]);
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException{
        {
            if (index < 0 || index >= pointsCount) {
                throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
            }
            if ((point.getX() <= points[index - 1].getX() && index != 0) || (point.getX() >= points[index + 1].getX() && index != pointsCount - 1)) {
                throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек табулированной функции");
            }
        }
        points[index] = new FunctionPoint(point);

    }
    public double getPointX(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
        }
        return points[index].getX();
    }
    public void setPointX(int index, double x) throws InappropriateFunctionPointException{
        {

            if (index < 0 || index >= pointsCount) {
                throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
            }
            if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
                throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
            }
            if(index == pointsCount-1 && x <= points[index - 1].getX()){
                throw new IllegalArgumentException("Новое значение x нарушает упорядоченность");
            }
            if(index == 0 && x >= points[index + 1].getX()){

                throw new IllegalArgumentException("Новое значение x нарушает упорядоченность");
            }
            if(index!=0&&index!=pointsCount-1){
                if ( x >= points[index + 1].getX() || x <= points[index - 1].getX()) {
                    throw new IllegalArgumentException("Новое значение x нарушает упорядоченность");
                }
            }
        }
        points[index].setX(x);

    }
    public double getPointY(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
        }
        return points[index].getY();
    }
    public void setPointY(int index, double y){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
        }
        points[index].setY(y);
    }
    public void deletePoint(int index){

        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
        }
        if (pointsCount <= 2) {
            throw new IllegalStateException("Нельзя удалить точку: функция должна содержать минимум 2 точки");
        }

        System.arraycopy(points,index +1,points,index,pointsCount-1-index);
        pointsCount--;
        points[pointsCount] = null;


    }
    public void addPoint(FunctionPoint point)throws InappropriateFunctionPointException {
        {
            int insertIndex = 0;
            while (insertIndex < pointsCount && point.getX() > points[insertIndex].getX()) {
                insertIndex++;
            }
            if (insertIndex < pointsCount && (Math.abs(point.getX() - points[insertIndex].getX()) <= EPSILON)) {
                throw new InappropriateFunctionPointException("Точка с x=" + point.getX() + " уже существует");
            }
        }
        int insertIndex = 0;
        while (insertIndex < pointsCount && point.getX() > points[insertIndex].getX()) {
            insertIndex++;
        }
        if (pointsCount == points.length) {
            int newCapacity = points.length + points.length / 2 + 1;
            FunctionPoint[] newPoints = new FunctionPoint[newCapacity];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        if (insertIndex < pointsCount) {
            System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);
        }
        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for(int i =0;i<pointsCount;i++){
            sb.append(points[i]);
            if(i != pointsCount-1)sb.append(", ");
        }
        sb.append('}');
        return sb.toString();
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;
        TabulatedFunction that = (TabulatedFunction) o;

        if (this.pointsCount != that.getPointsCount()) return false;
        if (o instanceof ArrayTabulatedFunctionExternalizable) {
            ArrayTabulatedFunctionExternalizable arrayThat = (ArrayTabulatedFunctionExternalizable) o;


            for (int i = 0; i < pointsCount; i++) {
                if (Math.abs(this.points[i].getX() - arrayThat.points[i].getX()) >= EPSILON ||
                        Math.abs(this.points[i].getY() - arrayThat.points[i].getY()) >= EPSILON) {
                    return false;
                }
            }
        } else {

            for (int i = 0; i < pointsCount; i++) {
                double thisX = this.getPointX(i);
                double thisY = this.getPointY(i);
                double thatX = that.getPointX(i);
                double thatY = that.getPointY(i);

                if (Math.abs(thisX - thatX) >= EPSILON ||
                        Math.abs(thisY - thatY) >= EPSILON) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public int hashCode(){
        int hash = pointsCount;


        for (int i = 0; i < pointsCount; i++) {
            hash = 31 * hash + points[i].hashCode();
        }

        return hash;
    }
    @Override
    public Object clone(){
        try {
            ArrayTabulatedFunctionExternalizable clone = (ArrayTabulatedFunctionExternalizable) super.clone();


            clone.points = new FunctionPoint[this.points.length];
            for (int i = 0; i < this.pointsCount; i++) {
                clone.points[i] = (FunctionPoint) this.points[i].clone();
            }

            clone.pointsCount = this.pointsCount;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}

