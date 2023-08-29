package Model;


public class Countries {

    private int Country_ID;
    private String Country_Name;

    public Countries(int Country_ID, String Country_Name) {

        this.Country_ID = Country_ID;
        this.Country_Name = Country_Name;
    }

    public int getId() {

        return Country_ID;
    }

    public String getName() {

        return Country_Name;
    }

    @Override
    public String toString(){

        return (Country_Name);
    }
}
