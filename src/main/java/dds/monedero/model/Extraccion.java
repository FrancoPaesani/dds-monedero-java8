package dds.monedero.model;

import java.math.BigDecimal;

public class Extraccion implements TipoOperacion{
    private static final Extraccion INSTANCE = new Extraccion();

    public static Extraccion getINSTANCE() { return INSTANCE; }

    public boolean isDeposito() { return false; }

    public BigDecimal calcularNuevoSaldo(BigDecimal saldo, BigDecimal monto) {
        return saldo.subtract(monto);
    }
}
