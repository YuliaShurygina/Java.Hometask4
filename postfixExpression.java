
//*Реализовать алгоритм перевода из инфиксной записи в постфиксную для арифметического выражения.
//http://primat.org/news/obratnaja_polskaja_zapis/2016-04-09-1181
//Вычислить запись если это возможно
//*Написать программу вычисляющую значение сложного арифметического выражения. Для простоты - выражение всегда вычисляемое
//Пример: (2^3 * (10 / (5 - 3)))^(Sin(Pi)) ответ - 1
import java.util.*;

public class postfixExpression {

    public static boolean check(char[] array) {
        Stack<Character> st = new Stack<>();
        for (int i = 0; i < array.length; i++) {
            switch (array[i]) {
                case '(': {
                    st.addElement(array[i]);
                    break;
                }
                case ')': {
                    if (st.empty() || st.pop() != '(') {
                        return false;
                    }
                    break;
                }
            }
        }
        if (st.empty())
            return true;
        else
            return false;
    }

    public static String[] digitParsing(char[] array, int i) {
        String[] indexAndString = new String[2];
        String result = "";
        while (Character.isDigit(array[i])) {
            result += array[i];
            i++;
        }
        indexAndString[0] = Integer.toString(i);
        indexAndString[1] = result;
        return indexAndString;
    }

    public static String[] letterParsing(char[] array, int i) {
        String[] indexAndString = new String[2];
        String result = "";
        while (Character.isLetter(array[i])) {
            result += array[i];
            i++;
        }
        indexAndString[0] = Integer.toString(i);
        indexAndString[1] = result;
        return indexAndString;
    }

    public static String[] expParsing(char[] array) {
        List<String> list = new LinkedList<>();
        int i = 0;
        while (i < array.length) {
            if (Character.isDigit(array[i])) {
                String[] indexAndString = digitParsing(array, i);
                i = Integer.parseInt(indexAndString[0]);
                String str = indexAndString[1];
                list.add(str);
            } else if (Character.isLetter(array[i])) {
                String[] indexAndString = letterParsing(array, i);
                i = Integer.parseInt(indexAndString[0]);
                String str = indexAndString[1];
                list.add(str);
            } else if (array[i] == ' ') {
                i++;
                continue;
            } else {
                list.add(Character.toString(array[i]));
                i++;
            }
        }
        String[] parsedArray = list.toArray(new String[list.size()]);
        return parsedArray;

    }

    public static int findPriority(String c) {

        if (c.equals("("))
            return 1;
        if (c.equals("^"))
            return 3;
        if (c.equals("*") || c.equals("/"))
            return 2;
        if (c.equals("+") || c.equals("-"))
            return 4;
        else
            return 0;
    }

    public static boolean isNumeric(String string) {
        int intValue;
        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String postfixExp(String[] array, String[] operations) {

        Stack<String> st = new Stack<>();
        String outputString = "";
        int i = 0;
        while (i < array.length) {

            if (isNumeric(array[i])) {
                outputString += array[i] + " ";
            }
            if (array[i].equals("Pi")) {
                outputString += array[i] + " ";
            }
            if (array[i].equals("Sin")) {
                st.add(array[i]);
            }
            if (array[i].equals("(")) {
                st.add(array[i]);
            }
            if (array[i].equals(")")) {

                while (st.peek().equals("(") == false) {
                    outputString += st.pop() + " ";
                }
                st.pop();
            }
            if (Arrays.asList(operations).contains(array[i])) {

                if (st.isEmpty() || (findPriority(st.peek()) < findPriority(array[i])))
                    st.add(array[i]);
                else if (findPriority(st.peek()) >= findPriority(array[i])) {
                    while (findPriority(st.peek()) >= findPriority(array[i])) {
                        outputString += st.pop() + " ";
                    }
                    if (st.isEmpty() || (findPriority(st.peek()) < findPriority(array[i])))
                        st.add(array[i]);
                }
            }
            if (i == array.length - 1) {
                while (!st.isEmpty()) {
                    outputString += st.pop();
                }
            }
            i++;
        }
        return outputString;
    }

    public static boolean isDigit(String string) {
        char[] array = string.toCharArray();
        if (Character.isDigit(array[0])) {
            return true;
        } else {
            return false;
        }
    }

    public static Double countPostfixExp(String exp) {
        String[] symbols = exp.split(" ");
        Double res = (double) 0;
        Stack<Double> st = new Stack<>();
        for (int i = 0; i < symbols.length; i++) {
            if (isDigit(symbols[i])) {
                st.push(Double.parseDouble(symbols[i]));
            } else if (symbols[i].equals("Pi")) {
                st.push(Math.PI);
            } else {
                switch (symbols[i]) {
                    case "+":
                        res = st.pop() + st.pop();
                        st.push(res);
                        break;
                    case "-":
                        res = -st.pop() + st.pop();
                        st.push(res);
                        break;
                    case "*":
                        res = st.pop() * st.pop();
                        st.push(res);
                        break;
                    case "/":
                        res = st.pop() / st.pop();
                        st.push(res);
                        break;
                    case "^":
                        Double a = st.pop();
                        Double b = st.pop();
                        res = Math.pow(b, a);
                        st.push(res);
                        break;
                    case "Sin":
                        res = Math.sin(st.pop());
                        st.push(res);
                        break;
                    default:
                        break;
                }
            }
        }
        return res = st.pop();
    }

    public static void main(String[] args) {
        String exp = "(2^3 * (10 / (5 - 3)))^(Sin(Pi))";
        String[] operations = { "+", "-", "/", "*", "^" };
        System.out.println("Исходное выражение: " + exp);
        char[] array = exp.toCharArray();
        if (!check(array)) {
            System.out.println("Что-то не так со скобками");
            System.exit(0);
        }
        String[] parsedExpression = expParsing(array);
        System.out.println("Выражение в постфиксной записи: " + postfixExp(parsedExpression, operations));
        Double result = countPostfixExp(postfixExp(parsedExpression, operations));
        System.out.println();
        System.out.printf("Результат выражения: %.2f\n", result);
    }
}
