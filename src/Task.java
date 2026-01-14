public class Task {

    private int id;
    private String title;
    private boolean isDone;

    //可読性の観点から、thisを付けるようにする。クラスのメンバ変数であると目印になって読みやすくなるため。
    //コンストラクタ
    public Task(int id, String title){
        this.id = id;
        this.title = title;
        this.isDone = false; //初期設定は未完了
    }

    //タスクの状態を表示しやすくするメソッド
    //@Overrideは書かなくてもエラーにならない。ただし、書くことでoverrideがうまくできてなければコンパイルエラーになってくれるようになるので、付ける癖付けすべし。
    @Override
    public String toString(){
        String status = isDone ? "[済]" : "[未]";  //三項演算子
        return String.format("%d : %s %s", id, status, title); //%d = digit(整数)、%s = string(文字列)
    }

    //完了フラグを切り替えるメソッド
    public void markAsDone(){
        this.isDone = true;
    }

    //getter
    public int getId(){ return id; };
    public String getTitle(){ return title; }
    public boolean isDone(){ return isDone; }

}
