@echo off
:: プロジェクトの場所に移動
cd /d "C:\dev\java-todo-app"

:: 実行
java -cp bin Main

:: 終わったあと勝手に画面が閉じないようにする
pause
