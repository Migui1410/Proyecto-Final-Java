package Modelo;

public class UsuarioNormal extends Usuario{
	   public UsuarioNormal(String username, String password) {
	        super(username, password);
	    }
	    
	    @Override
	    public boolean esAdmin() {
	        return false;
	    }
}
