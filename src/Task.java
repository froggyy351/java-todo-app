import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Task {

    private int id;
    private String title;
    private boolean isDone;
    private LocalDate deadline;
    private String ballOwner;

    //曜日付きの日付型を作るフォーマット用の定数
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd (E)", Locale.JAPANESE);

    //可読性の観点から、thisを付けるようにする。クラスのメンバ変数であると目印になって読みやすくなるため。
    //コンストラクタ
    public Task(int id, String title, LocalDate deadline, String ballOwner){
        this.id = id;
        this.title = title;
        this.isDone = false; //初期設定は未完了
        this.deadline = deadline;
        this.ballOwner = ballOwner;
    }

    //タスクの状態を表示しやすくするメソッド
    //@Overrideは書かなくてもエラーにならない。ただし、書くことでoverrideがうまくできてなければコンパイルエラーになってくれるようになるので、付ける癖付けすべし
    @Override
    public String toString(){
        String status = isDone ? "[済]" : "[未]";  //三項演算子
        return String.format("ID:%d [%-8s] %s | %s (期限：%s)", id, ballOwner, status, title, deadline.format(FORMATTER)); //%d = digit(整数)、%s = string(文字列)
    }

    //完了フラグを切り替えるメソッド
    public void markAsDone(){
        this.isDone = true;
    }

    //getter & setter
    public int getId(){ return id; };
    public String getTitle(){ return title; }
    public boolean isDone(){ return isDone; }
    public LocalDate getDeadline(){ return deadline; }
    public String getBallOwner(){ return ballOwner; }
    public void setBallOwner(String ballOwner){ this.ballOwner = ballOwner; }

}
