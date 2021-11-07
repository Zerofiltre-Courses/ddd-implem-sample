package com.zerofiltre.freeland.domain.freelancer.model;


public class Skill {

  private final String name;
  private final int score;


  public Skill(String name, int score) {
    this.name = name;
    this.score = score;
  }

  public String getName() {
    return name;
  }

  public int getScore() {
    return score;
  }


}
