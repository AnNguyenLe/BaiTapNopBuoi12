package Applications;

import UserInteractor.Console.ConsoleInteractable;

public interface ConsoleApplication {
    void run();
    void displayOptions(Iterable<String> options);
    int selectAnOption(ConsoleInteractable userInteractor, Iterable<String> options);
}