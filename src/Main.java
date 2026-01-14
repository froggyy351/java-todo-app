import java.util.ArrayList;
import java.util.List;

//javac -d bin src/*.java   srcフォルダ配下のjavaファイルを全てコンパイルして、binフォルダへclassファイルを配置
//java -cp bin Main         実行ファイルはbinフォルダにあるよ～
public class Main {
    public static void main(String args[]){
        //タスクを保存するリスト
        List<Task> toDoList = new ArrayList<>();

        //タスクを追加してみる
        toDoList.add(new Task(1, "Javaの勉強をする"));
        toDoList.add(new Task(2, "Githubにプッシュする"));
        toDoList.add(new Task(3, "ミーティングに参加する"));
        toDoList.add(new Task(4, "仕様書の修正をする"));

        //2番目のタスクを完了にしてみる
        toDoList.get(1).markAsDone();

        //一覧を表示する
        System.out.println("--- 今日のTODOリスト ---");
        for (Task task : toDoList){
            System.out.println(task);
        }
    }
}
