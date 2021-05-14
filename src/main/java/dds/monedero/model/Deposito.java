package dds.monedero.model;

public class Deposito implements TipoOperacion{
    private static final Deposito INSTANCE = new Deposito();

    public static Deposito getINSTANCE() { return INSTANCE; }

    public boolean isDeposito() { return true; }

    public double calcularNuevoSaldo(double saldo, double monto) {
        return saldo + monto;
    }
}
