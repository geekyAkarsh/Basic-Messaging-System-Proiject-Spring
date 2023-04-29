package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
//    private HashSet<String> userMobile;
    private HashMap<String,User> userMap;
    private HashMap<Integer,Message> idMessageMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>(); // map for messages in different groups
        this.groupUserMap = new HashMap<Group, List<User>>(); // map for users in different groups
        this.senderMap = new HashMap<Message, User>(); // map for message to user mapping
        this.adminMap = new HashMap<Group, User>(); // map for group to admin mapping
//        this.userMobile = new HashSet<>();
        this.userMap = new HashMap<String, User>(); // map for mobile number to user mapping
        this.idMessageMap = new HashMap<Integer, Message>(); // map for id to message mapping
        this.customGroupCount = 0; // count of groups
        this.messageId = 0; // incremental message ID
    }

    public Integer getCustomGroupCount(){
        return this.customGroupCount;
    }

    public void setCustomGroupCount(int customGroupCount) {
        this.customGroupCount = customGroupCount;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void createUser(String name, String mobile) {

        userMap.put(mobile,new User(name,mobile));
        return;
    }

    public Optional<Boolean> containsMobile(String mobile) {

        if(userMap.containsKey(mobile))
            return Optional.of(Boolean.TRUE);

        return Optional.empty();
    }

    public Integer createMessage(String content) {

        this.messageId++;
        this.idMessageMap.put(this.messageId,new Message(this.messageId,content));
        return messageId;
    }

    public Group createGroup(List<User> users) {


        this.customGroupCount++;
        String nameGroup = "Group " + this.customGroupCount;

        Group newGroup = new Group(nameGroup,users.size());
        this.groupUserMap.put(newGroup,users);
        this.adminMap.put(newGroup,users.get(0));

        return newGroup;
    }

    public Group createNewChat(List<User> users) {

        Group chat = new Group(users.get(1).getName(),2);
        this.groupUserMap.put(chat,users);

        // In case of chat are both members admin ?
//        this.adminMap.put(chat,users.get(0));
//        this.adminMap.put(chat,users.get(1));

        return chat;
    }

    public Optional<List<User>> getUsersfromGroup(Group group) {

        if(this.groupUserMap.containsKey(group)){
            return Optional.of(this.groupUserMap.get(group));
        }
        return Optional.empty();
    }

    public int sendMessage(User sender,Message message, Group group) {

        List<Message> oldMessages = new ArrayList<>();
        if(this.groupMessageMap.containsKey(group))
            oldMessages = this.groupMessageMap.get(group);

        oldMessages.add(message);
        this.groupMessageMap.put(group,oldMessages);
        this.senderMap.put(message,sender);

        return oldMessages.size();
    }

    public User getAdmin(Group group) {

        return this.adminMap.get(group);
    }

    public void changeAdmin(User user, Group group) {

        this.adminMap.put(group,user);
        return;
    }
}
