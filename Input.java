import java.util.ArrayList;
import java.util.Scanner;

public class Input {
    public class StateTransitionFunction{
        private String StartState;
        private String Input;
        private String EndState;
        public StateTransitionFunction(String initState, String input, String endState) {
            StartState = initState;
            Input = input;
            EndState = endState;
        }
        public String getInput() {
            return Input;
        }

        public String getEndState() {
            return EndState;
        }

        public String getStartState() {
            return StartState;
        }

    }
    private ArrayList<String> InputSet;
    private ArrayList<String> StateSet;
    private ArrayList<StateTransitionFunction> Functions;
    private ArrayList<String> FinalState;
    private String UniqueInitState; // 唯一的初态
    public Input() {
        InputSet = new ArrayList<>();
        StateSet = new ArrayList<>();
        Functions = new ArrayList<>();
        FinalState = new ArrayList<>();
        UniqueInitState = null;
    }
    public ArrayList<String> getInputSet() {
        return InputSet;
    }

    public ArrayList<String> getStateSet() {
        return StateSet;
    }

    public ArrayList<StateTransitionFunction> getFunctions() {
        return Functions;
    }

    public ArrayList<String> getFinalState() {
        return FinalState;
    }

    public String getUniqueInitState() {
        return UniqueInitState;
    }

    public void WelcomeInterface() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("======欢迎使用NFA到DFA转换机！=======");
        System.out.println("请输入NFA的状态集(输入exit退出)：");
        while (scanner.hasNext()) {
            String s = scanner.next();
            if (s.equals("exit")) {
                break;
            }
            this.StateSet.add(s);
        }
        System.out.println("请输入NFA的输入集(输入exit退出)：");
        while (scanner.hasNext()) {
            String s = scanner.next();
            if (s.equals("exit")) {
                break;
            }
            this.InputSet.add(s);
        }
        System.out.println("请输入NFA的唯一初态,请注意初态是唯一的，输入多个则以第一个为准：");
        String string = scanner.next();
        if (!this.StateSet.contains(string)) {
            System.out.println(string + "不存在于状态集中，已关闭程序");
            System.exit(0);
        }
        else {
            this.UniqueInitState = string;
        }
        scanner.nextLine();
        System.out.println("请输入NFA的最终态集(输入exit退出)：");
        while (scanner.hasNext()) {
            String s = scanner.next();
            if (s.equals("exit")) {
                break;
            }
            else if (!this.StateSet.contains(s)) {
                System.out.println(s + "不存在于状态集中，已关闭程序");
                System.exit(0);
            }
            this.FinalState.add(s);
        }
        System.out.println("请输入NFA的状态转移函数，其形式为：起始态 输入 到达态(输入exit退出)。");
        while (scanner.hasNext()) {
            String start = scanner.next();
            if (start.equals("exit")) {
                break;
            }
            if (!this.StateSet.contains(start)) {
                System.out.println(start + "不处于状态集中，已关闭程序。");
                System.exit(0);
            }
            String IP = scanner.next();
            if (IP.equals("exit")) {
                break;
            }
            if (!this.InputSet.contains(IP)) {
                System.out.println(IP + "不存在于输入集中，已关闭程序");
                System.exit(0);
            }
            String end = scanner.next();
            if (end.equals("exit")) {
                break;
            }
            if (!this.StateSet.contains(end)) {
                System.out.println(end + "不存在于状态集中，已关闭程序。");
                System.exit(0);
            }
            Functions.add(new StateTransitionFunction(start, IP, end));
        }
    }
}
