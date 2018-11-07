ToyTalk(トイ・トーク)  
ToyManager( https://github.com/hassoubeat/ToyManager )と対になる予定管理アプリケーション。  
  
  
ToyTalkはRaspberryPiで動作することを想定されたJava製のクライアントアプリケーションです。  
常駐型のデーモンで、決まった時刻にToyManagerのAPIをコールして登録された予定をコールしてJSON形式で取得します。  
その後、登録された予定の時刻が来た際に、ToyManagerで登録された予定を合成音声にて読み上げて通知します。  
サンプル : https://drive.google.com/open?id=0B6QVDoH6tD-fd1ZpUzVrZktHems  
  
  
拡張機能として"ファセット"という独自の処理を実行する予定がToyManagerに登録されている際には、
"ファセットプログラム"本体のURLをToyManagerより取得して、S3にアップロードされている"ファセットプログラム"をダウンロードします。
ファセットを実行する予定の時刻が来た際は、"ファセットプログラム"を実行して独自の処理を実行します。
サンプル : https://drive.google.com/open?id=0B6QVDoH6tD-fcDdoUHpVZXNtcnM

===============
