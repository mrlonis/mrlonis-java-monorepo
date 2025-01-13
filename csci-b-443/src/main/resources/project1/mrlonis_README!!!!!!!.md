# Project 1

## Notes

For this project, I did my work in IntelliJ IDE using the Java 9 SDK.
By using the IDE, I was able to find a lot of deprecated code and went
ahead and updated a lot of the code in order to not be deprecated and
even implemented a way to stop the program from throwing an
ArrayIndexOutOfBoundsException in Control.java by modifying the run
function as follows:

```
public void run() {
    Thread thisThread = Thread.currentThread();
    while (this.thread == thisThread) {
        try {
            this.processWork();
            this.processClockChange();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught Memory out of bounds!");
            break;
        }
    }
    this.stop();
}
```

On my machine these changes cause no issues and the project compiles and
runs successfully in both the IDE and by running the commands provided in
README.md in terminal. If you run into any issues while you're testing my
project please feel free to email me and I can try to come to office hours
and run the code on my machine or something in order to test it for you.
The only problem I forsee is the school's coputers not using Java 9 and
thus causing errors with the project.
