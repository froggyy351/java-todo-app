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
    public static void main(String args[]){
        //タスクを保存するリスト
        List<Task> toDoList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in, "Shift-JIS");

        //tasks.txtを読み込み
        loadFromFile(toDoList);

        //選択式でユーザ操作させる
        while (true){
            System.out.println("\n===============================");
            System.out.println("   TASK MANAGEMENT SYSTEM v1.0");
            System.out.println("===============================");
            System.out.println(" 1. [Create]  Add Task");
            System.out.println(" 2. [Read]    List Task");
            System.out.println(" 3. [Update]  Mark Task as Completed");
            System.out.println(" 4. [Exit]    Save & End");
            System.out.println(" 5. [Read]    History");
            System.out.println("\nSELECT ACTION > ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.println(">>> [Action: Create] Enter task title:");
                String title = scanner.nextLine();
                System.out.println("\n>>> Set deadline (yyyy-mm-dd):");
                String deadlineString = scanner.nextLine();
                LocalDate deadline = LocalDate.parse(deadlineString);

                toDoList.add(new Task(nextId++, title, deadline, "ME"));
                System.out.println("[SUCCESS] New task added.");
            } else if (choice.equals("2")) {
                while(true){
                    System.out.println("\n--- [DISPLAY: TASK LIST] ---");

                    //期限の昇順で表示
                    //期限が同じ場合ID順で表示
                    toDoList.sort(java.util.Comparator.comparing(Task::getDeadline).thenComparing(Task::getId));

                    System.out.println("\n[ MY BALL ]");
                    for(Task task : toDoList){
                        if( !task.isDone() && task.getBallOwner().equalsIgnoreCase("ME") ){
                            System.out.println(task);
                        }
                    }
                    System.out.println("\n[ OTHER BALL ]");
                    for(Task task : toDoList){
                        if( !task.isDone() && !task.getBallOwner().equalsIgnoreCase("ME") ){
                            System.out.println(task);
                        }
                    }
                    System.out.println("\n-------------------------------------------");
                    System.out.println("CMD: [mID] ME | [ID=Name] Pass to OTHER | [dID] Done | [Enter] Back");
                    System.out.print("COMMAND > ");

                    String cmd = scanner.nextLine().trim().toLowerCase();
                    if (cmd.isEmpty()) break;

                    try {
                        if(cmd.startsWith("d")){
                            int id = Integer.parseInt(cmd.substring(1));
                            //ラムダ式の矢印、ストリームと呼ばれるベルトコンベアのイメージのやつ、新しめのJavaの書き方らしい。慣れるしかない
                            toDoList.stream().filter(task -> task.getId() == id).findFirst().ifPresent(task -> {
                                task.markAsDone();
                                System.out.println(">> Task " + id + " -> DONE" );
                            });
                        } else if(cmd.startsWith("m")) {
                            int id = Integer.parseInt(cmd.substring(1));
                            toDoList.stream().filter(task -> task.getId() == id).findFirst().ifPresent(task -> {
                                task.setBallOwner("ME");
                                System.out.println(">> Task " + id + " -> to ME ");
                            });
                        } else if (cmd.contains("=")){
                            String[] parts = cmd.split("=");
                            int id = Integer.parseInt(parts[0]);
                            String name = parts[1];
                            toDoList.stream().filter(task -> task.getId() == id).findFirst().ifPresent(task -> {
                                task.setBallOwner(name);
                                System.out.println(">> Task " + id + "-> to " + name);
                            });
                        } else {
                            System.out.println(">> Unknown command. Use command [m1], [d1], or [1=Name].");
                        }
                    } catch (Exception e) {
                        System.out.println(">> Error: Invalid ID or format.");
                    }
                }
            } else if (choice.equals("3")) {
                System.out.println(">>> [Action: Update] Enter target Task ID:");
                int targetId = Integer.parseInt(scanner.nextLine());

                boolean found = false;
                for (Task task : toDoList) {
                    if (task.getId() == targetId) {
                        task.markAsDone();
                        System.out.println("[SUCCESS] Task ID: " + targetId + " updated to status: DONE.");
                        found = true;
                        break;
                    }
                }
                if (!found) {  //この変数がfalseならって意味になるらしい
                    System.out.println("[ERROR] Record not found. ID: " + targetId);
                }
            }else if (choice.equals("4")) {
                saveToFile(toDoList);
                System.out.println("[SYSTEM] Initializing shutdown sequence...");
                System.out.println("Goodbye.");
                break;  //whileの無限ループから抜ける
            } else if (choice.equals("5")){
                System.out.println("\n--- [DISPLAY: ALL TASK HISTORY] ---");

                //期限の昇順で表示
                //期限が同じ場合ID順で表示
                toDoList.sort(java.util.Comparator.comparing(Task::getDeadline).thenComparing(Task::getId));

                for(Task task : toDoList){
                    System.out.println(task);
                }
            } else {
                System.out.println("[WARNING] Invalid input. Please select from codes 1-4.");
            }
        }
        scanner.close();

    }

    private static void saveToFile(List<Task> toDoList){
        try (PrintWriter writer = new PrintWriter(new FileWriter("tasks.txt"))) {
            for( Task task : toDoList){
                writer.println(task.getId() + "," + task.getTitle() + "," + task.isDone() + "," + task.getDeadline() + "," + task.getBallOwner() );
            }
            System.out.println("[INFO] Data persistence complete.");    
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to write data to file: " + e.getMessage());
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
            System.out.println("[INFO] System state restored from " + file.getName());            
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to load data record from " + file.getName() +": " + e.getMessage());
        }
    }
}
