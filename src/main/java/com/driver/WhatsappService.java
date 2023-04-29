package com.driver;

import java.util.List;
import java.util.Optional;

public class WhatsappService {

    WhatsappRepository  whatsappRepository = new WhatsappRepository();

    public String createUser(String name, String mobile) throws Exception{

        Optional<Boolean> optionalMobile = containsMobile(mobile);

        if(optionalMobile.isPresent()){
            throw new Exception("User already exists");
        }

        whatsappRepository.createUser(name,mobile);
        return "SUCCESS";
    }

    private Optional<Boolean> containsMobile(String mobile) {

        Optional<Boolean> optionalMobile = whatsappRepository.containsMobile(mobile);
        return optionalMobile;
    }

    public Integer createMessage(String content) {

        return whatsappRepository.createMessage(content);
    }

    public Group createGroup(List<User> users) {

        if(users.size() == 2){

            return whatsappRepository.createNewChat(users);
        }

        return whatsappRepository.createGroup(users);
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{

        Optional<List<User>> optionalUsers = getUsersfromGroup(group);

        if(optionalUsers.isEmpty()){
            throw new Exception("Group does not exist");
        }

        List<User> users = optionalUsers.get();

        if(!users.contains(sender)){
            throw new Exception("You are not allowed to send message");
        }

        return whatsappRepository.sendMessage(sender,message,group);
    }

    private Optional<List<User>> getUsersfromGroup(Group group) {

        return whatsappRepository.getUsersfromGroup(group);
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{

        Optional<List<User>> optionalUsers = getUsersfromGroup(group);
        if(optionalUsers.isEmpty()){
            throw new Exception("Group does not exist");
        }

        List<User> users = optionalUsers.get();

        if(!users.contains(user)){
            throw new Exception("User is not a participant");
        }

        User curr_admin = getAdmin(group);

        if(!curr_admin.equals(approver)){
            throw new Exception("Approver does not have rights");
        }

        whatsappRepository.changeAdmin(user,group);
        return "SUCCESS";
    }

    private User getAdmin(Group group) {

        return whatsappRepository.getAdmin(group);
    }
}
