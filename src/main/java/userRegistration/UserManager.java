package userRegistration;

import java.util.HashMap;

public class UserManager {




    private HashMap<String,User> user = new HashMap<>();


 public User getUser(String name){
     return user.get(name);
 }
 public void removeUser(String name){
     if(!user.containsKey(name)){
         System.out.println("User not found");
     }
     user.remove(name);
 }

 public void addUser(String name,String password){

     if(!user.containsKey(name)){
         User newuser = new User(name,password);
         user.put(name,newuser);
         System.out.println("User created");
     }
     else{
         System.out.println("User already exists");
     }


 }



}
