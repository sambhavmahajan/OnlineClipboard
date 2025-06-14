## Online Clipboard
This is a Clipboard webapp having the following features
- **Creating Account**, email is must to be provided, software cant verify your email, this field is just there to simulate real world application registerations.<br>

- **Login**<br>

- **Creating text(clipboard)** that can be shared

- **Adding private restrictions**, where only the owner can access the clipboard.

Java 21 is used in this project, please use the appropriate JDK, ms-21 microsoft OpenJdk 21.07 is recommended 

Clone: 
```bash
git clone https://github.com/sambhavmahajan/OnlineClipboard.git
```

Please edit the following properties in application.properties
```
spring.datasource.url=jdbc:postgresql://localhost:5432/clipboard
spring.datasource.username=postgres
spring.datasource.password=12345678
```
