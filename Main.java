import java.util.*;

public class Main {
    public static void main(String[] args) {
        //调用输入类,得到nfa五元组
        Input input = new Input();
        input.WelcomeInterface();

        //a是nfa总状态数,b是总输入字符个数,q0是初态,F是终态,将q0和F换成int类型
        int a=input.getStateSet().size(),b=input.getInputSet().size();

        int q0=(int)Math.pow(2,input.getStateSet().indexOf(input.getUniqueInitState()));
        ArrayList<Integer> F=new ArrayList<>();
        transform(F,input);

        //用二维数组来表示nfa
        int[][] nfa=new int[a][b];
        setNfa(nfa,input);

        //nfa数组转换成成dfa数组
        int[][] dfa=new int[(int)Math.pow(2,a)][b];
        NFA2DFA(nfa,dfa,a,b);

        //mark标记所有可达状态
        ArrayList<Integer> mark=new ArrayList<>();
        mark.add(q0);
        removeStatus(q0,b,dfa,mark);

        //去掉不可达状态得到newDfa
        int[][] newDfa=new int[mark.size()][b];
        setNewDFA(dfa,newDfa,mark,b);

        //end标记去掉了不可达状态后的所有终态
        ArrayList<Integer> end=new ArrayList<Integer>();
        setFinalStatus(mark,F,end);

        //根据原本的输入状态给新的状态命名
        ArrayList<StringBuilder> name=new ArrayList<>();
        nameTotalStatus(mark,input.getStateSet(),name,input.getInputSet());

        //输出
        output(newDfa,mark.size(),name,end,mark.indexOf(q0),input.getInputSet());
    }

    public static void setNfa(int[][]nfa,Input input){
        for(int i=0;i<input.getFunctions().size();i++){
            nfa[input.getStateSet().indexOf( input.getFunctions().get(i).getStartState())]
                    [input.getInputSet().indexOf(input.getFunctions().get(i).getInput())]=
            (int)Math.pow(2, input.getStateSet().indexOf( input.getFunctions().get(i).getEndState())) |
            nfa[input.getStateSet().indexOf( input.getFunctions().get(i).getStartState())]
                    [input.getInputSet().indexOf(input.getFunctions().get(i).getInput())];
        }
    }

    public static void NFA2DFA(int[][] nfa,int[][] dfa,int a,int b ) {
        int i,j,k,temp;
        for( i=0;i<(int)Math.pow(2,a);i++){//dfa的第i行
            for(j=0;j<b;j++){//dfa[i][j]
                for(k=0,temp=i;k<a;k++){//一共有a个初始nfa状态
                    if(temp%2==1) {//如果有求并集
                        dfa[i][j] = dfa[i][j] | nfa[k][j];
                    }
                    temp=temp>>1;
                }
            }
        }
    }

    public static void removeStatus(int index,int b,int[][] dfa,ArrayList<Integer> mark) {
        for(int i=0;i<b;i++){
            if( !mark.contains(dfa[index][i]) ){//走到状态就开始新的循环
                mark.add(dfa[index][i]);
                if(dfa[index][i]!=0)//空集只能得到空集,递归没意义
                    removeStatus(dfa[index][i],b,dfa,mark);
            }
        }
    }

    public static void setNewDFA(int[][] dfa,int[][] newDfa,ArrayList<Integer> mark,int b) {
        Collections.sort(mark);//先给mark排序
        for(int i=0;i<mark.size();i++){
            for(int j=0;j<b;j++){
                //mark.get(i)是可达状态的索引,把能到达的状态在mark中的索引给newDfa中的元素
                //因为newDfa中的状态数和mark相同,要重新按0,1,2...的顺序给新状态排序
                newDfa[i][j]= mark.indexOf( dfa[mark.get(i)][j] );///
            }
        }
    }

    public static void transform(ArrayList<Integer>F,Input input) {
        for(int i=0;i<input.getFinalState().size();i++){
            F.add(     (int) Math.pow(2,input.getStateSet().indexOf( input.getFinalState().get(i) )) );
        }
    }

    public static void setFinalStatus(ArrayList<Integer> mark,ArrayList<Integer> F,ArrayList<Integer> end) {
        int i,j;
        for(i=0;i<mark.size();i++){//
            for(j=0;j<F.size();j++){
                //如果mark的某个元素包含F中的任意元素,就把它加到终态集合里
                if(   (mark.get(i) | F.get(j) ) == mark.get(i)  ){
                    end.add(i);
                    break;
                }
            }
        }
    }

    public static void nameTotalStatus(ArrayList<Integer> mark,ArrayList<String>Q,
                                       ArrayList<StringBuilder> name,ArrayList<String>input) {
        int i,j,temp,max=0;
        for(i=0;i<mark.size();i++){
            StringBuilder aname=new StringBuilder();
            for(j=0,temp=mark.get(i);j<Q.size();j++){
                if(temp%2==1){
                    if(aname.length()==0){
                        aname.append("["+Q.get(j));
                    }else{
                        aname.append(","+Q.get(j));
                    }
                }
                temp=temp>>1;
            }
            if(aname.length()!=0) {
                aname.append("]");
            }else{
                aname.append("空集");
            }
            name.add(aname);
        }

        //后面是想把所有状态和输入补到差不多长方便输出
        for(i=0;i<name.size();i++){
            if(name.get(i).length()>max)
                max=name.get(i).length();
        }
        for(i=0;i<name.size();i++){
            for(j=0;j<max-name.get(i).length();j++){
                name.get(i).append("  ");
            }
        }
        input.add(0,"");
        for(i=0;i<input.size();i++){
            StringBuilder sb=new StringBuilder(input.get(i));
            for(j=0;j<max-input.get(i).length();j++){
                sb.append(" ");
            }
            input.set(i,sb.toString());
        }
    }

    public static void output(int[][] newDfa,int rows,ArrayList<StringBuilder>name,
                              ArrayList<Integer>end,int start,ArrayList<String> input)
    {
        System.out.print("\t");
        for(int i=0;i<input.size();i++){
            System.out.print(input.get(i));
        }
        System.out.println();
        for(int i=0;i<rows;i++){
            if(i==start&&end.contains(i))
                System.out.print("->* ");
            else if(i==start)
                System.out.print("->  ");
            else if(end.contains(i))
                System.out.print("*   ");
            else
                System.out.print("    ");
            System.out.print(name.get(i));
            for(int j=0;j<input.size()-1;j++){
                System.out.print(name.get(newDfa[i][j]));
            }
            System.out.println();
        }
    }
}
