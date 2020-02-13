package com.example.flashcards;

public class Verifier {
    private String login;
    private String password;
    private Database db;

    Verifier(String login, String password, Database db){
        this.login=login;
        this.password=password;
        this.db=db;
    }

    public boolean ifLoginExist(){
        return db.doesTheLoginExist(this.login);
    }

    public String downloadPassword(){
        if(ifLoginExist()){
            return db.getUserPassword(this.login);
        }
        else{
            return null;
        }
    }
    boolean verifyAccount() {
        if (ifLoginExist()) {
            if (password.equals(downloadPassword())) {
                return true;
            } else {
                return false;
            }
        }
        else{
                return false;
            }
        }
    }