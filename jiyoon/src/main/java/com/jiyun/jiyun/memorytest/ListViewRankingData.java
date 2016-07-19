package com.jiyun.jiyun.memorytest;

public class ListViewRankingData {

    String Ranking_Number;
    String Ranking_ID;
    String Ranking_Score;

    public ListViewRankingData(String Ranking_Number, String Ranking_ID, String Ranking_Score) {
        // TODO Auto-generated constructor stub
        this.Ranking_Number = Ranking_Number;
        this.Ranking_ID = Ranking_ID;
        this.Ranking_Score = Ranking_Score;
    }

    public void setNumber(String Ranking_Number) {this.Ranking_Number = Ranking_Number;}
    public void setID(String Ranking_ID) {this.Ranking_ID = Ranking_ID;}
    public void setScore(String Ranking_Score) { this.Ranking_Score = Ranking_Score; }
    public String getNumber() { return Ranking_Number; }
    public String getID() { return Ranking_ID; }
    public String getScore() { return Ranking_Score;}
}