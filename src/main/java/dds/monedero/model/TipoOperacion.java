package dds.monedero.model;

public interface TipoOperacion {
    public double calcularNuevoSaldo(double saldo, double monto);
    public boolean isDeposito();
}
