import java.util.*;

public class Main {
    public static void main(String[] args) {

        Input input = new Input();
        input.WelcomeInterface();

        ArrayList<String>Q=input.getStateSet();
        int a=input.getStateSet().size(),b=input.getInputSet().size();
        int q0=(int)Math.pow(2,input.getStateSet().indexOf(input.getUniqueInitState()));

        ArrayList<Integer> F=new ArrayList<>();
        transform(F,input);

        int[][] nfa=new int[a][b];
        setNfa(nfa,input);

        int[][] dfa=new int[(int)Math.pow(2,a)-1][b];
        NFA2DFA(nfa,dfa,a,b);

        ArrayList<Integer> mark=new ArrayList<>();
        mark.add(--q0);
        dedup(q0,b,dfa,mark);

        int[][] newDfa=new int[mark.size()][b];
        setNewDFA(dfa,newDfa,mark,b);

        ArrayList<Integer> end=new ArrayList<Integer>();
        setFinalStatus(mark,F,end);

        ArrayList<StringBuilder> name=new ArrayList<>();
        nameTotalStatus(mark,Q,name,input.getInputSet());

        output(newDfa,a,name,end,mark.indexOf(++q0),input.getInputSet());

        int c=0;
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

    public static void transform(ArrayList<Integer>F,Input input) {
        for(int i=0;i<input.getFinalState().size();i++){
            F.add(     (int) Math.pow(2,input.getStateSet().indexOf( input.getFinalState().get(i) )) );
        }
    }

    public static void setNfa(int[][]nfa,Input input){
        for(int i=0;i<input.getFunctions().size();i++){
            nfa[input.getStateSet().indexOf( input.getFunctions().get(i).getStartState())]
                    [input.getInputSet().indexOf(input.getFunctions().get(i).getInput())]+=
                    (int)Math.pow(2, input.getStateSet().indexOf( input.getFunctions().get(i).getEndState()));
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
        for(int i=0;i<rows+1;i++){
            if(i==start) System.out.print("->  ");
            else if(end.contains(i)) System.out.print("*   ");
            else System.out.print("    ");
            System.out.print(name.get(i));
            for(int j=0;j<input.size()-1;j++){
                System.out.print(name.get(newDfa[i][j]));
            }
            System.out.println();
        }
    }
}
