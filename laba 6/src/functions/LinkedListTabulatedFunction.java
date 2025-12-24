package functions;

import java.io.Serializable;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    private static class FunctionNode implements Serializable {
        private static final long serialVersionUID = 1L;
        FunctionPoint point;
        transient FunctionNode prev;
        transient FunctionNode next;

        FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }
    private static final long serialVersionUID = 1L;
    private FunctionNode head;
    private int pointsCount;
    private transient FunctionNode lastAccessedNode;
    private transient int lastAccessedIndex;
    private static final double EPSILON = Math.ulp(1.0);;

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        initializeList();
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x, 0.0);
        }

    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        initializeList();
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x, values[i]);
        }
    }
    public LinkedListTabulatedFunction(FunctionPoint[] points){
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        for(int i = 1; i<points.length;i++){
            if(points[i-1].getX()>=points[i].getX()){
                throw new IllegalArgumentException("Точки в массиве не упорядочены" +
                        " по значению абсциссы");
            }
        }
        initializeList();
        for (int i = 0; i<points.length;i++){
            addNodeToTail().point = new FunctionPoint(points[i]) ;
        }
    }

    private void initializeList() {
        head = new FunctionNode(null);
        head.prev = head;
        head.next = head;
        pointsCount = 0;
        lastAccessedNode = head;
        lastAccessedIndex = -1;
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " " +
                    "выходит за границы [0, " + (pointsCount - 1) + "]");
        }
        FunctionNode node;
        if (lastAccessedIndex != -1 && Math.abs(index - lastAccessedIndex) < index &&
                Math.abs(index - lastAccessedIndex) < pointsCount - index) {
            node = lastAccessedNode;
            if (index > lastAccessedIndex) {
                for (int i = lastAccessedIndex; i < index; i++) {
                    node = node.next;
                }
            } else {
                for (int i = lastAccessedIndex; i > index; i--) {
                    node = node.prev;
                }
            }
        } else {
            // Начинаем с головы
            node = head.next;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        }

        lastAccessedNode = node;
        lastAccessedIndex = index;
        return node;
    }

    private FunctionNode addNodeToTail() {
        return addNodeByIndex(pointsCount);
    }

    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " " +
                    "выходит за границы [0, " + (pointsCount - 1) + "]");
        }
        FunctionNode newNode = new FunctionNode(null);
        FunctionNode targetNode;
        if (index == pointsCount) {
            targetNode = head;
        } else {
            targetNode = getNodeByIndex(index);
        }


        newNode.prev = targetNode.prev;
        newNode.next = targetNode;
        targetNode.prev.next = newNode;
        targetNode.prev = newNode;

        pointsCount++;
        lastAccessedIndex = -1; // Сбрасываем кэш

        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " " +
                    "выходит за границы [0, " + (pointsCount - 1) + "]");
        }
        FunctionNode node = getNodeByIndex(index);

        node.next.prev = node.prev;
        node.prev.next = node.next;

        pointsCount--;
        return node;
    }

    public double getLeftDomainBorder() {
        return getNodeByIndex(0).point.getX();
    }

    public double getRightDomainBorder() {
        return getNodeByIndex(pointsCount - 1).point.getX();
    }

    public int getPointsCount(){
        return pointsCount;
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        FunctionNode Ipoint = head.next;
        for (int i = 0; i < pointsCount; i++){
            if (Math.abs(Ipoint.point.getX() - x) <= EPSILON) {
                return Ipoint.point.getY();
            }
            Ipoint = Ipoint.next;
        }
        Ipoint = head.next;
        for (int i = 0; i < pointsCount - 1; i++) {
            if (x >= Ipoint.point.getX() && x <= Ipoint.next.point.getX()) {
                double leftX = Ipoint.point.getX();
                double rightX = Ipoint.next.point.getX();
                double leftY = Ipoint.point.getY();
                double rightY = Ipoint.next.point.getY();


                return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
            }
            Ipoint = Ipoint.next;
        }


        return Double.NaN;

    }
    public void showPoints(){
        FunctionNode node = head.next;
        for (int i = 0; i<pointsCount;i++){
            System.out.print("Значение точки "+ (i) + ": ");
            node.point.showPoint();
            System.out.println();
            node = node.next;
        }
    }
    public FunctionPoint getPoint(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
        }
        return new FunctionPoint(getNodeByIndex(index).point);
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException{
        {
            if (index < 0 || index >= pointsCount) {
                throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
            }
            if ((point.getX() <= getNodeByIndex(index-1).point.getX() && index != 0) || (point.getX() >= getNodeByIndex(index+1).point.getX() && index != pointsCount - 1)) {
                throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек табулированной функции");
            }
        }
        getNodeByIndex(index).point = new FunctionPoint(point);

    }
    public double getPointX(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
        }
        return getNodeByIndex(index).point.getX();
    }
    public void setPointX(int index, double x) throws InappropriateFunctionPointException{
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
        }
        FunctionNode node = getNodeByIndex(index);
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
        }
        if(index == 0 && x >= node.next.point.getX()){
            throw new IllegalArgumentException("Новое значение x нарушает упорядоченность");
        }
        if (x <= node.prev.point.getX() && index == pointsCount-1){
            throw new IllegalArgumentException("Новое значение x нарушает упорядоченность");
        }
        if(index>0&&index<pointsCount-1){
            if (x >= node.next.point.getX() || x <= node.prev.point.getX()) {
                throw new IllegalArgumentException("Новое значение x нарушает упорядоченность");
            }

        }
        node.point.setX(x);
    }
    public double getPointY(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
        }
        return getNodeByIndex(index).point.getY();
    }
    public void setPointY(int index, double y){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
        }
        getNodeByIndex(index).point.setY(y);
    }
    public void deletePoint(int index){

            if (index < 0 || index >= pointsCount) {
                throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы массива точек");
            }
            if (pointsCount <= 2) {
                throw new IllegalStateException("Нельзя удалить точку: функция должна содержать минимум 2 точки");
            }

        deleteNodeByIndex(index).point = null;


    }
    public void addPoint(FunctionPoint point)throws InappropriateFunctionPointException {
        {
            FunctionNode node = head.next;
            int insertIndex = 0;
            while (insertIndex < pointsCount && point.getX() > node.point.getX()) {
                insertIndex++;
                node = node.next;
            }
            if (insertIndex < pointsCount && (Math.abs(point.getX() - node.point.getX()) <= EPSILON)) {
                throw new InappropriateFunctionPointException("Точка с x=" + point.getX() + " уже существует");
            }
        }
        FunctionNode node = head.next;
        int insertIndex = 0;
        while (insertIndex < pointsCount && point.getX() > node.point.getX()) {
            insertIndex++;
            node = node.next;
        }
        addNodeByIndex(insertIndex).point = new FunctionPoint(point);
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        FunctionNode node = head.next;
        sb.append('{');
        for(int i =0;i<pointsCount;i++){
            sb.append(node.point);
            node = node.next;
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
        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction arrayThat = (LinkedListTabulatedFunction) o;

            FunctionNode node = this.head.next;
            FunctionNode Thatnode = arrayThat.head.next;
            for (int i = 0; i < pointsCount; i++) {
                if (!Thatnode.point.equals(node.point)) {
                    return false;
                }
                node = node.next;
                Thatnode = Thatnode.next;
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

        FunctionNode node = this.head.next;
        for (int i = 0; i < pointsCount; i++) {
            hash = 31 * hash + node.point.hashCode();
            node = node.next;
        }

        return hash;
    }
    @Override
    public Object clone(){
        try {
            LinkedListTabulatedFunction clone = (LinkedListTabulatedFunction) super.clone();
            clone.initializeList();
            if (this.pointsCount > 0){

                FunctionNode currentNode = this.head.next;
                FunctionNode lastCreatedNode = null;

                while (currentNode != this.head) {
                    FunctionNode newNode = new FunctionNode((FunctionPoint) currentNode.point.clone());
                    if (lastCreatedNode == null) {
                        clone.head.next = newNode;
                        newNode.prev = clone.head;
                    } else {
                        lastCreatedNode.next = newNode;
                        newNode.prev = lastCreatedNode;
                    }

                    lastCreatedNode = newNode;
                    clone.pointsCount++;
                    currentNode = currentNode.next;
                }

                if (lastCreatedNode != null) {
                    lastCreatedNode.next = clone.head;
                    clone.head.prev = lastCreatedNode;
                }
            }

            return clone;


        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}


