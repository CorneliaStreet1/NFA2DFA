
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
 /*       int a=sc.nextInt();//总元素个数
        int b=sc.nextInt();//输入元素个数
        int c=sc.nextInt();//终态个数*/


        int a=3,b=2,c=1;int q0=1;
        ArrayList<Integer> F=new ArrayList<>();
        F.add(4);

//        int[][] nfa=new int[a][b];
        int[][] dfa=new int[(int)Math.pow(2,a)-1][b];
        //测试用
        int[][] nfa={ {1,3},{4,4},{0,0} };

        NFA2DFA(nfa,dfa,a,b);

        ArrayList<Integer> mark=new ArrayList<>();
        mark.add(--q0);
        dedup(q0,b,dfa,mark);

        int[][] newDfa=new int[mark.size()][b];
        setNewDFA(dfa,newDfa,mark,b);

        ArrayList<Integer> end=new ArrayList<>();
        setFinalStatus(mark,F,end);

        c++;///debug用
    }

    //NFA的二维数组形式转化为DFA的二维数组形式
    public static void NFA2DFA(int[][] nfa,int[][] dfa,int a,int b ) {
        int i,j,k,temp;
        for( i=0;i<(int)Math.pow(2,a)-1;i++){//dfa的第i行
            for(j=0;j<b;j++){//dfa的第j列
                for(k=0,temp=i+1;k<a;k++){//dfa[i][j]的计算
                    if(temp%2==1) {
                        dfa[i][j] = dfa[i][j] | nfa[k][j];
                    }
                    temp=temp>>1;
                }
            }
        }
    }

    //mark对数组每一行进行标记,找出不需要的状态
    public static void dedup(int index,int b,int[][] dfa,ArrayList<Integer> mark) {
        for(int i=0;i<b;i++){
            if( !mark.contains(dfa[index][i]-1) ){
                mark.add(dfa[index][i]-1);
                if(dfa[index][i]!=0)
                    dedup(dfa[index][i]-1,b,dfa,mark);
            }
        }
    }

    //用新的数组存储DFA, 去掉了不需要的状态
    public static void setNewDFA(int[][] dfa,int[][] newDfa,ArrayList<Integer> mark,int b) {
        Collections.sort(mark);
        int i=0;
        if(mark.contains(-1)){
            for(int j=0;j<b;j++){
                newDfa[i][j]=0;
            }
        }
        i++;
        for(;i<mark.size();i++){
            for(int j=0;j<b;j++){
                newDfa[i][j]=dfa[mark.get(i)][j];
            }
        }
    }

    //找到终态的位置
    public static void setFinalStatus(ArrayList<Integer> mark,ArrayList<Integer> F,ArrayList<Integer> end) {
        int i,j;
        for(i=0;i<mark.size();i++){
            for(j=0;j<F.size();j++){
                if(   ((mark.get(i)+1) | F.get(j) ) == (mark.get(i)+1)  ){
                    end.add(i);
                    break;
                }
            }
        }
    }
}
