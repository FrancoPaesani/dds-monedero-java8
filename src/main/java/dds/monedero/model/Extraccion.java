package dds.monedero.model;

public class Extraccion implements TipoOperacion{
    private static final Extraccion INSTANCE = new Extraccion();

    public static Extraccion getINSTANCE() { return INSTANCE; }

    public boolean isDeposito() { return false; }

    public double calcularNuevoSaldo(double saldo, double monto) {
        return saldo - monto;
    }
}
