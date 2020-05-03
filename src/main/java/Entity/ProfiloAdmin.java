package Entity;

public class ProfiloAdmin {
    private String username;

    private static ProfiloAdmin istance=null;

    public static ProfiloAdmin getIstance() {
        if(istance==null)
            istance = new ProfiloAdmin();
        return istance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
