package co.empathy.academy.search.models;

public class Crew {

    private String tconst;
    private String Nconst;



    public void setTconst(String tconst) {
        this.tconst = tconst;
    }
    public void setNconst(String s) {
        this.Nconst = s;
    }

    public String getTconst() {
        return tconst;
    }

    public String getNconst() {
        return Nconst;
    }

   public String getDirector(){
        return "name: " + getNconst();
   }
}
