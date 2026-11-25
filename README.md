\# TP3 – TCP Client/Server



\## Introduction



 The main objective of this Lab was to implement a TCP client/server application in Java, explore TCP communication and understand the differences between TCP and UDP.



\## Project Structure



```text

tp3-tcp/

├── src/

│   ├── main/java/fr/ensea/rts/tcp/

│   │   ├── client/

│   │   │   └── TCPClient.java

│   │   ├── comparison/

│   │   │   └── UdpErrorTest.java

│   │   ├── server/

│   │   │   ├── MessageHistory.java

│   │   │   └── TCPServer.java

│   │   └── stream/

│   │       ├── TCPStreamClient.java

│   │       └── TCPStreamServer.java

│   └── test/java/fr/ensea/rts/tcp/server/

│       └── MessageHistoryTest.java

├── dist/

│   ├── TCPClient.jar

│   ├── TCPServer.jar

│   ├── TCPStreamClient.jar

│   └── TCPStreamServer.jar

├── lib/

│   └── junit-platform-console-standalone.jar

├── docs/

│   └── Report.pdf

├── README.md

└── .gitignore

```



\## Running the Application



Start the server:



```bash

java -jar dist/TCPServer.jar 8080

```



Start the client in another terminal:



```bash

java -jar dist/TCPClient.jar localhost 8080

```



\## Project Progress



The project was developed progressively from Step 1 to Step 14. Since the same Java files were modified throughout the lab, the repository contains the final version of the source code.



All previous versions and implementation steps can be found in the \[Git commit history](https://github.com/Douae-bee/tp3-tcp/commits/main/).



The detailed explanations, experiments, screenshots and answers are available in the \[Lab Report](docs/Report.pdf).



\## Conclusion



This lab helped us understand TCP communication, socket programming and the differences between TCP and UDP. It also improved our Java programming and teamwork skills using Git and GitHub.

