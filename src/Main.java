import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//javac -d bin src/*.java   srcフォルダ配下のjavaファイルを全てコンパイルして、binフォルダへclassファイルを配置
//java -cp bin Main         実行ファイルはbinフォルダにあるよ
public class Main {

    private static int nextId = 1;

    //色の定数
    public static final String RESET = "\u001b[0m";
    public static final String RED   = "\u001b[31m";
    public static final String GREEN = "\u001b[32m";
    public static final String YELLOW = "\u001b[33m";
    public static final String BLUE  = "\u001b[34m";
    public static final String CYAN  = "\u001b[36m";
    public static final String ORANGE  = "\u001b[38;5;208m"; // 鮮やかなオレンジ（256色拡張）
    public static final String PINK    = "\u001b[95m";       // 明るいピンク（高輝度マゼンタ）
    public static final String LIME    = "\u001b[92m";       // 蛍光ライムグリーン

    public static void main(String args[]){
        //タスクを保存するリスト
        List<Task> toDoList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in, "Shift-JIS");

        //出迎えてくれるアスキーアート
        System.out.println();
        System.out.println(LIME +"　　　　 ∧,,∧" + RESET);
        System.out.println(LIME +"　　,,＿(　･ω･)" + RESET);
        System.out.println(LIME +"　　)　　　　　 ﾌ" + RESET);
        System.out.println(LIME +"　 /　　　　/ ／" + RESET);
        System.out.println(LIME +"　/　　ω_ノ_/" + RESET);
        System.out.println(LIME +"　￣/　|　 " + RESET);
        System.out.println(LIME +"　　/　 |" + RESET);
        System.out.println(LIME +"　 (＿_ノ" + RESET);
        System.out.println();

        //tasks.txtを読み込み
        loadFromFile(toDoList);

