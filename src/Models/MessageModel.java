package Models;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class MessageModel {
    ActionType action;
    LocalDateTime timeSent;
    LinkedList<CDModel> data;
}
