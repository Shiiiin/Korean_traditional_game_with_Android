package com.example.shutda.view.data;

public class UserForRank {

    String name;
    Long score;

    public UserForRank(String name, Long score){
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getScore() {
        return score;
    }

}
