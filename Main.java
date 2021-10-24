import java.math.BigInteger;
import java.util.*;
import java.lang.StringBuilder;

class Main {

    public static TreeMap<BigInteger, Integer> results = new TreeMap<>() ;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String exp = scanner.nextLine();
        String infix = addSpaces(exp);
        String postfix = convert(infix);
        if (!postfix.contains("d")) {
            calculator(postfix);
        } else {
            replaceFirstd(postfix);
        }
        showresults();
    }

    private static void showresults() {
        int size = results.size();
        long totaloptions=0;
        for (int option: results.values()) {
            totaloptions+=option;
        }
        for (BigInteger set: results.keySet()) {
            System.out.println(set+" "+String.format("%.2f", 100.00/totaloptions*results.get(set)).replace(',','.'));
        }
    }

    public static String addSpaces(String expression) {
        StringBuilder sb = new StringBuilder();
        char[] chars = expression.toCharArray();
        int N = chars.length;
        for (int i = 0; i < N; i++) {
            if (isOperator(chars[i]) || isParenthesis(chars[i])) {
                sb.append(" ");

            }
            sb.append(chars[i]);
                if (isParenthesis(chars[i]) && (i+1)<N && Character.isDigit(chars[i+1])) {
                    sb.append(" ");
                }
                if (isOperator(chars[i]) && (i+1)<N && Character.isDigit(chars[i+1])) {
                    sb.append(" ");
                }
        }
        sb.append(" ");
        return sb.toString();
    }
    public static String convert(String expression) {
        // Используем StringBuilder,поскольку он быстрее, чем конкантенация строк
        StringBuilder sb = new StringBuilder();

        // Вводим стек для отслеживания операций
        Stack<Character> op = new Stack<>();

        // Преобразуем выражение в массив символов (Character)
        char[] chars = expression.toCharArray();

        int N = chars.length;

        for (int i = 0; i < N; i++) {
            char ch = chars[i];

            if (ch=='d') {
                sb.append(ch);
            } else if (Character.isDigit(ch)) {
                // Если встречаем цифру, добавляем к выходной строке
                    while (Character.isDigit(chars[i])) {
                        sb.append(chars[i++]);
                    }
                    sb.append(' ');
            } else if (ch == '(') {
                // Левая скобка, идет прямо в стек
                op.push(ch);
            } else if (ch == ')') {
                // Правая скобка, убираем и добавляем все, что до левой скобки
                while (op.peek() != '(') {
                    sb.append(op.pop()).append(' ');
                }
                // Выкидываем левую скобку
                op.pop();
            } else if (isOperator(ch)) {
                // Оператор, убираем сначала операторы с более высоким приоритетом,а потом добавляем их в стек
                while (!op.isEmpty() && priority(op.peek()) >= priority(ch)) {
                    sb.append(op.pop()).append(' ');
                }
                op.push(ch);
            }
        }

        // Убираем все,что осталось в стеке и добавляем к выходной строке
        while(!op.isEmpty()) {
            sb.append(op.pop()).append(' ');
        }

        return sb.toString();
    }

    //Проверка, явлется ли символ оператором
    private static boolean isOperator(char ch) {
        return ch == '*' || ch == '>' || ch == '+' || ch == '-';
    }

    //Проверка, явлется ли символ скобкой (для внутренее процедуры addSpaces
    private static boolean isParenthesis(char ch) {
        return ch == '(' || ch == ')' ;
    }

    //Приоритет операторов
    private static int priority(char operator) {
        switch (operator) {
            case '*' : return 3;
            case '+' :
            case '-' : return 2;
            case '>' : return 1;
        }
        return 0;
    }

    private static void replaceFirstd(String postfix) {
        String substring = postfix.substring(postfix.indexOf('d'), postfix.length() - 1);
        String test = substring.substring(substring.indexOf('d') + 1, substring.indexOf(' '));
        int counter = Integer.parseInt(test);
        for (int i = 1; i < counter+1 ; i++) {
            String newpostfix=postfix.replaceFirst("d\\d+", String.valueOf(i));
            if (newpostfix.contains("d")) {
                replaceFirstd(newpostfix);
            } else {
                calculator(newpostfix);
            }
        }
    }

    public static void calculator(String postfix) {
        String[] strArr = postfix.split(" ");
        //Используем BigInteger для расчетов, чтобы избежать переполнения при вычислениях
        Stack<BigInteger> operands = new Stack<>();

        for(String str : strArr) {
            if (str.trim().equals("")) {
                continue;
            }

            switch (str) {
                case "+":
                case "-":
                case "*":
                case ">":
                    BigInteger right = operands.pop();
                    BigInteger left = operands.pop();
                    BigInteger value = BigInteger.valueOf(0);
                    switch(str) {
                        case "+":
                            value = left.add(right);
                            break;
                        case "-":
                            value = left.subtract(right);
                            break;
                        case "*":
                            value = left.multiply(right);
                            break;
                        case ">":
                            int compare = left.compareTo(right);
                            value = compare>0 ? BigInteger.valueOf(1) : BigInteger.valueOf(0);
                            break;
                        default:
                            break;
                    }
                    operands.push(value);
                    break;
                default:
                    operands.push(new BigInteger(str));
                    break;
            }
        }
        addtoResults(operands.pop());
    }

    private static void addtoResults(BigInteger pop) {
        if (results.containsKey(pop)) {
            int currentfrequency = results.get(pop);
            currentfrequency++;
            results.put(pop,currentfrequency);
        } else {
            results.put(pop,1);
        }
    }
}