        //選択式でユーザ操作させる
        while (true){
            System.out.println("\n===============================");
            System.out.println("   TASK MANAGEMENT SYSTEM v1.1");
            System.out.println("===============================");
            System.out.println(" 1. [Create]  Add");
            System.out.println(" 2. [Read]    List");
            System.out.println(" 3. [Update]  Mark Task as Completed");
            System.out.println(" 4. [Exit]    End");
            System.out.println(" 5. [Read]    History");
            System.out.println(CYAN + "\nSELECT ACTION > " + RESET);

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.println(BLUE + ">>> [Action: Create] Enter task title:" + RESET);
                String title = scanner.nextLine();
                System.out.println(BLUE + "\n>>> Set deadline (yyyy-mm-dd):" + RESET);
                String deadlineString = scanner.nextLine();
                LocalDate deadline = LocalDate.parse(deadlineString);

                toDoList.add(new Task(nextId++, title, deadline, "ME"));
                saveToFile(toDoList);
                System.out.println(GREEN + "[SUCCESS] New task added." + RESET);
            } else if (choice.equals("2")) {
                while(true){
                    System.out.println("\n--- [DISPLAY: TASK LIST] ---");

                    //期限の昇順で表示
                    //期限が同じ場合ID順で表示
                    toDoList.sort(java.util.Comparator.comparing(Task::getDeadline).thenComparing(Task::getId));

                    System.out.println(PINK + "\n[ MY BALL ]" + RESET);
                    for(Task task : toDoList){
                        if( !task.isDone() && task.getBallOwner().equalsIgnoreCase("ME") ){
                            System.out.println(task);
                        }
                    }
                    System.out.println(PINK + "\n[ OTHER BALL ]" + RESET);
                    for(Task task : toDoList){
                        if( !task.isDone() && !task.getBallOwner().equalsIgnoreCase("ME") ){
                            System.out.println(task);
                        }
                    }
                    System.out.println("\n-------------------------------------------");
                    System.out.println("CMD: [mID] ME | [ID=Name] Pass to OTHER | [dID] Done | [Enter] Back");
                    System.out.print(CYAN + "COMMAND > " + RESET);

                    String cmd = scanner.nextLine().trim().toLowerCase();
                    if (cmd.isEmpty()) break;

                    try {
                        if(cmd.startsWith("d")){
                            int id = Integer.parseInt(cmd.substring(1));
                            //ラムダ式の矢印、ストリームと呼ばれるベルトコンベアのイメージのやつ、新しめのJavaの書き方らしい。慣れるしかない
                            toDoList.stream().filter(task -> task.getId() == id).findFirst().ifPresent(task -> {
                                task.markAsDone();
                                System.out.println(GREEN + ">> Task " + id + " -> DONE" + RESET );
                            });
                        } else if(cmd.startsWith("m")) {
                            int id = Integer.parseInt(cmd.substring(1));
                            toDoList.stream().filter(task -> task.getId() == id).findFirst().ifPresent(task -> {
                                task.setBallOwner("ME");
                                System.out.println(GREEN + ">> Task " + id + " -> to ME " + RESET);
                            });
                        } else if (cmd.contains("=")){
                            String[] parts = cmd.split("=");
                            int id = Integer.parseInt(parts[0]);
                            String name = parts[1];
                            toDoList.stream().filter(task -> task.getId() == id).findFirst().ifPresent(task -> {
                                task.setBallOwner(name);
                                System.out.println(GREEN + ">> Task " + id + "-> to " + name + RESET);
                            });
                        } else {
                            System.out.println(ORANGE + ">> Unknown command. Use command [m1], [d1], or [1=Name]." + RESET);
                        }
                    } catch (Exception e) {
                        System.out.println(RED + ">> Error: Invalid ID or format." + RESET);
                    }
                }
            } else if (choice.equals("3")) {
                System.out.println(BLUE + ">>> [Action: Update] Enter target Task ID:" + RESET);
                int targetId = Integer.parseInt(scanner.nextLine());

                boolean found = false;
                for (Task task : toDoList) {
                    if (task.getId() == targetId) {
                        task.markAsDone();
                        System.out.println(GREEN + "[SUCCESS] Task ID: " + targetId + " updated to status: DONE." + RESET);
                        found = true;
                        break;
                    }
                }
                if (!found) {  //この変数がfalseならって意味になるらしい
                    System.out.println(RED + "[ERROR] Record not found. ID: " + targetId + RESET);
                }
                saveToFile(toDoList);
            }else if (choice.equals("4")) {
                saveToFile(toDoList);
                System.out.println(GREEN + "[SYSTEM] Initializing shutdown sequence..." + RESET);
                System.out.println(GREEN + "Goodbye." + RESET);
                break;  //whileの無限ループから抜ける
            } else if (choice.equals("5")){
                System.out.println(PINK + "\n--- [DISPLAY: ALL TASK HISTORY] ---" + RESET);

                //期限の昇順で表示
                //期限が同じ場合ID順で表示
                toDoList.sort(java.util.Comparator.comparing(Task::getDeadline).thenComparing(Task::getId));

                for(Task task : toDoList){
                    System.out.println(task);
                }
            } else {
                System.out.println(ORANGE + "[WARNING] Invalid input. Please select from codes 1-4." + RESET);
            }
        }
        scanner.close();

    }

    private static void saveToFile(List<Task> toDoList){
        try (PrintWriter writer = new PrintWriter(new FileWriter("tasks.txt"))) {
            for( Task task : toDoList){
                writer.println(task.getId() + "," + task.getTitle() + "," + task.isDone() + "," + task.getDeadline() + "," + task.getBallOwner() );
            }
            System.out.println(GREEN + "[INFO] Data persistence complete." + RESET);    
        } catch (Exception e) {
            System.out.println(RED + "[ERROR] Failed to write data to file: " + e.getMessage() + RESET);
        }
    }

    private static void loadFromFile(List<Task> toDoList){
        File file = new File("tasks.txt");
        if(!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);               
                String title = parts[1];               
                boolean isDone = Boolean.parseBoolean(parts[2]);
                LocalDate deadline = LocalDate.parse(parts[3]);

                String ballOwner = (parts.length > 4) ? parts[4] : "ME";
                
                //タスクを復元
                Task task = new Task(id, title, deadline, ballOwner);
                if(isDone) task.markAsDone();
                toDoList.add(task);

                //前回の最後に発番したidから連番になるように
                nextId = Math.max(nextId, id + 1);
            }
            System.out.println(GREEN + "[INFO] System state restored from " + file.getName() + RESET);            
        } catch (Exception e) {
            System.out.println(RED + "[ERROR] Failed to load data record from " + file.getName() +": " + e.getMessage() + RESET);
        }
    }
}
