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
//java -cp bin Main         実行ファイルはbinフォルダにあるよ～
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
            System.out.println(" 1. [Create]  Add New Task");
            System.out.println(" 2. [Read]    List All Tasks");
            System.out.println(" 3. [Update]  Mark Task as Completed");
            System.out.println(" 4. [Exit]    Terminate Process");
            System.out.println("\nSELECT ACTION > ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.println(">>> [Action: Create] Enter task title:");
                String title = scanner.nextLine();
                System.out.println("\n>>> Set deadline (yyyy-mm-dd):");
                String deadlineString = scanner.nextLine();
                LocalDate deadline = LocalDate.parse(deadlineString);

                toDoList.add(new Task(nextId++, title, deadline));
                System.out.println("[SUCCESS] New task registered to the database.");
            } else if (choice.equals("2")) {
                System.out.println("\n--- [DISPLAY: CURRENT TASK LIST] ---");
                for(Task task : toDoList){
                    System.out.println(task);
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
            } else {
                System.out.println("[WARNING] Invalid input. Please select from codes 1-4.");
            }
        }
        scanner.close();

    }

    private static void saveToFile(List<Task> toDoList){
        try (PrintWriter writer = new PrintWriter(new FileWriter("tasks.txt"))) {
            for( Task task : toDoList){
                writer.println(task.getId() + "," + task.getTitle() + "," + task.isDone() + "," + task.getDeadline() );
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
                
                //タスクを復元
                Task task = new Task(id, title, deadline);
                if(isDone) task.markAsDone();
                toDoList.add(task);

                //前回の最後に発番したidから連番になるように
                nextId = id + 1;
                System.out.println("[INFO] System state restored from " + file.getName());
            }            
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to load data record from " + file.getName() +": " + e.getMessage());
        }

    }
}
