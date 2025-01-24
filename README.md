# A application reviewing system



## Instructions for use (update these whenever needed, and rewrite before final publish)
application.properties is used to system properties, ex where the database is used, which includes "secret" information, which is stored in the .env folder to avoid being uploaded, so any CI/CD tool must create this file + assign the correct values for the implementation in the resources folder

.env file should contain :

JDBC_DATABASE_URL= {jdbc url to database here, ex jdbc:postgresql://localhost:5432/puzzleconnect}
JDBC_DATABASE_USERNAME={username for database, ex postgres}
JDBC_DATABASE_PASSWORD={password to database, ex "password"}

The program shold then define the parameters using the format ${env:variableNameHere}='valueNameHere'

In visual studio code this can be done automatically, by creating a launch.json file in .vscode with the following content:

{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "DemoApplication",
            "request": "launch",
            "mainClass": "com.example.demo.DemoApplication",
            "projectName": "demo",
            "envFile": "${workspaceFolder}/.env"
        }
    ]
}