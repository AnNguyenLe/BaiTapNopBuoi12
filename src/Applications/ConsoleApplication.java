package Applications;

import UserInteractor.Interactable;

public interface ConsoleApplication {
    void run();
    void stop();
    void displayOptions(Iterable<String> options);
    int selectAnOption(Interactable userInteractor, Iterable<String> options);
}