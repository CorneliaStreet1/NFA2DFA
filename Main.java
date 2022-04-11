
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);

        int a=3,b=2,c=1;int q0=1;
        ArrayList<Integer> F=new ArrayList<>();
        F.add(4);
        ArrayList<String> Q=new ArrayList<>();
        Q.add("q0");Q.add("q1");Q.add("q2");Q.add("q3");


        int[][] dfa=new int[(int)Math.pow(2,a)-1][b];

        //测试用nfa
        int[][] nfa={ {1,3},{4,4},{0,0} };
        //       int[][] nfa={ {2,0},{2,6},{0,0} };

        NFA2DFA(nfa,dfa,a,b);

        ArrayList<Integer> mark=new ArrayList<>();
        mark.add(--q0);
        dedup(q0,b,dfa,mark);

        int[][] newDfa=new int[mark.size()][b];
        setNewDFA(dfa,newDfa,mark,b);

        ArrayList<Integer> end=new ArrayList<Integer>();
        setFinalStatus(mark,F,end);

        ArrayList<StringBuilder> name=new ArrayList<>();
        nameTotalStatus(mark,Q,name);

        //输出函数
        //output(int [][] newDfa,int rows,int lines,ArrayList<StringBuilder>name,ArrayList<Integer>end,String start);


        c++;///debug用
    }

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

    public static void dedup(int index,int b,int[][] dfa,ArrayList<Integer> mark) {
        for(int i=0;i<b;i++){
            if( !mark.contains(dfa[index][i]-1) ){
                mark.add(dfa[index][i]-1);
                if(dfa[index][i]!=0)
                    dedup(dfa[index][i]-1,b,dfa,mark);
            }
        }
    }

    public static void setNewDFA(int[][] dfa,int[][] newDfa,ArrayList<Integer> mark,int b) {
        Collections.sort(mark);
        int i=0;
        if(mark.contains(-1)){
            for(int j=0;j<b;j++){
                newDfa[i][j]=0;
            }
            i++;
        }
        for(;i<mark.size();i++){
            for(int j=0;j<b;j++){
                newDfa[i][j]= mark.indexOf( dfa[mark.get(i)][j] -1);
            }
        }
    }

    public static void setFinalStatus(ArrayList<Integer> mark,ArrayList<Integer> F,ArrayList<Integer> end) {
        int i,j;
        for(i=0;i<mark.size();i++){
            mark.set(i,mark.get(i)+1);
            for(j=0;j<F.size();j++){
                if(   (mark.get(i) | F.get(j) ) == mark.get(i)  ){
                    end.add(i);
                    break;
                }
            }
        }
    }

    public static void nameTotalStatus(ArrayList<Integer> mark,ArrayList<String>Q,ArrayList<StringBuilder> name) {
        int i,j,temp;
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
    }
}
