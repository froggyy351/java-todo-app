import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//javac -d bin src/*.java   srcフォルダ配下のjavaファイルを全てコンパイルして、binフォルダへclassファイルを配置
//java -cp bin Main         実行ファイルはbinフォルダにあるよ～
public class Main {
    public static void main(String args[]){
        //タスクを保存するリスト
        List<Task> toDoList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int nextId = 1;

        System.out.println("=== Java todo app ===");

        while (true){
            System.out.println("\n--- メニュー ---");
            System.out.println("1. タスクを追加");
            System.out.println("2. タスクを表示");
            System.out.println("3. 終了");
            System.out.println("\n選択してください。 >");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.println("タスクを入力");
                String title = scanner.nextLine();
                toDoList.add(new Task(nextId++, title));
                System.out.println("追加しました！");
            } else if (choice.equals("2")) {
                System.out.println("\n---現在のタスク---");
                for(Task task : toDoList){
                    System.out.println(task);
                }
            } else if (choice.equals("3")) {
                System.out.println("さようなら！");
                break;  //whileの無限ループから抜ける
            } else {
                System.out.println("1～3で入力してくださいね。");
            }
        }
        scanner.close();

    }
}
