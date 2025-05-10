package Modelo;

public class Admin extends Usuario{
	public Admin(String username, String password) {
        super(username, password);
    }
	
	public boolean esAdmin() {
        return true;
    }
}
