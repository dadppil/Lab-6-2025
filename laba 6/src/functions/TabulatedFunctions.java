package functions;

import java.io.*;

public class TabulatedFunctions {

    private TabulatedFunctions(){}
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if(leftX<function.getLeftDomainBorder()||rightX>function.getRightDomainBorder()){
            throw new IllegalArgumentException("Указанные границы для табулирования" +
                    " выходят за область определения функции");
        }
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        return new ArrayTabulatedFunction(leftX,rightX,values);
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out)throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        try {
            dataOut.writeInt(function.getPointsCount());

            for (int i = 0; i < function.getPointsCount(); i++) {
                dataOut.writeDouble(function.getPointX(i));
                dataOut.writeDouble(function.getPointY(i));
            }

            dataOut.flush();
        } finally {
        }
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in)throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        try {
            int pointsCount = dataIn.readInt();

            if (pointsCount < 2) {
                throw new IOException("Некорректные данные: количество точек должно быть не менее 2");
            }

            double[] xValues = new double[pointsCount];
            double[] yValues = new double[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                xValues[i] = dataIn.readDouble();
                yValues[i] = dataIn.readDouble();
            }

            for (int i = 1; i < pointsCount; i++) {
                if (xValues[i] <= xValues[i - 1]) {
                    throw new IOException("Некорректные данные: значения X должны быть упорядочены по возрастанию");
                }
            }
            double leftX = xValues[0];
            double rightX = xValues[xValues.length - 1];
            return new ArrayTabulatedFunction(leftX,rightX,yValues);

        } catch (EOFException e) {
        throw new IOException("Неожиданный конец потока: данные неполные", e);
        } catch (IllegalArgumentException e) {
        throw new IOException("Некорректные данные: " + e.getMessage(), e);
        } finally {

        }
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out)throws IOException{
        BufferedWriter writer = new BufferedWriter(out);
        try {
            writer.write(String.valueOf(function.getPointsCount()));
            writer.write(" ");

            for (int i = 0; i < function.getPointsCount(); i++) {
                writer.write(String.valueOf(function.getPointX(i)));
                writer.write(" ");
                writer.write(String.valueOf(function.getPointY(i)));

                if (i < function.getPointsCount() - 1) {
                    writer.write(" ");
                }
            }

            writer.newLine();
            writer.flush();
        } finally {
        }
    }
    public static TabulatedFunction readTabulatedFunction(Reader in)throws IOException{
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        try {
            // Настраиваем токенизатор
            tokenizer.resetSyntax();
            tokenizer.wordChars('0', '9');
            tokenizer.wordChars('.', '.');
            tokenizer.wordChars('-', '-');
            tokenizer.wordChars('e', 'e');
            tokenizer.wordChars('E', 'E');
            tokenizer.whitespaceChars(' ', ' ');
            tokenizer.whitespaceChars('\t', '\t');
            tokenizer.whitespaceChars('\n', '\n');
            tokenizer.whitespaceChars('\r', '\r');


            if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
                throw new IOException("Ожидалось количество точек (целое число)");
            }

            int pointsCount;
            try {
                pointsCount = Integer.parseInt(tokenizer.sval);
            } catch (NumberFormatException e) {
                throw new IOException("Некорректное количество точек: " + tokenizer.sval, e);
            }

            if (pointsCount < 2) {
                throw new IOException("Некорректные данные: количество точек должно быть не менее 2");
            }


            double[] xValues = new double[pointsCount];
            double[] yValues = new double[pointsCount];

            for (int i = 0; i < pointsCount; i++) {

                if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
                    throw new IOException("Ожидалось значение x для точки " + i);
                }

                try {
                    xValues[i] = Double.parseDouble(tokenizer.sval);
                } catch (NumberFormatException e) {
                    throw new IOException("Некорректное значение x для точки " + i + ": " + tokenizer.sval, e);
                }


                if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
                    throw new IOException("Ожидалось значение y для точки " + i);
                }

                try {
                    yValues[i] = Double.parseDouble(tokenizer.sval);
                } catch (NumberFormatException e) {
                    throw new IOException("Некорректное значение y для точки " + i + ": " + tokenizer.sval, e);
                }
            }


            for (int i = 1; i < pointsCount; i++) {
                if (xValues[i] <= xValues[i-1]) {
                    throw new IOException("Некорректные данные: значения X должны быть упорядочены по возрастанию");
                }
            }


            double leftX = xValues[0];
            double rightX = xValues[xValues.length - 1];
            return new ArrayTabulatedFunction(leftX,rightX,yValues);

        } finally {
        }
    }
}
