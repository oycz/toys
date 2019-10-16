import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class MathEvaluator {

    // MathEvaluator
    public static void main(String args[]) {
        MathEvaluator calc = new MathEvaluator();
        System.out.println(calc.calculate("((2))"));
        System.out.println(calc.calculate("((1))-3/ -2 + (8 -5) +4 *((2))"));
    }

    private enum Symbol {
        ADD, MINUS, TIMES, DEVIDE, LPAR, RPAR, NEGATIVE, EOF
    }
    private static Map<Symbol, Integer> priority = new HashMap<>();

    {
        priority.put(Symbol.LPAR, 0);
        priority.put(Symbol.ADD, 1);
        priority.put(Symbol.MINUS, 1);
        priority.put(Symbol.TIMES, 2);
        priority.put(Symbol.DEVIDE, 2);
        priority.put(Symbol.NEGATIVE, 3);
        priority.put(Symbol.RPAR, 4);
    }

    public double calculate(String expression) {
        Stack<Symbol> symbols = new Stack<>();
        Stack<Double> nums = new Stack<>();
        Symbol topSymbol;
        Integer topNum;
        expression = (expression + '\0').replaceAll(" ", "");
        char[] chars = expression.toCharArray();
        boolean requireNumber;

        int cur = 0;
        if(chars[cur] == '-' || chars[cur] == '(') {
            symbols.add(chars[cur] == '-'? Symbol.NEGATIVE: Symbol.LPAR);
            cur++;
            requireNumber = true;
        } else if(chars[cur] >= '0' && chars[cur] <= '9') {
            // read number
            while(cur < chars.length && ((chars[cur] >= '0' && chars[cur] <= '9') || chars[cur] == '.')) {
                cur++;
            }
            nums.add(charsToDouble(chars, 0, cur - 1));
            requireNumber = false;
        } else {
            // read symbol
            symbols.add(charToSymbol(chars[0]));
            requireNumber = true;
        }


        while(cur < chars.length) {
            if(requireNumber) {
                if(chars[cur] == '-' || chars[cur] == '(') {
                    symbols.add(chars[cur] == '-'? Symbol.NEGATIVE: Symbol.LPAR);
                    cur++;
                } else {
                    int next = cur;
                    while(next < chars.length && ((chars[next] >= '0' && chars[next] <= '9') || chars[next] == '.')) {
                        next++;
                    }
                    nums.add(charsToDouble(chars, cur, next - 1));
                    requireNumber = false;
                    cur = next;
                }
            } else {
                // judge priority
                Symbol curSymbol = charToSymbol(chars[cur]);
                if(curSymbol == Symbol.EOF) {
                    // eof
                    while(!symbols.isEmpty()) {
                        Symbol stackTopSymbol = symbols.pop();
                        Double n = unitCalculate(nums, stackTopSymbol);
                        nums.add(n);
                    }
                } else if(curSymbol == Symbol.RPAR) {
                    // right parentheses
                    while(!symbols.isEmpty()) {
                        Symbol stackTopSymbol = symbols.peek();
                        if(stackTopSymbol == Symbol.LPAR) {
                            symbols.pop();
                            break;
                        }
                        Double n = unitCalculate(nums, stackTopSymbol);
                        nums.add(n);
                        symbols.pop();
                    }
                }
                else {
                    // normal symbols
                    while(!symbols.isEmpty()) {
                        Symbol stackTopSymbol = symbols.peek();
                        if(priority.get(stackTopSymbol) < priority.get(curSymbol)) {
                            break;
                        }
                        Double n = unitCalculate(nums, stackTopSymbol);
                        nums.add(n);
                        symbols.pop();
                    }
                    symbols.add(curSymbol);
                    requireNumber = true;
                }
                cur++;
            }
        }

        return nums.pop();
    }

    private double unitCalculate(Stack<Double> nums, Symbol symbol) {
        Double ans;
        switch (symbol) {
            case ADD: {
                double num1 = nums.pop();
                double num2 = nums.pop();
                ans = num1 + num2;
                break;
            }
            case MINUS: {
                double num1 = nums.pop();
                double num2 = nums.pop();
                ans = num2 - num1;
                break;
            }
            case TIMES: {
                double num1 = nums.pop();
                double num2 = nums.pop();
                ans = num1 * num2;
                break;
            }
            case DEVIDE: {
                double num1 = nums.pop();
                double num2 = nums.pop();
                ans = num2 / num1;
                break;
            }
            case NEGATIVE: {
                double num = nums.pop();
                ans = -num;
                break;
            }
            default: {
                throw new RuntimeException(symbol + " is not calculatable.");
            }
        }
        return ans;
    }

    private double charsToDouble(char[] chars, int l, int r) {
        String s = "";
        for(int i = l; i <= r; ++i) {
            s += chars[i];
        }
        return Double.parseDouble(s);
    }

    private Symbol charToSymbol(char symbolChar) {
        Symbol s;
        switch (symbolChar) {
            case '+': {
                s = Symbol.ADD;
                break;
            }
            case '-': {
                s = Symbol.MINUS;
                break;
            }
            case '*': {
                s = Symbol.TIMES;
                break;
            }
            case '/': {
                s = Symbol.DEVIDE;
                break;
            }
            case '(': {
                s = Symbol.LPAR;
                break;
            }
            case ')': {
                s = Symbol.RPAR;
                break;
            }
            case '\0': {
                s = Symbol.EOF;
                break;
            }
            default: {
                throw new RuntimeException("'" + symbolChar + "' is not a valid character.");
            }
        }
        return s;
    }

}