package dds.monedero.model;

import java.math.BigDecimal;

public class Deposito implements TipoOperacion{
    private static final Deposito INSTANCE = new Deposito();

    public static Deposito getINSTANCE() { return INSTANCE; }

    public boolean isDeposito() { return true; }

    public BigDecimal calcularNuevoSaldo(BigDecimal saldo, BigDecimal monto) { return saldo.add(monto); }
}
