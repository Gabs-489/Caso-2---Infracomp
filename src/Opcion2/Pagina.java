package Opcion2;

public class Pagina {
    int numeroPagina;
    int marco;
    boolean modificada;
    boolean referenciada;

    public Pagina(int numeroPagina, int marco) {
        this.numeroPagina = numeroPagina;
        this.marco = marco;
        this.modificada = false;
        this.referenciada = false;
    }

    public boolean isModificada() {
        return modificada;
    }

    public void setModificada(boolean modificada) {
        this.modificada = modificada;
    }

    public boolean isReferenciada() {
        return referenciada;
    }

    public void setReferenciada(boolean referenciada) {
        this.referenciada = referenciada;
    }

    
    
}
